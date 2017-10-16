import java.util.*;
import java.io.*;

public class SearchModel {

    private InvertedIndex invIndex;

    public SearchModel(InvertedIndex invertedIn){
        invIndex = invertedIn;
    }

    public void searchByQuery(int numberOfFiles, ArrayList<String> queryList) {

        Tokenizer tokenizer = new Tokenizer();
        HashMap<String, Integer> queryTFs = tokenizer.tokenize(queryList);
        ArrayList<String> tokenizedQuery = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : queryTFs.entrySet()) {
            String token = entry.getKey();
            int termFreq = entry.getValue();
            tokenizedQuery.add(token);
        }

        if (queryTFs.size() > 0) {
            double queryVectorNorm = 0.0;
            int numberCorpusDocs = invIndex.invertedDocs.size();


            // Now calculate all the cosine similarities for all documents that contain at least one query term
            // We can build up the dot-product query term by query term and then divide through by vector norms
            HashMap<String, Double> queryDocumentDotProducts = new HashMap<String, Double>();
            for (String query : tokenizedQuery) {
                int queryTF = queryTFs.get(query);

                //Get all the documents and their term frequencies for this query term
                if (invIndex.invertedIndexTFs.containsKey(query)) {
                    HashMap<String, Integer> docTFs = invIndex.invertedIndexTFs.get(query);
                    double idf = invIndex.termsIDF.get(query);
                    double queryTfIdf = 1.0 * queryTF * idf;

                    //iterate through each document, make tf-idf, take dot product
                    //and add to previous value for document or initialise the dotProducts HashMap
                    for (Map.Entry<String, Integer> entry : docTFs.entrySet()) {

                        String docName = entry.getKey();
                        int termFreq = entry.getValue();

                        double tfIDF = termFreq * idf;

                        double termDotProduct = tfIDF * queryTfIdf;

                        if (queryDocumentDotProducts.containsKey(docName.toLowerCase())) {
                            queryDocumentDotProducts.put(docName, queryDocumentDotProducts.get(docName) + termDotProduct);
                        } else {
                            queryDocumentDotProducts.put(docName, termDotProduct);
                        }
                    }

                    //add to query vector norm
                    queryVectorNorm += queryTfIdf * queryTfIdf;

                } else {
                    queryVectorNorm += Math.log(numberCorpusDocs) * queryTF * Math.log(numberCorpusDocs) * queryTF;
                }
            }


            // Now build up the cosine similarity scores for each document
            // Use a PriorityQueue to automatically store documents in max order
            PriorityQueue<String> queryDocCosineSimSorted = new PriorityQueue<>(queryDocumentDotProducts.size(), new DoubleInStringComparator());
            for (Map.Entry<String, Double> entry : queryDocumentDotProducts.entrySet()) {
                String docName = entry.getKey();
                double dotProduct = entry.getValue();
                double cosineSimilarity = dotProduct / (Math.sqrt(invIndex.docVectorNormsSquared.get(docName)) * Math.sqrt(queryVectorNorm));

                String outputString = docName + "," + String.valueOf(cosineSimilarity);

                queryDocCosineSimSorted.add(outputString);
            }
            //Print out all the documents in order of cosine similarity
            for (int i = 0; i < queryDocumentDotProducts.size() && i < numberOfFiles; i++) {
                System.out.println(queryDocCosineSimSorted.poll());
            }
        } else {
            System.out.println("Your search produced no results.");
        }

    }
}