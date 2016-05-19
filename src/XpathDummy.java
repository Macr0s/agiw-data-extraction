import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.List;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

public class XpathDummy {
	static String file = PropertiesFile.getFile();
	static String site = PropertiesFile.getSite();
	static String key = PropertiesFile.getKey();
	static String xpath = PropertiesFile.getXpath();
	static String url = "http://www.futurepowerpc.com/scripts/product.asp?PRDCODE=2990-43112";

	public static void main(String args[]) {
		//System.out.println(cleanerString(url,xpath));
		checkAllUrl(JSONReadFromFile.urlList(file, site, key), xpath);
	}
	
	public static void checkAllUrl(List<String> urlList, String xpath) {
		String code = "";
		//String attribute=checkUrlXpath(urlList.get(1), attributePath);
		for (String url : urlList) {
			code = cleanerString(url, xpath);
			System.out.println("        {\"" + url + "\": \"" + code + "\"},");			
			//System.out.println(xpath + "= " + code);
		}
	}

	public static String cleanerString(String url, String xpath) {
		String s = "link errato";
		try {
			String html = Jsoup.connect(url).get().html();
			TagNode tagNode = new HtmlCleaner().clean(html);
			org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpathObj = xPathfactory.newXPath();
			XPathExpression expr = xpathObj.compile(xpath);
			s = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			s = "XPathExpressionException";
		} catch (IOException e) {
			e.printStackTrace();
			s = "IOException";
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			s = "ParserConfigurationException";
		}
		return s;
	}
}
