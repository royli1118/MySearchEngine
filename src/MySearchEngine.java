import java.io.File;
import java.util.ArrayList;

public class MySearchEngine {

    private static String INDEX_FILENAME = "index.txt";
    private static String STOPWORDS_DIR = "stopwords";

    public static void main(String[] args) {
        // read the command line options
        if (args == null) {
            System.out.println("Not correct input!");
        } else if (args[0].equals("index")) {
            // Option 1: User use the index option
            if (!args[1].equals(null) && !args[2].equals(null) && !args[3].equals(null)) {

                String collection_dir = args[1];
                String index_dir = args[2];
                if (!index_dir.equals("")) {
                    String stopwords_file = null;
                    if (args.length == 4) {
                        stopwords_file = STOPWORDS_DIR + "/" + args[3];
                        File stopwordsFile = new File(stopwords_file);
                        if (stopwordsFile.exists()) {
                            // Process multiple files and tokenization
                            IndexProcessor ipf = new IndexProcessor();
                            ipf.index(collection_dir, index_dir, INDEX_FILENAME, stopwords_file);
                        } else
                            System.out.println("The stopwords.txt does not exist!");
                    }
                } else
                    System.out.println("Please input a right index_dir!");
            } else {
                System.out.println("Invalid number of arguments, need to provide collection_dir, index_dir and optionally a stopwords text file.");
            }
        }
        // Option 2: User use the search option
        else if (args[0].equals("search")) {
            if (!args[1].equals(null)) {
                // Do the Vector models and Calculation here
                String index_dir = args[1];
                File file_Directory = new File(index_dir);
                if (file_Directory.exists()) {
                    String num_doc = args[2];
                    if (!num_doc.equals("") && isNumber(num_doc) == true) {
                        ArrayList<String> keyWordsList = new ArrayList<>();
                        int numOfDoc = Integer.parseInt(num_doc);
                        for (int i = 3; i < args.length; i++) {
                            keyWordsList.add(args[i].trim());
                        }
                        InvertedIndex invIndex = new InvertedIndex();
                        invIndex.constructInvertedIndexFromFile(index_dir);
                        SearchModel sm = new SearchModel(invIndex);
                        sm.searchByQuery(numOfDoc, keyWordsList);
                    } else {
                        System.out.println("Please input a right number");
                    }

                } else {
                    System.out.println("Please input a right index directory");
                }
            } else {
                System.out.println("Please input a correct arguments");
            }
        } else {
            System.out.println("No options selected");
        }
    }

    /**
     * A method of ID and Age Validation
     * <p>
     * return boolean
     */
    private static boolean isNumber(String inputNumber) {
        if (inputNumber.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < inputNumber.length(); i++) {
            if (inputNumber.charAt(i) < '0' || inputNumber.charAt(i) > '9') {
                System.out.println("Please enter a valid number");
                return false;
            }
        }
        return true;
    }
}

