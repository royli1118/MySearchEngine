

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

    public void process(File files) {
        Tokenizer tk = new Tokenizer();
        StopwordsRemover swr = new StopwordsRemover();
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
                    int indexb4 = hm.size();
                    hm = tk.pickSpecialWords(line, hm.size() + 1);
                    int indexafter = hm.size();
                    if (indexb4 - indexafter == 0) {
                        hm = tk.pickOtherWords(line, hm.size() + 1);
                    }
                    else if(indexb4 - indexafter == 1){
                        System.out.println("It's good");
                    }
                }

            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        try{
            writeFiles(hm);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            swr.removeStopwords("index.txt");
        } catch(Exception e){
            e.printStackTrace();
        }


    }

    public void writeFiles(HashMap<Integer,String> hm){
        try
        {
            PrintWriter outputFile = new PrintWriter("index.txt");
            Iterator it = hm.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                outputFile.write(pair.getKey()+" "+pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            outputFile.close();
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
        }

    }

}
