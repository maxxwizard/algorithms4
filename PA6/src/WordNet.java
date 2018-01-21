import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;

public class WordNet {

    // vertices and edges
    Digraph graph;

    // we'll store a dictionary of vertex-to-synonyms
    Bag<String> synsets;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        throw new java.lang.UnsupportedOperationException();
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

    }
}