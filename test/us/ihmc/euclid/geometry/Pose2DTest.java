package us.ihmc.euclid.geometry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import us.ihmc.euclid.geometry.tools.EuclidGeometryRandomTools;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.tuple2D.Vector2D;

public class Pose2DTest
{
   private static final int ITERATIONS = 1000;

   @Test
   public void testGeometricallyEquals()
   {
      Random random = new Random(19732L);
      Pose2D firstPose, secondPose;
      double epsilon = 1e-7;
      Vector2D perturb;

      firstPose = EuclidGeometryRandomTools.nextPose2D(random);
      secondPose = new Pose2D(firstPose);

      assertTrue(firstPose.geometricallyEquals(secondPose, epsilon));
      assertTrue(secondPose.geometricallyEquals(firstPose, epsilon));
      assertTrue(firstPose.geometricallyEquals(firstPose, epsilon));
      assertTrue(secondPose.geometricallyEquals(secondPose, epsilon));

      // Orientation
      for (int i = 0; i < ITERATIONS; ++i)
      { // Poses are equal if orientations are equal within +- epsilon points are the same
         firstPose = EuclidGeometryRandomTools.nextPose2D(random);
         secondPose = new Pose2D(firstPose);

         secondPose.setYaw(firstPose.getYaw() + 0.99 * epsilon);

         assertTrue(firstPose.geometricallyEquals(secondPose, epsilon));

         secondPose.setYaw(firstPose.getYaw() - 0.99 * epsilon);

         assertTrue(firstPose.geometricallyEquals(secondPose, epsilon));

         secondPose.setYaw(firstPose.getYaw() + 1.01 * epsilon);

         assertFalse(firstPose.geometricallyEquals(secondPose, epsilon));

         secondPose.setYaw(firstPose.getYaw() - 1.01 * epsilon);

         assertFalse(firstPose.geometricallyEquals(secondPose, epsilon));
      }

      // Point
      for (int i = 0; i < ITERATIONS; ++i)
      { // Poses are equal if points are equal within +- epsilon and orientations are the same
         firstPose = EuclidGeometryRandomTools.nextPose2D(random);
         secondPose = new Pose2D(firstPose);

         perturb = EuclidCoreRandomTools.nextVector2DWithFixedLength(random, 0.99 * epsilon);
         perturb.add(secondPose.getPosition());
         secondPose.setPosition(perturb);

         assertTrue(firstPose.geometricallyEquals(secondPose, epsilon));

         secondPose = new Pose2D(firstPose);
         perturb = EuclidCoreRandomTools.nextVector2DWithFixedLength(random, 1.01 * epsilon);
         perturb.add(secondPose.getPosition());
         secondPose.setPosition(perturb);

         assertFalse(firstPose.geometricallyEquals(secondPose, epsilon));
      }
   }
}