import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class StopwordsRemover {

    /**
     *
     * @param stopwordFilePath
     * @return
     */
    public HashSet<String> readStopwordFile(String stopwordFilePath) {
        HashSet<String> stopwords = new HashSet<String>();
        File file = new File(stopwordFilePath);

        BufferedReader br = null;
        FileReader fr = null;

        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                stopwords.add(sCurrentLine.trim());
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        return stopwords;
    }
}
