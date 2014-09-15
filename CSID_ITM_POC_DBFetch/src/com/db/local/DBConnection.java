package com.db.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class DBConnection {

	// String url = "jdbc:mysql://192.168.1.72:3306/";
	// String dbName = "ITM_CSI_14AUG";
	// String driver = "com.mysql.jdbc.Driver";
	// String userName = "root";
	// String password = "netweb12";

	String url;
	String dbName;
	String driver;
	String userName;
	String password;

	String DBFileName = "ItmtuResponsestatus";
	String strBasePathPER;

	String status = "";
	String apimodule = "";
	String strresponse;
	String filepath = "";

	int subscriber = 0;
	int icountdatecheck = 0;

	static FileWriter fw = null;
	static Connection conn;
	static String SLASH = "/";

	Statement st;
	Statement stmt;

	File file = null;

	long lnmincompare;
	long lnmaxdaily;
	long lnresponsetimeinmills = 0;
	long lnmin;
	long lnnext;

	Date dtmin = new Date();
	Date dtnext = new Date();
	Date dtmaxdaily = new Date();
	Date dt = new Date();
	Date date = new Date();
	Date time = new Date();
	Date dttimelong = new Date();

	Calendar cln = Calendar.getInstance();
	Calendar clnmindate = Calendar.getInstance();
	Calendar clnnextdate = Calendar.getInstance();
	Calendar clncuur = Calendar.getInstance();
	Calendar clnresponsetime = Calendar.getInstance();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:00:00");
	SimpleDateFormat sdftime = new SimpleDateFormat("hh:mm:ss");
	SimpleDateFormat sdftimeformat = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws Exception {

		DBConnection dbconn = new DBConnection();
		while (true) {
			dbconn.getPropValues();
			dbconn.DBConnection();
			conn.close();
			Thread.sleep(1000);
			System.out.println("Slept for 1 sec");
		}
	}

	public void DBConnection() throws IOException {
		try {

			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			stmt = conn.createStatement();
			ResultSet res;
			res = st.executeQuery("SELECT max(ERRORDATE) from ITMTURESPONSESTATUS"); // MAX
																						// DATE
			while (res.next()) {
				dtmaxdaily = res.getDate(1);
			}
			res = st.executeQuery("Select Count(*) from Date_Track"); // Date
																		// tracker
																		// for
																		// already
																		// processed
																		// records
			while (res.next()) {
				icountdatecheck = res.getInt(1);
			}
			if (icountdatecheck == 0) {
				res = st.executeQuery("SELECT MIN(ERRORDATE) from ITMTURESPONSESTATUS");
			} else {
				res = st.executeQuery("SELECT MAX(Date) from Date_Track");
			}

			while (res.next()) {
				dtmin = res.getDate(1);
				clnmindate.setTime(dtmin);
				lnmin = clnmindate.getTimeInMillis();
				lnnext = lnmin + (1000 * 60 * 60 * 24);
				clnnextdate.setTimeInMillis(lnnext);
				dtnext = clnnextdate.getTime();
				if (icountdatecheck != 0) {
					dtmin = dtnext;
				}
			}

			dt = new Date();
			System.out.println("Started Process");
			parseDBdata(st, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseDBdata(Statement st, ResultSet res) throws SQLException,
			ParseException, IOException {
		String strhr;
		if (dtmin.getTime() <= dtmaxdaily.getTime()) {
			for (int ihour = 0; ihour < 24; ihour++) {
				if (ihour == 0 || ihour == 1 || ihour == 2 || ihour == 3
						|| ihour == 5 || ihour == 5 || ihour == 6 || ihour == 7
						|| ihour == 8 || ihour == 9) {
					strhr = "0" + ihour + ":00:00";
				} else {
					strhr = ihour + ":00:00";
				}

				res = st.executeQuery("Select * from ITMTURESPONSESTATUS where ERRORDATE = '"
						+ sdftimeformat.format(dtmin)
						+ "'"
						+ "and ERRORTIME >='" + strhr + "'");

				while (res.next()) {

					date = res.getDate("ERRORDATE");
					time = res.getTime("ERRORTIME");
					subscriber = res.getInt("MEMID");
					status = res.getString("STATUS");
					apimodule = res.getString("APIMODULE");
					strresponse = res.getString("RESPONSETIME");
					dttimelong = sdftime.parse(strresponse);

					clnresponsetime.setTime(dttimelong);
					lnresponsetimeinmills = clnresponsetime
							.get(Calendar.MINUTE)
							* 60
							+ clnresponsetime.get(Calendar.SECOND);
					dt = sdf.parse(date.toString() + " " + sdftime.format(time));
			
					cln.setTimeInMillis(dt.getTime());

					inputWriteout(dt, subscriber, status, apimodule,
							lnresponsetimeinmills);
				}
			}

			stmt.executeUpdate("INSERT into Date_Track Values('"
					+ sdftimeformat.format(dtmin) + "')");
			stmt.close();
			st.close();
			res.close();
			System.out.println("Finished" + dtmin);

		}
	}

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
		url = prop.getProperty("url").trim();
		dbName = prop.getProperty("dbName").trim();
		driver = prop.getProperty("driver").trim();
		userName = prop.getProperty("userName").trim();
		password = prop.getProperty("password").trim();
		strBasePathPER = prop.getProperty("strBasePathPER");
	}

	private void inputWriteout(Date dt, int subscriber, String status,
			String apimodule, Long responsetime) throws IOException,
			ParseException {
		
		Calendar clnconvert = Calendar.getInstance();
		clnconvert.setTime(dt);
		file = new File(strBasePathPER + DBFileName + "_" + clnconvert.get(Calendar.YEAR)
				+ "_" + clnconvert.get(Calendar.MONTH) + "_"
				+ clnconvert.get(Calendar.DAY_OF_MONTH) + "_"
				+ clnconvert.get(Calendar.HOUR_OF_DAY) + ".csv");
		if (!file.isFile()) {
			file.createNewFile();
		}
		fw = new FileWriter(file, true);
		// filepath = file.getAbsolutePath().toString();
		writeout(fw, dt, subscriber, status, apimodule, responsetime);
		fw.close();
	}

	public void writeout(FileWriter fw, Date date, int subscriber,
			String status, String apimodule, Long responsetime)
			throws IOException, ParseException {
		fw.write(date.toString() + "," + subscriber + "," + status + ","
				+ apimodule + "," + responsetime + "\n");
	}

}
