import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

class JSONWriter {
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException {
		writeXpathJson();
		writeDataJsonFromTwoCsv();
	}
	
	public static void writeXpathJson(){
		System.out.println("### Writing XPath file ###");
		try {
			String tsvFile = PropertiesFile.getTSV();
			String precSite="";
			String currentSite;
			FileWriter xpathFile = new FileWriter(PropertiesFile.getXpathCognome(),false);
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = "\t";
			br = new BufferedReader(new FileReader(tsvFile));
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
				int i=5;
				while (i<lineSplit.length && !lineSplit[i].equals("")){
					xpathFile.write("      },\n");
					xpathFile.write("      {\n");
				    xpathFile.write("        \"rule\": \""+lineSplit[i]+"\",\n");
				    i++;
					xpathFile.write("        \"attribute_name\": \""+lineSplit[i]+"\",\n");
					i++;
					xpathFile.write("        \"page_id\": \""+lineSplit[i].toLowerCase()+"\"\n");
					i++;	
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
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeDataJsonFromTwoCsv(){
		System.out.println("### Writing Data file ###");
		try {
			String tsvFile = PropertiesFile.getTSV();
			Map<String, List<String>> url2Codes = UrlToCodeWriter.putCsvIntoMap();
			String precSite="";
			String currentSite;
			FileWriter dataFile = new FileWriter(PropertiesFile.getDataCognome(),false);
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = "\t";
			br = new BufferedReader(new FileReader(tsvFile));
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
				
				dataFile.write("    \""+item+"\": {\n");

				List<String> urlList =JSONReadFromFile.urlList(PropertiesFile.getFile(), currentSite, key);
				
				// creo la lista di attributi a true
				List<String> trueAttributeList = new LinkedList<String>();

				int i = 3;
				while (i < lineSplit.length) {
					if (lineSplit[i].equals("TRUE") || lineSplit[i].equals("1")) {
						trueAttributeList.add(lineSplit[i - 1]);
					}
					i++;
				}
				
				int attributePosition = 0;
				for (String trueAttribute : trueAttributeList) {
					dataFile.write("      \"" + trueAttribute + "\": [\n");
					int lastUrl=1;
					for (String url : urlList) {
						
						List<String> attributeArray = url2Codes.get(url);
						
						System.out.println(currentSite+" - "+key+" - "+url);
						if (attributeArray==null || attributeArray.size() == 0) {
							if(attributeArray==null)
							System.out.println("null o 0");
							dataFile.write("        {\"" + url+ "\": \"\"");
						} else {
							if(attributeArray.size()>attributePosition)
								dataFile.write("        {\"" + url + "\": \"" + attributeArray.get(attributePosition) + "\"");
							else
								dataFile.write("        {\"" + url + "\": \"\"");
						}
						if(lastUrl==urlList.size()){
							dataFile.write("}\n");
						}
						else {
							dataFile.write("},\n");
						}
						lastUrl++;
					}
					
					if(attributePosition != trueAttributeList.size() - 1) {
						dataFile.write("      ],\n");
					} else {
						dataFile.write("      ]\n");
					}
					attributePosition++;
				}
			
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
