import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Solver {

    private final Puzzle originalPuzzle;

    private class Puzzle {
        private final MinPQ<SearchNode> pq;
        private SearchNode currentNode;
        private boolean solved;

        public Puzzle(Board initial) {
            pq = new MinPQ<SearchNode>(new SearchNodeComparator());
            pq.insert(new SearchNode(initial, 0, null));
        }

        public boolean isSolved() {
            return solved;
        }

        /*
            Algorithm:
                remove search node from MinPQ
                add search node to game tree
                get neighbors of removed search node
                enqueue node only if it is not the same as the board of the predecessor search node
                add pruned neighbors to PQ

            Returns:
                true if this puzzle instance has reached goal board, false otherwise
         */
        public void step() {

            currentNode = pq.delMin();
            // StdOut.println(String.format("removed from PQ (moves = %d, Manhattan = %d, priority = %d):\n%s", currentNode.moves, currentNode.manhattan, currentNode.moves+currentNode.manhattan, currentNode.board.toString()));
            for (Board neighbor : currentNode.neighbors) {
                if (!seenBefore(currentNode.prev, neighbor)) { // ensure we've never seen this board before
                    pq.insert(new SearchNode(neighbor, currentNode.moves+1, currentNode));
                    // StdOut.println("added to PQ:\n" + neighbor.toString());
                }

            }
            // StdOut.println("----------------------------");

            // currentNode will contains a reference to the goal search node
            // and we can walk it backwards to get solution
            if (currentNode.isGoal) {
                solved = true;
            }
        }

        // walk up the entire chain to ensure we've never seen this board before
        private boolean seenBefore(SearchNode parent, Board neighbor) {
            if (parent == null)
            {
                return false;
            }
            else {
                SearchNode iter = parent;
                while (iter != null) {
                    if (neighbor.equals(iter.board))
                        return true;
                    iter = iter.prev;
                }
            }

            return false;
        }
    }

    private class SearchNode {
        final Board board;
        final int moves;
        final SearchNode prev;
        final boolean isGoal;
        final Iterable<Board> neighbors;
        final int manhattanPriority;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhattanPriority = this.moves + board.manhattan();
            this.isGoal = board.isGoal();
            this.neighbors = board.neighbors();
        }
    }

    private static class SearchNodeComparator implements Comparator<SearchNode> {

        public int compare(SearchNode b1, SearchNode b2) {
            // compare based on Manhattan priorities
            return Integer.compare(b1.manhattanPriority, b2.manhattanPriority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }

        originalPuzzle = new Puzzle(initial);
        Puzzle twinPuzzle = new Puzzle(initial.twin());

        // we loop until one of the two puzzles has reached its goal board
        do {
            originalPuzzle.step();
            twinPuzzle.step();
        } while (!originalPuzzle.isSolved() && !twinPuzzle.isSolved());

        // when we reach this point, it means either the original or the twin has been solved
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return originalPuzzle.isSolved();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return originalPuzzle.currentNode.moves;

        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            ArrayList<Board> path = new ArrayList<>();
            SearchNode it = originalPuzzle.currentNode;
            while (it != null) {
                path.add(it.board);
                it = it.prev;
            }

            // we want the solution to read from initial board to goal goal, i.e. chronologically forward
            Collections.reverse(path);

            return path;
        }

        return null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
