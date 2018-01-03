import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private final int[][] blocks;
    private final int n;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new java.lang.IllegalArgumentException();

        this.blocks = blocks;
        this.n = blocks.length;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // returns the expected number for a given row i and column j
    private int expectedNumber(int i, int j) {

        // each position should be i*n + j + 1, except very last block
        if (i == n-1 && j == n-1) {
            return 0;
        } else {
            return i*n + j + 1;
        }
    }

    // number of blocks out of place
    public int hamming() {

        int blocksOutOfPlace = 0;
        int expectedNum, actualNumber;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // we don't care about the last element because that's where the empty block should be
                if (!(i == n-1 && j == n-1)) {
                    expectedNum = expectedNumber(i, j);
                    actualNumber = blocks[i][j];

                    if (actualNumber != expectedNum) {
                        blocksOutOfPlace++;
                    }
                }
            }
        }

        return blocksOutOfPlace;
    }

    // returns the sum of vertical and horizontal distance of a block to its goal position
    private int manhattanDistance(int i, int j) {

        int blockValue = blocks[i][j];

        // calculate only if not empty block
        if (blockValue != 0) {
            int goalRow = blockValue / n;
            int goalColumn = (blockValue % n) - 1;

            // there's some wraparound trickiness because array indices start at 0 but the first element starts at 1
            if (goalColumn < 0) {
                goalRow--;
                goalColumn += n;
            }

            int distanceRow = Math.abs(goalRow - i);
            int distanceColumn = Math.abs(goalColumn - j);
            return distanceColumn + distanceRow;
        }

        // must be the empty block
        return 0;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {

        int totalDistance = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                totalDistance += manhattanDistance(i, j);
            }
        }

        return totalDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        // deep-clone
        int[][] twinBlocks = new int[n][];
        for (int i = 0; i < n; i++) {
            twinBlocks[i] = Arrays.copyOf(blocks[i], n);
        }

        // just swap first and last block
        int temp = twinBlocks[0][0];
        twinBlocks[0][0] = twinBlocks[n-1][n-1];
        twinBlocks[n-1][n-1] = temp;

        return new Board(twinBlocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;

        // check both boards' sizes are same
        if (this.dimension() != that.dimension()) {
            return false;
        }

        // compare each block
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        throw new java.lang.UnsupportedOperationException();
    }

    // string representation of this board (in the output format specified below)
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(blocks[i][j] + " ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

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
        Board board2 = new Board(b);

        // test toString()
        StdOut.println(board1.toString());

        // test equals()
        assert(board1.equals(board2));

        // test twin()
        assert(!board1.equals(board1.twin()));

        // test Hamming()
        assert(board1.hamming() == 5);

        // test Manhattan()
        assert(board1.manhattan() == 10);

        // test neighbors()

    }
}
