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

        root = insert(root, p, VERTICAL);
    }

    private Node insert(Node x, Point2D p, boolean orientation) {
        if (x == null) {
            Node newNode = new Node(p, 1, orientation);

            if (size() == 0) {
                StdOut.println("first node in tree");
                // we are the first node!
                //newNode.rect = new RectHV(p.x(), 0, p.x(), 1);
            }

            //StdOut.println(String.format("inserted Node (%.2f, %.2f) | size %d | rect (%.2f, %.2f, %.2f, %.2f) | %s \n",
            //        newNode.p.x(), newNode.p.y(), newNode.size, newNode.rect.xmin(), newNode.rect.ymin(), newNode.rect.xmax(), newNode.rect.ymax(), orientation));
            return newNode;
        }

        // go left if new Point is smaller
        // go right if new Point is larger
        int cmp = orientationCompare(x, p);

        // every time we go down a level, we flip orientation
        if (cmp < 0) {
            StdOut.println("going left");
            x.lb = insert(x.lb, p, !x.orientation);
        }
        else if (cmp > 0) {
            StdOut.println("going right");
            x.rt = insert(x.rt, p, !x.orientation);
        }
        else {
            StdOut.println("node already exists so do nothing");
        }

        x.size = 1 + size(x.lb) + size(x.rt);

        //x.rect = createRect(p, parent, orientation);
        //StdOut.println(String.format("set Node (%.2f, %.2f) to size %d with rect (%.2f, %.2f, %.2f, %.2f)",
        //        x.p.x(), x.p.y(), x.size, x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax()));

        return x;
    }

    private int orientationCompare(Node x, Point2D p) {
        // we choose the key to compare based on orientation
        int cmp;
        if (x.orientation == VERTICAL) {
            cmp = Double.compare(p.x(), x.p.x());
            //StdOut.println(String.format("vertical: (%.2f, %.2f) cmp (%.2f, %.2f) = %d", p.x(), p.y(), x.p.x(), x.p.y(), cmp));
        } else {
            cmp = Double.compare(p.y(), x.p.y());
            //StdOut.println(String.format("horizontal: (%.2f, %.2f) cmp (%.2f, %.2f) = %d", p.x(), p.y(), x.p.x(), x.p.y(), cmp));
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
        throw new java.lang.UnsupportedOperationException();
        /*
        if (x == null) return null;

        int cmp = orientationCompare(x, p, orientation);

        // every time we go down a level, we flip orientation
        if (cmp < 0) return get(x.lb, p, !orientation);
        if (cmp > 0) return get(x.rt, p, !orientation);
        else return x.p;
        */
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
        // draw its subdivision in the color of orientation (vertical = red, horizontal = blue)
        if (x.orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        StdDraw.setPenRadius();
        //x.rect.draw();
        // call draw on the left and right, flipping the orientation because we're moving levels
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
