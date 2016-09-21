/**
 * Created by Matthew Huynh on 9/6/2016.
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int SITE_STATE_CLOSED = 0;
    private static final int SITE_STATE_OPEN = 1;
    private static final int SITE_STATE_FULL = 2;

    private WeightedQuickUnionUF map;
    private int gridSize; // this is necessary to navigate our array
    private int[] openFull; // -1 = closed, 0 = open, 1 = full
    private int topSiteComponentId, bottomSiteComponentId;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        // validate param
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        // add 2 spots at the end of the array for the virtual top and bottom sites
        map = new WeightedQuickUnionUF(n*n + 2);
        gridSize = n;

        // each site should be initialized as closed
        openFull = new int[n * n];
        for (int i = 0; i < openFull.length; i++) {
            openFull[i] = SITE_STATE_CLOSED;
        }

        // initialize virtual top and bottom sites
        topSiteComponentId = map.find(n*n); // 2nd to last
        bottomSiteComponentId = map.find(n*n+1); // last
        for (int topSiteCol = 1; topSiteCol <= gridSize; topSiteCol++) {
            int topSiteIdx = getArrayIndex(1, topSiteCol);
            map.union(topSiteIdx, topSiteComponentId);
        }
        for (int bottomSiteCol = 1; bottomSiteCol <= gridSize; bottomSiteCol++) {
            int bottomSiteIdx = getArrayIndex(gridSize, bottomSiteCol);
            map.union(bottomSiteIdx, bottomSiteComponentId);
        }
    }

    // open site (row i, column j) if it is not open already
    public void open(int row, int col) {
        // the upper-left site is indexed (1, 1) but it will be presented as (0, 0) in our map
        validateIndices(row, col);

        // if position is already open
        if (isOpen(row, col)) {
            // do nothing
            return;
        } else {
            // System.out.printf("Opening (%d, %d)\n", row, col);
            // mark the site as open
            int newSiteArrIdx = getArrayIndex(row, col);
            openFull[newSiteArrIdx] = SITE_STATE_OPEN;

            // check if any adjacent sites are open and union with them if so
            unionNeighbors(row, col);

            // if the newly opened site is a top node OR a nearby site is full, promote the site from open to full
            if (row == 1 || hasFullNeighbor(row, col)) {
                openFull[newSiteArrIdx] = SITE_STATE_FULL;
            }

            // if new site is full, spread to neighbor open sites
            if (openFull[newSiteArrIdx] == SITE_STATE_FULL) {
                spreadLiquid(row, col);
            }
        }
    }

    private void spreadLiquid(int sourceRow, int sourceCol) {
        // start from a source and spread liquid to open sites
        spreadLiquid(sourceRow, sourceCol, sourceRow-1, sourceCol); // above
        spreadLiquid(sourceRow, sourceCol, sourceRow+1, sourceCol); // below
        spreadLiquid(sourceRow, sourceCol, sourceRow, sourceCol-1); // left
        spreadLiquid(sourceRow, sourceCol, sourceRow, sourceCol+1); // right
    }

    private void spreadLiquid(int sourceRow, int sourceCol, int targetRow, int targetCol) {
        // if either targetRow or targetCol is invalid, return
        if (!validateIndicesNoThrow(targetRow, targetCol)) {
            return;
        }

        // if we're trying to spread to the source, return
        if (sourceRow == targetRow && sourceCol == targetCol) {
            return;
        }

        // if site is already full, return (because the liquid already spread from that site)
        if (isFull(targetRow, targetCol)) {
            return;
        }

        // if site is open, mark as full and then spread the liquid to neighbors
        if (isOpen(targetRow, targetCol)) {
            openFull[getArrayIndex(targetRow, targetCol)] = SITE_STATE_FULL;

            // call this method on all neighbors to spread liquid
            spreadLiquid(sourceRow, sourceCol, targetRow-1, targetCol); // above
            spreadLiquid(sourceRow, sourceCol, targetRow+1, targetCol); // below
            spreadLiquid(sourceRow, sourceCol, targetRow, targetCol-1); // left
            spreadLiquid(sourceRow, sourceCol, targetRow, targetCol+1); // right
        }
    }

    private boolean hasFullNeighbor(int row, int col) {
        // above
        if (validateIndicesNoThrow(row-1, col)) {
            if (isFull(row-1, col)) {
                return true;
            }
        }

        // below
        if (validateIndicesNoThrow(row+1, col)) {
            if (isFull(row+1, col)) {
                return true;
            }
        }

        // left
        if (validateIndicesNoThrow(row, col-1)) {
            if (isFull(row, col-1)) {
                return true;
            }
        }

        // right
        if (validateIndicesNoThrow(row, col+1)) {
            if (isFull(row, col+1)) {
                return true;
            }
        }

        return false;
    }

    // tries to union a neighboring site if site node exists
    private void tryUnionNeighbor(int newSiteRow, int newSiteCol, int neighborRow, int neighborCol) {
        if (validateIndicesNoThrow(neighborRow, neighborCol)) {
            if (isOpen(neighborRow, neighborCol)) {
                int newSiteIdx = getArrayIndex(newSiteRow, newSiteCol);
                int neighborIdx = getArrayIndex(neighborRow, neighborCol);
                // System.out.println("tryUnionNeighbor: newSiteIdx=" + newSiteIdx + ", neighborIdx=" + neighborIdx);
                map.union(newSiteIdx, neighborIdx);
            }
        }
    }

    // find the above, below, left, and right of the new site and union if possible
    private void unionNeighbors(int row, int col) {
        // above
        tryUnionNeighbor(row, col, row-1, col);

        // below
        tryUnionNeighbor(row, col, row+1, col);

        // left
        tryUnionNeighbor(row, col, row, col-1);

        // right
        tryUnionNeighbor(row, col, row, col+1);
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        // the upper-left site is indexed (1, 1) but it will be presented as (0, 0) in our map
        validateIndices(i, j);

        int pos = getArrayIndex(i, j);
        return openFull[pos] != SITE_STATE_CLOSED;
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        // the upper-left site is indexed (1, 1) but it will be presented as (0, 0) in our map
        validateIndices(i, j);

        int pos = getArrayIndex(i, j);
        return openFull[pos] == SITE_STATE_FULL;
    }

    // checks that indices i and j are inside the map bounds
    private void validateIndices(int i, int j) {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    // true if valid indices, false otherwise
    private boolean validateIndicesNoThrow(int i, int j) {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize) {
            return false;
        }
        return true;
    }

    // expects only valid input
    private int getArrayIndex(int row, int col) {
        return (row-1)*gridSize + (col-1);
    }

    // does the system percolate?
    public boolean percolates() {
        // special case grid 1 where it will only percolate iff the site is full
        if (gridSize == 1) {
            return isFull(1, 1);
        }

        return map.connected(topSiteComponentId, bottomSiteComponentId);
    }

    public static void main(String[] args) {
        // test client (optional)

        test1x1();

        test2x2();

        test3x3();

        test5x5();
    }

    private static void test1x1() {
        System.out.println("1x1 Test");
        Percolation p = new Percolation(1);
        assert !p.isOpen(1, 1);
        boolean perc = p.percolates();
        assert !perc;
        p.open(1, 1);
        assert p.isFull(1, 1);
        perc = p.percolates();
        assert perc;
    }

    private static void test5x5() {
        System.out.println("5x5 Test");
        Percolation p = new Percolation(5);
        p.open(5, 4);
        p.open(5, 3);
        p.open(4, 3);
        p.open(3, 3);
        p.open(2, 3);
        p.open(1, 2);
        p.printDebugGrid();
        boolean perc = p.percolates();
        assert !perc;
        p.open(1, 3);
        perc = p.percolates();
        p.printDebugGrid();
        assert perc;
    }

    private static void test2x2() {
        System.out.println("2x2 Test");
        Percolation p = new Percolation(2);
        assert !p.isOpen(1, 1);
        assert !p.isOpen(2, 1);
        p.printDebugGrid();
        p.open(1, 1);
        p.open(2, 1);
        boolean perc = p.percolates();
        p.printDebugGrid();
        assert perc;
    }

    private static void test3x3() {
        System.out.println("3x3 Test");
        Percolation p = new Percolation(3);

        p.printDebugGrid();
        assert !p.isOpen(3, 3);
        assert !p.isFull(2, 2);
        p.open(3, 2);
        p.open(2, 2);
        p.printDebugGrid();
        assert !p.isFull(2, 2);
        assert p.isOpen(3, 2);
        assert p.isOpen(2, 2);
        boolean perc = p.percolates();
        p.printDebugGrid();
        assert !perc;

        p.open(1, 2);
        p.printDebugGrid();
        perc = p.percolates();
        p.printDebugGrid();
        assert p.isFull(1, 2);
        assert p.isFull(2, 2);
        assert p.isFull(3, 2);
        assert perc;
    }

    private void printDebugGrid() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int pos = row*gridSize + col;
                System.out.print(openFull[pos] + " ");
            }
            System.out.println();
        }
    }
}
