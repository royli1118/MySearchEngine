import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *Class TermFrequency
 *This is the TermFrequency class. This class calculates the term frequency in each ducument and put the terms in hashmap.
 * @author Yushan Wang 
 * @version 8.1 (16/10/2017)
 */
public class TermFrequency 
{
    //ArrayList is for the hashmap list of each term frequency in each document, eg. (doc3,4),(doc9,8),......
    private ArrayList<HashMap<String, Integer>> termFrequency;

    public TermFrequency() 
    {
        termFrequency = new ArrayList<HashMap<String, Integer>>();
    }
    
    /**
     * add termFrequency to a double String structure list hashmap,eg. (cat, (doc1,4,doc2,8.....))
     * @param termFreqs,aTermFrequency,alltextFiles
     * return termFreqs
     */
    public HashMap<String, String> addTermFrequency(HashMap<String, String> termFreqs, ArrayList<HashMap<String, Integer>> aTermFrequency, ArrayList<File> alltextFiles) 
    {
        int index = 0;

        Iterator<HashMap<String, Integer>> it = aTermFrequency.iterator();
        while (it.hasNext()) {
            //the frequency of each term in one document,eg. (cat,29) in doc1
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
        //a double String structure hashmap,eg. (cat, (doc1,4,doc2,8.....))
        return termFreqs;
    }

    /**
     * get termFrequency
     * return termFrequency
     */
    public ArrayList<HashMap<String, Integer>> getTermFrequency() {

        return termFrequency;
    }
}
