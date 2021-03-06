package us.ihmc.euclid.referenceFrame;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import us.ihmc.euclid.EuclidTestConstants;
import us.ihmc.euclid.geometry.interfaces.BoundingBox2DReadOnly;
import us.ihmc.euclid.geometry.tools.EuclidGeometryRandomTools;
import us.ihmc.euclid.referenceFrame.api.EuclidFrameAPIDefaultConfiguration;
import us.ihmc.euclid.referenceFrame.api.EuclidFrameAPITester;
import us.ihmc.euclid.referenceFrame.interfaces.FrameBoundingBox2DReadOnly;

public abstract class FrameBoundingBox2DReadOnlyTest<T extends FrameBoundingBox2DReadOnly>
{
   public abstract T createFrameBoundingBox(ReferenceFrame referenceFrame, BoundingBox2DReadOnly boundingBox);

   public final T createRandomBoundingBox(Random random)
   {
      return createRandomFrameBoundingBox(random, ReferenceFrame.getWorldFrame());
   }

   public final T createRandomFrameBoundingBox(Random random, ReferenceFrame referenceFrame)
   {
      return createFrameBoundingBox(referenceFrame, EuclidGeometryRandomTools.nextBoundingBox2D(random));
   }

   @Test
   public void testOverloading() throws Exception
   {
      EuclidFrameAPITester tester = new EuclidFrameAPITester(new EuclidFrameAPIDefaultConfiguration());
      tester.assertOverloadingWithFrameObjects(FrameBoundingBox2DReadOnly.class, BoundingBox2DReadOnly.class, false);
   }

   @Test
   public void testReferenceFrameChecks() throws Throwable
   {
      Predicate<Method> methodFilter = m -> !m.getName().equals("equals") && !m.getName().equals("epsilonEquals");
      EuclidFrameAPITester tester = new EuclidFrameAPITester(new EuclidFrameAPIDefaultConfiguration());
      tester.assertMethodsOfReferenceFrameHolderCheckReferenceFrame(this::createRandomFrameBoundingBox,
                                                                    methodFilter,
                                                                    EuclidTestConstants.API_FRAME_CHECKS_ITERATIONS);
   }
}
