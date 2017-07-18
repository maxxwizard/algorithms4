import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {

        int numOfItemsToPrint = Integer.parseInt(args[0]);

        String s;

        RandomizedQueue<String> rq = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            s = StdIn.readString();
            rq.enqueue(s);
        }

        // StdOut.println(rq.toString());

        // StdOut.println("printing " + numOfItemsToPrint);

        while (numOfItemsToPrint > 0) {
            StdOut.println(rq.dequeue());
            numOfItemsToPrint--;
        }

    }
}