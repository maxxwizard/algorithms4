import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

public class KdTree {

    private Node root;

    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private static final boolean LEFT = false;
    private static final boolean RIGHT = true;

    private static class Node {
        // even levels of the tree = vertical line = p.x() is the key
        // odd levels of the tree = horizontal line = p.y() is the key

        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;       // number of nodes in subtree
        private boolean orientation; // whether this node is vertical or horizontal

        public Node(Point2D point, int size, boolean orientation) {
            this.p = point;
            this.size = size;
            this.orientation = orientation;
        }
    }

    // construct an empty set of points
    public KdTree() {
        this.root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(this.root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        root = insert(root, p, null, LEFT, VERTICAL);
    }

    private Node insert(Node x, Point2D p, Node parent, boolean side, boolean orientation) {
        if (x == null) {
            Node newNode = new Node(p, 1, orientation);

            if (parent == null) {
                // we are the first node!
                newNode.rect = new RectHV(0, 0, 1, 1);
            } else {
                if (side == LEFT) {
                    if (orientation == VERTICAL) {
                        newNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                    } else if (orientation == HORIZONTAL) {
                        newNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                    }
                } else if (side == RIGHT) {
                    if (orientation == VERTICAL) {
                        newNode.rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                    } else if (orientation == HORIZONTAL) {
                        newNode.rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }
            }

            // StdOut.println(String.format("inserted Node (%.2f, %.2f) | rect (%.2f, %.2f, %.2f, %.2f) | %s \n", newNode.p.x(), newNode.p.y(), newNode.rect.xmin(), newNode.rect.ymin(), newNode.rect.xmax(), newNode.rect.ymax(), orientation));
            return newNode;
        }

        // go left if new Point is smaller
        // go right if new Point is larger
        int cmp = orientationCompare(x, p);

        // every time we go down a level, we flip orientation
        if (cmp < 0) {
            // StdOut.println("going left");
            x.lb = insert(x.lb, p, x, LEFT, !x.orientation);
        }
        else if (cmp > 0) {
            // StdOut.println("going right");
            Node rightnode = insert(x.rt, p, x, RIGHT, !x.orientation);
            x.rt = rightnode;
        }

        x.size = 1 + size(x.lb) + size(x.rt);

        return x;
    }

    // we choose the key to compare based on orientation
    private int orientationCompare(Node x, Point2D p) {
        int cmp;
        if (x.orientation == VERTICAL) {
            cmp = Double.compare(p.x(), x.p.x());
        } else {
            cmp = Double.compare(p.y(), x.p.y());
        }

        return cmp;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        return get(p) != null;
    }

    private Point2D get(Point2D p) {
        return get(root, p, VERTICAL);
    }

    private Point2D get(Node x, Point2D p, boolean orientation) {

        if (x == null) return null;

        int cmp = orientationCompare(x, p);

        // every time we go down a level, we flip orientation
        if (cmp < 0) return get(x.lb, p, !orientation);
        if (cmp > 0) return get(x.rt, p, !orientation);
        else return x.p;
    }

    // draw all points and subdivisions to standard draw
    public void draw() {
        if (root != null)
            draw(root);
    }

    private void draw(Node x) {
        if (x == null)
            return;
        // draw the current point in black
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();

        // draw the splitting lines in the color of orientation (vertical = red, horizontal = blue)
        if (x.orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        StdDraw.setPenRadius();


        // call draw on the left and right subtrees
        draw(x.lb);
        draw(x.rt);
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
