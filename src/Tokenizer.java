import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    private HashMap<Integer,String> hm;

    /**
     * Match all the words
     * @param s
     * @return
     */
    public HashMap<Integer, String> pickSpecialWords(String s)
    {
        hm = new HashMap<Integer, String>();
        // Matching all tokenization
        Pattern hypen = Pattern.compile("[a-zA-Z]+\\-[a-zA-Z]+"); // Tokenize for hypen
        Pattern ipv4 = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"); // Tokenize for ipv4
        Pattern ipv6 = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");// Tokenize for ipv6
        Pattern url = Pattern.compile("^[a-zA-z]+://(/w+(-/w+)*)(/.(/w+(-/w+)*))*(/?/S*)?$"); //Tokenize for URL
        Pattern email = Pattern.compile("^[/w-]+(/.[/w-]+)*@[/w-]+(/.[/w-]+)+$"); // Tokenize for email
        Pattern sgquo = Pattern.compile("([\"'])(?:(?=(\\\\?))\\2.)*?\\1");     // Tokenize for Single Quotation
        Pattern mwords = Pattern.compile("[A-Z][\\w\\s]+");       // Tokenize for two or more words separated by space
        Pattern acro = Pattern.compile("(?:[a-zA-Z]\\.){2,}");       // Tokenize for acronyms

        // Match all the patterns and put it into hashmap
        Matcher mHypen = hypen.matcher(s);
        Matcher mIpv4 = ipv4.matcher(s);
        Matcher mIpv6 = ipv6.matcher(s);
        Matcher mUrl = url.matcher(s);
        Matcher mEmail = email.matcher(s);
        Matcher sQuotation = sgquo.matcher(s);
        Matcher mWords = mwords.matcher(s);
        Matcher acronyms = acro.matcher(s);


        int i = 0;

        while (mHypen.find()){
            hm.put(i,mHypen.group(0));
            i++;

        }

        while(mIpv4.find()){
            hm.put(i,mIpv4.group(0));
            i++;
        }

        while(mIpv6.find()){
            hm.put(i,mIpv4.group(0));
            i++;
        }

        while(mUrl.find()){
            hm.put(i,mUrl.group(0));
            i++;
        }

        while(mEmail.find()){
            hm.put(i,mEmail.group(0));
            i++;
        }

        while(sQuotation.find()){
            hm.put(i,sQuotation.group(0));
            i++;
        }

        while(mWords.find()){
            hm.put(i,mWords.group(0));
            i++;
        }

        while(acronyms.find()){
            hm.put(i,acronyms.group(0));
            i++;
        }

        return hm;
    }

    /**
     * Picking other words which are not specified words
     * @param s
     * @return hm
     */
    public HashMap<Integer,String> pickOtherWords(String s) {

        String[] r = s.split("[.,:;”’()?!]");
        for (int i = 0; i < r.length;i++){
            hm.put(i,r[i]);
        }
        System.out.println(hm.size());
        return hm;
    }


    public static void main(String args[]){

        String s = "127.0.0.1  , hahahahahha, abc-123,  abc-abc, defsfdkljikcc dd 66,lala-hahahha,   192.168.1.2, 192,168.3.5";
//        String s = "127.0.0.1";
        Tokenizer tk = new Tokenizer();
        HashMap<Integer,String> hm = tk.pickSpecialWords(s);
        hm = tk.pickOtherWords(s);
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
//            System.out.println(pair.getKey() + " = " + pair.getValue().toString().replaceAll("-"," "));
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}