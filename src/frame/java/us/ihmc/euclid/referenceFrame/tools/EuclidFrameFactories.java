package us.ihmc.euclid.referenceFrame.tools;

import java.util.function.DoubleSupplier;

import us.ihmc.euclid.geometry.BoundingBox2D;
import us.ihmc.euclid.geometry.BoundingBox3D;
import us.ihmc.euclid.geometry.Orientation2D;
import us.ihmc.euclid.geometry.interfaces.BoundingBox2DBasics;
import us.ihmc.euclid.geometry.interfaces.BoundingBox3DBasics;
import us.ihmc.euclid.geometry.interfaces.Orientation2DBasics;
import us.ihmc.euclid.matrix.RotationMatrix;
import us.ihmc.euclid.matrix.interfaces.RotationMatrixBasics;
import us.ihmc.euclid.matrix.interfaces.RotationMatrixReadOnly;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameBoundingBox2DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameBoundingBox3DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameOrientation2DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFramePoint2DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFramePoint3DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameQuaternionBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameRotationMatrixBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameVector2DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FixedFrameVector3DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FrameBoundingBox2DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameBoundingBox3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameOrientation2DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint2DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameQuaternionReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameRotationMatrixReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameTuple2DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameTuple3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameVector2DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameVector3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.ReferenceFrameHolder;
import us.ihmc.euclid.tools.EuclidCoreFactories;
import us.ihmc.euclid.tools.EuclidHashCodeTools;
import us.ihmc.euclid.transform.interfaces.Transform;
import us.ihmc.euclid.tuple2D.Point2D;
import us.ihmc.euclid.tuple2D.Vector2D;
import us.ihmc.euclid.tuple2D.interfaces.Point2DBasics;
import us.ihmc.euclid.tuple2D.interfaces.Point2DReadOnly;
import us.ihmc.euclid.tuple2D.interfaces.Vector2DBasics;
import us.ihmc.euclid.tuple2D.interfaces.Vector2DReadOnly;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;
import us.ihmc.euclid.tuple4D.Quaternion;
import us.ihmc.euclid.tuple4D.interfaces.QuaternionBasics;

/**
 * This class provides a varieties of factories to create Euclid frame types.
 *
 * @author Sylvain Bertrand
 */
public class EuclidFrameFactories
{
   private EuclidFrameFactories()
   {
      // Suppresses default constructor, ensuring non-instantiability.
   }

   /**
    * Creates a new frame point that is linked to the {@code originalTuple} as follows:
    *
    * <pre>
    * linkedPoint = scale * originalTuple
    * </pre>
    *
    * where the scale is obtained from the given {@code scaleSupplier}.
    *
    * @param scaleSupplier the supplier to get the scale.
    * @param originalTuple the reference tuple to scale. Not modified.
    * @return the new point linked to {@code originalTuple}.
    */
   public static FramePoint2DReadOnly newLinkedFramePoint2DReadOnly(DoubleSupplier scaleSupplier, FrameTuple2DReadOnly originalTuple)
   {
      return newLinkedFramePoint2DReadOnly(EuclidCoreFactories.newLinkedPoint2DReadOnly(scaleSupplier, originalTuple), originalTuple);
   }

   /**
    * Creates a new frame vector that is linked to the {@code originalTuple} as follows:
    *
    * <pre>
    * linkedVector = scale * originalTuple
    * </pre>
    *
    * where the scale is obtained from the given {@code scaleSupplier}.
    *
    * @param scaleSupplier the supplier to get the scale.
    * @param originalTuple the reference tuple to scale. Not modified.
    * @return the new vector linked to {@code originalTuple}.
    */
   public static FrameVector2DReadOnly newLinkedFrameVector2DReadOnly(DoubleSupplier scaleSupplier, FrameTuple2DReadOnly originalTuple)
   {
      return newLinkedFrameVector2DReadOnly(EuclidCoreFactories.newLinkedVector2DReadOnly(scaleSupplier, originalTuple), originalTuple);
   }

   /**
    * Creates a new frame point that is linked to the {@code originalTuple} as follows:
    *
    * <pre>
    * linkedPoint = scale * originalTuple
    * </pre>
    *
    * where the scale is obtained from the given {@code scaleSupplier}.
    *
    * @param scaleSupplier the supplier to get the scale.
    * @param originalTuple the reference tuple to scale. Not modified.
    * @return the new point linked to {@code originalTuple}.
    */
   public static FramePoint3DReadOnly newLinkedFramePoint3DReadOnly(DoubleSupplier scaleSupplier, FrameTuple3DReadOnly originalTuple)
   {
      return newLinkedFramePoint3DReadOnly(EuclidCoreFactories.newLinkedPoint3DReadOnly(scaleSupplier, originalTuple), originalTuple);
   }

   /**
    * Creates a new frame vector that is linked to the {@code originalTuple} as follows:
    *
    * <pre>
    * linkedVector = scale * originalTuple
    * </pre>
    *
    * where the scale is obtained from the given {@code scaleSupplier}.
    *
    * @param scaleSupplier the supplier to get the scale.
    * @param originalTuple the reference tuple to scale. Not modified.
    * @return the new vector linked to {@code originalTuple}.
    */
   public static FrameVector3DReadOnly newLinkedFrameVector3DReadOnly(DoubleSupplier scaleSupplier, FrameTuple3DReadOnly originalTuple)
   {
      return newLinkedFrameVector3DReadOnly(EuclidCoreFactories.newLinkedVector3DReadOnly(scaleSupplier, originalTuple), originalTuple);
   }

   /**
    * Creates a new point 2D that is a read-only view of the three coordinate suppliers expressed in
    * the reference frame provided by {@code referenceFrameHolder}.
    *
    * @param xSupplier            the x-coordinate supplier.
    * @param ySupplier            the y-coordinate supplier.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame point 2D.
    */
   public static FramePoint2DReadOnly newLinkedFramePoint2DReadOnly(DoubleSupplier xSupplier, DoubleSupplier ySupplier,
                                                                    ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFramePoint2DReadOnly(EuclidCoreFactories.newLinkedPoint2DReadOnly(xSupplier, ySupplier), referenceFrameHolder);
   }

   /**
    * Creates a new vector 2D that is a read-only view of the three coordinate suppliers expressed in
    * the reference frame provided by {@code referenceFrameHolder}.
    *
    * @param xSupplier            the x-coordinate supplier.
    * @param ySupplier            the y-coordinate supplier.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame vector 2D.
    */
   public static FrameVector2DReadOnly newLinkedFrameVector2DReadOnly(DoubleSupplier xSupplier, DoubleSupplier ySupplier,
                                                                      ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFrameVector2DReadOnly(EuclidCoreFactories.newLinkedVector2DReadOnly(xSupplier, ySupplier), referenceFrameHolder);
   }

   /**
    * Creates a new point 3D that is a read-only view of the three coordinate suppliers expressed in
    * the reference frame provided by {@code referenceFrameHolder}.
    *
    * @param xSupplier            the x-coordinate supplier.
    * @param ySupplier            the y-coordinate supplier.
    * @param zSupplier            the z-coordinate supplier.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame point 3D.
    */
   public static FramePoint3DReadOnly newLinkedFramePoint3DReadOnly(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier zSupplier,
                                                                    ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFramePoint3DReadOnly(EuclidCoreFactories.newLinkedPoint3DReadOnly(xSupplier, ySupplier, zSupplier), referenceFrameHolder);
   }

   /**
    * Creates a new vector 3D that is a read-only view of the three coordinate suppliers expressed in
    * the reference frame provided by {@code referenceFrameHolder}.
    *
    * @param xSupplier            the x-coordinate supplier.
    * @param ySupplier            the y-coordinate supplier.
    * @param zSupplier            the z-coordinate supplier.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame vector 3D.
    */
   public static FrameVector3DReadOnly newLinkedFrameVector3DReadOnly(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier zSupplier,
                                                                      ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFrameVector3DReadOnly(EuclidCoreFactories.newLinkedVector3DReadOnly(xSupplier, ySupplier, zSupplier), referenceFrameHolder);
   }

   /**
    * Creates a new point 2D that is a read-only view of the point expressed in the reference frame
    * provided by {@code referenceFrameHolder}.
    *
    * @param point                the point to link. Not modified.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame point 2D.
    */
   public static FramePoint2DReadOnly newLinkedFramePoint2DReadOnly(Point2DReadOnly point, ReferenceFrameHolder referenceFrameHolder)
   {
      return new FramePoint2DReadOnly()
      {
         @Override
         public double getX()
         {
            return point.getX();
         }

         @Override
         public double getY()
         {
            return point.getY();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(point, getReferenceFrame());
         }

         @Override
         public boolean equals(Object object)
         {
            if (object instanceof Point2DReadOnly)
               return FramePoint2DReadOnly.super.equals((Point2DReadOnly) object);
            else
               return false;
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple2DString(this);
         }
      };
   }

   /**
    * Creates a new vector 2D that is a read-only view of the vector expressed in the reference frame
    * provided by {@code referenceFrameHolder}.
    *
    * @param vector               the vector to link. Not modified.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame vector 2D.
    */
   public static FrameVector2DReadOnly newLinkedFrameVector2DReadOnly(Vector2DReadOnly vector, ReferenceFrameHolder referenceFrameHolder)
   {
      return new FrameVector2DReadOnly()
      {
         @Override
         public double getX()
         {
            return vector.getX();
         }

         @Override
         public double getY()
         {
            return vector.getY();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(vector, getReferenceFrame());
         }

         @Override
         public boolean equals(Object object)
         {
            if (object instanceof Vector2DReadOnly)
               return FrameVector2DReadOnly.super.equals((Vector2DReadOnly) object);
            else
               return false;
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple2DString(this);
         }
      };
   }

   /**
    * Creates a new point 3D that is a read-only view of the point expressed in the reference frame
    * provided by {@code referenceFrameHolder}.
    *
    * @param point                the point to link. Not modified.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame point 3D.
    */
   public static FramePoint3DReadOnly newLinkedFramePoint3DReadOnly(Point3DReadOnly point, ReferenceFrameHolder referenceFrameHolder)
   {
      return new FramePoint3DReadOnly()
      {
         @Override
         public double getX()
         {
            return point.getX();
         }

         @Override
         public double getY()
         {
            return point.getY();
         }

         @Override
         public double getZ()
         {
            return point.getZ();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(point, getReferenceFrame());
         }

         @Override
         public boolean equals(Object object)
         {
            if (object instanceof Point3DReadOnly)
               return FramePoint3DReadOnly.super.equals((Point3DReadOnly) object);
            else
               return false;
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple3DString(this);
         }
      };
   }

   /**
    * Creates a new vector 3D that is a read-only view of the vector expressed in the reference frame
    * provided by {@code referenceFrameHolder}.
    *
    * @param vector               the vector to link. Not modified.
    * @param referenceFrameHolder the reference frame supplier. Not modified.
    * @return the new read-only frame vector 3D.
    */
   public static FrameVector3DReadOnly newLinkedFrameVector3DReadOnly(Vector3DReadOnly vector, ReferenceFrameHolder referenceFrameHolder)
   {
      return new FrameVector3DReadOnly()
      {
         @Override
         public double getX()
         {
            return vector.getX();
         }

         @Override
         public double getY()
         {
            return vector.getY();
         }

         @Override
         public double getZ()
         {
            return vector.getZ();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(vector, getReferenceFrame());
         }

         @Override
         public boolean equals(Object object)
         {
            if (object instanceof Vector3DReadOnly)
               return FrameVector3DReadOnly.super.equals((Vector3DReadOnly) object);
            else
               return false;
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple3DString(this);
         }
      };
   }

   /**
    * Creates a new frame point 2D that is a read-only view of the given {@code originalPoint} negated.
    *
    * @param originalPoint the original point to create linked negative point for. Not modified.
    * @return the negative read-only view of {@code originalPoint}.
    */
   public static FramePoint2DReadOnly newNegativeLinkedFramePoint2D(FramePoint2DReadOnly originalPoint)
   {
      return newLinkedFramePoint2DReadOnly(EuclidCoreFactories.newNegativeLinkedPoint2D(originalPoint), originalPoint);
   }

   /**
    * Creates a new frame vector 2D that is a read-only view of the given {@code originalVector}
    * negated.
    *
    * @param originalVector the original vector to create linked negative vector for. Not modified.
    * @return the negative read-only view of {@code originalVector}.
    */
   public static FrameVector2DReadOnly newNegativeLinkedFrameVector2D(FrameVector2DReadOnly originalVector)
   {
      return newLinkedFrameVector2DReadOnly(EuclidCoreFactories.newNegativeLinkedVector2D(originalVector), originalVector);
   }

   /**
    * Creates a new frame point 3D that is a read-only view of the given {@code originalPoint} negated.
    *
    * @param originalPoint the original point to create linked negative point for. Not modified.
    * @return the negative read-only view of {@code originalPoint}.
    */
   public static FramePoint3DReadOnly newNegativeLinkedFramePoint3D(FramePoint3DReadOnly originalPoint)
   {
      return newLinkedFramePoint3DReadOnly(EuclidCoreFactories.newNegativeLinkedPoint3D(originalPoint), originalPoint);
   }

   /**
    * Creates a new frame vector 3D that is a read-only view of the given {@code originalVector}
    * negated.
    *
    * @param originalVector the original vector to create linked negative vector for. Not modified.
    * @return the negative read-only view of {@code originalVector}.
    */
   public static FrameVector3DReadOnly newNegativeLinkedFrameVector3D(FrameVector3DReadOnly originalVector)
   {
      return newLinkedFrameVector3DReadOnly(EuclidCoreFactories.newNegativeLinkedVector3D(originalVector), originalVector);
   }

   public static FixedFramePoint2DBasics newFixedFramePoint2DBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFramePoint2DBasics(referenceFrameHolder, new Point2D());
   }

   public static FixedFramePoint2DBasics newLinkedFixedFramePoint2DBasics(ReferenceFrameHolder referenceFrameHolder, Point2DBasics originalPoint)
   {
      return new FixedFramePoint2DBasics()
      {
         @Override
         public void setX(double x)
         {
            originalPoint.setX(x);
         }

         @Override
         public void setY(double y)
         {
            originalPoint.setY(y);
         }

         @Override
         public double getX()
         {
            return originalPoint.getX();
         }

         @Override
         public double getY()
         {
            return originalPoint.getY();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public boolean equals(Object object)
         {
            if (object == this)
               return true;
            else if (object instanceof FramePoint2DReadOnly)
               return FixedFramePoint2DBasics.super.equals((FramePoint2DReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(originalPoint, getReferenceFrame());
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple2DString(this);
         }
      };
   }

   public static FixedFrameVector2DBasics newFixedFrameVector2DBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFrameVector2DBasics(referenceFrameHolder, new Vector2D());
   }

   public static FixedFrameVector2DBasics newLinkedFixedFrameVector2DBasics(ReferenceFrameHolder referenceFrameHolder, Vector2DBasics originalVector)
   {
      return new FixedFrameVector2DBasics()
      {
         @Override
         public void setX(double x)
         {
            originalVector.setX(x);
         }

         @Override
         public void setY(double y)
         {
            originalVector.setY(y);
         }

         @Override
         public double getX()
         {
            return originalVector.getX();
         }

         @Override
         public double getY()
         {
            return originalVector.getY();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public boolean equals(Object object)
         {
            if (object == this)
               return true;
            else if (object instanceof FrameVector2DReadOnly)
               return FixedFrameVector2DBasics.super.equals((FrameVector2DReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(originalVector, getReferenceFrame());
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple2DString(this);
         }
      };
   }

   public static FixedFramePoint3DBasics newFixedFramePoint3DBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFramePoint3DBasics(referenceFrameHolder, new Point3D());
   }

   public static FixedFramePoint3DBasics newLinkedFixedFramePoint3DBasics(ReferenceFrameHolder referenceFrameHolder, Point3DBasics originalPoint)
   {
      return new FixedFramePoint3DBasics()
      {
         @Override
         public void setX(double x)
         {
            originalPoint.setX(x);
         }

         @Override
         public void setY(double y)
         {
            originalPoint.setY(y);
         }

         @Override
         public void setZ(double z)
         {
            originalPoint.setZ(z);
         }

         @Override
         public double getX()
         {
            return originalPoint.getX();
         }

         @Override
         public double getY()
         {
            return originalPoint.getY();
         }

         @Override
         public double getZ()
         {
            return originalPoint.getZ();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public boolean equals(Object object)
         {
            if (object == this)
               return true;
            else if (object instanceof FramePoint3DReadOnly)
               return FixedFramePoint3DBasics.super.equals((FramePoint3DReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(originalPoint, getReferenceFrame());
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple3DString(this);
         }
      };
   }

   public static FixedFrameVector3DBasics newFixedFrameVector3DBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFrameVector3DBasics(referenceFrameHolder, new Vector3D());
   }

   public static FixedFrameVector3DBasics newLinkedFixedFrameVector3DBasics(ReferenceFrameHolder referenceFrameHolder, Vector3DBasics originalVector)
   {
      return new FixedFrameVector3DBasics()
      {
         @Override
         public void setX(double x)
         {
            originalVector.setX(x);
         }

         @Override
         public void setY(double y)
         {
            originalVector.setY(y);
         }

         @Override
         public void setZ(double z)
         {
            originalVector.setZ(z);
         }

         @Override
         public double getX()
         {
            return originalVector.getX();
         }

         @Override
         public double getY()
         {
            return originalVector.getY();
         }

         @Override
         public double getZ()
         {
            return originalVector.getZ();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public boolean equals(Object object)
         {
            if (object == this)
               return true;
            else if (object instanceof FrameVector3DReadOnly)
               return FixedFrameVector3DBasics.super.equals((FrameVector3DReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(originalVector, getReferenceFrame());
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple3DString(this);
         }
      };
   }

   public static FixedFrameOrientation2DBasics newFixedFrameOrientation2DBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFrameOrientation2DBasics(referenceFrameHolder, new Orientation2D());
   }

   public static FixedFrameOrientation2DBasics newLinkedFixedFrameOrientation2DBasics(ReferenceFrameHolder referenceFrameHolder,
                                                                                      Orientation2DBasics originalOrientation)
   {
      return new FixedFrameOrientation2DBasics()
      {
         @Override
         public void setYaw(double yaw)
         {
            originalOrientation.setYaw(yaw);
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public double getYaw()
         {
            return originalOrientation.getYaw();
         }

         @Override
         public void applyTransform(Transform transform)
         {
            originalOrientation.applyTransform(transform);
         }

         @Override
         public void applyInverseTransform(Transform transform)
         {
            originalOrientation.applyInverseTransform(transform);
         }

         @Override
         public boolean equals(Object object)
         {
            if (object == this)
               return true;
            else if (object instanceof FrameOrientation2DReadOnly)
               return FixedFrameOrientation2DBasics.super.equals((FrameOrientation2DReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(originalOrientation, getReferenceFrame());
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameOrientation2DString(this);
         }
      };
   }

   public static FixedFrameQuaternionBasics newFixedFrameQuaternionBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFrameQuaternionBasics(referenceFrameHolder, new Quaternion());
   }

   public static FixedFrameQuaternionBasics newLinkedFixedFrameQuaternionBasics(ReferenceFrameHolder referenceFrameHolder, QuaternionBasics originalQuaternion)
   {
      return new FixedFrameQuaternionBasics()
      {
         @Override
         public void setUnsafe(double qx, double qy, double qz, double qs)
         {
            originalQuaternion.setUnsafe(qx, qy, qz, qs);
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public double getX()
         {
            return originalQuaternion.getX();
         }

         @Override
         public double getY()
         {
            return originalQuaternion.getY();
         }

         @Override
         public double getZ()
         {
            return originalQuaternion.getZ();
         }

         @Override
         public double getS()
         {
            return originalQuaternion.getS();
         }

         @Override
         public boolean equals(Object object)
         {
            if (object == this)
               return true;
            else if (object instanceof FrameQuaternionReadOnly)
               return FixedFrameQuaternionBasics.super.equals((FrameQuaternionReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(originalQuaternion, getReferenceFrame());
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameTuple4DString(this);
         }
      };
   }

   public static FixedFrameRotationMatrixBasics newFixedFrameRotationMatrixBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFrameRotationMatrixBasics(referenceFrameHolder, new RotationMatrix());
   }

   public static FixedFrameRotationMatrixBasics newLinkedFixedFrameRotationMatrixBasics(ReferenceFrameHolder referenceFrameHolder,
                                                                                        RotationMatrixBasics originalRotationMatrix)
   {
      return new FixedFrameRotationMatrixBasics()
      {
         @Override
         public void setUnsafe(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22)
         {
            originalRotationMatrix.setUnsafe(m00, m01, m02, m10, m11, m12, m20, m21, m22);
         }

         @Override
         public void set(RotationMatrixReadOnly other)
         {
            originalRotationMatrix.set(other);
         }

         @Override
         public void setIdentity()
         {
            originalRotationMatrix.setIdentity();
         }

         @Override
         public void setToNaN()
         {
            originalRotationMatrix.setToNaN();
         }

         @Override
         public void normalize()
         {
            originalRotationMatrix.normalize();
         }

         @Override
         public boolean isIdentity()
         {
            return originalRotationMatrix.isIdentity();
         }

         @Override
         public void transpose()
         {
            originalRotationMatrix.transpose();
         }

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public boolean isDirty()
         {
            return originalRotationMatrix.isDirty();
         }

         /** {@inheritDoc} */
         @Override
         public double getM00()
         {
            return originalRotationMatrix.getM00();
         }

         /** {@inheritDoc} */
         @Override
         public double getM01()
         {
            return originalRotationMatrix.getM01();
         }

         /** {@inheritDoc} */
         @Override
         public double getM02()
         {
            return originalRotationMatrix.getM02();
         }

         /** {@inheritDoc} */
         @Override
         public double getM10()
         {
            return originalRotationMatrix.getM10();
         }

         /** {@inheritDoc} */
         @Override
         public double getM11()
         {
            return originalRotationMatrix.getM11();
         }

         /** {@inheritDoc} */
         @Override
         public double getM12()
         {
            return originalRotationMatrix.getM12();
         }

         /** {@inheritDoc} */
         @Override
         public double getM20()
         {
            return originalRotationMatrix.getM20();
         }

         /** {@inheritDoc} */
         @Override
         public double getM21()
         {
            return originalRotationMatrix.getM21();
         }

         /** {@inheritDoc} */
         @Override
         public double getM22()
         {
            return originalRotationMatrix.getM22();
         }

         @Override
         public boolean equals(Object object)
         {
            if (object == this)
               return true;
            else if (object instanceof FrameRotationMatrixReadOnly)
               return FixedFrameRotationMatrixBasics.super.equals((FrameRotationMatrixReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(originalRotationMatrix, getReferenceFrame());
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameMatrix3DString(this);
         }
      };
   }

   public static FixedFrameBoundingBox2DBasics newFixedFrameBoundingBox2DBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFrameBoundingBox2DBasics(referenceFrameHolder, new BoundingBox2D());
   }

   public static FixedFrameBoundingBox2DBasics newLinkedFixedFrameBoundingBox2DBasics(ReferenceFrameHolder referenceFrameHolder,
                                                                                      BoundingBox2DBasics originalBoundingBox2D)
   {
      FixedFrameBoundingBox2DBasics fixedFrameBoundingBox2DBasics = new FixedFrameBoundingBox2DBasics()
      {
         private final FixedFramePoint2DBasics minPoint = newLinkedFixedFramePoint2DBasics(referenceFrameHolder, originalBoundingBox2D.getMinPoint());
         private final FixedFramePoint2DBasics maxPoint = newLinkedFixedFramePoint2DBasics(referenceFrameHolder, originalBoundingBox2D.getMaxPoint());

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public FixedFramePoint2DBasics getMinPoint()
         {
            return minPoint;
         }

         @Override
         public FixedFramePoint2DBasics getMaxPoint()
         {
            return maxPoint;
         }

         @Override
         public boolean equals(Object object)
         {
            if (object instanceof FrameBoundingBox2DReadOnly)
               return FixedFrameBoundingBox2DBasics.super.equals((FrameBoundingBox2DReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(minPoint, maxPoint);
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameBoundingBox2DString(this);
         }
      };

      fixedFrameBoundingBox2DBasics.setToNaN();

      return fixedFrameBoundingBox2DBasics;
   }

   public static FixedFrameBoundingBox3DBasics newFixedFrameBoundingBox3DBasics(ReferenceFrameHolder referenceFrameHolder)
   {
      return newLinkedFixedFrameBoundingBox3DBasics(referenceFrameHolder, new BoundingBox3D());
   }

   public static FixedFrameBoundingBox3DBasics newLinkedFixedFrameBoundingBox3DBasics(ReferenceFrameHolder referenceFrameHolder,
                                                                                      BoundingBox3DBasics originalBoundingBox3D)
   {
      FixedFrameBoundingBox3DBasics fixedFrameBoundingBox3DBasics = new FixedFrameBoundingBox3DBasics()
      {
         private final FixedFramePoint3DBasics minPoint = newLinkedFixedFramePoint3DBasics(referenceFrameHolder, originalBoundingBox3D.getMinPoint());
         private final FixedFramePoint3DBasics maxPoint = newLinkedFixedFramePoint3DBasics(referenceFrameHolder, originalBoundingBox3D.getMaxPoint());

         @Override
         public ReferenceFrame getReferenceFrame()
         {
            return referenceFrameHolder.getReferenceFrame();
         }

         @Override
         public FixedFramePoint3DBasics getMinPoint()
         {
            return minPoint;
         }

         @Override
         public FixedFramePoint3DBasics getMaxPoint()
         {
            return maxPoint;
         }

         @Override
         public boolean equals(Object object)
         {
            if (object instanceof FrameBoundingBox3DReadOnly)
               return FixedFrameBoundingBox3DBasics.super.equals((FrameBoundingBox3DReadOnly) object);
            else
               return false;
         }

         @Override
         public int hashCode()
         {
            return EuclidHashCodeTools.toIntHashCode(minPoint, maxPoint);
         }

         @Override
         public String toString()
         {
            return EuclidFrameIOTools.getFrameBoundingBox3DString(this);
         }
      };

      fixedFrameBoundingBox3DBasics.setToNaN();

      return fixedFrameBoundingBox3DBasics;
   }
}