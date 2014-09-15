/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.db.local;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropUtil {

	private static final Properties prop;
	public static final String INPUT_FILE_PATH = "strBasePathPER";
	public static final String OUTPUT_FILE_PATH = "strOutPath";
	public static final String RENAME_FILE_PATH ="strrenamePath";
	
	
	static{
		InputStream is = null;
		prop = new Properties();
		try {
			is = ClassLoader.class.getResourceAsStream("/configuration.properties");
			prop.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getPropertyValue(String key){
		return prop.getProperty(key);
	}
	
	
}
