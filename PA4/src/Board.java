import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int[][] blocks;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new java.lang.IllegalArgumentException();

        // deep-clone
        this.blocks = deepClone(blocks);
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // returns the expected number for a given row i and column j
    private int expectedNumber(int i, int j) {

        // each position should be i*n + j + 1, except very last block
        if (i == dimension()-1 && j == dimension()-1) {
            return 0;
        } else {
            return i*dimension() + j + 1;
        }
    }

    // number of blocks out of place
    public int hamming() {
        int blocksOutOfPlace = 0;
        int expectedNum, actualNumber;

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                // we don't care about the last element because that's where the empty block should be
                if (!(i == dimension()-1 && j == dimension()-1)) {
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
            int goalRow = blockValue / dimension();
            int goalColumn = (blockValue % dimension()) - 1;

            // there's some wraparound trickiness because array indices start at 0 but the first element starts at 1
            if (goalColumn < 0) {
                goalRow--;
                goalColumn += dimension();
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

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                totalDistance += manhattanDistance(i, j);
            }
        }

        return totalDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        // choose 2 blocks so long as they are not the empty block

        Coordinate first = null, last = null;

        // iterate forward for first block
        for (int i = 0; i < dimension() && first == null; i++) {
            for (int j = 0; j < dimension() && first == null; j++) {
                if (blocks[i][j] != 0) {
                    first = new Coordinate(i, j);
                }
            }
        }

        // iterate backwards for last block
        for (int i = dimension()-1; i >= 0 && last == null; i--) {
            for (int j = dimension()-1; j >= 0 && last == null; j--) {
                if (blocks[i][j] != 0) {
                    last = new Coordinate(i, j);
                }
            }
        }

        return twin(first, last);
    }

    private static int[][] deepClone(int[][] blocks) {
        int[][] clonedBlocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            clonedBlocks[i] = Arrays.copyOf(blocks[i], blocks.length);
        }
        return clonedBlocks;
    }

    // returns a Board where blocks at Coordinate a and Coordinate b are exchanged
    private Board twin(Coordinate a, Coordinate b) {
        // deep-clone
        int[][] twinBlocks = deepClone(this.blocks);

        // swap blocks at coordinates a and b
        int temp = twinBlocks[a.i][a.j];
        twinBlocks[a.i][a.j] = twinBlocks[b.i][b.j];
        twinBlocks[b.i][b.j] = temp;

        return new Board(twinBlocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {

        // object is null or not of type Board
        if (!(y instanceof Board))
            return false;

        // object is itself
        if (this == y)
            return true;

        Board that = (Board) y;

        // check both boards' sizes are same
        if (this.dimension() != that.dimension()) {
            return false;
        }

        // compare each block
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private class Coordinate {
        private final int i;
        private final int j;

        public Coordinate(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    // all neighboring boards
    // basically, all boards where one of the blocks adjacent (not diagonal) to the empty block is swapped with it
    public Iterable<Board> neighbors() {

        ArrayList<Board> list = new ArrayList<>(4);

        // find empty block
        Coordinate emptyBlock = new Coordinate(0,0);
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] == 0) {
                    emptyBlock = new Coordinate(i, j);
                }
            }
        }

        // calculate adjacent boards
        Coordinate above = new Coordinate(emptyBlock.i-1, emptyBlock.j);
        Coordinate below = new Coordinate(emptyBlock.i+1, emptyBlock.j);
        Coordinate left = new Coordinate(emptyBlock.i, emptyBlock.j-1);
        Coordinate right = new Coordinate(emptyBlock.i, emptyBlock.j+1);

        // add adjacent boards to list if valid
        if (isWithinBounds(above)) {
            Board aboveBoard = twin(emptyBlock, above);
            list.add(aboveBoard);
        }

        if (isWithinBounds(below)) {
            Board belowBoard = twin(emptyBlock, below);
            list.add(belowBoard);
        }

        if (isWithinBounds(left)) {
            Board leftBoard = twin(emptyBlock, left);
            list.add(leftBoard);
        }

        if (isWithinBounds(right)) {
            Board rightBoard = twin(emptyBlock, right);
            list.add(rightBoard);
        }

        return list;
    }

    // given row i and column j, is the coordinate inside this board's dimensions?
    private boolean isWithinBounds(Coordinate c) {
        return c.i >= 0 && c.j >= 0 && c.i < dimension() && c.j < dimension();
    }


    // string representation of this board (in the output format specified below)
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(dimension() + "\n");

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

        int[][] b = new int[3][3];
        b[0][0] = 8;
        b[0][1] = 1;
        b[0][2] = 3;
        b[1][0] = 4;
        b[1][1] = 0;
        b[1][2] = 2;
        b[2][0] = 7;
        b[2][1] = 6;
        b[2][2] = 5;
        Board board1 = new Board(b);
        Board board1Dup = new Board(b);

        // test Board's immutability
        b[1][1] = 5;
        b[2][2] = 0;
        Board board2 = new Board(b);
        assert(!board1.equals(board2));
        assert(board1.hamming() != board2.hamming());

        int[][] bAbove = new int[3][3];
        bAbove[0][0] = 8;
        bAbove[0][1] = 0;
        bAbove[0][2] = 3;
        bAbove[1][0] = 4;
        bAbove[1][1] = 1;
        bAbove[1][2] = 2;
        bAbove[2][0] = 7;
        bAbove[2][1] = 6;
        bAbove[2][2] = 5;
        Board board1Above = new Board(bAbove);

        int[][] bBelow = new int[3][3];
        bBelow[0][0] = 8;
        bBelow[0][1] = 1;
        bBelow[0][2] = 3;
        bBelow[1][0] = 4;
        bBelow[1][1] = 6;
        bBelow[1][2] = 2;
        bBelow[2][0] = 7;
        bBelow[2][1] = 0;
        bBelow[2][2] = 5;
        Board board1Below = new Board(bBelow);

        int[][] bLeft = new int[3][3];
        bLeft[0][0] = 8;
        bLeft[0][1] = 1;
        bLeft[0][2] = 3;
        bLeft[1][0] = 0;
        bLeft[1][1] = 4;
        bLeft[1][2] = 2;
        bLeft[2][0] = 7;
        bLeft[2][1] = 6;
        bLeft[2][2] = 5;
        Board board1Left = new Board(bLeft);

        int[][] bRight = new int[3][3];
        bRight[0][0] = 8;
        bRight[0][1] = 1;
        bRight[0][2] = 3;
        bRight[1][0] = 4;
        bRight[1][1] = 2;
        bRight[1][2] = 0;
        bRight[2][0] = 7;
        bRight[2][1] = 6;
        bRight[2][2] = 5;
        Board board1Right = new Board(bRight);

        // test toString()
        StdOut.println(board1.toString());

        // test equals()
        assert(board1.equals(board1Dup));

        // test twin()
        assert(!board1.equals(board1.twin()));

        // test Hamming()
        assert(board1.hamming() == 5);

        // test Manhattan()
        assert(board1.manhattan() == 10);

        // test neighbors() - we should have 4 neighbors with our example
        ArrayList<Board> actualNeighbors = new ArrayList<>();
        actualNeighbors.add(board1Above);
        actualNeighbors.add(board1Below);
        actualNeighbors.add(board1Left);
        actualNeighbors.add(board1Right);
        ArrayList<Board> calculatedNeighbors = (ArrayList<Board>) board1.neighbors();
        calculatedNeighbors.removeIf(p -> actualNeighbors.contains(p));
        assert(calculatedNeighbors.isEmpty());

        // test isGoal()
        int[][] goalBlocks = new int[3][3];
        goalBlocks[0][0] = 1;
        goalBlocks[0][1] = 2;
        goalBlocks[0][2] = 3;
        goalBlocks[1][0] = 4;
        goalBlocks[1][1] = 5;
        goalBlocks[1][2] = 6;
        goalBlocks[2][0] = 7;
        goalBlocks[2][1] = 8;
        goalBlocks[2][2] = 0;
        Board goalBoard = new Board(goalBlocks);
        assert(goalBoard.isGoal());

    }
}
