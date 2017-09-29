
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Processor {

    private HashMap<Integer, String> hm;
    private Integer index;

    public Processor() {
        hm = new HashMap<Integer, String>();
        index = 0;
    }

    public HashMap<Integer, String> process(File files) {
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
}
