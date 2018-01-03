import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private MinPQ<Board> pq;

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
        pq = new MinPQ<Board>(new BoardComparator());
        pq.insert(initial);
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        throw new java.lang.UnsupportedOperationException();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        throw new java.lang.UnsupportedOperationException();
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        throw new java.lang.UnsupportedOperationException();
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

        UnitTests();

        /*
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
        */
    }
}
