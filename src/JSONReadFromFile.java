import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
  
public class JSONReadFromFile {
 
    public static List<String> urlList(String file, String siteurl, String key){
    	JSONParser parser = new JSONParser();
    	List<String> sites=new ArrayList<String>();
        try {
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
    }
}