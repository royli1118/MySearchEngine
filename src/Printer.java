import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Printer {

    HashMap<Integer,String> hm;

    public HashMap<Integer,String> print(File files) {
        hm = new HashMap<Integer, String>();
        Tokenizer tk = new Tokenizer();
        LineIterator it = null;
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
                if(!(line.equals(null)&&line.equals(""))){
                    hm = tk.pickSpecialWords(line);
                    hm = tk.pickOtherWords(line);
                }

            }
            return hm;
        } finally {
            LineIterator.closeQuietly(it);
        }
    }
}
