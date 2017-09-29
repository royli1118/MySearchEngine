
import org.apache.commons.cli.*;

import java.io.File;

public class MySearchEngine {

    private static String COLLECTION_DIR = "collections";
    private static String INDEX_DIR = "index";
    private static String OUTPUTFILE = "index.txt";


    public static void main(String[] args) {

        // read the command line options
        CommandLine cmd = ParseCommandLineOptions(args);
        if (cmd == null) {
            System.out.println("Not correct input!");
        } else {

            // Option 1: User use the index option
            if (cmd.hasOption("index")) {
                if (!cmd.getOptionValues("index").equals(null)) {
                    if (!cmd.getOptionValues("index")[0].equals(null)) {
                        File folder = new File(cmd.getOptionValues("index")[0]);
                        File outputfile = new File(INDEX_DIR + "/" + OUTPUTFILE);
                        File[] listOfFiles = folder.listFiles();

                        // Process multiple files and tokenization
                        IndexProcessor ipf = new IndexProcessor();

                        for (int i = 0; i < listOfFiles.length; i++) {
                            File file = listOfFiles[i];
                            if (file.isFile() && file.getName().endsWith(".txt")) {
                                ipf.process(file);
                            }
                        }
                    } else {
                        System.out.println("Please input a correct arguments");
                    }
                }
            }

            // Option 2: User use the search option
            if (cmd.hasOption("search")) {
                if (!cmd.getOptionValues("search").equals("")) {
                    // To do something here
                } else
                    System.out.println("Please input a correct arguments");
            }

        }
    }
    

    /**
     * using options, parse the arguments into the command line
     *
     * @param args the input arguments when executing the MySearchEngine.class
     * @return the command line
     */

    private static CommandLine ParseCommandLineOptions(String[] args) {

        // create a new options
        Options options = new Options();

        //The server must work exactly with the following command line options
        options.addOption("index", true, "index all the documents stored in collection_dir");
        options.addOption("search", true, "return a ranked list of the top num_docs documents that match the query\n" +
                "specified in keyword_list.");

        // parse the command line options
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ant", options);
        }

        // if there is a parseException, return null
        return null;
    }
}
