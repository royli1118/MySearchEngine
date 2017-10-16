import java.util.*;
import java.io.*;

/**
 * Write a description of class SearchModel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SearchModel {

    private InvertedIndex invIndex;
    private int numbSearchResults;


    public ArrayList<HashMap<String,Integer>> readIndexFiles(String index_dir){
        ArrayList<HashMap<String,Integer>> allTokens = new ArrayList();
        FileIO f = new FileIO();
        ArrayList<File> allIndexTextFiles = f.fileInFolder(index_dir);
        Iterator<File> it = allIndexTextFiles.iterator();
        while (it.hasNext()) {
            File textFile = it.next();
            if (!textFile.getName().equals("index.txt")){
                ArrayList<String> fileLines = f.readFile(textFile);
                HashMap<String, Integer> tokens = new HashMap<String, Integer>();
                if (fileLines.size() > 0) {
                    Iterator<String> itr = fileLines.iterator();
                    while (itr.hasNext()) {
                        String line = itr.next();
                        String token = line.split(",")[0];
                        int tokenFreq = Integer.parseInt(line.split(",")[1]);

                        tokens.put(token,tokenFreq);
                    }
                }
                allTokens.add(tokens);
            }
            else
                System.out.println("Skip the collection index file");
        }

        return allTokens;
    }

    public void searchByQuery(String index_dir,int numberOfFiles){
        ArrayList<HashMap<String,Integer>> allTokens = readIndexFiles(index_dir);

    }

    public void tokenQuery(String newKeywordList) {
        ArrayList<String> keywordsList = new ArrayList<String>();
        keywordsList.add(newKeywordList);
        Tokenizer tk = new Tokenizer();
        HashMap<String, Integer> queries = tk.tokenize(keywordsList);
        ArrayList<String> tokenedQuery = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : queries.entrySet()) {
            String token = entry.getKey();
            int termFrequency = entry.getValue();
            tokenedQuery.add(token);
        }
    }

    public void search(ArrayList<String> tokenedQuery, String num_docs, HashMap<String, Integer> queries) {

        double queryVectorNorm = 0.0;
        int numberDocuments = Integer.parseInt(num_docs);


        HashMap<String, Double> queryDocumentDotProducts = new HashMap<String, Double>();
        for (String query : tokenedQuery) {
            int queryTF = queries.get(query);
            if (!invIndex.invertedIndexTFs.containsKey(query)) {
                System.out.println("Sorry, the collections do not have the query.");
            } else {
                //get the weights of the tokens in the query
                HashMap<String, Integer> docTFs = invIndex.invertedIndexTFs.get(query);
                double idf = invIndex.termIDFs.get(query);
                double queryWeight = queryTF * idf * 1.0;

                //iterate through each document, make tf-idf, take dot product
                //and add to previous value for document or initialise the dotProducts HashMap
                for (Map.Entry<String, Integer> entry : docTFs.entrySet()) {
                    String docName = entry.getKey();
                    int termFrequency = entry.getValue();

                    double weight = termFrequency * idf;

                    double termDotProduct = weight * queryWeight;

                    if (queryDocumentDotProducts.containsKey(docName)) {
                        queryDocumentDotProducts.put(docName, queryDocumentDotProducts.get(docName) + termDotProduct);
                    } else {
                        queryDocumentDotProducts.put(docName, termDotProduct);
                    }
                }
                //add to query vector norm
                queryVectorNorm += queryWeight * queryWeight;
            }

            // Now build up the cosine similarity scores for each document
            // Use a PriorityQueue to automatically store documents in max order
            PriorityQueue<String> queryDocCosineSimSorted = new PriorityQueue<String>(queryDocumentDotProducts.size(), new DoubleInStringComparator());
            for (Map.Entry<String, Double> entry : queryDocumentDotProducts.entrySet()) {
                String docName = entry.getKey();
                double dotProduct = entry.getValue();
                double cosineSimilarity = dotProduct / (Math.sqrt(invIndex.docVectorNormsSquared.get(docName)) * Math.sqrt(queryVectorNorm));

                String outputString = docName + "," + String.format("%.3f", cosineSimilarity);

                queryDocCosineSimSorted.add(outputString);
            }
            //Print out all the documents in order of cosine similarity
            for (int i = 0; i < queryDocumentDotProducts.size() && i < numbSearchResults; i++) {
                System.out.println(queryDocCosineSimSorted.poll());
            }
        }
    }


//    public void searchByCosineSim(String[] queryTerms) {
//        //Tokenize the raw query
//        String queryAsLine = "";
//        for (String query : queryTerms) {
//            queryAsLine = queryAsLine + " " + query;
//        }
//        queryAsLine = queryAsLine.trim();
//        ArrayList<String> lineForTokenizer = new ArrayList<String>();
//        lineForTokenizer.add(queryAsLine);
//        Tokenizer tokenizer = new Tokenizer();
//        HashMap<String, Integer> queryTFs = tokenizer.tokenize(lineForTokenizer);
//        ArrayList<String> tokenizedQuery = new ArrayList<String>();
//        for (Map.Entry<String, Integer> entry : queryTFs.entrySet()) {
//            String token = entry.getKey();
//            int termFreq = entry.getValue();
//            tokenizedQuery.add(token);
//        }
//
//        if (queryTFs.size() > 0) {
//            double queryVectorNorm = 0.0;
//            int numberCorpusDocs = invIndex.corpusDocs.size();
//
//            // Now calculate all the cosine similarities for all documents that contain at least one query term
//            // We can build up the dot-product query term by query term and then divide through by vector norms
//            HashMap<String, Double> queryDocumentDotProducts = new HashMap<String, Double>();
//            for (String query : tokenizedQuery) {
//                int queryTF = queryTFs.get(query);
//
//                //Get all the documents and their term frequencies for this query term
//                if (invIndex.invertedIndexTFs.containsKey(query)) {
//                    HashMap<String, Integer> docTFs = invIndex.invertedIndexTFs.get(query);
//                    double idf = invIndex.termIDFs.get(query);
//                    double queryTfIdf = 1.0 * queryTF * idf;
//
//                    //iterate through each document, make tf-idf, take dot product
//                    //and add to previous value for document or initialise the dotProducts HashMap
//                    for (Map.Entry<String, Integer> entry : docTFs.entrySet()) {
//
//                        String docName = entry.getKey();
//                        int termFreq = entry.getValue();
//
//                        double tfIDF = termFreq * idf;
//
//                        double termDotProduct = tfIDF * queryTfIdf;
//
//                        if (queryDocumentDotProducts.containsKey(docName)) {
//                            queryDocumentDotProducts.put(docName, queryDocumentDotProducts.get(docName) + termDotProduct);
//                        } else {
//                            queryDocumentDotProducts.put(docName, termDotProduct);
//                        }
//                    }
//
//                    //add to query vector norm
//                    queryVectorNorm += queryTfIdf * queryTfIdf;
//
//                } else {
//                    queryVectorNorm += Math.log(numberCorpusDocs) * queryTF * Math.log(numberCorpusDocs) * queryTF;
//                }
//            }
//
//
//            // Now build up the cosine similarity scores for each document
//            // Use a PriorityQueue to automatically store documents in max order
//            PriorityQueue<String> queryDocCosineSimSorted = new PriorityQueue<String>(queryDocumentDotProducts.size(), new DoubleInStringComparator());
//            for (Map.Entry<String, Double> entry : queryDocumentDotProducts.entrySet()) {
//                String docName = entry.getKey();
//                double dotProduct = entry.getValue();
//                double cosineSimilarity = dotProduct / (Math.sqrt(invIndex.docVectorNormsSquared.get(docName)) * Math.sqrt(queryVectorNorm));
//
//                String outputString = docName + "," + String.format("%.3f", cosineSimilarity);
//
//                queryDocCosineSimSorted.add(outputString);
//            }
//            //Print out all the documents in order of cosine similarity
//            for (int i = 0; i < queryDocumentDotProducts.size() && i < numbSearchResults; i++) {
//                System.out.println(queryDocCosineSimSorted.poll());
//            }
//        } else {
//            System.out.println("Your search produced no results.");
//        }
//
//    }
}