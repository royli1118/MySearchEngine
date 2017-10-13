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

    // Term frequencies in one document
    private HashMap<String, Integer> termDocFreqs;
    // Term frequencies in all documents
    private HashMap<String, String> termFreqs;

    private ArrayList<String> textFiles;

    public IndexProcessor() {
        termDocFreqs = new HashMap<String, Integer>();
        termFreqs = new HashMap<String, String>();
    }


    /**
     * This method is for searching all files in selected folder
     *
     * @param folderPath
     * @return
     */
    public ArrayList<File> fileInFolder(String folderPath) {
        // read the txtfile
        File folder = new File(folderPath);

        ArrayList<File> files = new ArrayList<File>();

        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                files.add(file);
            }
        }
        return files;
    }

    /**
     * Read the File lines using Apache Common IO
     * @param file
     * @return
     */
    private ArrayList<String> readFile(File file) {
        ArrayList<String> fileLines = new ArrayList<String>();

        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(file, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (it.hasNext()) {
                String line = it.nextLine().trim();
                // faster reading line by Apache Commons IO,readlines with milliseconds
                // Doing the tokenization line by line
                fileLines.add(line);
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        return fileLines;
    }

    /**
     * The preparation of setting term in one doc frequencies and a term in all documents' frequencies
     * @param docTermFrequencies
     * @param alltextFiles
     */
    private void termFrequencyCalculation(ArrayList<HashMap<String, Integer>> docTermFrequencies,
                             ArrayList<File> alltextFiles) {
        termDocFreqs = new HashMap<String, Integer>();
        termFreqs = new HashMap<String, String>();
        TermFrequency tf = new TermFrequency();
        termFreqs = tf.addTermFrequency(termFreqs,termDocFreqs,docTermFrequencies,alltextFiles);

    }


    /**
     * The preparation of setting term in one doc frequencies and a term in all documents' frequencies
     * @param docTermFrequencies
     * @param index
     */
    private void termFrequencyByDocAndWriteToFile(HashMap<String, Integer> docTermFrequencies,int index) {
        TermFrequency tf = new TermFrequency();
        tf.TermFrequencyByDocAndWriteToFile(docTermFrequencies,index);

    }



    public void writeToIndexFile(int quantityOfDocuments, String index_dir, String indexFileName) {
        // now write inverted index out to file with appended IDF values at the end
        try {
            PrintWriter writer = new PrintWriter(index_dir + "/" + indexFileName);

            // iterate through all documents' tokens:
            for (Map.Entry<String, String> entry : termFreqs.entrySet()) {
                String term = entry.getKey();
                String docNameTermFreqs = entry.getValue();

                int docFrequency = termDocFreqs.get(term);
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
        ArrayList<File> allTextFiles = fileInFolder(collection_dir);

        ArrayList<HashMap<String, Integer>> docTermFrequency = new ArrayList<HashMap<String, Integer>>();
        int numbDocuments = 0;
//        Tokenizer tk = new Tokenizer();
//        StopwordsRemover swr = new StopwordsRemover();
//        if (!stopwords_file.equals(null)) {
//            tk.setStopwordList(swr.readStopwordFile(stopwords_file));
//        }

        Iterator<File> it = allTextFiles.iterator();
        while (it.hasNext()) {
            File textFile = it.next();
            ArrayList<String> fileLines = readFile(textFile);
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
            termFrequencyByDocAndWriteToFile(tokens,numbDocuments);
        }
        termFrequencyCalculation(docTermFrequency, allTextFiles);
        writeToIndexFile(numbDocuments, index_dir, indexFilename);
    }

}
