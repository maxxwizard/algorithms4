import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.*;

public class Solver {

    Puzzle originalPuzzle;
    Puzzle twinPuzzle;

    private class Puzzle {
        private MinPQ<SearchNode> pq;
        private ArrayList<Board> gameTree;
        private SearchNode currentNode;

        public Puzzle(Board initial) {
            gameTree = new ArrayList<>();
            pq = new MinPQ<SearchNode>(new SearchNodeComparator());
            pq.insert(new SearchNode(initial, 0, null));

            Iterable<Board> neighbors;
        /*
        do {
            remove search node from MinPQ
            add search node to game tree
            get neighbors of removed search node
            prune neighbors to ensure it's not a board we've seen before (search our game tree)
            add pruned neighbors to PQ
        } while (!board.isGoal)
         */
            do {
                currentNode = pq.delMin();
                StdOut.println(String.format("removed from PQ (move #%d):\n%s", currentNode.moves, currentNode.board.toString()));
                gameTree.add(currentNode.board);
                neighbors = currentNode.board.neighbors();
                pruneNeighbors(neighbors);
                for (Board neighbor : neighbors) {
                    pq.insert(new SearchNode(neighbor, currentNode.moves+1, currentNode));
                    StdOut.println("added to PQ:\n" + neighbor.toString());
                }
                StdOut.println("----------------------------");
            } while (!currentNode.board.isGoal());

            // currentNode now contains a reference to the goal search node and we can walk it backwards to get solution
            StdOut.println("goal:\n" + currentNode.board.toString());
        }

        private void pruneNeighbors(Iterable<Board> neighbors) {
            Iterator<Board> it = neighbors.iterator();
            while (it.hasNext()) {
                Board b = it.next();
                if (gameTree.contains(b)) {
                    // we've seen this board before so remove it
                    StdOut.println("pruned:\n" + b.toString());
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

    private static class BoardComparator implements Comparator<Board> {

        public int compare(Board b1, Board b2) {
            // compare based on Manhattan priorities
            int b1mp = b1.manhattan();
            int b2mp = b2.manhattan();

            return Integer.compare(b1mp, b2mp);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        originalPuzzle = new Puzzle(initial);
        //boolean twin = solvePuzzle(initial.twin());

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return originalPuzzle.currentNode != null;
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

    private static void UnitTests() {
        // test BoardComparator()
        BoardComparator comparator = new BoardComparator();

        int[][] b = new int[3][3];
        b[0][0]= 8;
        b[0][1]= 1;
        b[0][2]= 3;
        b[1][0]= 4;
        b[1][1]= 0;
        b[1][2]= 2;
        b[2][0]= 7;
        b[2][1]= 6;
        b[2][2]= 5;

        Board board1 = new Board(b);
        assert(board1.manhattan() == 10);
        Board board2 = board1.twin();
        assert(board2.manhattan() == 8);
        assert(comparator.compare(board1, board2) > 0);

        // test Solver()
        // test isSolvable()
        // test moves()
        // test solution()
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        //UnitTests();

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
