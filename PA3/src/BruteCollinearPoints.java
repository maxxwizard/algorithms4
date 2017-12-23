import java.util.*;
import java.util.Collections;

public class BruteCollinearPoints {

    private HashMap<Double /* slope */, List<Point> /* collinear points */> map;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // bad array
        if (points == null)
            throw new java.lang.IllegalArgumentException();

        // empty array
        if (points.length == 0)
            throw new java.lang.IllegalArgumentException();

        // init our hashmap
        map = new HashMap<>();

        // generate all combinations of (points choose 4)
        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                double slopeToJ = points[i].slopeTo(points[j]);
                for (int k = j+1; k < points.length; k++) {
                    double slopeToK = points[j].slopeTo(points[k]);
                    if (slopeToJ == slopeToK) {
                        for (int l = k+1; l < points.length; l++) {
                            double slopeToL = points[k].slopeTo(points[l]);
                            if (slopeToL == slopeToK) {
                                Point p1 = points[i];
                                Point p2 = points[j];
                                Point p3 = points[k];
                                Point p4 = points[l];

                                // ensure all points are unique
                                if (!pointsAreUnique(p1, p2, p3, p4))
                                    throw new java.lang.IllegalArgumentException();

                                // slope across 4 points are the same due to previous "if" comparisons
                                List<Point> list;
                                if (map.containsKey(slopeToL)) {
                                    list = map.get(slopeToL);
                                } else {
                                    list = new ArrayList<>();
                                    map.put(slopeToL, list);
                                }
                                list.add(p1);
                                list.add(p2);
                                list.add(p3);
                                list.add(p4);
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
        return segments().length;
    }

    // the line segments
    public LineSegment[] segments() {
        ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

        for (Double key : map.keySet()) {
            List<Point> list = map.get(key);
            Collections.sort(list);
            LineSegment newSegment = new LineSegment(list.get(0), list.get(list.size()-1));
            segments.add(newSegment);
        }

        LineSegment[] array = new LineSegment[segments.size()];
        return segments.toArray(array);
    }
}
