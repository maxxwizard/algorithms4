import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by matth on 9/20/2016.
 */

public class PercolationStats {
    double[] thresholds;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        // validate params
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            // now spawn a random site until the system percolates
            while (!p.percolates()) {
                int randomRow = StdRandom.uniform(1, n+1);
                int randomCol = StdRandom.uniform(1, n+1);
                p.open(randomRow, randomCol);
            }
            // system must be percolating now so store its threshold
            thresholds[i] = p.getThreshold();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return 0;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return 0;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return 0;
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
                System.exit(1);
            }

            // run our Monte Carlo simulation
            PercolationStats ps = new PercolationStats(gridSize, trials);
            double mean = ps.mean();
            System.out.printf("%-23s = %f", "mean", mean);
        } else {
            System.err.println("Usage: java PercolationStats <n> <trials>");
        }
    }
}
