package us.ihmc.euclid.shape.convexPolytope.tools;

import static us.ihmc.euclid.geometry.tools.EuclidGeometryPolygonTools.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;

import us.ihmc.euclid.matrix.Matrix3D;
import us.ihmc.euclid.matrix.interfaces.Matrix3DBasics;
import us.ihmc.euclid.matrix.interfaces.Matrix3DReadOnly;
import us.ihmc.euclid.shape.convexPolytope.Face3D;
import us.ihmc.euclid.shape.convexPolytope.HalfEdge3D;
import us.ihmc.euclid.shape.convexPolytope.Vertex3D;
import us.ihmc.euclid.shape.convexPolytope.interfaces.ConvexPolytope3DReadOnly;
import us.ihmc.euclid.shape.convexPolytope.interfaces.Face3DReadOnly;
import us.ihmc.euclid.shape.convexPolytope.interfaces.Vertex3DReadOnly;
import us.ihmc.euclid.shape.tools.EuclidShapeTools;
import us.ihmc.euclid.tools.TupleTools;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;

public class EuclidPolytopeConstructionTools
{
   public static final double DEFAULT_CONSTRUCTION_EPSILON = 1.0e-10;

   /**
    * Computes the faces containing the given {@code vertex} as follows:
    * <ul>
    * <li>if the vertex is in the plane of a silhouette edge's face, the face is expanded to include
    * the new vertex;
    * <li>otherwise, a new face is created from the vertex and the silhouette edge.
    * </ul>
    * 
    * @param vertex faces are modified and/or created to include this vertex.
    * @param silhouetteEdges the contour visible from the vertex. Each edge is expected to be
    *           associated with either a hidden face or an in-plane face.
    * @param inPlaneFaces the list of faces for which the vertex is considered to lie in the face's
    *           support plane. These faces are expanded to include the new vertex.
    * @param epsilon tolerance used for testing edge-cases such as equivalent vertices, vertex lying on
    *           a line, etc.
    * @return the list of new faces that were created in the the process.
    */
   public static List<Face3D> computeVertexNeighborFaces(Vertex3D vertex, Collection<HalfEdge3D> silhouetteEdges, Collection<Face3D> inPlaneFaces,
                                                         double epsilon)
   {
      List<Face3D> newFaces = new ArrayList<>();

      for (HalfEdge3D silhouetteEdge : silhouetteEdges)
      { // Modify/Create the faces that are to contain the new vertex. The faces will take care of updating the edges.
         if (inPlaneFaces.contains(silhouetteEdge.getFace()))
         { // The face has to be extended to include the new vertex
            silhouetteEdge.getFace().addVertex(vertex);
         }
         else
         { // Creating a new face.
            newFaces.add(newFace3DFromVertexAndTwinEdge(vertex, silhouetteEdge, epsilon));
         }
      }

      for (HalfEdge3D startingFrom : vertex.getAssociatedEdges())
      { // Going through the new edges and associating the twins.
         HalfEdge3D endingTo = startingFrom.getDestination().getEdgeTo(vertex);

         startingFrom.setTwin(endingTo);
         endingTo.setTwin(startingFrom);
      }

      return newFaces;
   }

   /**
    * Creates a new face such that:
    * <ul>
    * <li>the given {@code vertex} belongs to the new face;
    * <li>the new face has an edge that is the twin of the given {@code twinEdge};
    * <li>the new face's normal can be computed using the direction of the given twin-edge.
    * </ul>
    * 
    * @param vertex a vertex of the new face.
    * @param twinEdge the edge which twin's associated face is the new face.
    * @param epsilon tolerance used when testing if a vertex should be added or not.
    * @return the new face.
    */
   public static Face3D newFace3DFromVertexAndTwinEdge(Vertex3D vertex, HalfEdge3D twinEdge, double epsilon)
   { // TODO the epsilon should probably be set to zero here as otherwise we would probably not create the face.
      Vertex3D v1 = twinEdge.getDestination();
      Vertex3D v2 = twinEdge.getOrigin();
      Vertex3D v3 = vertex;

      // Estimate the face's normal based on its vertices and knowing the expecting ordering based on the twin-edge: v1, v2, then v3.
      Vector3D initialNormal = EuclidPolytopeTools.crossProductOfLineSegment3Ds(v1, v2, v2, v3);
      // As the vertices are clock-wise ordered the cross-product of 2 successive edges should be negated to obtain the face's normal.
      initialNormal.negate();

      Face3D face = new Face3D(initialNormal, epsilon);

      face.addVertex(v1);
      face.addVertex(v2);
      face.addVertex(v3);

      HalfEdge3D faceFirstEdge = face.getEdge(0);

      twinEdge.setTwin(faceFirstEdge);
      faceFirstEdge.setTwin(twinEdge);

      return face;
   }

   public static boolean updateFace3DNormal(List<? extends Point3DReadOnly> vertices, Point3DBasics averageToPack, Vector3DBasics normalToUpdate)
   {
      return EuclidPolytopeConstructionTools.updateFace3DNormal(vertices, averageToPack, normalToUpdate, null);
   }

   public static boolean updateFace3DNormal(List<? extends Point3DReadOnly> vertices, Point3DBasics averageToPack, Vector3DBasics normalToUpdate,
                                            Tuple3DBasics eigenValuesToPack)
   {
      Matrix3D covariance = new Matrix3D();
      EuclidPolytopeConstructionTools.computeCovariance3D(vertices, averageToPack, covariance);
      if (eigenValuesToPack == null)
         eigenValuesToPack = new Vector3D();

      Vector3D newNormal = new Vector3D();
      boolean success = EuclidPolytopeConstructionTools.computeEigenVectors(covariance, eigenValuesToPack, null, null, newNormal);
      if (!success)
         return false;

      if (newNormal.dot(normalToUpdate) < 0.0)
         newNormal.negate();

      normalToUpdate.set(newNormal);

      return true;
   }

   public static boolean computeEigenVectors(Matrix3DReadOnly matrix, Tuple3DBasics eigenValues, Vector3DBasics firstEigenVector,
                                             Vector3DBasics secondEigenVector, Vector3DBasics thirdEigenVector)
   {
      DenseMatrix64F denseMatrix = new DenseMatrix64F(3, 3);
      matrix.get(denseMatrix);

      EigenDecomposition<DenseMatrix64F> eig = DecompositionFactory.eig(3, true, true);
      if (!eig.decompose(denseMatrix))
         return false;
      double eigenValue0 = eig.getEigenvalue(0).getReal();
      double eigenValue1 = eig.getEigenvalue(1).getReal();
      double eigenValue2 = eig.getEigenvalue(2).getReal();

      int largeEigenValueIndex, midEigenValueIndex, smallEigenValueIndex;

      if (eigenValue0 > eigenValue1)
      {
         if (eigenValue1 > eigenValue2)
         { // eigenValue0 > eigenValue1 > eigenValue2
            largeEigenValueIndex = 0;
            midEigenValueIndex = 1;
            smallEigenValueIndex = 2;
         }
         else if (eigenValue0 > eigenValue2)
         { // eigenValue0 > eigenValue2 > eigenValue1
            largeEigenValueIndex = 0;
            midEigenValueIndex = 2;
            smallEigenValueIndex = 1;
         }
         else
         { // eigenValue2 > eigenValue0 > eigenValue1
            largeEigenValueIndex = 2;
            midEigenValueIndex = 0;
            smallEigenValueIndex = 1;
         }
      }
      else
      {
         if (eigenValue0 > eigenValue2)
         { // eigenValue1 > eigenValue0 > eigenValue2
            largeEigenValueIndex = 1;
            midEigenValueIndex = 0;
            smallEigenValueIndex = 2;
         }
         else if (eigenValue1 > eigenValue2)
         { // eigenValue1 > eigenValue2 > eigenValue0
            largeEigenValueIndex = 1;
            midEigenValueIndex = 2;
            smallEigenValueIndex = 0;
         }
         else
         { // eigenValue2 > eigenValue1 > eigenValue0
            largeEigenValueIndex = 2;
            midEigenValueIndex = 1;
            smallEigenValueIndex = 0;
         }
      }

      if (eigenValues != null)
      {
         eigenValues.setX(eig.getEigenvalue(largeEigenValueIndex).getReal());
         eigenValues.setY(eig.getEigenvalue(midEigenValueIndex).getReal());
         eigenValues.setZ(eig.getEigenvalue(smallEigenValueIndex).getReal());
      }

      if (firstEigenVector != null)
         firstEigenVector.set(eig.getEigenVector(largeEigenValueIndex));
      if (secondEigenVector != null)
         secondEigenVector.set(eig.getEigenVector(midEigenValueIndex));
      if (thirdEigenVector != null)
         thirdEigenVector.set(eig.getEigenVector(smallEigenValueIndex));

      return true;
   }

   public static void computeCovariance3D(List<? extends Tuple3DReadOnly> input, Tuple3DBasics averageToPack, Matrix3DBasics covarianceToPack)
   {
      double meanX = 0.0;
      double meanY = 0.0;
      double meanZ = 0.0;

      for (int i = 0; i < input.size(); i++)
      {
         Tuple3DReadOnly element = input.get(i);
         meanX += element.getX();
         meanY += element.getY();
         meanZ += element.getZ();
      }

      double inverseOfInputSize = 1.0 / input.size();

      meanX *= inverseOfInputSize;
      meanY *= inverseOfInputSize;
      meanZ *= inverseOfInputSize;

      if (averageToPack != null)
      {
         averageToPack.set(meanX, meanY, meanZ);
      }

      covarianceToPack.setToZero();

      for (int i = 0; i < input.size(); i++)
      {
         Tuple3DReadOnly element = input.get(i);
         double devX = element.getX() - meanX;
         double devY = element.getY() - meanY;
         double devZ = element.getZ() - meanZ;

         double covXX = devX * devX * inverseOfInputSize;
         double covYY = devY * devY * inverseOfInputSize;
         double covZZ = devZ * devZ * inverseOfInputSize;
         double covXY = devX * devY * inverseOfInputSize;
         double covXZ = devX * devZ * inverseOfInputSize;
         double covYZ = devY * devZ * inverseOfInputSize;

         covarianceToPack.addM00(covXX);
         covarianceToPack.addM11(covYY);
         covarianceToPack.addM22(covZZ);
         covarianceToPack.addM01(covXY);
         covarianceToPack.addM10(covXY);
         covarianceToPack.addM02(covXZ);
         covarianceToPack.addM20(covXZ);
         covarianceToPack.addM12(covYZ);
         covarianceToPack.addM21(covYZ);
      }
   }

   public static double computeConvexPolygon3DArea(List<? extends Point3DReadOnly> convexPolygon3D, Vector3DReadOnly normal, int numberOfVertices,
                                                   boolean clockwiseOrdered, Point3DBasics centroidToPack)
   {
      EuclidPolytopeConstructionTools.checkNumberOfVertices(convexPolygon3D, numberOfVertices);

      if (numberOfVertices == 0)
      {
         if (centroidToPack != null)
            centroidToPack.setToNaN();
         return Double.NaN;
      }
      else if (numberOfVertices < 3)
      {
         if (centroidToPack != null)
         {
            centroidToPack.setToZero();
            for (int i = 0; i < numberOfVertices; i++)
               centroidToPack.add(convexPolygon3D.get(i));
            centroidToPack.scale(1.0 / numberOfVertices);
         }
         return 0.0;
      }
      else
      {
         double area = 0.0;
         double Cx = 0.0;
         double Cy = 0.0;
         double Cz = 0.0;

         if (clockwiseOrdered)
         {
            for (int i = 0; i < numberOfVertices; i++)
            {
               Point3DReadOnly ci = convexPolygon3D.get(i);
               Point3DReadOnly ciMinus1 = convexPolygon3D.get(previous(i, numberOfVertices));

               double wx = ci.getY() * ciMinus1.getZ() - ci.getZ() * ciMinus1.getY();
               double wy = ci.getZ() * ciMinus1.getX() - ci.getX() * ciMinus1.getZ();
               double wz = ci.getX() * ciMinus1.getY() - ci.getY() * ciMinus1.getX();
               double weight = TupleTools.dot(wx, wy, wz, normal);

               Cx += (ci.getX() + ciMinus1.getX()) * weight;
               Cy += (ci.getY() + ciMinus1.getY()) * weight;
               Cz += (ci.getZ() + ciMinus1.getZ()) * weight;

               area += weight;
            }
         }
         else
         {
            for (int i = 0; i < numberOfVertices; i++)
            {
               Point3DReadOnly ci = convexPolygon3D.get(i);
               Point3DReadOnly ciPlus1 = convexPolygon3D.get(next(i, numberOfVertices));

               double wx = ci.getY() * ciPlus1.getZ() - ci.getZ() * ciPlus1.getY();
               double wy = ci.getZ() * ciPlus1.getX() - ci.getX() * ciPlus1.getZ();
               double wz = ci.getX() * ciPlus1.getY() - ci.getY() * ciPlus1.getX();
               double weight = TupleTools.dot(wx, wy, wz, normal);

               Cx += (ci.getX() + ciPlus1.getX()) * weight;
               Cy += (ci.getY() + ciPlus1.getY()) * weight;
               Cz += (ci.getZ() + ciPlus1.getZ()) * weight;

               area += weight;
            }
         }

         area *= 0.5;

         if (centroidToPack != null)
         {
            if (area < 1.0e-5)
            {
               centroidToPack.set(convexPolygon3D.get(0));
            }
            else
            {
               double scale = 1.0 / (6.0 * area);
               Cx *= scale;
               Cy *= scale;
               Cz *= scale;

               centroidToPack.set(Cx, Cy, Cz);

               double dot = TupleTools.dot(Cx, Cy, Cz, normal);
               centroidToPack.scaleAdd(-dot, normal, centroidToPack);

               double average = 0.0;

               for (int i = 0; i < numberOfVertices; i++)
               {
                  Point3DReadOnly vertex = convexPolygon3D.get(i);
                  average += TupleTools.dot(vertex, normal) / numberOfVertices;
               }

               centroidToPack.scaleAdd(average, normal, centroidToPack);
            }
         }

         return area;
      }
   }

   public static double computeConvexPolytope3DVolume(ConvexPolytope3DReadOnly convexPolytope3D, Point3DBasics centroidToPack)
   {
      centroidToPack.setToZero();
      double volume = 0.0;

      if (convexPolytope3D.getNumberOfVertices() <= 4)
      {
         for (int vertexIndex = 0; vertexIndex < convexPolytope3D.getNumberOfVertices(); vertexIndex++)
            centroidToPack.add(convexPolytope3D.getVertex(vertexIndex));
         centroidToPack.scale(1.0 / convexPolytope3D.getNumberOfVertices());

         if (convexPolytope3D.getNumberOfVertices() == 4)
            volume = EuclidShapeTools.tetrahedronVolume(convexPolytope3D.getVertex(0), convexPolytope3D.getVertex(1), convexPolytope3D.getVertex(2),
                                                        convexPolytope3D.getVertex(3));
         else
            volume = 0.0;
         return volume;
      }

      Point3DReadOnly a = convexPolytope3D.getFace(0).getVertex(0);

      // We can skip the first face as the vertex 'a' comes from it, so it does not participate in the centroid/volume calculation.
      for (int faceIndex = 1; faceIndex < convexPolytope3D.getNumberOfFaces(); faceIndex++)
      {
         /*
          * Each face is decomposed into triangles from which form tetrahedrons (a, b, c, d) using each
          * triangle vertices plus 'a'. We compute the centroid of these tetrahedrons which are then weighted
          * with their volume.
          */
         Face3DReadOnly face = convexPolytope3D.getFace(faceIndex);
         int numberOfTriangles = face.getNumberOfEdges() - 2;
         Vertex3DReadOnly b = face.getVertex(0);

         for (int triangleIndex = 0; triangleIndex < numberOfTriangles; triangleIndex++)
         {
            Vertex3DReadOnly c = face.getVertex(triangleIndex + 1);
            Vertex3DReadOnly d = face.getVertex(triangleIndex + 2);
            double tetrahedronVolume = EuclidShapeTools.tetrahedronVolume(a, b, c, d);
            // The centroid of the tetrahedron is: tetrahedronCentroid = (a + b + c + d) / 4.0
            // The centroid of the polytope is updated as: centroid += tetrahedronVolume * tetrahedronCentroid
            // which is equivalent to the following:
            double scale = 0.25 * tetrahedronVolume;
            centroidToPack.scaleAdd(scale, a, centroidToPack);
            centroidToPack.scaleAdd(scale, b, centroidToPack);
            centroidToPack.scaleAdd(scale, c, centroidToPack);
            centroidToPack.scaleAdd(scale, d, centroidToPack);
            volume += tetrahedronVolume;
         }
      }

      centroidToPack.scale(1.0 / volume);

      return volume;
   }

   static void checkNumberOfVertices(List<? extends Point3DReadOnly> convexPolygon3D, int numberOfVertices)
   {
      if (numberOfVertices < 0 || numberOfVertices > convexPolygon3D.size())
         throw new IllegalArgumentException("Illegal numberOfVertices: " + numberOfVertices + ", expected a value in ] 0, " + convexPolygon3D.size() + "].");
   }

}
