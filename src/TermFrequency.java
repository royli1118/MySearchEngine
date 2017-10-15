import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

    public TermFrequency(ArrayList<HashMap<String, Integer>> termFrequency) {
        this.termFrequency = termFrequency;
    }

    public HashMap<String, String> addTermFrequency(HashMap<String, String> termFreqs, HashMap<String, Integer> termDocFreqs, ArrayList<HashMap<String, Integer>> aTermFrequency, ArrayList<File> alltextFiles) {
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

                // add to hashmap of term document frequencies
                if (termDocFreqs.containsKey(term)) {
                    termDocFreqs.put(term, termDocFreqs.get(term) + 1);
                } else {
                    termDocFreqs.put(term, 1);
                }

                //add to term frequencies list hashmap
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


    public void TermFrequencyByDocAndWriteToFile(HashMap<String, Integer> termDocFreqs,int index) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("index/index" + "_" + index + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // List all tokens from this document
        for (Map.Entry<String, Integer> entry : termDocFreqs.entrySet()) {
            String term = entry.getKey();
            int termFreq = entry.getValue();
            String indexLineForOutput = term + "," + String.valueOf(termFreq);

            //Finalize to writing to a file
            writer.write(indexLineForOutput + "\r\n");
        }
        writer.close();
    }


    public ArrayList<HashMap<String, Integer>> getTermFrequency() {

        return termFrequency;
    }
}
