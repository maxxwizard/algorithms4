import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    // graph so we can calculate SAP and whatnot
    private final Digraph graph;

    // we'll store a dictionary of synonym-to-integer (multivalued to single)
    private final ST<String, Integer> map;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException();
        }

        // we need a symbol table to map strings to integers
        map = new ST<>();

        int countVertices = 0;
        In in_synsets = new In(synsets);
        while (in_synsets.hasNextLine()) {
            String line = in_synsets.readLine();
            countVertices++;
            String[] tokens = line.split(",");
            int synset = Integer.parseInt(tokens[0]);
            StdOut.println(String.format("synset %d", synset));
            for (String syn : tokens[1].split(" ")) {
                StdOut.println(String.format(" syn %s", syn));
                map.put(syn, synset);
            }
        }

        graph = new Digraph(countVertices);
        In in_hypernyms = new In(hypernyms);
        while (in_hypernyms.hasNextLine()) {
            String line = in_hypernyms.readLine();
            String[] tokens = line.split(",");
            int synset = Integer.parseInt(tokens[0]);
            StdOut.println(String.format("synset %d", synset));
            for (int i = 1; i < tokens.length; i++) {
                int hypernym = Integer.parseInt(tokens[i]);
                StdOut.println(String.format(" hypernym %d", hypernym));
                graph.addEdge(synset, hypernym);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        throw new java.lang.UnsupportedOperationException();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        throw new java.lang.UnsupportedOperationException();
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        throw new java.lang.UnsupportedOperationException();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        throw new java.lang.UnsupportedOperationException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
    }
}