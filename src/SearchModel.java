import java.util.*;
import java.io.*;

/**
 * Write a description of class SearchModel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SearchModel
{
    
    private InvertedIndex invIndex;
  	private int numbSearchResults;
  	
    public SearchModel(InvertedIndex invertedIn, int searchResults)
    {
        invIndex = invertedIn;
        numbSearchResults = searchResults;
    }

    public void tokenQuery(String newKeywordList)
    {
        ArrayList<String> keywordsList = new ArrayList<String>();
        keywordsList.add(newKeywordList);
        Tokenizer tk = new Tokenizer();
        HashMap<String, Integer> queries = tk.tokenize(keywordsList);
        ArrayList<String> tokenedQuery = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : queries.entrySet())
        {
            String token = entry.getKey();
            int termFrequency = entry.getValue();
            tokenedQuery.add(token);
        }
    }

    public void search(ArrayList<String> tokenedQuery,String num_docs,HashMap<String, Integer> queries)
    {       
            
            double queryVectorNorm = 0.0;
            int numberDocuments = Integer.parseInt(num_docs);
            
            
            HashMap<String, Double> queryDocumentDotProducts = new HashMap<String, Double>();
            for(String query : tokenedQuery)
            {
                int queryTF = queries.get(query);
                if (!invIndex.invertedIndexTFs.containsKey(query))
                {   
                    System.out.println("Sorry, the collections do not have the query.");
                }
                else
                {   
                    //get the weights of the tokens in the query
                    HashMap<String, Integer> docTFs = invIndex.invertedIndexTFs.get(query);
                    double idf = invIndex.termIDFs.get(query);
                    double queryWeight = queryTF*idf*1.0;

                    //iterate through each document, make tf-idf, take dot product 
                    //and add to previous value for document or initialise the dotProducts HashMap
                    for (Map.Entry<String, Integer> entry : docTFs.entrySet())
                    {
                        String docName = entry.getKey();
                        int termFrequency = entry.getValue();

                        double weight = termFrequency*idf;

                        double termDotProduct = weight * queryWeight;

                        if (queryDocumentDotProducts.containsKey(docName))
                        {
                            queryDocumentDotProducts.put(docName, queryDocumentDotProducts.get(docName) + termDotProduct);
                        } else{
                            queryDocumentDotProducts.put(docName, termDotProduct);
                        }
                    }
                    //add to query vector norm
                    queryVectorNorm += queryWeight*queryWeight;
                }
                
                // Now build up the cosine similarity scores for each document
                // Use a PriorityQueue to automatically store documents in max order
                PriorityQueue<String> queryDocCosineSimSorted = new PriorityQueue<String>(queryDocumentDotProducts.size(), new DoubleInStringComparator());
                for (Map.Entry<String, Double> entry : queryDocumentDotProducts.entrySet())
                {
                    String docName = entry.getKey();
                    double dotProduct = entry.getValue();	
                    double cosineSimilarity = dotProduct / (Math.sqrt(invIndex.docVectorNormsSquared.get(docName))*Math.sqrt(queryVectorNorm));

                    String outputString = docName+","+String.format("%.3f",cosineSimilarity);

                    queryDocCosineSimSorted.add(outputString);
                }
                //Print out all the documents in order of cosine similarity
                for (int i = 0; i < queryDocumentDotProducts.size() && i < numbSearchResults; i++)
                {
                    System.out.println(queryDocCosineSimSorted.poll());
                }
            } 
        }  
}