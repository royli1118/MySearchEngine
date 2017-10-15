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
                String stopwords_file = null;
                if (args.length == 4) {
                    stopwords_file = STOPWORDS_DIR+"/"+ args[3];
                }
                // Process multiple files and tokenization
                IndexProcessor ipf = new IndexProcessor();

                ipf.index(collection_dir, index_dir, INDEX_FILENAME, stopwords_file);

            } else {
                System.out.println("Invalid number of arguments, need to provide collection_dir, index_dir and optionally a stopwords text file.");
            }
        }

        // Option 2: User use the search option
        else if (args[0].equals("search")) {
            if (!args[1].equals(null)) {
                // Do the Vector models and Calculation here
                String index_dir = args[1];
                String num_doc = args[2];
                String[] query = new String[args.length - 3];
                int numOfDoc = Integer.parseInt(num_doc);
                for (int i = 3;i<args.length;i++){
                    query[i - 3] = args[i].trim();
                }
                Searcher sc = new Searcher();
                sc.searchByQuery(index_dir);

            } else
                System.out.println("Please input a correct arguments");
        } else {
            System.out.println("No options selected");
        }

    }
}

