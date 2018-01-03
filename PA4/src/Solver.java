import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.*;

public class Solver {

    Puzzle originalPuzzle;
    Puzzle twinPuzzle;

    private static class Debugger {
        public static void Log(String s) {
            //StdOut.println(s); // remove the comment if we want debugging output
        }
    }

    private class Puzzle {
        private MinPQ<SearchNode> pq;
        private ArrayList<Board> gameTree;
        private SearchNode currentNode;
        private boolean solved;

        public Puzzle(Board initial) {
            gameTree = new ArrayList<>();
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
                prune neighbors to ensure it's not a board we've seen before (search our game tree)
                add pruned neighbors to PQ

            Returns:
                true if this puzzle instance has reached goal board, false otherwise
         */
        public void step() {
            Iterable<Board> neighbors;
            currentNode = pq.delMin();
            Debugger.Log(String.format("removed from PQ (move #%d):\n%s", currentNode.moves, currentNode.board.toString()));
            gameTree.add(currentNode.board);
            neighbors = currentNode.board.neighbors();
            pruneNeighbors(neighbors);
            for (Board neighbor : neighbors) {
                pq.insert(new SearchNode(neighbor, currentNode.moves+1, currentNode));
                Debugger.Log("added to PQ:\n" + neighbor.toString());
            }
            Debugger.Log("----------------------------");

            // currentNode will contains a reference to the goal search node
            // and we can walk it backwards to get solution
            if (currentNode.board.isGoal()) {
                solved = true;
            }
        }

        private void pruneNeighbors(Iterable<Board> neighbors) {
            Iterator<Board> it = neighbors.iterator();
            while (it.hasNext()) {
                Board b = it.next();
                if (gameTree.contains(b)) {
                    // we've seen this board before so remove it
                    Debugger.Log("pruned:\n" + b.toString());
                    it.remove();
                }
            }
        }
    }

    private class SearchNode {
        final Board board;
        final int moves;
        final SearchNode prev;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
    }

    private static class SearchNodeComparator implements Comparator<SearchNode> {

        public int compare(SearchNode b1, SearchNode b2) {
            // compare based on Manhattan priorities + moves
            int b1priority = b1.board.manhattan() + b1.moves;
            int b2priority = b2.board.manhattan() + b2.moves;

            return Integer.compare(b1priority, b2priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        originalPuzzle = new Puzzle(initial);
        twinPuzzle = new Puzzle(initial.twin());

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
