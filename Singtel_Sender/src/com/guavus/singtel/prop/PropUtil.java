/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guavus.singtel.prop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author shridhar
 */
public final class PropUtil {

	private static final Properties prop;
	public static final String GnGi_INPUT_FILE_PATH = "gnginputpath";
	public static final String GnGi_OUTPUT_FILE_PATH = "gngoutputpath";
	public static final String GnGi_IP="ip";
	public static final String GnGi_Port = "port";
	
	static{
		InputStream is = null;
		prop = new Properties();
		try {
			is = ClassLoader.class.getResourceAsStream("/Config.properties");
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
