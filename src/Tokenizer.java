

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Tokenizer
 * This is the Tokenizer class. This class includes the methods of tokenizing the terms, removing the stopwords and stemming terms.
 *
 * @author Yushan Wang
 * @version 8.1 (16/10/2017)
 */
public class Tokenizer {

    private HashSet<String> stopwords;

    //This hashMap has entries that has the tokenized terms as keys and their frequencies as values
    private HashMap<String, Integer> tokenAndQuantities;

    //regex for words with hyphen
    private final String HYPHEN_PATTERN = "^[/w-]+(/.[/w-]+)*@[/w-]+(/.[/w-]+)+$";
    // regex for email addresses
    private final String EMAIL_PATTERN = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}";
    // regex for URL
    private final String URL_PATTERN = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[\\-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9\\.\\-]+|(?:www\\.|[\\-;:&=\\+\\$,\\w]+@)[A-Za-z0-9\\.\\-]+)((?:\\/[\\+~%\\/\\.\\w\\-_]*)?\\??(?:[\\-\\+=&;%@\\.\\w_]*)#?(?:[\\.\\!\\/\\\\\\w]*))?)";
    // regex for IPV4
    private final String IPV4_PATTERN = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    // regex for IPV6
    private final String IPV6_PATTERN = "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$";
    // regex for SingleQuotation
    private final String SINGLEQUATATION_PATTERN = "\"(?<=').*?(?=')\"";
    // regex for multipleWords
    private final String MULTIPLEWORDS_PATTERN = "[A-Z][a-z]+\\s[A-Z]\\w{0,}";
    // regex for acronyms
    private final String ACRONYMS_PATTERN = "(?:[a-zA-Z]\\.){2,}";

    /**
     * create a Tokenizer
     */
    public Tokenizer() {
        stopwords = new HashSet<String>();
        tokenAndQuantities = new HashMap<String, Integer>();
    }

    /**
     * set stopwords list
     *
     * @param stopwordsSet
     */
    public void setStopwordList(HashSet<String> stopwordsSet) {
        stopwords.addAll(stopwordsSet);
    }


    /**
     * Add the token to the hashmap
     * Stemming the tokens befeore add the token
     *
     * @param token
     */
    private void addToken(String token) {
        //Reference from http://tartarus.org/martin/PorterStemmer/
        Stemmer porterStemmer = new Stemmer();
        porterStemmer.add(token.toCharArray(), token.length());
        porterStemmer.stem();
        String stemmedToken = porterStemmer.toString();

        if (tokenAndQuantities.containsKey(stemmedToken)) {
            tokenAndQuantities.put(stemmedToken, tokenAndQuantities.get(stemmedToken) + 1);
        } else {
            tokenAndQuantities.put(stemmedToken, 1);
        }
    }


    /**
     * Token Hyphen Words
     *
     * @param check
     * @return
     */
    private String getHyphen(String check) {
        String back = check;

        Pattern hyphen_pattern = Pattern.compile(HYPHEN_PATTERN); // Tokenize hyphen words
        //Compare "check" and the regex of hyphen words
        Matcher m = hyphen_pattern.matcher(check);
        while (m.find()) {
            String hyphen = m.group(0);//get the whole matcher
            hyphen = hyphen.replaceAll("-", "");
            addToken(hyphen);
            back = back.replace(hyphen, "");//replace the hyphen word with "" in the original file 
        }

        return back;
    }

    /**
     * Token Email Address
     *
     * @param check
     * @return
     */
    private String getEmailAddresses(String check) {
        String back = check;

        Pattern email_pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE); // Tokenize email

        Matcher m = email_pattern.matcher(check);
        while (m.find()) {
            String email = m.group(0);
            addToken(email);
            back = back.replace(email, "");
        }

        return back;
    }

    /**
     * Token for URL
     *
     * @param check
     * @return
     */
    private String getURL(String check) {
        String back = check;

        Pattern url_pattern = Pattern.compile(URL_PATTERN); //Tokenize URL

        Matcher m = url_pattern.matcher(check);
        while (m.find()) {
            String url = m.group(0);
            addToken(url);
            back = back.replace(url, "");
        }

        return back;
    }


    /**
     * Token for IPV4
     *
     * @param check
     * @return
     */
    private String getIPV4(String check) {
        String back = check;

        Pattern ipv4_pattern = Pattern.compile(IPV4_PATTERN); // Tokenize ipv4

        Matcher m = ipv4_pattern.matcher(check);
        while (m.find()) {
            String ipv4 = m.group(0);
            addToken(ipv4);
            back = back.replace(ipv4, "");
        }

        return back;
    }

    /**
     * Token for IPV6
     *
     * @param check
     * @return
     */
    private String getIPV6(String check) {
        String back = check;

        Pattern ipv6_pattern = Pattern.compile(IPV6_PATTERN);// Tokenize ipv6

        Matcher m = ipv6_pattern.matcher(check);
        while (m.find()) {
            String ipv6 = m.group(0);
            addToken(ipv6);
            back = back.replace(ipv6, "");
        }

        return back;
    }

    /**
     * Token for SingleQuotation
     *
     * @param check
     * @return
     */
    private String getSingleQuotation(String check) {
        String back = check;

        Pattern sgquo_pattern = Pattern.compile(SINGLEQUATATION_PATTERN);     // Tokenize Single Quotation

        Matcher m = sgquo_pattern.matcher(check);
        while (m.find()) {
            String sgquo = m.group(0);
            addToken(sgquo);
            back = back.replace(sgquo, "");
        }

        return back;
    }

    /**
     * Token for Multiple Words (Which are two words and more, may have some words from last line and connect to the next line word)
     *
     * @param check
     * @return
     */
    private String getMultipleWords(String check) {
        String back = check;

        Pattern mwords_pattern = Pattern.compile(MULTIPLEWORDS_PATTERN);       // Tokenize two or more words separated by space

        Matcher m = mwords_pattern.matcher(check);
        while (m.find()) {
            String mwords = m.group(0);
            addToken(mwords);
            back = back.replace(mwords, "");
        }

        return back;
    }


    /**
     * Token for acronyms
     *
     * @param check
     * @return
     */
    private String getAcronyms(String check) {
        String back = check;

        Pattern acro_pattern = Pattern.compile(ACRONYMS_PATTERN); //Tokenize acronyms words

        Matcher m = acro_pattern.matcher(check);
        while (m.find()) {
            String acro = m.group(0);
            addToken(acro);
            back = back.replace(acro, "");
        }

        return back;
    }


    /**
     * HashMap of {token -> frequency}
     */
    public HashMap<String, Integer> tokenize(ArrayList<String> lines) {

        Iterator<String> it = lines.iterator();

        while (it.hasNext()) {
            String line = it.next(); //get the next line
            //In order to get tokens in specialized requirements, we need to take some actions such as using regular expressions
            if (!line.equals("\uFEFF") && !line.equals("")) {
                line = getHyphen(line);
                line = getEmailAddresses(line);
                line = getURL(line);
                line = getIPV4(line);
                line = getIPV6(line);
                line = getSingleQuotation(line);
                line = getMultipleWords(line);
                line = getAcronyms(line);
                // If we cannot pickup the tokens in specialized requirements, we need to regard every words as tokens
                String[] words = line.split("[ .,:;”’()?!]");
                if (words.length > 0) {
                    for (String word : words) {
                        word = word.replaceAll("\\W", "");//replace special characters
                        word = word.replaceAll("_", "");
                        word = word.replaceAll("[A-Za-z0-9]", "");
                        if (!stopwords.contains(word) && !word.equals("")) {
                            addToken(word);
                        }
                    }
                }
            }
        }

        return tokenAndQuantities;
    }
}
