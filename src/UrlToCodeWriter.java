import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlToCodeWriter {
	public static void main(String args[]) {
		UrlToCode();
		/*Map<String, List<String>> url2Codes = putCsvIntoMap();
		System.out.println(url2Codes.keySet().size());
		System.out.println(url2Codes.get("http://www.focusedtechnology.com/william-sound.html"));
		*/
	}

	//mappa usata per andare pi� veloci in fase di ricerca url
	public static Map<String, List<String>> putCsvIntoMap() {
		Map<String, List<String>> url2Codes = new HashMap<String, List<String>>();
		try {
			String line = "";
			String cvsSplitBy = ",";
			String csvFile = PropertiesFile.getUrlToCode();
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] lineSplit = line.split(cvsSplitBy);
				List<String> codeList = new ArrayList<String>();//deve mantenere ordinamento con cui li inserisco
				for (int i = 1; i < lineSplit.length; i++) {
					codeList.add(lineSplit[i].replaceAll("   ", ""));
				}
				url2Codes.put(lineSplit[0], codeList);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url2Codes;
	}

	// preso il csv scrive un altro csv con i link e i vari codici
	public static void UrlToCode() {
		try {
			String value = "";
			String csvFile = PropertiesFile.getTSV();
			String currentSite;
			FileWriter dataFile = new FileWriter(PropertiesFile.getUrlToCode(), true);
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			String line = "";
			String cvsSplitBy = "\t";
			line = br.readLine();//salto descrizione
			while ((line = br.readLine()) != null) {
				String completeLine = "";
				String[] lineSplit = line.split(cvsSplitBy);
				currentSite = lineSplit[0];
				String key = lineSplit[1];

				List<String> urlList = JSONReadFromFile.urlList(PropertiesFile.getFile(), currentSite, key);
				//System.out.println(currentSite+" "+key+urlList.toString());
				for (String url : urlList) {
					completeLine = url;
					int i = 2;
					while (i < lineSplit.length && !lineSplit[i].equals("")) {
						if (isPageProduct(lineSplit[i + 2])) {
							value = XpathDummy.cleanerString(url, lineSplit[i]);//riga che fa il controllo
							completeLine = completeLine + "\t" + value.replaceAll("\n", "");
						}
						i = i + 3;
					}
					System.out.println(completeLine);
					dataFile.write(completeLine + "\n");
				}
			}
			dataFile.flush();
			dataFile.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPageProduct(String pageProductField) {
		return pageProductField.toLowerCase().equals("true") || pageProductField.toLowerCase().equals("1");
	}
}
