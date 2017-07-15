package us.ihmc.euclid.referenceFrame;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import us.ihmc.euclid.referenceFrame.exceptions.ReferenceFrameMismatchException;
import us.ihmc.euclid.referenceFrame.interfaces.FrameTuple3DReadOnly;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DReadOnly;

public class FramePoint3DTest extends FrameTuple3DTest<FramePoint3D, Point3D>
{
   public static double epsilon = 1e-10;

   @Override
   public FramePoint3D createTuple(ReferenceFrame referenceFrame, double x, double y, double z)
   {
      return createFrameTuple(referenceFrame, x, y, z);
   }

   @Override
   public FramePoint3D createEmptyFrameTuple()
   {
      return new FramePoint3D();
   }

   @Override
   public FramePoint3D createFrameTuple(ReferenceFrame referenceFrame, double x, double y, double z)
   {
      return new FramePoint3D(referenceFrame, x, y, z);
   }

   @Override
   public double getEpsilon()
   {
      return 1.0e-15;
   }

   @Test
   public final void testRunTestMain()
   {
      // create frames
      ReferenceFrame A = ReferenceFrame.constructARootFrame("A");

      RigidBodyTransform transform = new RigidBodyTransform();
      Vector3D euler = new Vector3D(Math.PI / 2.0, 0.0, 0.0);
      transform.setRotationEulerAndZeroTranslation(euler);
      Vector3D translation = new Vector3D(5.0, 0.0, 0.0);
      transform.setTranslation(translation);

      //    System.out.println("B translation = \n" + transform);
      ReferenceFrame B = ReferenceFrame.constructFrameWithUnchangingTransformToParent("B", A, transform);

      transform = new RigidBodyTransform();
      euler = new Vector3D(0.0, Math.PI / 2.0, 0.0);
      transform.setRotationEulerAndZeroTranslation(euler);
      translation = new Vector3D(5.0, 0.0, 0.0);
      transform.setTranslation(translation);

      //    System.out.println("C translation = \n" + transform);
      ReferenceFrame C = ReferenceFrame.constructFrameWithUnchangingTransformToParent("C", B, transform);

      transform = new RigidBodyTransform();
      euler = new Vector3D(0.0, 0.0, Math.PI / 2.0);
      transform.setRotationEulerAndZeroTranslation(euler);
      translation = new Vector3D(5.0, 0.0, 0.0);
      transform.setTranslation(translation);

      //    System.out.println("D translation = \n" + transform);
      ReferenceFrame D = ReferenceFrame.constructFrameWithUnchangingTransformToParent("D", C, transform);

      FramePoint3D V1 = new FramePoint3D(D, 2.0, 3.0, 4.0);

      // System.out.println("V1 = " + V1);
      assertEquals(2, V1.getX(), epsilon);
      assertEquals(3, V1.getY(), epsilon);
      assertEquals(4, V1.getZ(), epsilon);

      try
      {
         FramePoint3D V1inD = new FramePoint3D(V1);
         V1inD.changeFrame(D);

         //       System.out.println(V1inD);
         assertEquals(2, V1inD.getX(), epsilon);
         assertEquals(3, V1inD.getY(), epsilon);
         assertEquals(4, V1inD.getZ(), epsilon);

         FramePoint3D V1inC = new FramePoint3D(V1);
         V1inC.changeFrame(C);

         //       System.out.println(V1inC);
         assertEquals(2, V1inC.getX(), epsilon);
         assertEquals(2, V1inC.getY(), epsilon);
         assertEquals(4, V1inC.getZ(), epsilon);

         FramePoint3D V1inB = new FramePoint3D(V1);
         V1inB.changeFrame(B);

         //       System.out.println(V1inB);
         assertEquals(9, V1inB.getX(), epsilon);
         assertEquals(2, V1inB.getY(), epsilon);
         assertEquals(-2, V1inB.getZ(), epsilon);

         FramePoint3D V1inA = new FramePoint3D(V1);
         V1inA.changeFrame(A);

         //       System.out.println(V1inA);
         assertEquals(14, V1inA.getX(), epsilon);
         assertEquals(2, V1inA.getY(), epsilon);
         assertEquals(2, V1inA.getZ(), epsilon);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail();
      }

      V1 = new FramePoint3D(A, 2.0, 3.0, 4.0);
      assertEquals(2, V1.getX(), epsilon);
      assertEquals(3, V1.getY(), epsilon);
      assertEquals(4, V1.getZ(), epsilon);

      // System.out.println("V2 = " + V1);
      try
      {
         FramePoint3D V1inA = new FramePoint3D(V1);
         V1inA.changeFrame(A);
         assertEquals(2, V1inA.getX(), epsilon);
         assertEquals(3, V1inA.getY(), epsilon);
         assertEquals(4, V1inA.getZ(), epsilon);

         FramePoint3D V1inB = new FramePoint3D(V1);
         V1inB.changeFrame(B);
         assertEquals(-3, V1inB.getX(), epsilon);
         assertEquals(4, V1inB.getY(), epsilon);
         assertEquals(-3, V1inB.getZ(), epsilon);

         FramePoint3D V1inC = new FramePoint3D(V1);
         V1inC.changeFrame(C);
         assertEquals(3, V1inC.getX(), epsilon);
         assertEquals(4, V1inC.getY(), epsilon);
         assertEquals(-8, V1inC.getZ(), epsilon);

         FramePoint3D V1inD = new FramePoint3D(V1);
         V1inD.changeFrame(D);
         assertEquals(4, V1inD.getX(), epsilon);
         assertEquals(2, V1inD.getY(), epsilon);
         assertEquals(-8, V1inD.getZ(), epsilon);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail();
      }

      V1 = new FramePoint3D(B, 2.0, 3.0, 4.0);
      assertEquals(2, V1.getX(), epsilon);
      assertEquals(3, V1.getY(), epsilon);
      assertEquals(4, V1.getZ(), epsilon);

      try
      {
         FramePoint3D V1inA = new FramePoint3D(V1);
         V1inA.changeFrame(A);
         assertEquals(7, V1inA.getX(), epsilon);
         assertEquals(-4, V1inA.getY(), epsilon);
         assertEquals(3, V1inA.getZ(), epsilon);
         FramePoint3D V1inB = new FramePoint3D(V1);
         V1inB.changeFrame(B);
         assertEquals(2, V1inB.getX(), epsilon);
         assertEquals(3, V1inB.getY(), epsilon);
         assertEquals(4, V1inB.getZ(), epsilon);
         FramePoint3D V1inC = new FramePoint3D(V1);
         V1inC.changeFrame(C);
         assertEquals(-4, V1inC.getX(), epsilon);
         assertEquals(3, V1inC.getY(), epsilon);
         assertEquals(-3, V1inC.getZ(), epsilon);
         FramePoint3D V1inD = new FramePoint3D(V1);
         V1inD.changeFrame(D);
         assertEquals(3, V1inD.getX(), epsilon);
         assertEquals(9, V1inD.getY(), epsilon);
         assertEquals(-3, V1inD.getZ(), epsilon);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail();
      }

   }

   @Test
   public void testOtherConstructors() //Brett was here
   {
      Tuple3DBasics position = new Point3D(1.0, 1.0, 1.0);
      FramePoint3D framePosition = new FramePoint3D(theFrame, position);
      assertEquals("These should be equal", position, framePosition.getPoint());
      assertEquals("These should be equal", theFrame, framePosition.getReferenceFrame());

      FramePoint3D framePositionName = new FramePoint3D(aFrame, position);
      assertEquals("These should be equal", position, framePositionName.getPoint());
      assertEquals("These should be equal", aFrame, framePositionName.getReferenceFrame());

      double[] doubleArray = {7.0, 7.0, 7.0};
      Tuple3DBasics position2 = new Point3D(doubleArray);
      FramePoint3D framePositionArray = new FramePoint3D(theFrame, doubleArray);
      assertEquals("These should be equal", position2, framePositionArray.getPoint());
      assertEquals("These should be equal", theFrame, framePositionArray.getReferenceFrame());

      double[] doubleArray2 = {-7.0, 14.0, 21.0};
      Tuple3DBasics position3 = new Point3D(doubleArray2);
      FramePoint3D framePositionArrayName = new FramePoint3D(theFrame, doubleArray2);
      assertEquals("These should be equal", position3, framePositionArrayName.getPoint());
      assertEquals("These should be equal", theFrame, framePositionArrayName.getReferenceFrame());

      FramePoint3D frameName = new FramePoint3D(theFrame);
      Tuple3DBasics position4 = new Point3D();
      assertEquals("These should be equal", position4, frameName.getPoint());
      assertEquals("These should be equal", theFrame, frameName.getReferenceFrame());
   }

   @Test
   public void testGetXYplaneDistance()
   {
      FramePoint3D firstPoint = new FramePoint3D(theFrame, 1.0, 2.0, 5.0);
      FramePoint3D secondPoint = new FramePoint3D(theFrame, 4.0, -2.0, -3.0);

      assertEquals(5.0, firstPoint.distanceXY(secondPoint), epsilon);

      //Test for reference frame mismatch
      FramePoint3D thirdPoint = new FramePoint3D(aFrame, 4.0, -2.0, -3.0);
      try
      {
         firstPoint.distanceXY(thirdPoint);
         fail("Should have thrown a " + ReferenceFrameMismatchException.class.getSimpleName());
      }
      catch (ReferenceFrameMismatchException e)
      {
         // Good.
      }
   }

   @Test
   public void testDistance() //Brett
   {
      FramePoint3D framePoint1 = new FramePoint3D(theFrame);
      FramePoint3D framePoint2 = new FramePoint3D(aFrame);

      FramePoint3D framePoint = new FramePoint3D(theFrame, 1.0, 2.0, 3.0);
      double expectedReturn = Math.sqrt(14.0);
      double actualReturn = framePoint1.distance(framePoint);
      assertEquals("return value", expectedReturn, actualReturn, Double.MIN_VALUE);

      //Test for reference frame mismatch
      try
      {
         framePoint1.distance(framePoint2);
         fail("Should have thrown a " + ReferenceFrameMismatchException.class.getSimpleName());
      }
      catch (ReferenceFrameMismatchException e)
      {
         // Good.
      }
   }

   @Test
   public void testDistanceSquared() //Brett
   {
      FramePoint3D framePoint1 = new FramePoint3D(theFrame);
      FramePoint3D framePoint2 = new FramePoint3D(aFrame);

      FramePoint3D framePoint = new FramePoint3D(theFrame, 1.0, 2.0, 3.0);
      double expectedReturn = 14.0;
      double actualReturn = framePoint.distanceSquared(framePoint1);
      assertEquals("return value", expectedReturn, actualReturn, Double.MIN_VALUE);

      //Test for reference frame mismatch
      try
      {
         framePoint1.distanceSquared(framePoint2);
         fail("Should have thrown a " + ReferenceFrameMismatchException.class.getSimpleName());
      }
      catch (ReferenceFrameMismatchException e)
      {
         // Good.
      }
   }

   @Test
   public void testGetPoint()
   {
      Point3D tuple3d = new Point3D(1.0, 1.0, 1.0);
      Point3D tuple3dCopy = new Point3D();
      FramePoint3D framePoint = new FramePoint3D(theFrame, tuple3d);

      tuple3dCopy = framePoint.getPoint();
      assertTrue(tuple3d.epsilonEquals(tuple3dCopy, epsilon));
   }

   @Test
   public void testFrameChanges()
   {
      FramePoint3D framePoint = new FramePoint3D(theFrame);
      FramePoint3D result = new FramePoint3D(framePoint);

      result = new FramePoint3D(framePoint);
      result.changeFrame(theFrame);
      result.checkReferenceFrameMatch(theFrame);

      framePoint.changeFrame(theFrame); //cause of failure
      framePoint.checkReferenceFrameMatch(theFrame);
   }

   @Test
   public final void testApplyTransform()
   {
      FramePoint3D frameTuple = new FramePoint3D(theFrame);

      RigidBodyTransform transform = new RigidBodyTransform();
      transform.setTranslation(new Vector3D(2.0, 4.0, 8.0));
      frameTuple.applyTransform(transform);

      assertEquals(2.0, frameTuple.getX(), epsilon);
      assertEquals(4.0, frameTuple.getY(), epsilon);
      assertEquals(8.0, frameTuple.getZ(), epsilon);
      frameTuple.applyTransform(transform);
      assertEquals(4.0, frameTuple.getX(), epsilon);
      assertEquals(8.0, frameTuple.getY(), epsilon);
      assertEquals(16.0, frameTuple.getZ(), epsilon);
   }

   @Test
   public final void testApplyTransformScale()
   {
      FramePoint3D framePoint = new FramePoint3D(theFrame, 1.0, 2.0, 3.0);
      AffineTransform transform3D = new AffineTransform();
      transform3D.setScale(0.1, 0.01, 0.001);

      FramePoint3D resultPoint = new FramePoint3D(framePoint);
      resultPoint.applyTransform(transform3D);
      FramePoint3D expectedResultPoint = new FramePoint3D(theFrame, 0.1, 0.02, 0.003);

      assertNotSame(framePoint, resultPoint);
      assertTrue(expectedResultPoint.epsilonEquals(resultPoint, epsilon));
   }

   @Test
   public final void testApplyTransformTranslate()
   {
      FramePoint3D framePoint = new FramePoint3D(theFrame, 1.0, 2.0, 3.0);
      AffineTransform transform3D = new AffineTransform();

      Vector3D translateVector = new Vector3D(0.1, 0.5, 0.9);
      transform3D.setTranslation(translateVector);
      FramePoint3D resultPoint = new FramePoint3D(framePoint);
      resultPoint.applyTransform(transform3D);
      FramePoint3D expectedResultPoint = new FramePoint3D(theFrame, 1.1, 2.5, 3.9);

      assertNotSame(framePoint, resultPoint);
      assertTrue(expectedResultPoint.epsilonEquals(resultPoint, epsilon));
   }

   @Test
   public final void testApplyTransformRotateZ()
   {
      FramePoint3D framePoint = new FramePoint3D(theFrame, 1.0, 2.0, 3.0);
      RigidBodyTransform transform3D = new RigidBodyTransform();

      Vector3D rotateVector = new Vector3D(0.0, 0.0, Math.PI / 2.0);
      transform3D.setRotationEulerAndZeroTranslation(rotateVector);
      FramePoint3D resultPoint = new FramePoint3D(framePoint);
      resultPoint.applyTransform(transform3D);
      FramePoint3D expectedResultPoint = new FramePoint3D(theFrame, -2.0, 1.0, 3.0);

      assertNotSame(framePoint, resultPoint);
      assertTrue(expectedResultPoint.epsilonEquals(resultPoint, epsilon));
   }

   @Test
   public void testConstructors()
   {
      Random random = new Random();
      double[] xyz = new double[] {EuclidCoreRandomTools.generateRandomDouble(random), EuclidCoreRandomTools.generateRandomDouble(random),
            EuclidCoreRandomTools.generateRandomDouble(random)};
      FramePoint3D pointToBeTested;
      ReferenceFrame referenceFrame = null;
      pointToBeTested = new FramePoint3D(referenceFrame, xyz);

      pointToBeTested = new FramePoint3D(aFrame, xyz);
      Point3D point3dExpected = new Point3D(xyz);
      assertTrue(aFrame == pointToBeTested.getReferenceFrame());
      assertTrue(pointToBeTested.getPoint().epsilonEquals(point3dExpected, epsilon));

      double max = Double.MAX_VALUE / 2.0;
      point3dExpected = EuclidCoreRandomTools.generateRandomPoint3D(random, max, max, max);
      pointToBeTested = new FramePoint3D(referenceFrame, point3dExpected);

      pointToBeTested = new FramePoint3D(aFrame, point3dExpected);
      assertTrue(aFrame == pointToBeTested.getReferenceFrame());
      assertTrue("Expected: " + point3dExpected + ", actual: " + pointToBeTested.getPoint(),
                 point3dExpected.epsilonEquals(pointToBeTested.getPoint(), epsilon));

      xyz = new double[] {EuclidCoreRandomTools.generateRandomDouble(random), EuclidCoreRandomTools.generateRandomDouble(random),
            EuclidCoreRandomTools.generateRandomDouble(random)};
      pointToBeTested = new FramePoint3D(referenceFrame, xyz);

      pointToBeTested = new FramePoint3D(aFrame, xyz);
      point3dExpected = new Point3D(xyz);
      assertTrue(aFrame == pointToBeTested.getReferenceFrame());
      assertTrue(pointToBeTested.getPoint().epsilonEquals(point3dExpected, epsilon));
   }

   @Override
   public void testOverloading() throws Exception
   {
      super.testOverloading();
      assertSuperMethodsAreOverloaded(FrameTuple3DReadOnly.class, Tuple3DReadOnly.class, FramePoint3D.class, Point3DBasics.class);
   }
}
