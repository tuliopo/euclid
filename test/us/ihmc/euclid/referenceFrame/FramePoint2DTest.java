package us.ihmc.euclid.referenceFrame;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Test;

import us.ihmc.euclid.referenceFrame.exceptions.ReferenceFrameMismatchException;
import us.ihmc.euclid.referenceFrame.interfaces.FrameTuple2DReadOnly;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple2D.Point2D;
import us.ihmc.euclid.tuple2D.interfaces.Point2DBasics;
import us.ihmc.euclid.tuple2D.interfaces.Point2DReadOnly;
import us.ihmc.euclid.tuple2D.interfaces.Tuple2DReadOnly;
import us.ihmc.euclid.tuple3D.Point3D;

public class FramePoint2DTest extends FrameTuple2DTest<FramePoint2D>
{

   @Override
   public FramePoint2D createTuple(ReferenceFrame referenceFrame, double x, double y)
   {
      return createFrameTuple(referenceFrame, x, y);
   }

   @Override
   public FramePoint2D createFrameTuple(ReferenceFrame referenceFrame, double x, double y)
   {
      return new FramePoint2D(referenceFrame, x, y);
   }

   @After
   public void tearDown() throws Exception
   {
   }

   @Override
   public double getEpsilon()
   {
      return 1.0e-15;
   }

   @Test
   public void testOne()
   {
      FramePoint2D point = new FramePoint2D(worldFrame, 1.0, 2.0);

      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame_double_double_String()
   {
      FramePoint2D point = new FramePoint2D(worldFrame, 1.0, 2.0);

      point.checkReferenceFrameMatch(worldFrame);

      try
      {
         point.checkReferenceFrameMatch(theFrame);
         fail("Should have thrown ReferenceFrameMismatchException");
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good
      }
      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_FrameTuple2d()
   {
      FrameTuple2D<?, ?> frameTuple = createFrameTuple(theFrame, 1.0, 2.0);
      FramePoint2D point = new FramePoint2D(frameTuple);

      point.checkReferenceFrameMatch(theFrame);

      try
      {
         point.checkReferenceFrameMatch(aFrame);
         fail("Should have thrown ReferenceFrameMismatchException");
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good
      }

      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame()
   {
      FramePoint2D point = new FramePoint2D(theFrame);
      point.checkReferenceFrameMatch(theFrame);

      try
      {
         point.checkReferenceFrameMatch(aFrame);
         fail("Should have thrown ReferenceFrameMismatchException");
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good
      }
      assertEquals(0.0, point.getX(), 1e-7);
      assertEquals(0.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d()
   {
      FramePoint2D point = new FramePoint2D();

      point.checkReferenceFrameMatch(worldFrame);

      try
      {
         point.checkReferenceFrameMatch(theFrame);
         fail("Should have thrown ReferenceFrameMismatchException");
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good
      }
      assertEquals(0.0, point.getX(), 1e-7);
      assertEquals(0.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame_double_double()
   {
      FramePoint2D point = new FramePoint2D(worldFrame, 1.0, 2.0);
      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame_double()
   {
      double[] position = {1.0, 2.0};
      FramePoint2D point = new FramePoint2D(worldFrame, position);
      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame_double_String()
   {
      double[] position = {1.0, 2.0};
      FramePoint2D point = new FramePoint2D(worldFrame, position);
      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame_String()
   {
      FramePoint2D point = new FramePoint2D(worldFrame);
      assertEquals(0.0, point.getX(), 1e-7);
      assertEquals(0.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame_Tuple2d()
   {
      Point2D position = new Point2D(1.0, 2.0);
      FramePoint2D point = new FramePoint2D(worldFrame, position);
      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testFramePoint2d_ReferenceFrame_Tuple2d_String()
   {
      Point2D position = new Point2D(1.0, 2.0);
      FramePoint2D point = new FramePoint2D(worldFrame, position);
      assertEquals(1.0, point.getX(), 1e-7);
      assertEquals(2.0, point.getY(), 1e-7);
   }

   @Test
   public void testDistance_FramePoint2d()
   {
      FramePoint2D point1 = new FramePoint2D(theFrame, 1.0, 2.0);
      FramePoint2D point2 = new FramePoint2D(theFrame, 3.0, 4.0);
      double num = sumOfSquares(point1, point2);
      assertEquals("Should be equal", Math.sqrt(num), point1.distance(point2), epsilon);

      FramePoint2D point3 = new FramePoint2D(theFrame, 1.0, 2.0);
      num = sumOfSquares(point1, point3);
      assertEquals("Should be equal", 0.0, point1.distance(point3), epsilon);

      try
      {
         FramePoint2D point4 = new FramePoint2D(aFrame, 1.0, 2.0);
         point1.distance(point4);
         fail();
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good
      }
   }

   @Test
   public void testDistanceSquared_FramePoint2d()
   {
      FramePoint2D point1 = new FramePoint2D(theFrame, 1.0, 2.0);
      FramePoint2D point2 = new FramePoint2D(theFrame, 3.0, 4.0);
      double num = sumOfSquares(point1, point2);
      assertEquals("Should be equal", num, point1.distanceSquared(point2), epsilon);

      FramePoint2D point3 = new FramePoint2D(theFrame, 1.0, 2.0);
      num = sumOfSquares(point1, point3);
      assertEquals("Should be equal", num, point1.distanceSquared(point3), epsilon);

      try
      {
         FramePoint2D point4 = new FramePoint2D(aFrame, 1.0, 2.0);
         point1.distanceSquared(point4);
         fail();
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good
      }
   }

   @Test
   public void testGetPoint()
   {
      FramePoint2D point1 = new FramePoint2D(theFrame, 1.0, 2.0);
      Point2DReadOnly point2d = point1.getPoint();
      assertEquals("Should be equal", point1.getX(), point2d.getX(), epsilon);
      assertEquals("Should be equal", point1.getY(), point2d.getY(), epsilon);
   }

   @Test
   public void testApplyTransform_Transform3D_boolean()
   {
      boolean requireTransformInPlane = false;
      Random random = new Random(398742498237598750L);
      RigidBodyTransform transform = EuclidCoreRandomTools.generateRandomRigidBodyTransform(random);

      Point3D pointToTransform = EuclidCoreRandomTools.generateRandomPoint3D(random, 100.0, 100.0, 0.0);
      FramePoint2D pointToTest = new FramePoint2D(null, new Point2D(pointToTransform.getX(), pointToTransform.getY()));

      transform.transform(pointToTransform);
      pointToTest.applyTransform(transform, requireTransformInPlane);

      //transform on Point2d gives same result as FramePoint2d, transform not required in plane
      assertEquals("Should be equal", pointToTransform.getX(), pointToTest.getX(), epsilon);
      assertEquals("Should be equal", pointToTransform.getY(), pointToTest.getY(), epsilon);
      try
      {
         pointToTest.applyTransform(transform, true);
         fail("Should have thrown RuntimeException");
      }
      catch (RuntimeException re)
      {
         //Good
      }

      double[] matrix = {6.0, 7.0, 0.0};
      RigidBodyTransform transform2 = EuclidCoreRandomTools.generateRandomRigidBodyTransform2D(random);

      Point3D pointToTransform2 = new Point3D(matrix);
      FramePoint2D pointToTest2 = new FramePoint2D(null, matrix);

      transform2.transform(pointToTransform2);
      pointToTest2.applyTransform(transform2, true);

      //transform on Point2d gives same result as FramePoint2d, transform required in plane
      assertEquals("Should be equal", pointToTransform2.getX(), pointToTest2.getX(), epsilon);
      assertEquals("Should be equal", pointToTransform2.getY(), pointToTest2.getY(), epsilon);
   }

   @Test
   public void testApplyTransform_Transform3D()
   {
      Random random = new Random(398742498237598750L);
      RigidBodyTransform transform = EuclidCoreRandomTools.generateRandomRigidBodyTransform(random);

      Point3D pointToTransform = EuclidCoreRandomTools.generateRandomPoint3D(random, 100.0, 100.0, 0.0);
      FramePoint2D pointToTest = new FramePoint2D(null, new Point2D(pointToTransform.getX(), pointToTransform.getY()));

      try
      {
         pointToTest.applyTransform(transform);
         fail("Should have thrown RuntimeException");
      }
      catch (RuntimeException re)
      {
         //Good
      }

      double[] matrix = {6.0, 7.0};
      RigidBodyTransform transform2 = EuclidCoreRandomTools.generateRandomRigidBodyTransform2D(random);
      FramePoint2D pointToTransform2 = new FramePoint2D(null, matrix);
      FramePoint2D pointToTest2 = new FramePoint2D(null, matrix);

      pointToTransform2.applyTransform(transform2);
      pointToTest2.applyTransform(transform2, true);

      assertEquals("Should be equal", pointToTransform2.getX(), pointToTest2.getX(), epsilon);
      assertEquals("Should be equal", pointToTransform2.getY(), pointToTest2.getY(), epsilon);
   }

   //	@DeployableTestMethod(estimatedDuration = 0.0)
   //	@Test
   //   public void testApplyTransformCopy_Transform3D()
   //   {
   //      double[] matrix = { 6.0, 7.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.0 };
   //      RigidBodyTransform transform = new RigidBodyTransform(matrix);
   //      FramePoint2d pointToTransform2 = new FramePoint2d(null, matrix);
   //      FramePoint2d pointToTest2 = new FramePoint2d(null, matrix);
   //
   //      pointToTransform2.applyTransform(transform);
   //      FramePoint2d copy = pointToTest2.applyTransformCopy(transform);
   //
   //      assertEquals("Should be equal", pointToTransform2.getX(), copy.getX(), epsilon);
   //      assertEquals("Should be equal", pointToTransform2.getY(), copy.getY(), epsilon);
   //   }

   @Test
   public void testChangeFrame_ReferenceFrame()
   {
      FramePoint2D frame = new FramePoint2D(theFrame);
      frame.changeFrame(theFrame);

      frame.changeFrame(childFrame);
      frame.checkReferenceFrameMatch(childFrame);
      try
      {
         frame.checkReferenceFrameMatch(theFrame);
         fail("Should have thrown ReferenceFrameMismatchException");
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good 
      }
   }

   @Test
   public void testChangeFrameAndProjectToXYPlane_ReferenceFrame()
   {
      FramePoint2D frame = new FramePoint2D(theFrame);
      frame.changeFrameAndProjectToXYPlane(theFrame);

      frame.changeFrameAndProjectToXYPlane(childFrame);
      frame.checkReferenceFrameMatch(childFrame);
      try
      {
         frame.checkReferenceFrameMatch(theFrame);
         fail("Should have thrown ReferenceFrameMismatchException");
      }
      catch (ReferenceFrameMismatchException rfme)
      {
         //Good 
      }
   }

   double sumOfSquares(FramePoint2D framePoint1, FramePoint2D framePoint2)
   {
      double ret = (framePoint2.getX() - framePoint1.getX()) * (framePoint2.getX() - framePoint1.getX())
            + (framePoint2.getY() - framePoint1.getY()) * (framePoint2.getY() - framePoint1.getY());
      return ret;
   }

   @Override
   public void testOverloading() throws Exception
   {
      super.testOverloading();
      FrameTuple3DReadOnlyTest.assertSuperMethodsAreOverloaded(FrameTuple2DReadOnly.class, Tuple2DReadOnly.class, FramePoint2D.class, Point2DBasics.class);
   }
}
