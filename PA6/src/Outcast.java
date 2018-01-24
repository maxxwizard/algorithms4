import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        // calculate the distance between the nouns and return the one with highest total distance to everyone else
        int[] distances = new int[nouns.length];

        for (int i = 0; i < nouns.length; i++) {
            distances[i] = 0;
            for (String s : nouns) {
                distances[i] += wordnet.distance(nouns[i], s);
            }
        }

        int highestDistance = -1;
        int highestDistanceIdx = -1;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > highestDistance) {
                highestDistance = distances[i];
                highestDistanceIdx = i;
            }
        }

        return nouns[highestDistanceIdx];
    }

    // unit testing
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }

    }
}
