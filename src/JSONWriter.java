import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class JSONWriter {
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException {
		//writeXpathJson();
		writeDataJsonFromTwoCsv();
		//CsvToJson();
	}

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
		}
		
		jsonattribute.add(sampleInnerElement);
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

	public static void CsvToJson() throws XPathExpressionException, ParserConfigurationException{
		String csvFile = "AGIW.csv";
		String precSite="";
		String currentSite;
		String page_id;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		JSONObject objXpath = new JSONObject();
		JSONObject objData = new JSONObject();

		try {
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			while (line != null) {
				String[] lineSplit = line.split(cvsSplitBy);
				currentSite=lineSplit[0];
				if(!currentSite.equals(precSite)){
					precSite=currentSite;
				}
				JSONObject jsonsite = new JSONObject();
				JSONArray jsonitem = new JSONArray();//array di rule...
				JSONObject sampleInnerElement = new JSONObject();//contiene rule, attr ...
				JSONObject sampleInnerElement2 = new JSONObject();

				JSONObject jsonsiteData = new JSONObject();
				JSONObject jsonitemData = new JSONObject();
				JSONArray jsonattributeData = new JSONArray();
				JSONArray jsonattribute2Data = new JSONArray();
				JSONObject sampleInnerElementData = new JSONObject();
				JSONObject sampleInnerElement2Data = new JSONObject();

				String item=lineSplit[1].split("-")[1];
				String key=lineSplit[1];
				System.out.println(currentSite+" "+key);
				//page_id=lineSplit[3];
				//JSONReadFromFile.urlList(PropertiesFile.getFile(), currentSite, key);

				sampleInnerElement.put("rule",lineSplit[2]);
				sampleInnerElement.put("attribute_name",lineSplit[3]);
				sampleInnerElement.put("page_id",lineSplit[4].toLowerCase());
				jsonitem.add(sampleInnerElement);
				//System.out.println("jsonitem = "+jsonitem.toString());
				if(lineSplit.length>5){
					sampleInnerElement2.put("rule",lineSplit[5]);
					sampleInnerElement2.put("attribute_name",lineSplit[6]);
					sampleInnerElement2.put("page_id",lineSplit[7].toLowerCase());
					jsonitem.add(sampleInnerElement2);
					//System.out.println("jsonitem2 = "+jsonitem.toString());
				}
				//System.out.println("----"+item);
				jsonsite.put(item, jsonitem);
				//System.out.println("jsonsite = "+jsonsite.toString());
				objXpath.put(currentSite, jsonsite);
				List<String> urlList =JSONReadFromFile.urlList(PropertiesFile.getFile(), currentSite, key);
				for(String url:urlList){
					String value=XpathDummy.checkUrlXpath(url, lineSplit[2]).replaceAll(" ", "").replaceAll("\n", "");
					System.out.println(value);
					sampleInnerElementData.put(url,value);
					if(lineSplit.length>5){
						String value2=XpathDummy.checkUrlXpath(url, lineSplit[5]).replaceAll("\n", "");//.replaceAll(" ", "");
						System.out.println(value2);
						sampleInnerElement2Data.put(url,value2);
					}
					//jsonattributeData.add(sampleInnerElementData);
				}
				jsonattributeData.add(sampleInnerElementData);
				jsonattribute2Data.add(sampleInnerElement2Data);
				jsonitemData.put(lineSplit[3], jsonattributeData);
				if(lineSplit.length>5){
					jsonitemData.put(lineSplit[6], jsonattributeData);
				}
				jsonsiteData.put(item, jsonitemData);
				objData.put(currentSite, jsonsiteData);

				//System.out.println("obj = "+objXpath.toString());
				//System.out.println("prima = "+line);
				line = br.readLine();
				//System.out.println("dopo = "+line);
				precSite=currentSite;
				currentSite=lineSplit[0];

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			FileWriter file = new FileWriter("xpath.massuda.json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = gson.toJson(objXpath);
			file.write(prettyJson+"\n");
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter file = new FileWriter("data.massuda.json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = gson.toJson(objData);
			file.write(prettyJson+"\n");
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeXpathJson(){
		try {
			String csvFile = "AGIW.tsv";
			String precSite="";
			String currentSite;
			FileWriter xpathFile = new FileWriter(PropertiesFile.getXpathCognome(),true);
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = "\t";
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();//salto descrizione
			line = br.readLine();
			xpathFile.write("{\n");
			while (line != null) {
				String[] lineSplit = line.split(cvsSplitBy);
				currentSite=lineSplit[0];
				if(!currentSite.equals(precSite)){
					precSite=currentSite;
					xpathFile.write("  \""+currentSite+"\": {\n");
				}
				String item=lineSplit[1].split("-",2)[1];
				String key=lineSplit[1];
				System.out.println(currentSite+" - "+key);
				xpathFile.write("    \""+item+"\": [\n");
				xpathFile.write("      {\n");
				xpathFile.write("        \"rule\": \""+lineSplit[2]+"\",\n");
				xpathFile.write("        \"attribute_name\": \""+lineSplit[3]+"\",\n");
				xpathFile.write("        \"page_id\": \""+lineSplit[4].toLowerCase()+"\"\n");
				if(lineSplit.length>5 && !lineSplit[5].equals("")){
					xpathFile.write("        \"rule\": \""+lineSplit[5]+"\",\n");
					xpathFile.write("        \"attribute_name\": \""+lineSplit[6]+"\",\n");
					xpathFile.write("        \"page_id\": \""+lineSplit[7].toLowerCase()+"\"\n");
				}
				if(lineSplit.length>8 && !lineSplit[8].equals("")){
					xpathFile.write("        \"rule\": \""+lineSplit[8]+"\",\n");
					xpathFile.write("        \"attribute_name\": \""+lineSplit[9]+"\",\n");
					xpathFile.write("        \"page_id\": \""+lineSplit[10].toLowerCase()+"\"\n");
				}
				xpathFile.write("      }\n");
				line = br.readLine();
				if(line!=null){
					precSite=currentSite;
					currentSite=line.split(cvsSplitBy)[0];
					if(precSite.equals(currentSite)){
						xpathFile.write("    ],\n");
					}
					else {
						xpathFile.write("    ]\n");
						xpathFile.write("  },\n");
					}
				}
				else{
					xpathFile.write("    ]\n");
					xpathFile.write("  }\n");
				}
			}
			xpathFile.write("}");
			xpathFile.flush();
			xpathFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeDataJson(){
		try {
			String csvFile = "AGIW.tsv";
			String precSite="";
			String currentSite;
			FileWriter dataFile = new FileWriter(PropertiesFile.getDataCognome(),true);
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = "\t";
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();//salto descrizione
			line = br.readLine();
			dataFile.write("{\n");
			while (line != null) {
				String[] lineSplit = line.split(cvsSplitBy);
				currentSite=lineSplit[0];
				if(!currentSite.equals(precSite)){
					precSite=currentSite;
					dataFile.write("  \""+currentSite+"\": {\n");
				}
				String item=lineSplit[1].split("-",2)[1];
				String key=lineSplit[1];
				System.out.println(currentSite+" - "+key);
				dataFile.write("    \""+item+"\": {\n");
				dataFile.write("      \""+lineSplit[3]+"\": [\n");
				List<String> urlList =JSONReadFromFile.urlList(PropertiesFile.getFile(), currentSite, key);
				for(String url:urlList){
					dataFile.write("        {\""+url+"\": \""+XpathDummy.checkUrlXpath(url, lineSplit[2]).replaceAll("\n", "")+"\"}\n");
				}
				if(lineSplit.length>5 && !lineSplit[5].equals("")){
					dataFile.write("      ],\n");
					dataFile.write("      \""+lineSplit[6]+"\": [\n");
					for(String url:urlList){
						dataFile.write("        {\""+url+"\": \""+XpathDummy.checkUrlXpath(url, lineSplit[5])+"\"}\n");
					}
				}
				if(lineSplit.length>8 && !lineSplit[8].equals("")){
					dataFile.write("      ],\n");
					dataFile.write("      \""+lineSplit[9]+"\": [\n");
					for(String url:urlList){
						dataFile.write("        {\""+url+"\": \""+XpathDummy.checkUrlXpath(url, lineSplit[8])+"\"}\n");
					}
				}
				dataFile.write("      ]\n");
				line = br.readLine();
				if(line!=null){
					precSite=currentSite;
					currentSite=line.split(cvsSplitBy)[0];
					if(precSite.equals(currentSite)){
						dataFile.write("    },\n");
					}
					else{
						dataFile.write("    }\n");
						dataFile.write("  },\n");
					}
				}
				else{
					dataFile.write("    }\n");
					dataFile.write("  }\n");
				}
			}
			dataFile.write("}");
			dataFile.flush();
			dataFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//da iniziare
	public static void writeDataJsonFromTwoCsv(){
		try {
			String csvFile = "AGIW.tsv";
			Map<String, List<String>> url2Codes = UrlToCodeWriter.putCsvIntoMap();
			String precSite="";
			String currentSite;
			FileWriter dataFile = new FileWriter(PropertiesFile.getDataCognome(),true);
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = "\t";
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();//salto descrizione
			line = br.readLine();
			dataFile.write("{\n");
			while (line != null) {
				String[] lineSplit = line.split(cvsSplitBy);
				currentSite=lineSplit[0];
				if(!currentSite.equals(precSite)){
					precSite=currentSite;
					dataFile.write("  \""+currentSite+"\": {\n");
				}
				String item=lineSplit[1].split("-",2)[1];
				String key=lineSplit[1];
				System.out.println(currentSite+" - "+key);
				dataFile.write("    \""+item+"\": {\n");
				dataFile.write("      \""+lineSplit[3]+"\": [\n");
				List<String> urlList =JSONReadFromFile.urlList(PropertiesFile.getFile(), currentSite, key);
				for(String url:urlList){
					dataFile.write("        {\""+url+"\": \""+url2Codes.get(url).toArray()[0].toString()+"\"}\n");
					//dataFile.write("        {\""+url+"\": \""+XpathDummy.checkUrlXpath(url, lineSplit[2]).replaceAll("\n", "")+"\"}\n");
				}
				if(lineSplit.length>5 && !lineSplit[5].equals("")){
					dataFile.write("      ],\n");
					dataFile.write("      \""+lineSplit[6]+"\": [\n");
					for(String url:urlList){
						dataFile.write("        {\""+url+"\": \""+url2Codes.get(url).toArray()[1].toString()+"\"}\n");
						//dataFile.write("        {\""+url+"\": \""+XpathDummy.checkUrlXpath(url, lineSplit[5])+"\"}\n");
					}
				}
				if(lineSplit.length>8 && !lineSplit[8].equals("")){
					dataFile.write("      ],\n");
					dataFile.write("      \""+lineSplit[9]+"\": [\n");
					for(String url:urlList){
						dataFile.write("        {\""+url+"\": \""+url2Codes.get(url).toArray()[2].toString()+"\"}\n");
						//dataFile.write("        {\""+url+"\": \""+XpathDummy.checkUrlXpath(url, lineSplit[8])+"\"}\n");
					}
				}
				dataFile.write("      ]\n");
				line = br.readLine();
				if(line!=null){
					precSite=currentSite;
					currentSite=line.split(cvsSplitBy)[0];
					if(precSite.equals(currentSite)){
						dataFile.write("    },\n");
					}
					else{
						dataFile.write("    }\n");
						dataFile.write("  },\n");
					}
				}
				else{
					dataFile.write("    }\n");
					dataFile.write("  }\n");
				}
			}
			dataFile.write("}");
			dataFile.flush();
			dataFile.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
