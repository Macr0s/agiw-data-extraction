import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UrlToCodeWriter {
	public static void main(String args[]){
		UrlToCode();
		/*Map<String, List<String>> url2Codes = putCsvIntoMap();
		System.out.println(url2Codes.keySet().size());
		System.out.println(url2Codes.get("http://www.forwardforward.com/product-brinley-sunglasses/OPEO-WG49/?&pdpsrc=rec1&sectionURL=Direct+Hit&d=null"));
	*/
	}

	//mappa usata per andare più veloci in fase di ricerca url
	public static Map<String, List<String>> putCsvIntoMap(){
		Map<String, List<String>> url2Codes = new HashMap<String, List<String>>();
		try {
			String line = "";
			String cvsSplitBy = "\t";
			String csvFile = PropertiesFile.getUrlToCode();
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] lineSplit = line.split(cvsSplitBy);
				List<String> codeList = new ArrayList<String>();//deve mantenere ordinamento con cui li inserisco
				for(int i=1;i<lineSplit.length;i++){
					codeList.add(lineSplit[i].replaceAll("   ", ""));
				}
				url2Codes.put(lineSplit[0], codeList);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url2Codes;
	}

	// preso il csv scrive un altro csv con i link e i vari codici
	public static void UrlToCode(){
		try {
			String value="";
			String csvFile = PropertiesFile.getTSV();
			String currentSite;
			FileWriter dataFile = new FileWriter(PropertiesFile.getUrlToCode(),true);
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			String line = "";
			String cvsSplitBy = "\t";
			line = br.readLine();//salto descrizione
			while ((line = br.readLine()) != null) {
				String complete_line="";
				String[] lineSplit = line.split(cvsSplitBy);
				currentSite=lineSplit[0];
				String key=lineSplit[1];
				//System.out.println(currentSite+" - "+key);
				List<String> urlList =JSONReadFromFile.urlList(PropertiesFile.getFile(), currentSite, key);
				for(String url:urlList){
					String html = Jsoup.connect(url).get().html();
					TagNode tagNode = new HtmlCleaner().clean(html);
					org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
					XPathFactory xPathfactory = XPathFactory.newInstance();
					XPath xpathObj = xPathfactory.newXPath();
					XPathExpression expr = xpathObj.compile(lineSplit[2]);
					value = (String) expr.evaluate(doc, XPathConstants.STRING);
					complete_line=url+"\t"+value.replaceAll("\n", "");
					int i=5;
					while(i<lineSplit.length && !lineSplit[i].equals("")){
						if(lineSplit[i+2].toLowerCase().equals("true")){
							expr = xpathObj.compile(lineSplit[i]);
							value = (String) expr.evaluate(doc, XPathConstants.STRING);
							complete_line=complete_line+"\t"+value.replaceAll("\n", "");
						}
						i=i+3;
					}

					/*if(lineSplit.length>5 && !lineSplit[5].equals("")){
						expr = xpathObj.compile(lineSplit[5]);
						value = (String) expr.evaluate(doc, XPathConstants.STRING);
						complete_line=complete_line+"\t"+value.replaceAll("\n", "");
					}
					if(lineSplit.length>8 && !lineSplit[8].equals("")){
						expr = xpathObj.compile(lineSplit[8]);
						value = (String) expr.evaluate(doc, XPathConstants.STRING);
						complete_line=complete_line+"\t"+value.replaceAll("\n", "");
					}*/
					System.out.println(complete_line);
					dataFile.write(complete_line+"\n");
				}
			}
			dataFile.flush();
			dataFile.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}