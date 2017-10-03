import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IndexProcessor {

    private HashMap<Integer, String> hm;

    public IndexProcessor() {
        hm = new HashMap<Integer, String>();
    }

    public HashMap<Integer, String> process(File files) {
        Tokenizer tk = new Tokenizer();
        LineIterator it = null;
        // First we need to read all files
        try {
            it = FileUtils.lineIterator(files, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (it.hasNext()) {
                String line = it.nextLine().trim();
                // faster reading line by Apache Commons IO,readlines with milliseconds
                // Doing the tokenization line by line
                if (!(line.equals(null) && line.equals(""))) {
                    hm = tk.pickSpecialWords(line, hm.size() + 1);
                    hm = tk.pickOtherWords(line, hm.size() + 1);
                }
            }
            return hm;
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    public void writeFiles(HashMap<Integer, String> hm, File outFile) {
        try {

            PrintWriter outputFile = new PrintWriter(outFile);
            Iterator it = hm.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                outputFile.write(pair.getKey() + " " + pair.getValue());
                outputFile.write("\r\n");
                it.remove(); // avoids a ConcurrentModificationException
            }
            outputFile.close();

        } catch (
                IOException e)

        {
            System.out.println(e.toString());
        }

    }

}
