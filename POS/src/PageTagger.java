import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
 
public class PageTagger {
	public String tagText(String in) {
		MaxentTagger tagger = new MaxentTagger(
                "taggers/english-left3words-distsim.tagger");
 
        // The tagged string
        return tagger.tagString(in);
	}
	public String getText(URL url) {
		String urlString = "";
		try {
			URLConnection conn = url.openConnection();
			HttpURLConnection connection = null;
	        connection = (HttpURLConnection) conn;
	        Boolean body=false;
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(connection.getInputStream()));
	                String current;
	                while((current = in.readLine()) != null)
	                {
	                	if(current.contains("body"))
	                		body=true;
	                		
	                   if(body)
	                   urlString += current;
	                }
	                //using Jsoup, which is how html parsing is done
	                Document doc = Jsoup.parse(urlString);
	                return (doc.text());
	                
	                /*Pattern pattern = Pattern.compile("(<style[^>]*>([\\s\\S]*?)<\\/style>)");
	                Matcher matcher = pattern.matcher(urlString);
	                
	            
	                	urlString = matcher.replaceAll("");
	                
	                //to replace all script tags and it's contents by ""
	                pattern = Pattern.compile("(<script[^>]*>([\\s\\S]*?)<\\/script>)");
	                matcher = pattern.matcher(urlString);
	                urlString = matcher.replaceAll("");pattern = Pattern.compile("<.*?>");
	                matcher = pattern.matcher(urlString);urlString = matcher.replaceAll("");
	                return urlString;*/ 
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		 return urlString;
	}
    public static void main(String[] args) throws IOException,
            ClassNotFoundException {
 
    	String input = args[0];
    	URL url = new URL(input); 
        PageTagger pagetag = new PageTagger();
        String res1, res2;
        try {
        	res1 = pagetag.getText(url);
        	res2 = pagetag.tagText(res1);
        	PrintWriter writer = new PrintWriter("/home/prapthi/workspace_java/POS/output.txt", "UTF-8");
        	writer.println("Tagged text of "+input+" is:");
        	writer.println(res2);
        	writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
