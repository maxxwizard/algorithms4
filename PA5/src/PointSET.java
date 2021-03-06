import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        this.set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        this.set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        return this.set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException();

        ArrayList<Point2D> insidePoints = new ArrayList<Point2D>();

        for (Point2D point : this.set) {
            if (rect.contains(point)) {
                insidePoints.add(point);
            }
        }

        return insidePoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        if (this.set.isEmpty())
            return null;

        Point2D nearest = null;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : this.set) {
            double distance = p.distanceSquaredTo(point);
            if (nearest == null || distance < shortestDistance) {
                shortestDistance = distance;
                nearest = point;
            }
        }

        return nearest;
    }
}
