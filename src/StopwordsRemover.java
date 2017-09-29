package src;

import java.io.*;

public class StopwordsRemover {


    public void removeStopwords() {

        try {
            FileInputStream stopWordStream = new FileInputStream("stopwords.txt");
            InputStreamReader iStreamReader = new InputStreamReader(stopWordStream, "UTF-8");
            BufferedReader StopWordBr = new BufferedReader(iStreamReader);
            FileInputStream oldFileStream = new FileInputStream("index.txt");
            InputStreamReader isr = new InputStreamReader(oldFileStream, "UTF-8");
            BufferedReader oldFileBr = new BufferedReader(isr);

            FileOutputStream newFileStream = new FileOutputStream("index.txt");
            OutputStreamWriter osw = new OutputStreamWriter(newFileStream, "UTF-8");
            BufferedWriter newFileBw = new BufferedWriter(osw);
            String ss = null;
            String sa = null;
            String saa = null;
            StringBuffer s2 = new StringBuffer();
            StringBuffer s1 = new StringBuffer();
            while ((ss = StopWordBr.readLine()) != null) {

                s1.append(ss).append(" ");

            }
            String[] resultArray = (s1.toString()).split(" ");
            while ((sa = oldFileBr.readLine()) != null) {

                s2.append(sa).append(" ");

            }
            String[] srcArray = (s2.toString()).split(" ");
            for (int i = 0; i < srcArray.length; i++)
                for (int j = 0; j < resultArray.length; j++) {

                    if (srcArray[i].equals(resultArray[j])) {

                        srcArray[i] = "";

                    }
                }

            StringBuffer finalStr = new StringBuffer();
            for (int i = 0; i < srcArray.length; i++) {

                if (srcArray[i] != null) {

                    finalStr = finalStr.append(srcArray[i]).append(" ");
                }

            }

            newFileBw.write(finalStr.toString());
            newFileBw.newLine();
            newFileBw.flush();
            newFileBw.close();
            StopWordBr.close();
            oldFileBr.close();
        } catch (FileNotFoundException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
}
