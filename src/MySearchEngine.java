import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MySearchEngine {

    private static String COLLECTION_DIR = "collections";
    private static String INDEX_DIR = "index";
    private static String OUTPUTFILE = "index.txt";
    private static HashMap<Integer,String> hm;
    private static ArrayList<String> arrayList;


    public static void main(String[] args) {


        // read the command line options
        if (args == null) {
            System.out.println("Not correct input!");
        } else {
            // Option 1: User use the index option
            if (args[0].equals("index")) {
                if (!args[1].equals(null) && !args[2].equals(null)&&!args[3].equals(null)) {

                    File folder = new File(args[1]);
                    File outputfile = new File(INDEX_DIR + "/" + args[2]+".txt");
                    if(!outputfile.exists()){
                        try {
                            outputfile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    File[] listOfFiles = folder.listFiles();

                    // Process multiple files and tokenization
                    IndexProcessor2 ipf = new IndexProcessor2();

                    for (int i = 0; i < listOfFiles.length; i++) {
                        File file = listOfFiles[i];
                        if (file.isFile() && file.getName().endsWith(".txt")) {
                            arrayList = ipf.process(file);
                        }
                    }

                    ipf.writeFiles(arrayList,outputfile);

                    // Remove the Stopwords
                    //StopwordsRemover sws = new StopwordsRemover();
                    //sws.removeStopwords(args[3],INDEX_DIR + "/" + OUTPUTFILE);
                }
            }

            // Option 2: User use the search option
            else if (args[0].equals("search")) {
                if (!args[1].equals(null) && !args[2].equals(null)) {
                    // Do the Vector models and Calculation here

                } else
                    System.out.println("Please input a correct arguments");
            } else {
                System.out.println("No options selected");
            }

        }
    }

}
