import java.util.*;
import java.io.*;

/**
 *Class InvertedIndex
 *This is the InvertedIndex class. 
 * @author Yushan Wang 
 * @version 8.1 (16/10/2017)
 */
public class InvertedIndex {
    // term -> {doc1 -> term frequency ,doc2 -> term frequency , ...}
    public HashMap<String, HashMap<String, Integer>> invertedIndexTFs;
    // term -> inverse-document-frequency value
    public HashMap<String, Double> termsIDF;
    // docName -> vectorNorm value (pre-computed to use in cosine similarity calculations)
    public HashMap<String, Double> docVectorNormsSquared;
    // set of all documents
    public HashSet<String> invertedDocs;

    private static String indexFileName = "index.txt";
    
    /**
     * create a InvertedIndex
     */
    public InvertedIndex() 
    {
        invertedIndexTFs = new HashMap<String, HashMap<String, Integer>>();
        termsIDF = new HashMap<String, Double>();
        docVectorNormsSquared = new HashMap<String, Double>();
        invertedDocs = new HashSet<String>();
    }

    /**
     * pick up some parts of each line of the index.txt and do the calculations
     * @param index_dir
     */
    public void constructInvertedIndexFromFile(String index_dir) 
    {
        FileIO f = new FileIO();
        ArrayList<File> allIndexFiles = f.fileInFolder(index_dir);
        Iterator<File> it = allIndexFiles.iterator();
        while (it.hasNext()) {
            File textFile = it.next();
            ArrayList<String> fileLines = f.readFile(textFile);
            Iterator<String> fileLine = fileLines.iterator();
            while (fileLine.hasNext()) {
                String line = fileLine.next(); 
                String[] parts = line.trim().split(",");  // Split the line by space into a String array
                if (parts.length > 0) {
                    String token = parts[0];
                    // Take the last part(the IDF of the term)
                    String idfStr = parts[parts.length - 1];
                    double idf = Double.parseDouble(idfStr);
                    termsIDF.put(token, idf);

                    // get all the doc,term-frequency pairs
                    // the for loop variable increments by 2 each iteration
                    HashMap<String, Integer> docTFs = new HashMap<String, Integer>();
                    for (int i = 1; i < parts.length - 1; i += 2) {
                        String docName = parts[i];
                        int termFreq = Integer.parseInt(parts[i + 1]);
                        docTFs.put(docName, termFreq);

                        //build up non-duplicate set of corpus doc names
                        invertedDocs.add(docName);

                        //add to document vector norm calculation
                        double weightSquared = (termFreq * idf) * (termFreq * idf);
                        if (docVectorNormsSquared.containsKey(parts[i])) {
                            docVectorNormsSquared.put(parts[i], docVectorNormsSquared.get(parts[i]) + weightSquared);
                        } else {
                            docVectorNormsSquared.put(parts[i], weightSquared);
                        }
                    }
                    invertedIndexTFs.put(token, docTFs);
                }
            }
        }
    }
}