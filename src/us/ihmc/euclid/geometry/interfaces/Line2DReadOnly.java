package us.ihmc.euclid.geometry.interfaces;

import us.ihmc.euclid.exceptions.NotAMatrix2DException;
import us.ihmc.euclid.geometry.ConvexPolygon2D;
import us.ihmc.euclid.geometry.Line2D;
import us.ihmc.euclid.geometry.LineSegment2D;
import us.ihmc.euclid.geometry.exceptions.OutdatedPolygonException;
import us.ihmc.euclid.geometry.tools.EuclidGeometryTools;
import us.ihmc.euclid.transform.interfaces.Transform;
import us.ihmc.euclid.tuple2D.Point2D;
import us.ihmc.euclid.tuple2D.Vector2D;
import us.ihmc.euclid.tuple2D.interfaces.Point2DBasics;
import us.ihmc.euclid.tuple2D.interfaces.Point2DReadOnly;
import us.ihmc.euclid.tuple2D.interfaces.Vector2DBasics;
import us.ihmc.euclid.tuple2D.interfaces.Vector2DReadOnly;

public interface Line2DReadOnly
{
   Point2DReadOnly getPoint();

   Vector2DReadOnly getDirection();
   
   boolean hasPointBeenSet();
   
   boolean hasDirectionBeenSet();
   
   default void checkHasBeenInitialized()
   {
      if (!hasPointBeenSet())
         throw new RuntimeException("The point of this line has not been initialized.");
      if (!hasDirectionBeenSet())
         throw new RuntimeException("The direction of this line has not been initialized.");
   }

   /**
    * Gets the direction defining this line by storing its components in the given argument
    * {@code directionToPack}.
    *
    * @param directionToPack vector in which the components of this line's direction are stored.
    *           Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default void getDirection(Vector2DBasics directionToPack)
   {
      checkHasBeenInitialized();
      directionToPack.set(getDirection());
   }

   /**
    * Gets the x-component of this line's direction.
    *
    * @return the x-component of this line's direction.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double getDirectionX()
   {
      checkHasBeenInitialized();
      return getDirection().getX();
   }

   /**
    * Gets the y-component of this line's direction.
    *
    * @return the y-component of this line's direction.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double getDirectionY()
   {
      checkHasBeenInitialized();
      return getDirection().getY();
   }

   /**
    * Gets the point defining this line by storing its coordinates in the given argument
    * {@code pointToPack}.
    *
    * @param pointToPack point in which the coordinates of this line's point are stored. Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default void getPoint(Point2DBasics pointOnLineToPack)
   {
      pointOnLineToPack.set(getPoint());
   }

   /**
    * Gets the point and direction defining this line by storing their components in the given
    * arguments {@code pointToPack} and {@code directionToPack}.
    *
    * @param pointToPack point in which the coordinates of this line's point are stored. Modified.
    * @param directionToPack vector in which the components of this line's direction are stored.
    *           Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default void getPointAndDirection(Point2DBasics pointToPack, Vector2DBasics directionToPack)
   {
      getPoint(pointToPack);
      getDirection(directionToPack);
   }

   /**
    * Gets the x-coordinate of a point this line goes through.
    *
    * @return the x-coordinate of this line's point.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double getPointX()
   {
      checkHasBeenInitialized();
      return getPoint().getX();
   }

   /**
    * Gets the y-coordinate of a point this line goes through.
    *
    * @return the y-coordinate of this line's point.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double getPointY()
   {
      checkHasBeenInitialized();
      return getPoint().getY();
   }
   
   /**
    * Copies this line, transforms the copy using the given homogeneous transformation matrix, and
    * returns the result.
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    *
    * @param transform the transform to apply on this line's copy. Not modified.
    * @param the copy of this transformed.
    * @throws RuntimeException if this line has not been initialized yet.
    * @throws NotAMatrix2DException if the rotation part of {@code transform} is not a
    *            transformation in the XY-plane.
    */
   default Line2D applyTransformCopy(Transform transform)
   {
      checkHasBeenInitialized();
      Line2D copy = new Line2D(this);
      copy.applyTransform(transform);
      return copy;
   }

   /**
    * Copies this line, transforms the copy using the given homogeneous transformation matrix and
    * project the result onto the XY-plane, and returns the result.
    *
    * @param transform the transform to apply on this line's copy. Not modified.
    * @param the copy of this transformed.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default Line2D applyTransformAndProjectToXYPlaneCopy(Transform transform)
   {
      Line2D copy = new Line2D(this);
      copy.applyTransformAndProjectToXYPlane(transform);
      return copy;
   }

   /**
    * Computes the minimum distance the given 3D point and this line.
    * <p>
    * Edge cases:
    * <ul>
    * <li>if {@code direction.length() < }{@link EuclidGeometryTools#ONE_TRILLIONTH}, this method
    * returns the distance between {@code point} and the given {@code point}.
    * </ul>
    * </p>
    *
    * @param point 2D point to compute the distance from the line. Not modified.
    * @return the minimum distance between the 2D point and this 2D line.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double distance(Point2DReadOnly point)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.distanceFromPoint2DToLine2D(point, getPoint(), getDirection());
   }

   /**
    * Computes the coordinates of the possible intersection(s) between this line and the given
    * convex polygon 2D.
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    * <p>
    * Edge cases:
    * <ul>
    * <li>If the polygon has no vertices, this method behaves as if there is no intersections and
    * returns {@code null}.
    * <li>If no intersections exist, this method returns {@code null}.
    * </ul>
    * </p>
    *
    * @param convexPolygon the convex polygon this line may intersect. Not modified.
    * @return the intersections between between the line and the polygon or {@code null} if the
    *         method failed or if there is no intersections.
    * @throws RuntimeException if this line has not been initialized yet.
    * @throws OutdatedPolygonException if the convex polygon is not up-to-date.
    */
   default Point2D[] intersectionWith(ConvexPolygon2D convexPolygon)
   {
      checkHasBeenInitialized();
      return convexPolygon.intersectionWith(this);
   }

   /**
    * Computes the coordinates of the possible intersection(s) between this line and the given
    * convex polygon 2D.
    * <p>
    * Edge cases:
    * <ul>
    * <li>If the polygon has no vertices, this method behaves as if there is no intersections.
    * <li>If no intersections exist, this method returns {@code 0} and the two intersection-to-pack
    * arguments remain unmodified.
    * <li>If there is only one intersection, this method returns {@code 1} and the coordinates of
    * the only intersection are stored in {@code firstIntersectionToPack}.
    * {@code secondIntersectionToPack} remains unmodified.
    * </ul>
    * </p>
    *
    * @param convexPolygon the convex polygon this line may intersect. Not modified.
    * @param firstIntersectionToPack point in which the coordinates of the first intersection. Can
    *           be {@code null}. Modified.
    * @param secondIntersectionToPack point in which the coordinates of the second intersection. Can
    *           be {@code null}. Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    * @throws OutdatedPolygonException if the convex polygon is not up-to-date.
    */
   default int intersectionWith(ConvexPolygon2D convexPolygon, Point2DBasics firstIntersectionToPack, Point2DBasics secondIntersectionToPack)
   {
      checkHasBeenInitialized();
      return convexPolygon.intersectionWith(this, firstIntersectionToPack, secondIntersectionToPack);
   }

   /**
    * Calculates the coordinates of the intersection between this line and the given line and
    * returns the result.
    * <p>
    * Edge cases:
    * <ul>
    * <li>if the two lines are parallel but not collinear, the two lines do not intersect and this
    * method returns {@code null}.
    * <li>if the two lines are collinear, the two lines are assumed to be intersecting at
    * {@code this.point}.
    * </ul>
    * </p>
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    *
    * @param secondLine the other line that may intersect this line. Not modified.
    * @return the coordinates of the intersection if the two lines intersects, {@code null}
    *         otherwise.
    * @throws RuntimeException if either this line or {@code secondLine} has not been initialized
    *            yet.
    */
   default Point2D intersectionWith(Line2D secondLine)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.intersectionBetweenTwoLine2Ds(getPoint(), getDirection(), secondLine.getPoint(), secondLine.getDirection());
   }

   /**
    * Calculates the coordinates of the intersection between this line and the given line and stores
    * the result in {@code intersectionToPack}.
    * <p>
    * Edge cases:
    * <ul>
    * <li>if the two lines are parallel but not collinear, the two lines do not intersect and this
    * method returns {@code null}.
    * <li>if the two lines are collinear, the two lines are assumed to be intersecting at
    * {@code this.point}.
    * </ul>
    * </p>
    *
    * @param secondLine the other line that may intersect this line. Not modified.
    * @param intersectionToPack the 2D point in which the result is stored. Modified.
    * @return {@code true} if the two lines intersects, {@code false} otherwise.
    * @throws RuntimeException if either this line or {@code secondLine} has not been initialized
    *            yet.
    */
   default boolean intersectionWith(Line2D secondLine, Point2DBasics intersectionToPack)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.intersectionBetweenTwoLine2Ds(getPoint(), getDirection(), secondLine.getPoint(), secondLine.getDirection(), intersectionToPack);
   }

   /**
    * Calculates the coordinates of the intersection between this line and the given line segment
    * and returns the result.
    * <p>
    * Edge cases:
    * <ul>
    * <li>When this line and the line segment are parallel but not collinear, they do not intersect.
    * <li>When this line and the line segment are collinear, they are assumed to intersect at
    * {@code lineSegmentStart}.
    * <li>When this line intersects the line segment at one of its endpoints, this method returns a
    * copy of the endpoint where the intersection is happening.
    * </ul>
    * </p>
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    *
    * @param lineSegment the line segment that may intersect this line. Not modified.
    * @return the coordinates of the intersection if the line intersects the line segment,
    *         {@code null} otherwise.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default Point2D intersectionWith(LineSegment2D lineSegment)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.intersectionBetweenLine2DAndLineSegment2D(getPoint(), getDirection(), lineSegment.getFirstEndpoint(), lineSegment.getSecondEndpoint());
   }

   /**
    * Calculates the coordinates of the intersection between this line and the given line segment
    * and stores the result in {@code intersectionToPack}.
    * <p>
    * Edge cases:
    * <ul>
    * <li>When this line and the line segment are parallel but not collinear, they do not intersect.
    * <li>When this line and the line segment are collinear, they are assumed to intersect at
    * {@code lineSegmentStart}.
    * <li>When this line intersects the line segment at one of its endpoints, this method returns
    * {@code true} and the endpoint is the intersection.
    * </ul>
    * </p>
    *
    * @param lineSegment the line segment that may intersect this line. Not modified.
    * @param intersectionToPack the 2D point in which the result is stored. Can be {@code null}.
    *           Modified.
    * @return {@code true} if the line intersects the line segment, {@code false} otherwise.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean intersectionWith(LineSegment2D lineSegment, Point2DBasics intersectionToPack)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.intersectionBetweenLine2DAndLineSegment2D(getPoint(), getDirection(), lineSegment.getFirstEndpoint(), lineSegment.getSecondEndpoint(),
                                                                           intersectionToPack);
   }

   /**
    * Returns a boolean value, stating whether the query point is in behind of this line or not.
    * <p>
    * The idea of 'behind' refers to the side of the line the x-axis is pointing away.
    * </p>
    *
    * @param point the coordinates of the query. Not modified.
    * @return {@code true} if the point is in behind of this line, {@code false} if the point is
    *         front the line.
    * @throws RuntimeException if this line has not been initialized yet.
    * @throws RuntimeException if the given point is located exactly on this line.
    */
   default boolean isPointBehindLine(Point2DReadOnly point)
   {
      return !isPointInFrontOfLine(point);
   }

   /**
    * Returns a boolean value, stating whether the query point is in front of this line or not.
    * <p>
    * The idea of 'front' refers to the side of the line toward which the x-axis is pointing.
    * </p>
    *
    * @param point the coordinates of the query. Not modified.
    * @return {@code true} if the point is in front of this line, {@code false} if the point is
    *         behind the line.
    * @throws RuntimeException if this line has not been initialized yet.
    * @throws RuntimeException if the given point is located exactly on this line.
    */
   default boolean isPointInFrontOfLine(Point2DReadOnly point)
   {
      checkHasBeenInitialized();
      if (getDirectionY() > 0.0)
         return isPointOnRightSideOfLine(point);
      else if (getDirectionY() < 0.0)
         return isPointOnLeftSideOfLine(point);
      else
         throw new RuntimeException("Not defined when line is pointing exactly along the x-axis");
   }

   /**
    * Returns a boolean value, stating whether the query point is in front of this line or not.
    * <p>
    * The idea of 'front' refers to the side of the line toward which the given vector
    * {@code frontDirection} is pointing.
    * </p>
    *
    * @param frontDirection the vector used to define the side of the line which is to be considered
    *           as the front. Not modified.
    * @param point the coordinates of the query. Not modified.
    * @return {@code true} if the point is in front of this line, {@code false} if the point is
    *         behind the line.
    * @throws RuntimeException if this line has not been initialized yet.
    * @throws RuntimeException if the given point is located exactly on this line.
    */
   default boolean isPointInFrontOfLine(Vector2DReadOnly frontDirection, Point2DReadOnly point)
   {
      double crossProduct = frontDirection.cross(getDirection());
      if (crossProduct > 0.0)
         return isPointOnRightSideOfLine(point);
      else if (crossProduct < 0.0)
         return isPointOnLeftSideOfLine(point);
      else
         throw new RuntimeException("Not defined when line is pointing exactly along the front direction");
   }

   /**
    * Returns a boolean value, stating whether a 2D point is on the left or right side of this line.
    * The idea of "side" is determined based on the direction of the line.
    * <p>
    * For instance, given the {@code this.direction} components x = 0, and y = 1, and the
    * {@code this.point} coordinates x = 0, and y = 0, a point located on:
    * <ul>
    * <li>the left side of this line has a negative y coordinate.
    * <li>the right side of this line has a positive y coordinate.
    * </ul>
    * </p>
    * This method will return {@code false} if the point is on this line.
    *
    * @param point the coordinates of the query point.
    * @return {@code true} if the point is on the left side of this line, {@code false} if the point
    *         is on the right side or exactly on this line.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean isPointOnLeftSideOfLine(Point2DReadOnly point)
   {
      return isPointOnSideOfLine(point, true);
   }

   /**
    * Tests if the given is located on this line.
    * <p>
    * More precisely, the point is assumed to be on this line if it is located at a distance less
    * than {@code 1.0e-8} from it.
    * </p>
    *
    * @param point the coordinates of the query. Not modified.
    * @return {@code true} if the point is located on this line, {@code false} otherwise.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean isPointOnLine(Point2DReadOnly point)
   {
      return isPointOnLine(point, 1.0e-8);
   }

   /**
    * Tests if the given is located on this line.
    * <p>
    * More precisely, the point is assumed to be on this line if it is located at a distance less
    * than {@code epsilon} from it.
    * </p>
    *
    * @param point the coordinates of the query. Not modified.
    * @param epsilon the tolerance used for this test.
    * @return {@code true} if the point is located on this line, {@code false} otherwise.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean isPointOnLine(Point2DReadOnly point, double epsilon)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.distanceFromPoint2DToLine2D(point, getPoint(), getDirection()) < epsilon;
   }

   /**
    * Returns a boolean value, stating whether a 2D point is on the left or right side of this line.
    * The idea of "side" is determined based on the direction of the line.
    * <p>
    * For instance, given the {@code this.direction} components x = 0, and y = 1, and the
    * {@code this.point} coordinates x = 0, and y = 0, a point located on:
    * <ul>
    * <li>the left side of this line has a negative y coordinate.
    * <li>the right side of this line has a positive y coordinate.
    * </ul>
    * </p>
    * This method will return {@code false} if the point is on this line.
    *
    * @param point the coordinates of the query point.
    * @return {@code true} if the point is on the right side of this line, {@code false} if the
    *         point is on the left side or exactly on this line.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean isPointOnRightSideOfLine(Point2DReadOnly point)
   {
      return isPointOnSideOfLine(point, false);
   }

   /**
    * Returns a boolean value, stating whether a 2D point is on the left or right side of this line.
    * The idea of "side" is determined based on the direction of the line.
    * <p>
    * For instance, given the {@code this.direction} components x = 0, and y = 1, and the
    * {@code this.point} coordinates x = 0, and y = 0, a point located on:
    * <ul>
    * <li>the left side of this line has a negative y coordinate.
    * <li>the right side of this line has a positive y coordinate.
    * </ul>
    * </p>
    * This method will return {@code false} if the point is on this line.
    *
    * @param pointX the x-coordinate of the query point.
    * @param pointY the y-coordinate of the query point.
    * @param testLeftSide the query of the side, when equal to {@code true} this will test for the
    *           left side, {@code false} this will test for the right side.
    * @return {@code true} if the point is on the query side of this line, {@code false} if the
    *         point is on the opposite side or exactly on this line.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean isPointOnSideOfLine(double pointX, double pointY, boolean testLeftSide)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.isPoint2DOnSideOfLine2D(pointX, pointY, getPoint(), getDirection(), testLeftSide);
   }

   /**
    * Returns a boolean value, stating whether a 2D point is on the left or right side of this line.
    * The idea of "side" is determined based on the direction of the line.
    * <p>
    * For instance, given the {@code this.direction} components x = 0, and y = 1, and the
    * {@code this.point} coordinates x = 0, and y = 0, a point located on:
    * <ul>
    * <li>the left side of this line has a negative y coordinate.
    * <li>the right side of this line has a positive y coordinate.
    * </ul>
    * </p>
    * This method will return {@code false} if the point is on this line.
    *
    * @param point the coordinates of the query point.
    * @param testLeftSide the query of the side, when equal to {@code true} this will test for the
    *           left side, {@code false} this will test for the right side.
    * @return {@code true} if the point is on the query side of this line, {@code false} if the
    *         point is on the opposite side or exactly on this line.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean isPointOnSideOfLine(Point2DReadOnly point, boolean testLeftSide)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.isPoint2DOnSideOfLine2D(point, getPoint(), getDirection(), testLeftSide);
   }

   /**
    * Computes the orthogonal projection of the given 2D point on this 2D line.
    * <p>
    * Edge cases:
    * <ul>
    * <li>if the given line direction is too small, i.e.
    * {@code lineDirection.lengthSquared() < }{@value #ONE_TRILLIONTH}, this method fails and
    * returns {@code false}.
    * </ul>
    * </p>
    *
    * @param pointToProject the point to project on this line. Modified.
    * @return whether the method succeeded or not.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean orthogonalProjection(Point2DBasics pointToProject)
   {
      return orthogonalProjection(pointToProject, pointToProject);
   }

   /**
    * Computes the orthogonal projection of the given 2D point on this 2D line.
    * <p>
    * Edge cases:
    * <ul>
    * <li>if the given line direction is too small, i.e.
    * {@code lineDirection.lengthSquared() < }{@value #ONE_TRILLIONTH}, this method fails and
    * returns {@code false}.
    * </ul>
    * </p>
    *
    * @param pointToProject the point to compute the projection of. Not modified.
    * @param projectionToPack point in which the projection of the point onto the line is stored.
    *           Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default boolean orthogonalProjection(Point2DReadOnly pointToProject, Point2DBasics projectionToPack)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.orthogonalProjectionOnLine2D(pointToProject, getPoint(), getDirection(), projectionToPack);
   }

   /**
    * Computes the orthogonal projection of the given 2D point on this 2D line.
    * <p>
    * Edge cases:
    * <ul>
    * <li>if the given line direction is too small, i.e.
    * {@code lineDirection.lengthSquared() < }{@value #ONE_TRILLIONTH}, this method fails and
    * returns {@code false}.
    * </ul>
    * </p>
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    *
    * @param pointToProject the point to compute the projection of. Not modified.
    * @return the projection of the point onto the line or {@code null} if the method failed.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default Point2D orthogonalProjectionCopy(Point2DReadOnly pointToProject)
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.orthogonalProjectionOnLine2D(pointToProject, getPoint(), getDirection());
   }

   /**
    * Calculates the parameter 't' corresponding to the coordinates of the given {@code pointOnLine}
    * 'p' by solving the line equation:<br>
    * p = t * n + p<sub>0</sub><br>
    * where n is the unit-vector defining the direction of this line and p<sub>0</sub> is the point
    * defining this line which also corresponds to the point for which t=0.
    * <p>
    * Note that the absolute value of 't' is equal to the distance between the point 'p' and the
    * point p<sub>0</sub> defining this line.
    * </p>
    *
    * @param pointOnLine the coordinates of the 'p' from which the parameter 't' is to be
    *           calculated. The point has to be on the line. Not modified.
    * @param epsilon the maximum distance allowed between the given point and this line. If the
    *           given point is at a distance less than {@code epsilon} from this line, it is
    *           considered as being located on this line.
    * @return the value of the parameter 't' corresponding to the given point.
    * @throws RuntimeException if this line has not been initialized yet.
    * @throws RuntimeException if the given point is located at a distance greater than
    *            {@code epsilon} from this line.
    */
   default double parameterGivenPointOnLine(Point2DReadOnly pointOnLine, double epsilon)
   {
      if (!isPointOnLine(pointOnLine, epsilon))
      {
         throw new RuntimeException("The given point is not on this line, distance from line: " + distance(pointOnLine));
      }
      else
      {
         double x0 = getPointX();
         double y0 = getPointY();
         double x1 = x0 + getDirectionX();
         double y1 = y0 + getDirectionY();
         return EuclidGeometryTools.percentageAlongLineSegment2D(pointOnLine.getX(), pointOnLine.getY(), x0, y0, x1, y1);
      }
   }

   /**
    * Calculates and returns a line that is perpendicular to this line, with its direction pointing
    * to the left of this line, while going through the given point.
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    *
    * @param point the point the line has to go through. Not modified.
    * @return the line perpendicular to {@code this} and going through {@code point}.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default Line2D perpendicularLineThroughPoint(Point2DReadOnly point)
   {
      checkHasBeenInitialized();
      return new Line2D(point, perpendicularVector());
   }

   /**
    * Modifies {@code perpendicularLineToPack} such that it is perpendicular to this line, with its
    * direction pointing to the left of this line, while going through the given point.
    *
    * @param point the point the line has to go through. Not modified.
    * @param perpendicularLineToPack the line perpendicular to {@code this} and going through
    *           {@code point}. Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default void perpendicularLineThroughPoint(Point2DReadOnly point, Line2D perpendicularLineToPack)
   {
      checkHasBeenInitialized();
      perpendicularLineToPack.set(point.getX(), point.getY(), -getDirection().getY(), getDirection().getX());
   }

   /**
    * Returns the vector that is perpendicular to this line and pointing to the left.
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    *
    * @return the perpendicular vector to this line.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default Vector2D perpendicularVector()
   {
      checkHasBeenInitialized();
      return EuclidGeometryTools.perpendicularVector2D(getDirection());
   }

   /**
    * Packs into {@code vectorToPack} the vector that is perpendicular to this line and pointing to
    * the left.
    *
    * @param vectorToPack the perpendicular vector to this line. Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default void perpendicularVector(Vector2DBasics vectorToPack)
   {
      checkHasBeenInitialized();
      EuclidGeometryTools.perpendicularVector2D(getDirection(), vectorToPack);
   }

   /**
    * Calculates the coordinates of the point 'p' given the parameter 't' as follows:<br>
    * p = t * n + p<sub>0</sub><br>
    * where n is the unit-vector defining the direction of this line and p<sub>0</sub> is the point
    * defining this line which also corresponds to the point for which t=0.
    * <p>
    * Note that the absolute value of 't' is equal to the distance between the point 'p' and the
    * point p<sub>0</sub> defining this line.
    * </p>
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    *
    * @param t the parameter used to calculate the point coordinates.
    * @return the coordinates of the point 'p'.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default Point2D pointOnLineGivenParameter(double t)
   {
      Point2D pointToReturn = new Point2D();
      pointOnLineGivenParameter(t, pointToReturn);
      return pointToReturn;
   }

   /**
    * Calculates the coordinates of the point 'p' given the parameter 't' as follows:<br>
    * p = t * n + p<sub>0</sub><br>
    * where n is the unit-vector defining the direction of this line and p<sub>0</sub> is the point
    * defining this line which also corresponds to the point for which t=0.
    * <p>
    * Note that the absolute value of 't' is equal to the distance between the point 'p' and the
    * point p<sub>0</sub> defining this line.
    * </p>
    *
    * @param t the parameter used to calculate the point coordinates.
    * @param pointToPack the point in which the coordinates of 'p' are stored. Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default void pointOnLineGivenParameter(double t, Point2DBasics pointToPack)
   {
      checkHasBeenInitialized();
      pointToPack.scaleAdd(t, getDirection(), getPoint());
   }

   /**
    * Gets the coordinates of two distinct points this line goes through.
    *
    * @param firstPointOnLineToPack the coordinates of a first point located on this line. Modified.
    * @param secondPointOnLineToPack the coordinates of a second point located on this line.
    *           Modified.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default void getTwoPointsOnLine(Point2DBasics firstPointOnLineToPack, Point2DBasics secondPointOnLineToPack)
   {
      checkHasBeenInitialized();
      firstPointOnLineToPack.set(getPoint());
      secondPointOnLineToPack.add(getPoint(), getDirection());
   }

   /**
    * Calculates the interior bisector defined by this line and the given {@code secondLine}.
    * <p>
    * The interior bisector is defined as follows:
    * <ul>
    * <li>It goes through the intersection between this line and {@code secondLine}.
    * <li>Its direction point toward this line direction and the {@code secondLine}'s direction such
    * that: {@code interiorBisector.direction.dot(this.direction) > 0.0} and
    * {@code interiorBisector.direction.dot(secondLine.direction) > 0.0}.
    * <li>Finally the angle from {@code this} to the interior bisector is half the angle from
    * {@code this} to {@code secondLine}.
    * </ul>
    * </p>
    * <p>
    * WARNING: This method generates garbage.
    * </p>
    * <p>
    * Edge cases:
    * <ul>
    * <li>If the two lines are parallel but not collinear, this method fails, returns {@code null}.
    * <li>If the two lines are collinear, this method returns a copy of {@code this}.
    * </ul>
    * </p>
    *
    * @param secondLine the second line needed to calculate the interior bisector. Not modified.
    * @return the interior bisector if this method succeeded, {@code null} otherwise.
    * @throws RuntimeException if either this line or {@code secondLine} has not been initialized
    *            yet.
    */
   default Line2D interiorBisector(Line2DReadOnly secondLine)
   {
      Line2D interiorBisector = new Line2D();
      boolean success = interiorBisector(secondLine, interiorBisector);
      return success ? interiorBisector : null;
   }

   /**
    * Calculates the interior bisector defined by this line and the given {@code secondLine}.
    * <p>
    * The interior bisector is defined as follows:
    * <ul>
    * <li>It goes through the intersection between this line and {@code secondLine}.
    * <li>Its direction point toward this line direction and the {@code secondLine}'s direction such
    * that: {@code interiorBisector.direction.dot(this.direction) > 0.0} and
    * {@code interiorBisector.direction.dot(secondLine.direction) > 0.0}.
    * <li>Finally the angle from {@code this} to the interior bisector is half the angle from
    * {@code this} to {@code secondLine}.
    * </ul>
    * </p>
    * <p>
    * Edge cases:
    * <ul>
    * <li>If the two lines are parallel but not collinear, this method fails, returns {@code false},
    * and {@code interiorBisectorToPack} remains unchanged.
    * <li>If the two lines are collinear, {@code interiorBisectorToPack} is set to {@code this}.
    * </ul>
    * </p>
    *
    * @param secondLine the second line needed to calculate the interior bisector. Not modified.
    * @param interiorBisectorToPack the line in which the interior bisector point and direction are
    *           stored. Modified.
    * @return {@code true} if this method succeeded, {@code false} otherwise.
    * @throws RuntimeException if either this line or {@code secondLine} has not been initialized
    *            yet.
    */
   default boolean interiorBisector(Line2DReadOnly secondLine, Line2DBasics interiorBisectorToPack)
   {
      checkHasBeenInitialized();
      secondLine.checkHasBeenInitialized();

      double t = EuclidGeometryTools.percentageOfIntersectionBetweenTwoLine2Ds(getPoint(), getDirection(), secondLine.getPoint(), secondLine.getDirection());

      if (Double.isNaN(t))
      { // Lines are parallel but not collinear
         return false;
      }
      else if (t == 0.0 && EuclidGeometryTools.areVector2DsParallel(getDirection(), secondLine.getDirection(), 1.0e-7))
      { // Lines are collinear
         interiorBisectorToPack.set(this);
         return true;
      }
      else
      { // Lines are not parallel
         double pointOnBisectorX = t * getDirectionX() + getPointX();
         double pointOnBisectorY = t * getDirectionY() + getPointY();
         double bisectorDirectionX = getDirectionX() + secondLine.getDirectionX();
         double bisectorDirectionY = getDirectionY() + secondLine.getDirectionY();
         interiorBisectorToPack.set(pointOnBisectorX, pointOnBisectorY, bisectorDirectionX, bisectorDirectionY);
         return true;
      }
   }

   /**
    * The x-coordinate at which this line intercept the x-axis, i.e. the line defined by
    * {@code y=0}.
    *
    * @return the x-coordinate of the intersection between this line and the x-axis.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double xIntercept()
   {
      checkHasBeenInitialized();
      double parameterAtIntercept = -getPointY() / getDirectionY();
      return parameterAtIntercept * getDirectionX() + getPointX();
   }

   /**
    * The y-coordinate at which this line intercept the y-axis, i.e. the line defined by
    * {@code x=0}.
    *
    * @return the y-coordinate of the intersection between this line and the y-axis.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double yIntercept()
   {
      checkHasBeenInitialized();
      double parameterAtIntercept = -getPointX() / getDirectionX();
      return parameterAtIntercept * getDirectionY() + getPointY();
   }

   /**
    * Calculates the slope value of this line.
    * <p>
    * The slope 's' can be used to calculate the y-coordinate of a point located on the line given
    * its x-coordinate:<br>
    * y = s * x + y<sub>0</sub><br>
    * where y<sub>0</sub> is the y-coordinate at which this line intercepts the y-axis and which can
    * be obtained with {@link #yIntercept()}.
    * </p>
    *
    * @return the value of the slope of this line.
    * @throws RuntimeException if this line has not been initialized yet.
    */
   default double slope()
   {
      checkHasBeenInitialized();
      if (getDirectionX() == 0.0 && getDirectionY() > 0.0)
      {
         return Double.POSITIVE_INFINITY;
      }

      if (getDirectionX() == 0.0 && getDirectionY() < 0.0)
      {
         return Double.NEGATIVE_INFINITY;
      }

      return getDirectionY() / getDirectionX();
   }

   /**
    * Compares {@code this} with {@code other} to determine if the two lines are collinear.
    *
    * @param other the line to compare to. Not modified.
    * @param epsilon the tolerance of the comparison.
    * @return {@code true} if the lines are collinear, {@code false} otherwise.
    */
   default boolean isCollinear(Line2D other, double epsilon)
   {
      return isCollinear(other, epsilon, epsilon);
   }

   /**
    * Compares {@code this} with {@code other} to determine if the two lines are collinear.
    *
    * @param other the line to compare to. Not modified.
    * @param angleEpsilon the tolerance of the comparison for angle.
    * @param distanceEpsilon the tolerance of the comparison for distance.
    * @return {@code true} if the lines are collinear, {@code false} otherwise.
    */
   default boolean isCollinear(Line2D other, double angleEpsilon, double distanceEpsilon)
   {
      return EuclidGeometryTools.areLine2DsCollinear(getPoint(), getDirection(), other.getPoint(), other.getDirection(), angleEpsilon, distanceEpsilon);
   }

   /**
    * Tests if this line and the other line are perpendicular.
    *
    * @param other the query. Not modified.
    * @return {@code true} if the two lines are perpendicular, {@code false} otherwise.
    * @throws RuntimeException if either this line or {@code other} has not been initialized yet.
    */
   default boolean areLinesPerpendicular(Line2D other)
   {
      checkHasBeenInitialized();
      // Dot product of two vectors is zero if the vectors are perpendicular
      return getDirection().dot(other.getDirection()) < 1e-7;
   }
}
