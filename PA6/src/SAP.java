import edu.princeton.cs.algs4.*;

import java.util.HashMap;
import java.util.LinkedList;

public class SAP {

    private final Digraph graph;
    private final HashMap<Integer, LinkedList<SAPResult>> cachedResults;   // caches shortest common ancestor and ancestral path length between v->w

    private class SAPResult {

        public final int v;
        public final int w;
        public final int ancestor;
        public final int pathLength;

        public SAPResult(int v, int w, int ancestor, int pathLength) {
            this.v = v;
            this.w = w;
            this.ancestor = ancestor;
            this.pathLength = pathLength;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {

        if (G == null) {
            throw new java.lang.IllegalArgumentException();
        }

        graph = new Digraph(G);

        cachedResults = new HashMap<>();
    }

    private boolean validateVertex(int v) {
        if (v < 0 || v >= graph.V())
            return false;

        return true;
    }

    private SAPResult getCachedResult(int v, int w) {
        LinkedList<SAPResult> resultsList = cachedResults.get(v);
        if (resultsList != null) {
            for (SAPResult result : resultsList) {
                if (result.w == w) {
                    // StdOut.println(String.format("returning cached result for v = %d, w = %d", v, w));
                    return result;
                }
            }
        }

         StdOut.println(String.format("no cached result for v = %d, w = %d", v, w));
        return null;
    }

    private void putCachedResult(int v, SAPResult result) {
        LinkedList<SAPResult> resultsList = cachedResults.get(v);
        if (resultsList == null) {
            resultsList = new LinkedList<>();
            cachedResults.put(v, resultsList);
        }
        resultsList.add(result);
    }

    private void calculate(int v, int w) {
        // do a BFS from v and a BFS from w
        // iterate through all vertices and see if there is a path from v to x AND a path from w to x
        // if so, x is a common ancestor and the ancestral path length = length from v to x + length from w to x
        // keep track of the shortest ancestral path while we iterate

        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(graph, w);

        int shortestAncestralPathLength = Integer.MAX_VALUE;
        int shortestCommonAncestor = Integer.MAX_VALUE;

        Queue<Integer> q = new Queue<>();
        boolean[] marked = new boolean[graph.V()];
        marked[v] = true;
        q.enqueue(v);
        while (!q.isEmpty()) {
            int a = q.dequeue();
            // StdOut.println(String.format("processing vertex %d", a));

            if (bfs_v.hasPathTo(a) && bfs_w.hasPathTo(a)) {
                int ancestralPathLength = bfs_v.distTo(a) + bfs_w.distTo(a);
                if (ancestralPathLength < shortestAncestralPathLength) {
                    shortestCommonAncestor = a;
                    shortestAncestralPathLength = ancestralPathLength;
                }
            }

            // add all neighbors for processing
            for (int b : graph.adj(a)) {
                // StdOut.println(String.format(" adj[%d] = %d", a, b));
                if (!marked[b]) {
                    marked[b] = true;
                    q.enqueue(b);
                }
            }
        }

        // convert POS_INFINITY to -1
        if (shortestCommonAncestor == Integer.MAX_VALUE) {
            shortestCommonAncestor = -1;
            shortestAncestralPathLength = -1;
        }

        StdOut.println(String.format("  v = %d, w = %d, length = %d, ancestor = %d", v, w, shortestAncestralPathLength, shortestCommonAncestor));

        // cache the result bidirectionally
        SAPResult result = new SAPResult(v, w, shortestCommonAncestor, shortestAncestralPathLength);
        SAPResult reversedResult = new SAPResult(w, v, shortestCommonAncestor, shortestAncestralPathLength);
        putCachedResult(v, result);
        putCachedResult(w, reversedResult);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        // ensure vertex params are valid
        if (!validateVertex(v) || !validateVertex(w)) {
            throw new java.lang.IllegalArgumentException();
        }

        // check for a cached result
        SAPResult cachedResult = getCachedResult(v, w);
        if (cachedResult != null) {
            return cachedResult.pathLength;
        } else {
            calculate(v, w);
        }

        return getCachedResult(v, w).pathLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // ensure vertex params are valid
        if (!validateVertex(v) || !validateVertex(w)) {
            throw new java.lang.IllegalArgumentException();
        }

        // check for a cached result
        SAPResult cachedResult = getCachedResult(v, w);
        if (cachedResult != null) {
            return cachedResult.ancestor;
        } else {
            calculate(v, w);
        }

        return getCachedResult(v, w).ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        throw new java.lang.UnsupportedOperationException();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        throw new java.lang.UnsupportedOperationException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        SAP sap = new SAP(new Digraph(new In("C:\\sources\\algorithms4\\PA6\\wordnet\\digraph1.txt")));

        assert(sap.length(3, 11) == 4);
        assert(sap.length(11, 3) == 4);
        assert(sap.ancestor(3, 11) == 1);
        assert(sap.ancestor(11, 3) == 1);

        assert(sap.length(9, 12) == 3);
        assert(sap.length(12, 9) == 3);
        assert(sap.ancestor(9, 12) == 5);
        assert(sap.ancestor(12, 9) == 5);

        assert(sap.length(7, 2) == 4);
        assert(sap.length(2, 7) == 4);
        assert(sap.ancestor(7, 2) == 0);
        assert(sap.ancestor(2, 7) == 0);

        assert(sap.length(1, 6) == -1);
        assert(sap.length(6, 1) == -1);
        assert(sap.ancestor(1, 6) == -1);
        assert(sap.ancestor(6, 1) == -1);
    }
}