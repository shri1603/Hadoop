package com.db.local;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigRead {

	static String url;
	static String dbname;
	static String driver;
	static String userName;
	static String password;

	public void getPropValues() throws IOException {
		Properties prop = new Properties();
		String propFileName = "\\Resources\\configuration.properties";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);
		prop.load(inputStream);
		if (inputStream == null) {
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}
		url = prop.getProperty("url");
		dbname = prop.getProperty("dbName");
		driver = prop.getProperty("driver");
		userName = prop.getProperty("userName");
		password = prop.getProperty("password");
	}

	public static String geturl() {
		return url;
	}

	public static String getdbname() {
		return dbname;
	}

	public static String getdriver() {
		return driver;
	}

	public static String getuserName() {
		return userName;
	}

	public static String getpassword() {
		return password;
	}

}
