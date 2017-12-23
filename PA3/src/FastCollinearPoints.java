import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

public class FastCollinearPoints {

    List<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // bad array
        if (points == null)
            throw new java.lang.IllegalArgumentException();

        // empty array
        if (points.length == 0)
            throw new java.lang.IllegalArgumentException();

        // init line segment storage
        segments = new ArrayList<LineSegment>();

        ArrayList<Point> seenPoints = new ArrayList<Point>();
        Point[] sortedPoints = points.clone();

        // iterate assuming current point is the origin
        System.out.println(String.format("processing %d points", points.length));
        for (int i = 0; i < points.length; i++) {
            System.out.print(points[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < points.length; i++) {

            Point origin = points[i];

            //System.out.println(String.format("origin = points[%d] = %s", i, origin));
            
            // we want points to be unique
            if (seenPoints.contains(origin))
                throw new java.lang.IllegalArgumentException();
            else
                seenPoints.add(origin);
            
            // sort the points according to the slope they make with the current point
            Arrays.sort(sortedPoints, origin.slopeOrder());

            // if we have 3 or more adjacent points with equal slopes, these points are collinear
            Double currentSlope = 0.0;
            Double currentStreakSlope = Double.NEGATIVE_INFINITY;
            ArrayList<Point> streak = new ArrayList<>();

            // if encounter ourselves
            //   do nothing
            // if same slope
            //   add to streak
            // else (different slope)
            //  if our streak qualifies, add it as a line segment and continue searching
            //  reset streak and overwrite the streak slope
            for (int j = 0; j < sortedPoints.length; j++) {
                Point currentPoint = sortedPoints[j];
                currentSlope = origin.slopeTo(currentPoint);
                //System.out.println(String.format("slope to %s: %2f", currentPoint, currentSlope));

                if (currentSlope.equals(Double.NEGATIVE_INFINITY))
                    continue;

                if (currentStreakSlope.equals(currentSlope)) {
                    streak.add(currentPoint);
                    //System.out.println(String.format(" added %s to streak", currentPoint));
                }
                else {
                    if (checkForValidStreak(origin, i, streak)) {
                        streak.clear();
                        continue;
                    }
                    else {
                        currentStreakSlope = currentSlope;
                        //System.out.println(String.format(" streak slope = %f", currentStreakSlope));
                        streak.clear();
                        //System.out.println(" cleared streak");
                        streak.add(currentPoint);
                        //System.out.println(String.format(" added %s to streak", currentPoint));
                    }
                }

                // we need to check for valid streak on the last element as well
                if (j == sortedPoints.length-1)
                    checkForValidStreak(origin, i, streak);
            }

        }
    }

    private boolean checkForValidStreak(Point origin, int originIndex, ArrayList<Point> streak) {
        if (streak.size() >= 3) {
            //System.out.println(String.format("\nStreak of %d points found:", streak.size()));

            // add origin to streak
            streak.add(origin);

            Collections.sort(streak);
            for (int i = 0; i < streak.size(); i++) {
                StdOut.println(streak.get(i));
            }

            // only add this line segment if the origin is the lowest in the sorted array
            // this avoids permutations of this line
            Point lowest = streak.get(0);
            Point highest = streak.get(streak.size()-1);
            if (origin.equals(lowest)) {
                LineSegment newSegment = new LineSegment(lowest, highest);
                segments.add(newSegment);
                //StdOut.println(String.format("adding LineSegment %s", newSegment));
            }

            StdOut.println();
            return true;
        }

        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segmentsArray = new LineSegment[segments.size()];
        return segments.toArray(segmentsArray);
    }
}
