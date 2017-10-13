

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class StopwordsRemover {

    /**
     * Read the stopwords from File.
     *
     */
    public HashSet<String> readStopwordFile(String stopwordFilePath) {
        HashSet<String> stopwords = new HashSet<String>();
        File file = new File(stopwordFilePath);

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
                stopwords.add(line);
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        return stopwords;
    }
}
