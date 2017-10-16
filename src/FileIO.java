import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hli224 on 16/10/2017.
 */
public class FileIO {

    /**
     * This method is for searching all files in selected folder
     *
     * @param folderPath
     * @return
     */
    public ArrayList<File> fileInFolder(String folderPath) {
        // read the txtfile
        File folder = new File(folderPath);
        ArrayList<File> files = new ArrayList<File>();
        if (folder.exists()) {
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    files.add(file);
                }
            }
        } else
            System.out.println("Please input a correct directory Path!");

        return files;
    }

    /**
     * Read the File lines using Apache Common IO,exempted, Will not use
     * @param file
     * @return
     */
//    public ArrayList<String> readFile3(File file) {
//        ArrayList<String> fileLines = new ArrayList<String>();
//
//        LineIterator it = null;
//        try {
//            it = FileUtils.lineIterator(file, "UTF-8");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            while (it.hasNext()) {
//                String line = it.nextLine().trim();
//                // faster reading line by Apache Commons IO,readlines with milliseconds
//                // Doing the tokenization line by line
//                fileLines.add(line);
//            }
//        } finally {
//            LineIterator.closeQuietly(it);
//        }
//
//        return fileLines;
//    }

    /**
     * Read the File lines using Apache Common IO
     *
     * @param file
     * @return
     */
    public ArrayList<String> readFile(File file) {
        ArrayList<String> fileLines = new ArrayList<String>();

        BufferedReader br = null;
        FileReader fr = null;

        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                fileLines.add(sCurrentLine.trim());
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

        return fileLines;
    }
}
