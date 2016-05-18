import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

// static String path="//div[@class='label']/text()[preceding-sibling::br]";following
public class XpathDummy {
	//static String url="http://www.frys.com/product/7790658;jsessionid=kqpCq6tHkCZWMzkzpc+Y2A__.node4?site=sr:SEARCH:MAIN_RSLT_PG";
	static String file = PropertiesFile.getFile();
	static String site = PropertiesFile.getSite();
	static String key = PropertiesFile.getKey();
	static String xpath = PropertiesFile.getXpath();
	static String url = "http://www.futurepowerpc.com/scripts/product.asp?PRDCODE=2990-43112";

	public static void main(String args[]){
		//System.out.println(cleanerString("http://www.futurepowerpc.com/scripts/product.asp?PRDCODE=2990-43112",xpath));
		checkAllUrl(JSONReadFromFile.urlList(file, site, key), xpath);
		//System.out.println(checkUrlXpath("https://www.frontierpc.com/storage/storage-arrays/das-array/lacie/5big-thunderbolt-2-professional-5-disk-hardware-raid-9000503u-1027805142.html", PropertiesFile.getXpath()));
		//System.out.println(JSONReadFromFile.urlList(file, site, key));
	}

	public static String checkUrlXpath(String url, String path) {
		String s = null;
		try {
			Document doc2 = Jsoup.connect(url).get();
			String html = doc2.html();
			TagNode tagNode = new HtmlCleaner().clean(html);
			org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpathObj = xPathfactory.newXPath();
			XPathExpression expr = xpathObj.compile(path);
			//NodeList nl = null;
			//nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			s = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		/*for (int i = 0; i < nl.getLength(); i++) {
			System.out.println("nel for-----------------------------------------------");
			System.out.println(nl.item(i).getNodeValue());
		    //System.out.println(nl.item(i).getFirstChild().getNodeValue()); 
		}*/
		return s;//nl.item(0).getFirstChild().getNodeValue();//.replaceAll(" ", "").replaceAll("\n", "");
	}

	public static void checkAllUrl(List<String> urlList, String xpath){
		String item = key.split("-")[1];
		String code = "";
		//String attribute=checkUrlXpath(urlList.get(1), attributePath);
		for (String url : urlList) {
			System.out.println(url);
			code = cleanerString(url, xpath);
			System.out.println(xpath+"= "+code);
		}
		//JSONWriter.writeData(site, item, attribute, urlList, code);
		//JSONWriter.writeXpath(site, item, xpathList, attribute);

	}

	public static String cleanerString(String url, String xpath){
		String s=null;
		try{
			String html = Jsoup.connect(url).get().html();
			TagNode tagNode = new HtmlCleaner().clean(html);
			org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpathObj = xPathfactory.newXPath();
			XPathExpression expr = xpathObj.compile(xpath);
			s = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return s;
	}

}
