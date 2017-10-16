import java.util.*;
import java.io.*;

/**
 * Write a description of class InvertedIndex here.
 *
 * @author (your name)
 * @version (a version number or a date)
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

    public InvertedIndex() {
        invertedIndexTFs = new HashMap<String, HashMap<String, Integer>>();
        termsIDF = new HashMap<String, Double>();
        docVectorNormsSquared = new HashMap<String, Double>();
        invertedDocs = new HashSet<String>();
    }


    public void constructInvertedIndexFromFile(String index_dir) {
        FileIO f = new FileIO();
        ArrayList<File> allIndexFiles = f.fileInFolder(index_dir);
        Iterator<File> it = allIndexFiles.iterator();
        while (it.hasNext()) {
            File textFile = it.next();
            ArrayList<String> fileLines = f.readFile(textFile);
            Iterator<String> fileLine = fileLines.iterator();
            while (fileLine.hasNext()) {
                String line = fileLine.next();    // Read one line of the text file into a string
                String[] parts = line.trim().split(",");  // Split the line by space into a String array
                if (parts.length > 0) {
                    String token = parts[0];
                    // Take last part off, its the IDF
                    String idfStr = parts[parts.length - 1];
                    double idf = Double.parseDouble(idfStr);
                    termsIDF.put(token, idf);

                    // now get all the doc,term-frequency pairs
                    // NOTE!! the for loop variable increments by 2 each iteration
                    HashMap<String, Integer> docTFs = new HashMap<String, Integer>();
                    for (int i = 1; i < parts.length - 1; i += 2) {
                        String docName = parts[i];
                        int termFreq = Integer.parseInt(parts[i + 1]);
                        docTFs.put(docName, termFreq);

                        //build up non-duplicate set of corpus doc names
                        invertedDocs.add(docName);

                        //add to document vector norm calculation
                        double tfIDFSquared = (termFreq * idf) * (termFreq * idf);
                        if (docVectorNormsSquared.containsKey(parts[i])) {
                            docVectorNormsSquared.put(parts[i], docVectorNormsSquared.get(parts[i]) + tfIDFSquared);
                        } else {
                            docVectorNormsSquared.put(parts[i], tfIDFSquared);
                        }
                    }
                    invertedIndexTFs.put(token, docTFs);
                }
            }
        }
    }
}