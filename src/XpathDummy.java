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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

public class XpathDummy {
	private static final String CONNECTION_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";

	public static void main(String args[]) {
		testSourceUrls();
	}

	private static void testSourceUrls() {
		String sourceFile = PropertiesFile.getFile();

		String site = PropertiesFile.getSite();
		String key = PropertiesFile.getKey();
		String xpath = PropertiesFile.getXpath();

		System.out.println(key);

		List<String> urlList = JSONReadFromFile.urlList(sourceFile, site, key);
		checkAllUrl(urlList, xpath);
	}

	public static void checkAllUrl(List<String> urlList, String xpath) {
		String code = "";

		for (String url : urlList) {
			if (url != null)
				code = cleanerString(url, xpath);

			// System.out.println(" {\"" + url + "\": \"" + code + "\"},");
			System.out.println(code + " => " + xpath + " => " + url);
		}

	}

	public static String cleanerString(String url, String xpath) {
		String xpathValue = "link errato";

		try {
			Connection jsoupConnection = Jsoup.connect(url).userAgent(CONNECTION_AGENT);
			Document htmlDocument = jsoupConnection.timeout(10000).get();
			String html = htmlDocument.html();

			TagNode tagNode = new HtmlCleaner().clean(html);
			org.w3c.dom.Document cleanedDocument = new DomSerializer(new CleanerProperties()).createDOM(tagNode);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpathObj = xPathfactory.newXPath();
			XPathExpression expr = xpathObj.compile(xpath);
			xpathValue = (String) expr.evaluate(cleanedDocument, XPathConstants.STRING);
			xpathValue = xpathValue.trim();

		} catch (XPathExpressionException e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
			xpathValue = "XPathExpressionException";
		} catch (IOException e) {
			// 404, 403
			// e.printStackTrace();
			System.out.println(e.getMessage());
			xpathValue = "IOException";
		} catch (ParserConfigurationException e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
			xpathValue = "ParserConfigurationException";
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
			xpathValue = "UnspecifiedException";
		}
		

		return xpathValue;
	}

	// private static void testSingleUrl() {
	// String testUrl = "http://www.futurepowerpc.com/scripts/product.asp?PRDCODE=2990-43112";
	// String xpath = "//*[@class='sku']";
	// String xpathValue = cleanerString(testUrl,xpath);
	//
	// System.out.println(xpathValue);
	// }

}
