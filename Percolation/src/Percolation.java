/**
 * Created by Matthew Huynh on 9/6/2016.
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    WeightedQuickUnionUF map;
    int gridSize; // this is necessary to navigate our array
    int[] openFull; // -1 = closed, 0 = open, 1 = full

    final int SITE_STATE_CLOSED = 0;
    final int SITE_STATE_OPEN = 1;
    final int SITE_STATE_FULL = 2;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        // validate param
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        map = new WeightedQuickUnionUF(n*n);
        gridSize = n;
        // each site should be initialized as closed
        openFull = new int[n * n];
        for (int i = 0; i < openFull.length; i++) {
            openFull[i] = SITE_STATE_CLOSED;
        }
    }

    // open site (row i, column j) if it is not open already
    public void open(int row, int col) {
        // the upper-left site is indexed (1, 1) but it will be presented as (0, 0) in our map
        validatePositionOneBased(row, col);

        // if position is already open
        if (isOpen(row, col)) {
            // do nothing
            return;
        } else {
            //StdOut.printf("Opening (%d, %d)\n", row, col);
            // mark the site as open
            int newSiteArrIdx = getArrayIndex(row, col);
            openFull[newSiteArrIdx] = SITE_STATE_OPEN;

            // check if any adjacent sites are open and union with them if so
            unionNeighbors(row, col);

            // if the newly opened site is a top node, promote the site from open to full
            if (row == 1) {
                openFull[newSiteArrIdx] = SITE_STATE_FULL;
            }
        }
    }

    // tries to union a neighboring site if site node exists
    private void tryUnionNeighbor(int newSiteRow, int newSiteCol, int neighborRow, int neighborCol) {
        try {
            if (isOpen(neighborRow, neighborCol)) {
                int newSiteIdx = getArrayIndex(newSiteRow, newSiteCol);
                int neighborIdx = getArrayIndex(neighborRow, neighborCol);
                //System.out.println("tryUnionNeighbor: newSiteIdx=" + newSiteIdx + ", neighborIdx=" + neighborIdx);
                map.union(newSiteIdx, neighborIdx);
            }
        } catch (IndexOutOfBoundsException e) {
            // do nothing
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
        validatePositionZeroBased(i-1, j-1);

        int pos = (i-1)*gridSize + (j-1);
        boolean result = openFull[pos] != SITE_STATE_CLOSED;
        return result;
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        // the upper-left site is indexed (1, 1) but it will be presented as (0, 0) in our map
        validatePositionZeroBased(i-1, j-1);

        int pos = (i-1)*gridSize + (j-1);
        boolean result = openFull[pos] == SITE_STATE_FULL;
        return result;
    }

    // checks that indices i and j are inside the map bounds
    private void validatePositionZeroBased(int i, int j) {
        if (i < 0 || i >= gridSize || j < 0 || j >= gridSize) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    // checks that indices i and j are inside the map bounds
    private void validatePositionOneBased(int i, int j) {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    private int getArrayIndex(int row, int col) {
        return (row-1)*gridSize + (col-1);
    }

    // does the system percolate?
    public boolean percolates() {
        //System.out.println("percolating system... ");
        // for each open site that is connected to a top node, promote the site from open to full (skip top row)
        for (int row = 2; row <= gridSize; row++) {
            for (int col = 1; col <= gridSize; col++) {
                int currentSiteIdx = getArrayIndex(row, col);
                //System.out.print("Checking if (" + row + ", " + col + ") is connected to a top-row full site... ");
                if (hasConnectionToOpenTopRowSite(row, col)) {
                    //System.out.println("yes.");
                    openFull[currentSiteIdx] = SITE_STATE_FULL;
                } else {
                    //System.out.println("no.");
                }
            }
        }

        // check if there is a component that has a connection between a site in the top and a site in the bottom
        for (int topSite = 0; topSite < gridSize; topSite++) {
            for (int j = 0; j < gridSize; j++) {
                int bottomSite = (gridSize-1)*gridSize + j;
                //System.out.println("checking if topSite " + topSite + " is connected with bottomSite " + bottomSite + "...");
                if (map.connected(topSite, bottomSite)) {
                    //System.out.println(" true");
                    return true;
                } else {
                    //System.out.println(" false");
                }
            }
        }

        return false; // there is no connection thus the system must not percolate
    }

    public double getThreshold() {
        // threshold is (# of open sites / # of total sites)
        int numOpenSites = 0;
        for (int i : openFull) {
            if (i != SITE_STATE_CLOSED) {
                numOpenSites++;
            }
        }
        int numTotalSites = openFull.length;

        return (double) numOpenSites / numTotalSites;
    }

    private boolean hasConnectionToOpenTopRowSite(int row, int col) {
        validatePositionOneBased(row, col);

        int newSiteIdx = getArrayIndex(row, col);
        // iterate through top row
        for (int currentCol = 1; currentCol <= gridSize; currentCol++) {
            // if current column's top-most site is full
            if (isFull(1, currentCol)) {
                // check if the target site is connected to this full site
                //System.out.println("site (1, " + currentCol + ") is full");
                int currentColArrayIndex = getArrayIndex(1, currentCol);
                //System.out.println("newSiteIdx " + newSiteIdx + " is connected to currentColArrayIndex " + currentColArrayIndex + "?");
                if (map.connected(newSiteIdx, currentColArrayIndex)) {
                    //System.out.println("yes");
                    return true;
                } else {
                    //System.out.println("no");
                }
            }

        }
        return false;
    }

    public static void main(String[] args) {
        // test client (optional)

        Test2x2();

        Test3x3();

        Test5x5();
    }

    private static void Test5x5() {
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

    private static void Test2x2() {
        Percolation p = new Percolation(2);
        assert !p.isOpen(1, 1);
        assert !p.isOpen(2, 1);
        p.printDebugGrid();
        p.open(1,1);
        p.open(2,1);
        boolean perc = p.percolates();
        p.printDebugGrid();
        assert perc;
    }

    private static void Test3x3() {
        Percolation p = new Percolation(3);

        p.printDebugGrid();
        assert p.isOpen(3, 3) == false;
        assert p.isFull(2, 2) == false;
        p.open(3, 2);
        p.open(2, 2);
        p.printDebugGrid();
        assert p.isFull(2, 2) == false;
        assert p.isOpen(3, 2) == true;
        assert p.isOpen(2, 2) == true;
        boolean perc = p.percolates();
        p.printDebugGrid();
        assert perc == false;

        p.open(1, 2);
        p.printDebugGrid();
        perc = p.percolates();
        p.printDebugGrid();
        assert p.isFull(1, 2);
        assert p.isFull(2, 2);
        assert p.isFull(3, 2);
        assert perc == true;
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
