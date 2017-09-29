import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;

public class Printer {

    public void print(File files) {
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(files, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                // faster reading line by Apache Commons IO,readlines with milliseconds
                System.out.println(line);
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }
}
