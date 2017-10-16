import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Write a description of class TermFrequency here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TermFrequency {
    private ArrayList<HashMap<String, Integer>> termFrequency;

    public TermFrequency() {
        termFrequency = new ArrayList<HashMap<String, Integer>>();
    }

    public HashMap<String, String> addTermFrequency(HashMap<String, String> termFreqs, ArrayList<HashMap<String, Integer>> aTermFrequency, ArrayList<File> alltextFiles) {
        int index = 0;

        Iterator<HashMap<String, Integer>> it = aTermFrequency.iterator();
        while (it.hasNext()) {

            HashMap<String, Integer> termFreqsForDoc = it.next();

            // Get the filename of this document
            String fileName = alltextFiles.get(index).getName();

            // List all tokens from this document
            for (Map.Entry<String, Integer> entry : termFreqsForDoc.entrySet()) {
                String term = entry.getKey();
                int termFreq = entry.getValue();

                // Add to term frequencies list hashmap
                // And contstruct a index.txt format
                // e.g.
                // [token],[fileName1,frequencies,filename2,frequencies....],[IDF]
                // cat,d1,1,d2,3,d3,4,0.99997
                // but IDF calculation is in writeToIndexFile function in indexProcessor.java
                if (termFreqs.containsKey(term)) {
                    termFreqs.put(term, termFreqs.get(term) + "," + fileName + "," + Integer.toString(termFreq));
                } else {
                    termFreqs.put(term, "," + fileName + "," + Integer.toString(termFreq));
                }
            }
            index += 1;
        }
        return termFreqs;
    }

    public ArrayList<HashMap<String, Integer>> getTermFrequency() {

        return termFrequency;
    }
}
