import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet; 
import java.util.Set;  

public class MySearchEngine 
{

    // Logging system, Start from the program executing

    public static void main(String[] args)
    {

        // read the command line options
        CommandLine cmd = ParseCommandLineOptions(args);
        if(cmd == null){
            System.out.println("Not correct input!");
        }
        File folder = new File("collections");
        File[] listOfFiles = folder.listFiles();
        Printer pf = new Printer();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) 
            {
                pf.print(file);
            }
        }
    }
    


    /**
     * using options, parse the arguments into the command line
     * @param args the input arguments when executing the MySearchEngine.class
     * @return the command line
     */
    private static CommandLine ParseCommandLineOptions(String[] args)
    {

        // create a new options
        Options options = new Options();

        //The server must work exactly with the following command line options
        options.addOption("index", true, "debug modes on/off");
        options.addOption("search", true, "debug modes on/off");

        // parse the command line options
        CommandLineParser parser = new DefaultParser();
        try 
        {
            CommandLine cmd = parser.parse(options, args);
            return cmd;
        } 
        catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "ant", options );
        }

        // if there is a parseException, return null
        return null;
    }
}
