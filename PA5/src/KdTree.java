import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {

    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private static final boolean LEFT = false;
    private static final boolean RIGHT = true;

    private Node root;
    private Point2D nearestPoint;
    private double nearestPointDistance;

    private static class Node {
        // even levels of the tree = vertical line = p.x() is the key
        // odd levels of the tree = horizontal line = p.y() is the key

        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;       // number of nodes in subtree
        private final boolean orientation; // whether this node is vertical or horizontal

        public Node(Point2D point, RectHV rect, int size, boolean orientation) {
            this.p = point;
            this.rect = rect;
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
            RectHV newRect = null;

            if (parent == null) {
                // we are the first node!
                newRect = new RectHV(0, 0, 1, 1);
            } else {
                if (side == LEFT) {
                    if (orientation == VERTICAL) {
                        newRect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                    } else if (orientation == HORIZONTAL) {
                        newRect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                    }
                } else if (side == RIGHT) {
                    if (orientation == VERTICAL) {
                        newRect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                    } else if (orientation == HORIZONTAL) {
                        newRect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }
            }

            Node newNode = new Node(p, newRect, 1, orientation);

            // StdOut.println(String.format("inserted Node (%.2f, %.2f) | rect (%.2f, %.2f, %.2f, %.2f) | %s \n", newNode.p.x(), newNode.p.y(), newNode.rect.xmin(), newNode.rect.ymin(), newNode.rect.xmax(), newNode.rect.ymax(), orientation));
            return newNode;
        }

        // we do not support duplicate points so return existing point
        if (x.p.equals(p)) {
            return x;
        }

        // go left if new Point is smaller
        // go right if new Point is larger
        int cmp = orientationCompare(x, p);

        // every time we go down a level, we flip orientation
        if (cmp < 0) {
            // StdOut.println("going left");
            x.lb = insert(x.lb, p, x, LEFT, !x.orientation);
        }
        else {
            // StdOut.println("going right");
            x.rt = insert(x.rt, p, x, RIGHT, !x.orientation);
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

        return get(p, root, VERTICAL) != null;
    }

    private Point2D get(Point2D p, Node x, boolean orientation) {

        if (x == null) return null;

        if (x.p.equals(p))
            return x.p;

        int cmp = orientationCompare(x, p);

        // every time we go down a level, we flip orientation
        if (cmp < 0) return get(p, x.lb, !x.orientation);
        else return get(p, x.rt, !x.orientation);
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

        ArrayList<Point2D> points = new ArrayList<Point2D>();

        range(rect, root, points);

        return points;
    }

    private void range(RectHV rect, Node x, ArrayList<Point2D> points) {

        if (x == null) {
            return;
        }

        if (rect.contains(x.p)) {
            points.add(x.p);
        }

        if (x.lb != null && rect.intersects(x.lb.rect)) {
            range(rect, x.lb, points);
        }

        if (x.rt != null && rect.intersects(x.rt.rect)) {
            range(rect, x.rt, points);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        if (this.size() == 0)
            return null;

        nearestPointDistance = Double.POSITIVE_INFINITY;
        nearest(p, root);

        return nearestPoint;
    }

    private void nearest(Point2D p, Node x) {

        if (x == null)
            return;

        if (x.p.distanceSquaredTo(p) < nearestPointDistance) {
            nearestPoint = x.p;
            nearestPointDistance = x.p.distanceSquaredTo(p);
        }

        if (x.orientation == VERTICAL) {
            if (p.x() < x.p.x()) { // p is on left side of splitting line
                nearest(p, x.lb);
                if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < nearestPointDistance) {
                    // if distance to opposite side's rect is less than distance to the current nearest point found,
                    // then search that rect as well
                    nearest(p, x.rt);
                }
            } else { // p is on right side of splitting line
                nearest(p, x.rt);
                if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < nearestPointDistance) {
                    nearest(p, x.lb);
                }
            }
        } else if (x.orientation == HORIZONTAL) {
            if (p.y() < x.p.y()) { // p is on bottom side of splitting line
                nearest(p, x.lb);
                if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < nearestPointDistance) {
                    nearest(p, x.rt);
                }
            } else { // p is on top side of splitting line
                nearest(p, x.rt);
                if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < nearestPointDistance) {
                    nearest(p, x.lb);
                }
            }
        }
    }

    // unit tests
    public static void main(String[] args) {
        KdTree tree = new KdTree();

        assert(!tree.contains(new Point2D(0.0, 1.0)));

        tree.insert(new Point2D(0.25, 0.125));
        tree.insert(new Point2D(0.0, 0.875));
        tree.insert(new Point2D(0.5, 0.75));
        tree.insert(new Point2D(0.625, 0.25));
        tree.insert(new Point2D(0.375, 0.0));
        tree.insert(new Point2D(1.0, 0.875));
        tree.insert(new Point2D(0.875, 0.75));
        assert(tree.size() == 7);

        tree = new KdTree();
        tree.insert(new Point2D(1.0, 1.0));
        tree.insert(new Point2D(0.0, 1.0));
        tree.insert(new Point2D(1.0, 0.0));
        tree.insert(new Point2D(1.0, 1.0));
        assert(tree.size() == 3);

        tree = new KdTree();
        tree.insert(new Point2D(0.5, 0.5));
        assert(!tree.contains(new Point2D(0.5, 0.24)));

        tree = new KdTree();
        tree.insert(new Point2D(0.0, 0.0));
        assert(tree.contains(new Point2D(0.0, 0.0)));
    }
}
