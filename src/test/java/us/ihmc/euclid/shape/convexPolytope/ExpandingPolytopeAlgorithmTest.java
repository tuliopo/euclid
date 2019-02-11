package us.ihmc.euclid.shape.convexPolytope;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import us.ihmc.euclid.geometry.interfaces.Vertex3DSupplier;
import us.ihmc.euclid.shape.convexPolytope.interfaces.ConvexPolytope3DReadOnly;
import us.ihmc.euclid.shape.convexPolytope.tools.EuclidPolytopeFactories;
import us.ihmc.euclid.shape.convexPolytope.tools.EuclidPolytopeRandomTools;
import us.ihmc.euclid.shape.tools.EuclidShapeRandomTools;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.tools.EuclidCoreTestTools;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple4D.Quaternion;

class ExpandingPolytopeAlgorithmTest
{
   private static final int ITERATIONS = 1000;
   private static final double EPSILON = 1.0e-10;

   @Test
   void testNonCollidingCubeAndTetrahedron()
   {
      Random random = new Random(34534);

      ConvexPolytope3D cube = EuclidPolytopeFactories.newCube(1.0);

      for (int i = 0; i < ITERATIONS; i++)
      {
         Point3D tetrahedronClosest = new Point3D(0.5, 0.0, 0.0);
         Point3D tetrahedronFarthest0 = new Point3D(1.0, 1.0, 0.0);
         Point3D tetrahedronFarthest1 = new Point3D(1.0, 0.0, 1.0);
         Point3D tetrahedronFarthest2 = new Point3D(1.0, -1.0, 0.0);

         Vector3D translation = new Vector3D();
         translation.setX(EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0)); // Positive => no collision
         translation.setY(EuclidCoreRandomTools.nextDouble(random, 0.5));
         translation.setZ(EuclidCoreRandomTools.nextDouble(random, 0.5));

         Arrays.asList(tetrahedronClosest, tetrahedronFarthest0, tetrahedronFarthest1, tetrahedronFarthest2).forEach(p -> p.add(translation));

         ConvexPolytope3D tetrahedron = new ConvexPolytope3D(Vertex3DSupplier.asVertex3DSupplier(tetrahedronClosest, tetrahedronFarthest0, tetrahedronFarthest1,
                                                                                                 tetrahedronFarthest2));

         double distance = Double.POSITIVE_INFINITY;

         for (Vertex3D tetraVertex : tetrahedron.getVertices())
         {
            assertFalse(cube.isPointInside(tetraVertex));

            for (Face3D cubeFace : cube.getFaces())
            {
               distance = Math.min(distance, cubeFace.distance(tetraVertex));
            }
         }

         for (Vertex3D cubeVertex : cube.getVertices())
         {
            assertFalse(tetrahedron.isPointInside(cubeVertex));

            for (Face3D tetraFace : tetrahedron.getFaces())
            {
               distance = Math.min(distance, tetraFace.distance(cubeVertex));
            }
         }

         assertEquals(translation.getX(), distance, EPSILON);

         ExpandingPolytopeAlgorithm epa = new ExpandingPolytopeAlgorithm();
         epa.runEPAExpansion(cube, tetrahedron);
         Vector3D collisionVector = new Vector3D(epa.getCollisionVector());
         Point3D pointOnCube = new Point3D(epa.getCollisionPointOnA());
         Point3D pointOnTetrahedron = new Point3D(epa.getCollisionPointOnB());

         double separatingDistance = collisionVector.length();
         assertEquals(0.0, cube.distance(pointOnCube), EPSILON);
         assertEquals(0.0, tetrahedron.distance(pointOnTetrahedron), EPSILON);
         assertEquals(cube.distance(tetrahedronClosest), separatingDistance, EPSILON);

         EuclidCoreTestTools.assertTuple3DEquals(tetrahedronClosest, pointOnTetrahedron, EPSILON);
         assertEquals(tetrahedronClosest.getY(), pointOnCube.getY(), EPSILON);
         assertEquals(tetrahedronClosest.getZ(), pointOnCube.getZ(), EPSILON);
      }
   }

   @Test
   void testCollidingCubeAndPoint()
   {
      Random random = new Random(34534);

      ConvexPolytope3D cube = EuclidPolytopeFactories.newCube(1.0);

      { // Predefined example
         Point3D point = new Point3D(0.25, 0.0, 0.0);
         ConvexPolytope3D singleton = new ConvexPolytope3D(Vertex3DSupplier.asVertex3DSupplier(point));

         ExpandingPolytopeAlgorithm epa = new ExpandingPolytopeAlgorithm();
         epa.runEPAExpansion(cube, singleton);
         Vector3D collisionVector = new Vector3D(epa.getCollisionVector());
         Point3D pointOnCube = new Point3D(epa.getCollisionPointOnA());
         Point3D pointOnSingleton = new Point3D(epa.getCollisionPointOnB());

         assertEquals(0.0, cube.distance(pointOnCube), EPSILON);
         assertEquals(0.0, singleton.distance(pointOnSingleton), EPSILON);
         double separatingDistance = collisionVector.length();
         assertEquals(0.25, separatingDistance, EPSILON);
         assertEquals(pointOnCube.distance(pointOnSingleton), separatingDistance, EPSILON);
         assertEquals(cube.distance(point), separatingDistance, EPSILON);

         EuclidCoreTestTools.assertTuple3DEquals(point, pointOnSingleton, EPSILON);
         assertEquals(point.getY(), pointOnCube.getY(), EPSILON);
         assertEquals(point.getZ(), pointOnCube.getZ(), EPSILON);

      }

      for (int i = 0; i < ITERATIONS; i++)
      {
         Point3D point = EuclidCoreRandomTools.nextPoint3D(random, -0.5, 0.5);
         ConvexPolytope3D tetrahedron = new ConvexPolytope3D(Vertex3DSupplier.asVertex3DSupplier(point));

         ExpandingPolytopeAlgorithm epa = new ExpandingPolytopeAlgorithm();
         epa.runEPAExpansion(cube, tetrahedron);
         Vector3D collisionVector = new Vector3D(epa.getCollisionVector());
         Point3D pointOnCube = new Point3D(epa.getCollisionPointOnA());
         Point3D pointOnSingleton = new Point3D(epa.getCollisionPointOnB());

         double separatingDistance = collisionVector.length();
         assertEquals(pointOnCube.distance(pointOnSingleton), separatingDistance, EPSILON);
         assertEquals(cube.distance(point), separatingDistance, EPSILON);
         assertEquals(0.0, cube.distance(pointOnCube), EPSILON);
         assertEquals(0.0, tetrahedron.distance(pointOnSingleton), EPSILON);

         EuclidCoreTestTools.assertTuple3DEquals(point, pointOnSingleton, EPSILON);
      }
   }

   @Test
   void testCollidingCubeAndTetrahedron()
   {
      Random random = new Random(34534);

      ConvexPolytope3D cube = EuclidPolytopeFactories.newCube(1.0);

      for (int i = 0; i < ITERATIONS; i++)
      { // Superficiel collision, the result is straightforward
         Point3D tetrahedronClosest = new Point3D(0.5, 0.0, 0.0);
         Point3D tetrahedronFarthest0 = new Point3D(100.0, 0.02, 0.0);
         Point3D tetrahedronFarthest1 = new Point3D(100.0, 0.0, 0.02);
         Point3D tetrahedronFarthest2 = new Point3D(100.0, -0.02, 0.0);

         Vector3D translation = new Vector3D();
         translation.setX(EuclidCoreRandomTools.nextDouble(random, -0.25, 0.0)); // Negative => collision
         translation.setY(EuclidCoreRandomTools.nextDouble(random, 0.25));
         translation.setZ(EuclidCoreRandomTools.nextDouble(random, 0.25));

         Arrays.asList(tetrahedronClosest, tetrahedronFarthest0, tetrahedronFarthest1, tetrahedronFarthest2).forEach(p -> p.add(translation));

         ConvexPolytope3D tetrahedron = new ConvexPolytope3D(Vertex3DSupplier.asVertex3DSupplier(tetrahedronClosest, tetrahedronFarthest0, tetrahedronFarthest1,
                                                                                                 tetrahedronFarthest2));

         ExpandingPolytopeAlgorithm epa = new ExpandingPolytopeAlgorithm();
         epa.runEPAExpansion(cube, tetrahedron);
         Vector3D collisionVector = new Vector3D(epa.getCollisionVector());
         Point3D pointOnCube = new Point3D(epa.getCollisionPointOnA());
         Point3D pointOnTetrahedron = new Point3D(epa.getCollisionPointOnB());

         double penetrationDistance = collisionVector.length();
         assertEquals(pointOnCube.distance(pointOnTetrahedron), penetrationDistance, EPSILON);
         assertEquals(0.0, cube.distance(pointOnCube), EPSILON);
         assertEquals(0.0, tetrahedron.distance(pointOnTetrahedron), EPSILON);
         EuclidCoreTestTools.assertTuple3DEquals(tetrahedronClosest, pointOnTetrahedron, EPSILON);

         assertEquals(cube.distance(tetrahedronClosest), penetrationDistance, EPSILON);
         assertEquals(tetrahedronClosest.getY(), pointOnCube.getY(), EPSILON);
         assertEquals(tetrahedronClosest.getZ(), pointOnCube.getZ(), EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Deep collision, check that EPA can be used to resolve collision
         Point3D tetrahedronClosest = EuclidCoreRandomTools.nextPoint3D(random, 0.5);
         Point3D tetrahedronFarthest0 = new Point3D(1.0, 1.2, 0.0);
         Point3D tetrahedronFarthest1 = new Point3D(1.0, 0.0, 1.2);
         Point3D tetrahedronFarthest2 = new Point3D(1.0, -1.2, 0.0);

         ConvexPolytope3D tetrahedron = new ConvexPolytope3D(Vertex3DSupplier.asVertex3DSupplier(tetrahedronClosest, tetrahedronFarthest0, tetrahedronFarthest1,
                                                                                                 tetrahedronFarthest2));

         assertResolvingCollision(cube, tetrahedron);
      }
   }

   @Test
   void testNonCollidingConvexPolytope3DWithTetrahedron() throws Exception
   {
      Random random = new Random(45345);

      for (int i = 0; i < ITERATIONS; i++)
      { // Create the tetrahedron to have its top vertex closest to a face. 
         ConvexPolytope3D convexPolytope3D = EuclidPolytopeRandomTools.nextConvexPolytope3DWithEdgeCases(random);

         if (convexPolytope3D.isEmpty())
         {
            performAssertionsOnEPA(random, convexPolytope3D, EuclidPolytopeRandomTools.nextConeConvexPolytope3D(random), null, null);
            performAssertionsOnEPA(random, EuclidPolytopeRandomTools.nextConeConvexPolytope3D(random), convexPolytope3D, null, null);
         }
         else
         {
            Face3D face = convexPolytope3D.getFace(random.nextInt(convexPolytope3D.getNumberOfFaces()));
            HalfEdge3D edge = face.getEdge(random.nextInt(face.getNumberOfEdges()));
            Point3D closestOnConvexPolytope3D = EuclidShapeRandomTools.nextPoint3DInTriangle(random, face.getCentroid(), edge.getOrigin(),
                                                                                             edge.getDestination());
            Point3D closestOnTetrahedron = new Point3D(closestOnConvexPolytope3D);
            closestOnTetrahedron.scaleAdd(EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0), face.getNormal(), closestOnTetrahedron);

            ConvexPolytope3D tetrahedron = GilbertJohnsonKeerthiCollisionDetectorTest.newTetrahedron(random, closestOnTetrahedron, face.getNormal(), 1.0);

            performAssertionsOnEPA(random, convexPolytope3D, tetrahedron, closestOnConvexPolytope3D, closestOnTetrahedron);
         }
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Create the tetrahedron to have its top vertex closest to an edge. 
         ConvexPolytope3D convexPolytope3D = EuclidPolytopeRandomTools.nextConvexPolytope3DWithEdgeCases(random);

         if (convexPolytope3D.isEmpty())
         {
            performAssertionsOnEPA(random, convexPolytope3D, EuclidPolytopeRandomTools.nextConeConvexPolytope3D(random), null, null);
            performAssertionsOnEPA(random, EuclidPolytopeRandomTools.nextConeConvexPolytope3D(random), convexPolytope3D, null, null);
         }
         else
         {
            Face3D firstFace = convexPolytope3D.getFace(random.nextInt(convexPolytope3D.getNumberOfFaces()));
            HalfEdge3D closestEdge = firstFace.getEdge(random.nextInt(firstFace.getNumberOfEdges()));

            Vector3D towardOutside = new Vector3D();
            if (closestEdge.getTwin() != null)
            {
               Face3D secondFace = closestEdge.getTwin().getFace();
               towardOutside.interpolate(firstFace.getNormal(), secondFace.getNormal(), EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0));
            }
            else
            {
               Vector3D faceNormal = new Vector3D(firstFace.getNormal());
               towardOutside.cross(faceNormal, closestEdge.getDirection(false));
               if (random.nextBoolean())
                  faceNormal.negate();
               towardOutside.interpolate(faceNormal, EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0));
            }

            towardOutside.normalize();

            Point3D pointOnEdge = new Point3D();
            pointOnEdge.interpolate(closestEdge.getOrigin(), closestEdge.getDestination(), EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0));
            Point3D pointOutside = new Point3D();
            pointOutside.scaleAdd(EuclidCoreRandomTools.nextDouble(random, 0.0, 10.0), towardOutside, pointOnEdge);

            ConvexPolytope3D tetrahedron = GilbertJohnsonKeerthiCollisionDetectorTest.newTetrahedron(random, pointOutside, towardOutside, 1.0);

            performAssertionsOnEPA(random, convexPolytope3D, tetrahedron, pointOnEdge, pointOutside);

         }
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Point outside closest to a vertex
         ConvexPolytope3D convexPolytope3D = EuclidPolytopeRandomTools.nextConvexPolytope3DWithEdgeCases(random);

         if (convexPolytope3D.isEmpty())
         {
            performAssertionsOnEPA(random, convexPolytope3D, EuclidPolytopeRandomTools.nextConeConvexPolytope3D(random), null, null);
            performAssertionsOnEPA(random, EuclidPolytopeRandomTools.nextConeConvexPolytope3D(random), convexPolytope3D, null, null);
         }
         else
         {
            Vertex3D closestVertex = convexPolytope3D.getVertex(random.nextInt(convexPolytope3D.getNumberOfVertices()));

            Vector3D towardOutside = new Vector3D();
            closestVertex.getAssociatedEdges().stream().forEach(edge -> towardOutside.scaleAdd(random.nextDouble(), edge.getFace().getNormal(), towardOutside));
            towardOutside.normalize();

            Point3D pointOutside = new Point3D();
            pointOutside.scaleAdd(EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0), towardOutside, closestVertex);

            ConvexPolytope3D tetrahedron = GilbertJohnsonKeerthiCollisionDetectorTest.newTetrahedron(random, pointOutside, towardOutside, 1.0);

            performAssertionsOnEPA(random, convexPolytope3D, tetrahedron, closestVertex, pointOutside);
         }
      }
   }

   public static void performAssertionsOnEPA(Random random, ConvexPolytope3DReadOnly polytopeA, ConvexPolytope3DReadOnly polytopeB,
                                             Point3DReadOnly expectedPointOnA, Point3DReadOnly expectedPointOnB)
   {
      ExpandingPolytopeAlgorithm epa = new ExpandingPolytopeAlgorithm();
      epa.runEPAExpansion(polytopeA, polytopeB);
      Vector3D expectedCollisionVector = new Vector3D();

      if (polytopeA.isEmpty() || polytopeB.isEmpty())
      {
         assertNull(epa.getCollisionVector());
         assertNull(epa.getCollisionPointOnA());
         assertNull(epa.getCollisionPointOnB());
      }
      else
      {
         Point3D actualPointOnA = new Point3D(epa.getCollisionPointOnA());
         Point3D actualPointOnB = new Point3D(epa.getCollisionPointOnB());
         Vector3D actualCollisionVector = new Vector3D(epa.getCollisionVector());

         assertEquals(0.0, polytopeA.distance(actualPointOnA), EPSILON);
         assertEquals(0.0, polytopeB.distance(actualPointOnB), EPSILON);
         EuclidCoreTestTools.assertTuple3DEquals(expectedPointOnA, actualPointOnA, EPSILON);
         EuclidCoreTestTools.assertTuple3DEquals(expectedPointOnB, actualPointOnB, EPSILON);
         expectedCollisionVector.sub(actualPointOnB, actualPointOnA);
         EuclidCoreTestTools.assertTuple3DEquals(expectedCollisionVector, actualCollisionVector, EPSILON);
      }
   }

   public static void assertResolvingCollision(ConvexPolytope3DReadOnly polytopeA, ConvexPolytope3DReadOnly polytopeB)
   {
      assertTrue(new GilbertJohnsonKeerthiCollisionDetector().doCollisionTest(polytopeA, polytopeB));

      ExpandingPolytopeAlgorithm epa = new ExpandingPolytopeAlgorithm();
      epa.runEPAExpansion(polytopeA, polytopeB);
      Point3D pointOnA = new Point3D(epa.getCollisionPointOnA());
      Point3D pointOnB = new Point3D(epa.getCollisionPointOnB());
      Vector3D actualCollisionVector = new Vector3D(epa.getCollisionVector());
      Vector3D expectedCollisionVector = new Vector3D();

      expectedCollisionVector.sub(pointOnA, pointOnB);
      EuclidCoreTestTools.assertTuple3DEquals(expectedCollisionVector, actualCollisionVector, EPSILON);

      assertEquals(0.0, polytopeA.distance(pointOnA), EPSILON);
      assertEquals(0.0, polytopeB.distance(pointOnB), EPSILON);

      Vector3D augmentedCollisionVector = new Vector3D();
      augmentedCollisionVector.setAndScale(0.5, actualCollisionVector);
      ConvexPolytope3D polytopeBTranslated = new ConvexPolytope3D(polytopeB);
      polytopeBTranslated.applyTransform(new RigidBodyTransform(new Quaternion(), augmentedCollisionVector));
      // We translate the polytopeB but not enough to resolve the collision
      assertTrue(new GilbertJohnsonKeerthiCollisionDetector().doCollisionTest(polytopeA, polytopeBTranslated));

      augmentedCollisionVector.setAndScale(1.01, actualCollisionVector);
      polytopeBTranslated = new ConvexPolytope3D(polytopeB);
      polytopeBTranslated.applyTransform(new RigidBodyTransform(new Quaternion(), augmentedCollisionVector));
      // We translate the polytopeB just enough to resolve the collision
      assertFalse(new GilbertJohnsonKeerthiCollisionDetector().doCollisionTest(polytopeA, polytopeBTranslated));
   }
}
