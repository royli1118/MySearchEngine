import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IndexProcessor {
    // Term frequencies in all documents
    // The format is [term],[FileName1,frequencies,FileName2,Frequencies]
    private HashMap<String, String> termFreqs;

    private ArrayList<String> textFiles;

    public IndexProcessor() {
        termFreqs = new HashMap<String, String>();
    }


    /**
     * The preparation of setting term in one doc frequencies and a term in all documents' frequencies
     *
     * @param docTermFrequencies
     * @param alltextFiles
     */
    private void termFrequencyCalculation(ArrayList<HashMap<String, Integer>> docTermFrequencies,
                                          ArrayList<File> alltextFiles) {
        termFreqs = new HashMap<String, String>();
        TermFrequency tf = new TermFrequency();
        termFreqs = tf.addTermFrequency(termFreqs, docTermFrequencies, alltextFiles);

    }

    /**
     * Calculate the IDF and write to index.txt file
     *
     * @param quantityOfDocuments
     * @param index_dir
     * @param indexFileName
     */
    public void calculateIDFAndWriteToIndexFile(int quantityOfDocuments, String index_dir, String indexFileName) {
        // now write inverted index out to file with appended IDF values at the end
        try {
            PrintWriter writer = new PrintWriter(index_dir + "/" + indexFileName);

            // iterate through all documents' tokens:
            for (Map.Entry<String, String> entry : termFreqs.entrySet()) {
                String term = entry.getKey();
                String docNameTermFreqs = entry.getValue();

                // The docFrequency is the term appears in number of Documents
                int docFrequency = (docNameTermFreqs.split(",").length) / 2;
                //calculate IDF
                double idf = Math.log(quantityOfDocuments / (docFrequency + 1));
                //round idf
                idf = (double) Math.round(idf * 1000) / 1000;

                //Combine a final String for every line
                String indexLineForOutput = term + docNameTermFreqs + "," + String.valueOf(idf);

                //Finalize to writing to a file
                writer.write(indexLineForOutput + "\r\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.print("Unexpected I/O exception\n");
        }
    }


    public void index(String collection_dir, String index_dir, String indexFilename, String stopwords_file) {

        // search all files in the folder
        FileIO f = new FileIO();
        ArrayList<File> allTextFiles = f.fileInFolder(collection_dir);

        ArrayList<HashMap<String, Integer>> docTermFrequency = new ArrayList<HashMap<String, Integer>>();
        int numbDocuments = 0;
        Iterator<File> it = allTextFiles.iterator();
        while (it.hasNext()) {
            File textFile = it.next();
            ArrayList<String> fileLines = f.readFile(textFile);
            HashMap<String, Integer> tokens = new HashMap<String, Integer>();
            if (fileLines.size() > 0) {
                numbDocuments = 1 + numbDocuments;
                Tokenizer tk = new Tokenizer();
                StopwordsRemover swr = new StopwordsRemover();
                if (!stopwords_file.equals(null)) {
                    tk.setStopwordList(swr.readStopwordFile(stopwords_file));
                }
                tokens = tk.tokenize(fileLines);
                docTermFrequency.add(tokens);
            }
        }

        // Construct the token frequencies in index.txt
        // index.txt includes
        // e.g.
        // [token],[fileName1,termFrequencies,fileName2,termFrequencies.....],[IDF}
        // cat,15
        termFrequencyCalculation(docTermFrequency, allTextFiles);
        calculateIDFAndWriteToIndexFile(numbDocuments, index_dir, indexFilename);
    }

}
