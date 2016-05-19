import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONTester {
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException {
		//testXpathCognome(PropertiesFile.getXpathCognome());
	}
	
	/*public static void testXpathCognome(String file){
    	
        try {
        	JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject site = (JSONObject)jsonObject.get(siteurl);
            JSONArray links = (JSONArray)site.get(key);
            //System.out.println("\nLink List:");
            Iterator<String> iterator = links.iterator();
            while (iterator.hasNext()) {
                //System.out.println(iterator.next());
                sites.add(iterator.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return sites;
    }*/
}
