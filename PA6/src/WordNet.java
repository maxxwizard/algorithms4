import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {

    // graph so we can calculate SAP and whatnot
    //private final Digraph graph;

    private final SAP sap;

    // we'll store a dictionary of synonym-to-integer (multivalued to single)
    private final ST<String, Integer> map;
    private final ST<Integer, String> intToString_map;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException();
        }

        // we need a symbol table to map strings to integers
        map = new ST<>();
        intToString_map = new ST<>();

        int countVertices = 0;
        In in_synsets = new In(synsets);
        while (in_synsets.hasNextLine()) {
            String line = in_synsets.readLine();
            countVertices++;
            String[] tokens = line.split(",");
            int synset = Integer.parseInt(tokens[0]);
            intToString_map.put(synset, tokens[1]);
            // StdOut.println(String.format("synset %d", synset));
            for (String syn : tokens[1].split(" ")) {
                // StdOut.println(String.format(" syn %s", syn));
                map.put(syn, synset);
            }
        }

        Digraph graph = new Digraph(countVertices);
        In in_hypernyms = new In(hypernyms);
        while (in_hypernyms.hasNextLine()) {
            String line = in_hypernyms.readLine();
            String[] tokens = line.split(",");
            int synset = Integer.parseInt(tokens[0]);
            // StdOut.println(String.format("synset %d", synset));
            for (int i = 1; i < tokens.length; i++) {
                int hypernym = Integer.parseInt(tokens[i]);
                // StdOut.println(String.format(" hypernym %d", hypernym));
                graph.addEdge(synset, hypernym);
            }
        }

        sap = new SAP(graph);

        StdOut.println(String.format("%d vertices with %d edges", graph.V(), graph.E()));
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return map.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        int dist = sap.length(map.get(nounA), map.get(nounB));

        StdOut.println(String.format("dist(%s, %s) = %d", nounA, nounB, dist));

        return dist;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        // find a common ancestor synset using SAP
        int commonAncestor = sap.ancestor(map.get(nounA), map.get(nounB));

        StdOut.println(String.format("sap(%s, %s) = %d", nounA, nounB, commonAncestor));

        if (commonAncestor != -1) {
            // map the synset from integer back to string
            return intToString_map.get(commonAncestor);
        }

        return null; // we found no common ancestor
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        assert(wordnet.isNoun("hematohiston"));
        assert(wordnet.isNoun("protein_molecule"));
        assert(!wordnet.isNoun("xyz123"));

        assert(wordnet.distance("Christmas_factor", "Hageman_factor") == 2);
        assert(wordnet.sap("Christmas_factor", "Hageman_factor").equals("coagulation_factor clotting_factor"));

        // assert(wordnet.distance("worm", "bird") == 5);

        // assert(wordnet.sap("worm", "bird").equals("animal"));
    }
}