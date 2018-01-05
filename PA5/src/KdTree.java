import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class KdTree {

    // even levels of the tree = vertical line
    // odd levels of the tree = horizontal line

    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D point) {
            this.p = point;
        }
    }

    // construct an empty set of points
    public KdTree() {
        this.root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.root.lb == null && this.root.rt == null;
    }

    // number of points in the set
    public int size() {
        return size(this.root);
    }

    private int size(Node n) {
        if (n == null)
            return 0;
        else
            return 1 + size(n.lb) + size(n.rt);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        throw new java.lang.UnsupportedOperationException();
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        throw new java.lang.UnsupportedOperationException();
    }

    // draw all points to standard draw
    public void draw() {
        throw new java.lang.UnsupportedOperationException();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException();

        throw new java.lang.UnsupportedOperationException();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        if (this.size() == 0)
            return null;

        throw new java.lang.UnsupportedOperationException();
    }
}
