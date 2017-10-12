import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class FormatAdjustment here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class FormatAdjustment {
    private String term;
    private ArrayList<HashMap<String, Integer>> termFrequency;
    private float idf;

    public FormatAdjustment() {
        term = "";
        termFrequency = new ArrayList<HashMap<String, Integer>>();
        idf = 0.0f;
    }

    public FormatAdjustment(String term, String filename, int tf, float idf) {
        setTerm(term);
        this.termFrequency = termFrequency;
        setIdf(idf);
    }

    public String getTerm() {
        return term;
    }

    public float getIdf() {
        return idf;
    }

    public void setTerm(String term) {
        if (!term.isEmpty() || term.length() != 0)
            this.term = term;
        else
            this.term = "NoTerm";
    }

    public void setIdf(float idf) {
        if (idf != 0.0f)
            this.idf = idf;
        else
            this.idf = 0.0f;
    }
}
