import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
class JSONWriter {
	/*static String path="//div[@class='span5 product-name-box']/text()[3]";
	static String file="sources5601-5800.json";
	static String site="www.freestylephoto.biz";
	static String key="5617-Flashes";
	public static void main(String[] args) {
		String item=key.split("-")[1];
		List<String> xpathList=new ArrayList<>();
		xpathList.add(path);
		xpathList.add("//div[@class=");
		String attribute="attr";
		writeXpath(site, item, xpathList, attribute);
		writeData(site, item, attribute, "http//", "23");
	}*/

	public static void writeXpath(String site, String item, List<String> xpathList, String attribute){
		JSONObject obj = new JSONObject();
		JSONObject jsonsite = new JSONObject();
		JSONArray jsonitem = new JSONArray();
		JSONObject sampleInnerElement = new JSONObject();
		for(String xpath:xpathList){
			sampleInnerElement.put("rule",xpath);
			sampleInnerElement.put("attribute_name",attribute);
			sampleInnerElement.put("page_id","true");
			jsonitem.add(sampleInnerElement);
		}
		jsonsite.put(item, jsonitem);
		obj.put(site, jsonsite);
		try {
			FileWriter file = new FileWriter("xpath.massuda.json",true);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = gson.toJson(obj);
			file.write(prettyJson+"\n");
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeData(String site, String item, String attribute, List<String> urlList, String code){
		JSONObject obj = new JSONObject();
		JSONObject jsonsite = new JSONObject();
		JSONObject jsonitem = new JSONObject();
		JSONArray jsonattribute = new JSONArray();
		JSONObject sampleInnerElement = new JSONObject();
		for(String url:urlList){
			sampleInnerElement.put(url,code);
			//sampleInnerElement.put("attribute_name",attribute);
			//sampleInnerElement.put("page_id","true");

			jsonattribute.add(sampleInnerElement);
		}
		jsonitem.put(attribute, jsonattribute);
		jsonsite.put(item, jsonitem);
		obj.put(site, jsonsite);

		try {
			FileWriter file = new FileWriter("data.massuda.json",true);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = gson.toJson(obj);
			file.write(prettyJson+"\n");
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
