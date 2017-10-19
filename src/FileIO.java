import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *Class FileIO
 *This is the FileIO class. 
 * @author Yushan Wang 
 * @version 8.1 (16/10/2017)
 */
public class FileIO 
{

    /**
     * This method is for searching all files in selected folder
     * @param folderPath
     * @return
     */
    public ArrayList<File> fileInFolder(String folderPath) {
        // read the txtfile
        File folder = new File(folderPath);

        ArrayList<File> files = new ArrayList<File>();

        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                files.add(file);
            }
        }
        return files;
    }

    /**
     * Read the File lines using Apache Common IO
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
                fileLines.add(sCurrentLine);
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
