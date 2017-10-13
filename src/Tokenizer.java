

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    private HashSet<String> stopwords;
    private HashMap<String, Integer> tokenAndQuantities;


    public Tokenizer() {
        stopwords = new HashSet<String>();
        tokenAndQuantities = new HashMap<String,Integer>();
    }

    public Tokenizer(String clear) {
        tokenAndQuantities = new HashMap<String,Integer>();
    }

    public void setStopwordList(HashSet<String> stopwordsSet) {
        stopwords.addAll(stopwordsSet);
    }

    /**
     * Add the Token Quantities to the hashmap
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
     * Token Hypen Words
     *
     * @param check
     * @return
     */
    private String getHypen(String check) {
        String back = check;

        // regex for email addresses
        Pattern email_pattern = Pattern.compile("^[/w-]+(/.[/w-]+)*@[/w-]+(/.[/w-]+)+$"); // Tokenize for email

        Matcher m = email_pattern.matcher(check);
        while (m.find()) {
            String email = m.group(0);
            addToken(email);
            back = back.replaceFirst(email, "");
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

        // regex for email addresses
        Pattern email_pattern = Pattern.compile("^[/w-]+(/.[/w-]+)*@[/w-]+(/.[/w-]+)+$"); // Tokenize for email

        Matcher m = email_pattern.matcher(check);
        while (m.find()) {
            String email = m.group(0);
            addToken(email);
            back = back.replaceFirst(email, "");
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

        // regex for URL
        Pattern url_pattern = Pattern.compile("^[a-zA-z]+://(/w+(-/w+)*)(/.(/w+(-/w+)*))*(/?/S*)?$"); //Tokenize for URL
        Matcher m = url_pattern.matcher(check);
        while (m.find()) {
            String email = m.group(0);
            addToken(email);
            back = back.replaceFirst(email, "");
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

        // regex for IPV4
        Pattern ipv4_pattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"); // Tokenize for ipv4
        Matcher m = ipv4_pattern.matcher(check);
        while (m.find()) {
            String ipv4 = m.group(0);
            addToken(ipv4);
            back = back.replaceFirst(ipv4, "");
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

        // regex for IPV6
        Pattern ipv6_pattern = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");// Tokenize for ipv6
        Matcher m = ipv6_pattern.matcher(check);
        while (m.find()) {
            String ipv4 = m.group(0);
            addToken(ipv4);
            back = back.replaceFirst(ipv4, "");
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

        // regex for SingleQuotation
        Pattern sgquo_pattern = Pattern.compile("([\"'])(?:(?=(\\\\?))\\2.)*?\\1");     // Tokenize for Single Quotation
        Matcher m = sgquo_pattern.matcher(check);
        while (m.find()) {
            String sgquo = m.group(0);
            addToken(sgquo);
            back = back.replaceFirst(sgquo, "");
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

        // regex for multipleWords
        Pattern mwords_pattern = Pattern.compile("[A-Z][\\w\\s]+");       // Tokenize for two or more words separated by space
        Matcher m = mwords_pattern.matcher(check);
        while (m.find()) {
            String mwords = m.group(0);
            addToken(mwords);
            back = back.replaceFirst(mwords, "");
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

        // regex for acronyms
        Pattern acro_pattern = Pattern.compile("(?:[a-zA-Z]\\.){2,}");
        Matcher m = acro_pattern.matcher(check);
        while (m.find()) {
            String acro = m.group(0);
            addToken(acro);
            back = back.replaceFirst(acro, "");
        }

        return back;
    }


    /**
     * HashMap of {token -> frequency}
     * 1. remove whitespace seperated capital letter chaines
     * a. if its chain at end of line, dont add it wait to check next line
     * b. if chain is at start of line and last line had chain then first combine them
     * c. otherwise just add the token
     * 2. remove email addresses
     * 3. split rest of tokens on whitespace and punctuation
     */
    public HashMap<String, Integer> tokenize(ArrayList<String> lines) {

        Iterator<String> it = lines.iterator();

        while (it.hasNext()) {
            String line = it.next(); //get the next line
            //In order to get tokens in specialized requirements, we need to take some actions such as using regular expressions
            if (!line.equals("\uFEFF")&&!line.equals("")){
                line = getHypen(line);
                line = getEmailAddresses(line);
                line = getURL(line);
                line = getIPV4(line);
                line = getIPV6(line);
                //line = getSingleQuotation(line);
                line = getMultipleWords(line);
                line = getAcronyms(line);
                // If we cannot pickup the tokens in specialized requirements, we need to regard every words as tokens
                String[] words = line.split("[ .,:;”’()?!]");
                if(words.length>0){
                    for (String word : words) {
                        if (!stopwords.contains(word)&&!word.equals("")) {
                            addToken(word);
                        }
                    }
                }
            }
        }

        return tokenAndQuantities;
    }


}
