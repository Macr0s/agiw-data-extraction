

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 
 * @author Luca Massuda
 * Serve per leggere e scrivere un file di configurazione 
 * al fine di caricare il percorso dei file csv.
 */
public class PropertiesFile {

	public static String getFile(){
		String path = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			path=prop.getProperty("file");
			return path;
		} catch (IOException ex) {
			ex.printStackTrace();
			return path;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getSite(){
		String path = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			path=prop.getProperty("site");
			return path;
		} catch (IOException ex) {
			ex.printStackTrace();
			return path;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String getKey(){
		String path = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			path=prop.getProperty("key");
			return path;
		} catch (IOException ex) {
			ex.printStackTrace();
			return path;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
