import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *Class IndexProcessor
 *This is the IndexProcessor class. This class processes the final index file.
 * @author Yushan Wang 
 * @version 8.1 (16/10/2017)
 */
public class IndexProcessor {
    // Term frequencies in each document which contains this term.
    // The format is [term],[FileName1,frequencies,FileName2,Frequencies].eg. (cat, (doc1,4,doc2,8.....))
    private HashMap<String, String> termFreqs;

    private ArrayList<String> textFiles;

    public IndexProcessor() 
    {
        termFreqs = new HashMap<String, String>();
    }

    /**
     * add termFrequency to a double String structure list hashmap,eg. (cat, (doc1,4,doc2,8.....))
     * @param docTermFrequencies
     * @param alltextFiles
     */
    private void termFrequencyCalculation(ArrayList<HashMap<String, Integer>> docTermFrequencies, ArrayList<File> alltextFiles) 
    {
        termFreqs = new HashMap<String, String>();
        TermFrequency tf = new TermFrequency();
        termFreqs = tf.addTermFrequency(termFreqs, docTermFrequencies, alltextFiles);

    }

    /**
     * Calculate the IDF and write to index.txt file
     * @param quantityOfDocuments
     * @param index_dir
     * @param indexFileName
     */
    public void calculateIDFAndWriteToIndexFile(int quantityOfDocuments, String index_dir, String indexFileName) {
        // now write inverted index out to file with appended IDF values at the end
        try {
            File indexDIR = new File(index_dir);

            // if the directory does not exist, create it
            if (!indexDIR.exists()) {
                System.out.println("creating directory: " + indexDIR.getName());
                boolean result = false;

                try {
                    indexDIR.mkdir();
                    result = true;
                } catch (SecurityException se) {
                    //handle it
                }
                if (result) {
                    System.out.println("DIR created");
                }
            }

            File indexTXT = new File(index_dir + File.separator + indexFileName);
            if (!indexTXT.exists()){
                indexTXT.createNewFile();
            }
            PrintWriter writer = new PrintWriter(index_dir + File.separator + indexFileName);

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
            e.printStackTrace();
            System.out.print("Unexpected I/O exception\n");
        }
    }

    /**
     * index function
     * @param collection_dir
     * @param index_dir
     * @param indexFilename
     * @param stopwords_file
     */
    public void index(String collection_dir, String index_dir, String indexFilename, String stopwords_file) 
    {

        // search all files in the folder
        FileIO f = new FileIO();
        ArrayList<File> allTextFiles = f.fileInFolder(collection_dir);

        ArrayList<HashMap<String, Integer>> docTermFrequencies = new ArrayList<HashMap<String, Integer>>();
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
                docTermFrequencies.add(tokens);
            }
        }

        // Construct the token frequencies in index.txt
        // index.txt includes
        // eg.[token],[fileName1,termFrequencies,fileName2,termFrequencies.....],[IDF}
        termFrequencyCalculation(docTermFrequencies, allTextFiles);
        calculateIDFAndWriteToIndexFile(numbDocuments, index_dir, indexFilename);
    }

}
