import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
//	static String path="//div[@class='label']/text()[preceding-sibling::br]";following
public class XpathDummy {
	//static String url="http://www.frys.com/product/7790658;jsessionid=kqpCq6tHkCZWMzkzpc+Y2A__.node4?site=sr:SEARCH:MAIN_RSLT_PG";
	static String file="sources5601-5800.json";
	static String site="www.freestylephoto.biz";
	static String key="5617-Flashes";
	static String attributePath="//div[@class='span5 product-name-box']/text()[2]";
	public static void main(String args[]) throws XPathExpressionException, IOException, ParserConfigurationException{
		List<String> xpathList=new ArrayList<>();
		xpathList.add("//div[@class='span5 product-name-box']/text()[3]");
		checkAllUrl(JSONReadFromFile.urlList(file, site, key), xpathList);
	}
	
	public static String checkUrlXpath(String url, String path)throws XPathExpressionException, IOException, ParserConfigurationException {
		Document doc2 = Jsoup.connect(url).get();
		String html = doc2.html();
		TagNode tagNode = new HtmlCleaner().clean(html);
		org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpathObj = xPathfactory.newXPath();
		XPathExpression expr = xpathObj.compile(path);
		NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		/*for (int i = 0; i < nl.getLength(); i++) {
			System.out.println("nel for-----------------------------------------------");
			System.out.println(nl.item(i).getNodeValue());
		    //System.out.println(nl.item(i).getFirstChild().getNodeValue()); 
		}*/
		return nl.item(0).getNodeValue().replaceAll(" ", "").replaceAll("\n", "");
	}
	public static void checkAllUrl(List<String> urlList, List<String> xpathList) throws XPathExpressionException, IOException, ParserConfigurationException{
		String item=key.split("-")[1];
		String code="";
		String attribute=checkUrlXpath(urlList.get(1), attributePath);
		for(String url:urlList){
			
			for(String xpath:xpathList){
				code=checkUrlXpath(url, xpath);
				System.out.println(url);
				System.out.println(xpath+" = "+checkUrlXpath(url, xpath));
				
			}
			
		}
		JSONWriter.writeData(site, item, attribute, urlList, code);
		JSONWriter.writeXpath(site, item, xpathList, attribute);
		
	}
}
