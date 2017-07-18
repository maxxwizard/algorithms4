import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by matth on 9/20/2016.
 */

public class PercolationStats {
    private static final double Z_VALUE = 1.96;

    private double[] thresholds;
    private int trials;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        // validate params
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        // store values
        this.trials = trials;

        thresholds = new double[trials];
        int randomRow, randomCol;

        for (int i = 0; i < trials; i++) {
            // System.out.printf("Trial %d...\n", i);

            int numOpenSites = 0;

            Percolation p = new Percolation(n);
            // now spawn a random site until the system percolates
            while (!p.percolates()) {
                randomRow = StdRandom.uniform(1, n+1);
                randomCol = StdRandom.uniform(1, n+1);
                if (p.isOpen(randomRow, randomCol)) {
                    // do nothing
                } else {
                    p.open(randomRow, randomCol);
                    numOpenSites++;
                }
            }
            // system must be percolating now so store its threshold (# of open sites / # of total sites)
            int numTotalSites = n*n;
            double threshold = (double) numOpenSites / numTotalSites;
            thresholds[i] = threshold;
            // System.out.printf("Threshold: %f\n", threshold);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - stdError();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + stdError();
    }

    private double stdError() {
        double stdErr = stddev() / Math.sqrt(trials);
        // z-value * stdErr = margin of error
        return Z_VALUE * stdErr;
    }

    // test client (described below)
    public static void main(String[] args) {

        int gridSize = 0, trials = 0;
        if (args.length == 2) {
            try {
                gridSize = Integer.parseInt(args[0]);
                trials = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
            }

            // run our Monte Carlo simulation
            PercolationStats ps = new PercolationStats(gridSize, trials);
            double mean = ps.mean();
            double stdDev = ps.stddev();
            double confIntLow = ps.confidenceLo();
            double confIntHigh = ps.confidenceHi();
            System.out.printf("%-23s = %f\n", "mean", mean);
            System.out.printf("%-23s = %f\n", "stddev", stdDev);
            System.out.printf("%-23s = %f, %f\n", "95% confidence interval", confIntLow, confIntHigh);
        } else {
            System.err.println("Usage: java PercolationStats <n> <trials>");
        }
    }
}
