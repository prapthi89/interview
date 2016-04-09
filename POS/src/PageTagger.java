import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.jsoup.Jsoup;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PageTagger 
{
	private MaxentTagger tagger;
	public PageTagger()
	{
		tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
	}
	public String tagText(String in) 
	{
		return tagger.tagString(in);
	}
	
	public String getText(URL url) 
	{
		String urlString = "";
		try 
		{
			URLConnection conn = url.openConnection();
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) conn;
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String current;
			while((current = in.readLine()) != null)
			{
				urlString += current;
			}
			urlString = Jsoup.parse(urlString).text();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return urlString;
	}
	public static void main(String[] args) throws IOException,
	ClassNotFoundException 
	{

		URL url;
		PageTagger tagger = new PageTagger();
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		for(int i=0;i<args.length;i++)
		{
			url = new URL(args[i]);
			writer.println("************** POS Tagged For "+url.getHost()+" *****************\n");
			//text has the text of the url
			String text = tagger.getText(url);
			//splitting text in to chunks of size 1000, and tagging it using tagText
			String[] textChunks = text.split(" ");
			long size = textChunks.length;
			int chunkSize = 1000;
			int j = 0;
			while(j<size)
			{
				String subString = "";
				for(int k=0;k<chunkSize && j<size;k++,j++)
				{
					subString = subString+" "+ textChunks[j];
				}
				writer.println(tagger.tagText(subString));
			}
			writer.println();
			writer.println("\n********************************************\n\n");
		}
		
		writer.close();
	}
}