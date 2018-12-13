package us.ihmc.euclid.referenceFrame;

import static us.ihmc.euclid.testSuite.EuclidTestSuite.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import us.ihmc.euclid.matrix.Matrix3D;
import us.ihmc.euclid.matrix.interfaces.Matrix3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameMatrix3DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FrameMatrix3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.ReferenceFrameHolder;
import us.ihmc.euclid.referenceFrame.tools.EuclidFrameAPITestTools;
import us.ihmc.euclid.referenceFrame.tools.EuclidFrameRandomTools;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.tools.EuclidCoreTestTools;

public class FrameMatrix3DTest extends FrameMatrix3DReadOnlyTest<FrameMatrix3D>
{
   public static final double EPSILON = 1.0e-15;

   @Override
   public FrameMatrix3D createFrameMatrix3D(ReferenceFrame referenceFrame, Matrix3DReadOnly pose)
   {
      return new FrameMatrix3D(referenceFrame, pose);
   }

   @Test
   public void testConsistencyWithMatrix3D()
   {
      Random random = new Random(234235L);

      EuclidFrameAPITestTools.FrameTypeBuilder<? extends ReferenceFrameHolder> frameTypeBuilder = (frame,
                                                                                                   matrix) -> createFrameMatrix3D(frame,
                                                                                                                                  (Matrix3DReadOnly) matrix);
      EuclidFrameAPITestTools.GenericTypeBuilder framelessTypeBuilder = () -> EuclidCoreRandomTools.nextMatrix3D(random);
      Predicate<Method> methodFilter = m -> !m.getName().equals("hashCode") && !m.getName().equals("epsilonEquals");
      EuclidFrameAPITestTools.assertFrameMethodsOfFrameHolderPreserveFunctionality(frameTypeBuilder, framelessTypeBuilder, methodFilter);
   }

   @Override
   @Test
   public void testOverloading() throws Exception
   {
      super.testOverloading();
      Map<String, Class<?>[]> framelessMethodsToIgnore = new HashMap<>();
      framelessMethodsToIgnore.put("set", new Class<?>[] {Matrix3D.class});
      framelessMethodsToIgnore.put("equals", new Class<?>[] {Matrix3D.class});
      framelessMethodsToIgnore.put("epsilonEquals", new Class<?>[] {Matrix3D.class, Double.TYPE});
      framelessMethodsToIgnore.put("geometricallyEquals", new Class<?>[] {Matrix3D.class, Double.TYPE});
      EuclidFrameAPITestTools.assertOverloadingWithFrameObjects(FrameMatrix3D.class, Matrix3D.class, true, 1, framelessMethodsToIgnore);
   }

   @Test
   public void testSetMatchingFrame() throws Exception
   {
      Random random = new Random(544354);

      for (int i = 0; i < ITERATIONS; i++)
      {
         ReferenceFrame sourceFrame = EuclidFrameRandomTools.nextReferenceFrame(random, true);
         ReferenceFrame destinationFrame = EuclidFrameRandomTools.nextReferenceFrame(random, true);

         FrameMatrix3DReadOnly source = EuclidFrameRandomTools.nextFrameMatrix3D(random, sourceFrame);
         FixedFrameMatrix3DBasics actual = EuclidFrameRandomTools.nextFrameMatrix3D(random, destinationFrame);

         actual.setMatchingFrame(source);

         FrameMatrix3D expected = new FrameMatrix3D(source);
         expected.changeFrame(destinationFrame);

         EuclidCoreTestTools.assertMatrix3DEquals(expected, actual, EPSILON);
      }
   }
}