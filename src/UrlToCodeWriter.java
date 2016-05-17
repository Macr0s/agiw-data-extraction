import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	}

	public static void UrlToCode(){
		try {
			String value="";
			String csvFile = "AGIW.tsv";
			String currentSite;
			FileWriter dataFile = new FileWriter("urlToCode",true);
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
					Document doc2 = Jsoup.connect(url).get();
					String html = doc2.html();
					TagNode tagNode = new HtmlCleaner().clean(html);
					org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
					XPathFactory xPathfactory = XPathFactory.newInstance();
					XPath xpathObj = xPathfactory.newXPath();
					XPathExpression expr = xpathObj.compile(lineSplit[2]);
					value = (String) expr.evaluate(doc, XPathConstants.STRING);

					complete_line=url+","+value.replaceAll("\n", "");
					if(lineSplit.length>5 && !lineSplit[5].equals("")){
						expr = xpathObj.compile(lineSplit[5]);
						value = (String) expr.evaluate(doc, XPathConstants.STRING);
						complete_line=complete_line+","+value.replaceAll("\n", "");
					}
					if(lineSplit.length>8 && !lineSplit[8].equals("")){
						expr = xpathObj.compile(lineSplit[8]);
						value = (String) expr.evaluate(doc, XPathConstants.STRING);
						complete_line=complete_line+","+value.replaceAll("\n", "");
					}
				}
				System.out.println(complete_line);
				dataFile.write(complete_line+"\n");
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
