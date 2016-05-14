import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class XPathTester{
	private static String htmlPage="http://www.fullcompass.com/product/305897.html";
	private static String xpath="//div";
	public static void main(String args[]) throws XPathExpressionException, IOException, ParserConfigurationException {
		execute();
	}
	public XPathTester(String htmlPage, String xpath) {
		this.htmlPage = htmlPage;
		this.xpath = xpath;
	}

	public static void execute() {
		//XPathExecutor executor = new XPathExecutor();
		try {
			Document doc = Jsoup.connect(htmlPage).get();
			String html = doc.html();
			List<String> xpathResult = XPathExecutor.executeXPath(html, xpath);
			System.out.println(xpathResult);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}