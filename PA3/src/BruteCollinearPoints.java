import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class BruteCollinearPoints {

    private List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // bad array
        if (points == null)
            throw new java.lang.IllegalArgumentException();

        // empty array
        if (points.length == 0)
            throw new java.lang.IllegalArgumentException();

        // null entry in array
        for (Point p : points) {
            if (p == null) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        // init our segments storage
        segments = new ArrayList<>();

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);

        // generate all combinations of (points choose 4)
        for (int i = 0; i < sortedPoints.length; i++) {
            for (int j = i+1; j < sortedPoints.length; j++) {
                double slopeToJ = sortedPoints[i].slopeTo(sortedPoints[j]);
                for (int k = j+1; k < sortedPoints.length; k++) {
                    double slopeToK = sortedPoints[j].slopeTo(sortedPoints[k]);
                    if (slopeToJ == slopeToK) {
                        for (int l = k+1; l < sortedPoints.length; l++) {
                            double slopeToL = sortedPoints[k].slopeTo(sortedPoints[l]);
                            if (slopeToL == slopeToK) {
                                Point p1 = sortedPoints[i];
                                Point p2 = sortedPoints[j];
                                Point p3 = sortedPoints[k];
                                Point p4 = sortedPoints[l];

                                // ensure all points are unique
                                if (!pointsAreUnique(p1, p2, p3, p4))
                                    throw new java.lang.IllegalArgumentException();

                                // slope across 4 points are the same due to previous "if" comparisons
                                // add the segment using the lowest and highest points
                                segments.add(new LineSegment(p1, p4));
                            }
                        }
                    }

                }
            }
        }
    }

    private boolean pointsAreUnique(Point p1, Point p2, Point p3, Point p4) {
        if (p1.equals(p2) || p1.equals(p3) || p1.equals(p4)
                || p2.equals(p3) || p2.equals(p4)
                || p3.equals(p4))
            return false;

        return true;
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
