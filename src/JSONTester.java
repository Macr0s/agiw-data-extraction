import java.io.BufferedReader;
import java.io.FileReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONTester {
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException {
		testDataCognome(PropertiesFile.getDataCognome(), PropertiesFile.getTSV());
		testXpathCognome(PropertiesFile.getXpathCognome(), PropertiesFile.getTSV());
	}

	public static void testXpathCognome(String fileXpathCognome, String fileTSV) {
		try {
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(fileTSV));
			JSONParser parser = new JSONParser();
			Object objXpathCognome = parser.parse(new FileReader(fileXpathCognome));
			line = br.readLine();

			while ((line = br.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String siteString = lineSplit[0];
				String keyString = lineSplit[1].split("-", 2)[1];
				JSONObject jsonObject = (JSONObject) objXpathCognome;
				JSONObject site = (JSONObject) jsonObject.get(siteString);
				JSONArray item = (JSONArray) site.get(keyString);

				if (keyString.equals("iPad Cleaning Kits")) {
					line = br.readLine();
				}

				if (keyString.equals("Winterizing")) {
					line = br.readLine();
					line = br.readLine();
				}

				System.out.println(siteString + "\t" + keyString + "= " + !item.isEmpty());

				if (item.isEmpty()) {
					break;
				}

			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void testDataCognome(String fileDataCognome, String fileTSV) {
		try {
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(fileTSV));
			JSONParser parser = new JSONParser();
			Object objDataCognome = parser.parse(new FileReader(fileDataCognome));
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String siteString = lineSplit[0];
				String keyString = lineSplit[1].split("-", 2)[1];
				JSONObject jsonObject = (JSONObject) objDataCognome;
				JSONObject site = (JSONObject) jsonObject.get(siteString);

				if (keyString.equals("iPad Cleaning Kits")) {
					line = br.readLine();
				}

				if (keyString.equals("Winterizing")) {
					line = br.readLine();
					line = br.readLine();
				}

				JSONObject item = (JSONObject) site.get(keyString);
				System.out.print(keyString + "= ");
				System.out.println(!item.isEmpty());

				if (item.isEmpty()) {
					break;
				}

			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
