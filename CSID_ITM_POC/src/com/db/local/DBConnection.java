package com.db.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DBConnection {

	String url = "jdbc:mysql://192.168.1.72:3306/";
	String dbName = "ITM_CSI_14AUG";
	String driver = "com.mysql.jdbc.Driver";
	String userName = "root";
	String password = "netweb12";

	/*
	 * static String url; static String dbName; static String driver; static
	 * String userName; static String password;
	 */

	String DBFileName = "ItmtuResponsestatus";
	// String localpath = "/home/FolderTest/";
	// String localpath = "D:/Shridhar/FolderTest/";

	HashSet hshour = new HashSet();
	HashSet hsday = new HashSet();
	HashSet hsMonth = new HashSet();
	HashSet hsYear = new HashSet();
	HashSet hsdate = new HashSet();

	int icurrentHour;
	int iNextHour = -1;

	int icurrentDay;
	int iNextDay = -1;

	int icurrentMonth;
	int iNextMonth = -1;

	int icurrentYear;
	int iNextYear = -1;

	//String strBasePathPER = "D:/Shridhar/FolderTest/NewApproach/";
	 String strBasePathPER = "/home/FolderTest/";
	static String SLASH = "/";

	String stryear;
	String strMonth;
	String strDay;
	String strHour;

	List<Integer> arr30 = new ArrayList<Integer>();
	List<Integer> arr31 = new ArrayList<Integer>();

	int[] i30 = { 4, 6, 9, 11 };

	static FileWriter fw = null;

	String strdate;
	String strtime;
	Date dt = null;
	Calendar cln = Calendar.getInstance();
	static Connection conn;
	Date dtcurrent = null;
	static int idatecondition = -1;
	Date dtprevious = null;

	File file = null;
	String filepath = "";
	Date date = null;
	Date time = null;
	int subscriber = 0;
	String status = "";
	String apimodule = "";
	String Path = "";
	long lnresponsetimeinmills = 0;

	long lnmin;
	long lnnext;

	int icountdatecheck = 0;
	
   




	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:00:00");

	SimpleDateFormat sdftime = new SimpleDateFormat("hh:mm:ss");
	
	SimpleDateFormat sdftimeformat = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws Exception {

		DBConnection dbconn = new DBConnection();
		
		
		while (true) {
			
			// dbconn.getPropValues();
			dbconn.DBConnection();
			conn.close();
			Thread.sleep(1000);
			System.out.println("Slept for 1 min");
		}
	}

	public void DBConnection() throws IOException {
		try {

			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			Statement st = conn.createStatement();
			ResultSet res;

			Calendar clncuur = Calendar.getInstance();

			Date dtmin = new Date();
			Date dtnext = new Date();

			Calendar clnmindate = Calendar.getInstance();
			Calendar clnnextdate = Calendar.getInstance();

			res = st.executeQuery("Select Count(*) from Date_Track");

			while (res.next()) {
				icountdatecheck = res.getInt(1);

			}
	
			if (icountdatecheck == 0) {
				res = st.executeQuery("SELECT MIN(ERRORDATE) from ITMTURESPONSESTATUS");
               
			}

			else

			{
				res = st.executeQuery("SELECT MAX(Date) from Date_Track");

			}
            
			while (res.next()) {
				dtmin = res.getDate(1);
				clnmindate.setTime(dtmin);
				lnmin = clnmindate.getTimeInMillis();
				lnnext = lnmin + (1000 * 60 * 60 * 24);
				clnnextdate.setTimeInMillis(lnnext);
				dtnext = clnnextdate.getTime();
				if(icountdatecheck != 0)
				{
					dtmin = dtnext;
				}
			}
            
			System.out.println("Select * from ITMTURESPONSESTATUS where ERRORDATE = '"
					+ sdftimeformat.format(dtmin) + "'");
			arr30.add(4); // Adding elements for the Month checking
			arr30.add(6);
			arr30.add(9);
			arr30.add(11);

			arr31.add(1);
			arr31.add(3);
			arr31.add(5);
			arr31.add(7);
			arr31.add(8);
			arr31.add(10);
			arr31.add(12);
			dt = new Date();
			System.out.println("Started Process");
			String strhr="";
			for(int ihour=0;ihour<24;ihour++)
			{
				if(ihour == 0 ||ihour == 1 || ihour == 2 || ihour == 3 || ihour == 5 ||ihour == 5 ||ihour == 6 ||ihour == 7 ||ihour == 8 ||ihour == 9 )
				{
				strhr = "0"+ihour+":00:00";
				}
				else
				{
					strhr = ihour+":00:00";
				}
				
			res = st.executeQuery("Select * from ITMTURESPONSESTATUS where ERRORDATE = '"
					+ sdftimeformat.format(dtmin) + "'" +"and ERRORTIME >='"+strhr+"'");

			while (res.next()) {

				date = res.getDate("ERRORDATE");
				time = res.getTime("ERRORTIME");
				subscriber = res.getInt("MEMID");
				status = res.getString("STATUS");
				apimodule = res.getString("APIMODULE");
				String strresponse = res.getString("RESPONSETIME");
				Date dttimelong = sdftime.parse(strresponse);
				lnresponsetimeinmills = dttimelong.getMinutes() * 60
						+ dttimelong.getSeconds();

				dt = sdf.parse(date.toString() + " " + time.getHours()
						+ ":00:00");

				cln.setTimeInMillis(dt.getTime());
				structureInput(dt, subscriber, status, apimodule,
						lnresponsetimeinmills);
			}
			}
			
			Statement stmt = conn.createStatement();
			System.out.println();
			stmt.executeUpdate("INSERT into Date_Track Values('"+sdftimeformat.format(dtmin)+"')");
		    stmt.close();
		    st.close();
		    res.close();
			System.out.println("Finished" +dtmin);

		} catch (Exception e) {
			e.printStackTrace();
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
	}

	private void structureInput(Date dt, int subscriber, String status,
			String apimodule, Long responsetime) throws IOException,
			ParseException {
		File file;
		String Path;
		if (!hsYear.contains(cln.get(Calendar.YEAR))) {
			icurrentYear = cln.get(Calendar.YEAR);
			if (iNextYear == -1) {
				iNextYear = icurrentYear + 1;
			}

			if (!hsMonth.contains(cln.get(Calendar.MONTH))) {
				icurrentMonth = cln.get(Calendar.MONTH);
				if (iNextMonth == -1) {
					iNextMonth = icurrentMonth + 1;
				}

				if (!hsday.contains(cln.get(Calendar.DAY_OF_MONTH))) {
					icurrentDay = cln.get(Calendar.DAY_OF_MONTH);
					if (iNextDay == -1) {
						iNextDay = icurrentDay + 1;
					}
					if (!hshour.contains(cln.get(Calendar.HOUR_OF_DAY))) {
						icurrentHour = cln.get(Calendar.HOUR_OF_DAY);
						if (iNextHour == -1) {
							iNextHour = icurrentHour + 1;
						}

						inputWriteout(dt, subscriber, status, apimodule,
								responsetime);
						if (icurrentHour >= iNextHour) {

							hshour.add(icurrentHour - 1);
							// objhdfs.copyFileToHDFS(Path);
							iNextHour = icurrentHour + 1;
							if (iNextHour == 24 || iNextHour == 25) {
								hshour.clear();
								hshour = new HashSet();
							}
						}
						dtprevious = dtcurrent;

					}
					if (icurrentDay >= iNextDay) {
						hsday.add(icurrentDay + 1);
						iNextDay = icurrentDay + 1;
						int iMonth = cln.get(Calendar.MONTH);

						if ((iNextDay == 29 && iMonth == 2)
								|| (iNextDay == 30 && iMonth == 2 && cln
										.get(Calendar.YEAR) % 4 == 0)
								|| (iNextDay == 31 && arr30.contains(iMonth))
								|| ((iNextDay == 32 && arr30.contains(iMonth)))) {
							hsday.clear();
							hsday = new HashSet();
						}
					}

				}
				if (icurrentMonth >= iNextMonth) {
					hsMonth.add(icurrentMonth - 1);
					iNextMonth = icurrentMonth + 1;
					if (iNextMonth == 13) {
						hsMonth.clear();
						hsMonth = new HashSet();
					}
				}
			}
			if (icurrentYear >= iNextYear) {
				hsYear.add(icurrentYear - 1);
				iNextYear = icurrentYear + 1;
				if (iNextYear == 2299) {
					hsYear.clear();
					hsYear = new HashSet();
				}
			}
		}
	}

	private void inputWriteout(Date dt, int subscriber, String status,
			String apimodule, Long responsetime) throws IOException,
			ParseException {
		File file;
		File file_finish;
		Path = strBasePathPER;
		// Path = checkIfDirectoryExists(dt);
		Calendar clnconvert = Calendar.getInstance();
		clnconvert.setTime(dt);
		file = new File(Path + DBFileName + "_" + clnconvert.get(Calendar.YEAR)
				+ "_" + clnconvert.get(Calendar.MONTH) + "_"
				+ clnconvert.get(Calendar.DAY_OF_MONTH) + "_"
				+ clnconvert.get(Calendar.HOUR_OF_DAY) + ".csv");
		if (!file.isFile()) {
			file.createNewFile();
		}
		fw = new FileWriter(file, true);
		String filepath = file.getAbsolutePath().toString();
		writeout(fw, dt, subscriber, status, apimodule, responsetime);
		fw.close();
		// file_finish = new File(Path + DBFileName + "_"
		// + clnconvert.get(Calendar.YEAR) + "_"
		// + clnconvert.get(Calendar.MONTH) + "_"
		// + clnconvert.get(Calendar.DAY_OF_MONTH) + "_"
		// + clnconvert.get(Calendar.HOUR_OF_DAY) + "_Finish");
		// if (!file_finish.isFile()) {
		// file_finish.createNewFile();
		// }

	}

	public void writeout(FileWriter fw, Date date, int subscriber,
			String status, String apimodule, Long responsetime)
			throws IOException, ParseException {
		fw.write(date.toString() + "," + subscriber + "," + status + ","
				+ apimodule + "," + responsetime + "\n");
	}

	private String checkIfDirectoryExists(Date messageDateTime) {

		StringBuilder fullFilePath = new StringBuilder(strBasePathPER)
				.append(SLASH);

		cln.setTimeInMillis(messageDateTime.getTime());
		String pathTillYear = fullFilePath.append(cln.get(Calendar.YEAR))
				.append(SLASH).toString();
		String pathTillMonth = fullFilePath.append(cln.get(Calendar.MONTH))
				.append(SLASH).toString();
		String pathTillDate = fullFilePath
				.append(cln.get(Calendar.DAY_OF_MONTH)).append(SLASH)
				.toString();
		String pathTillHour = fullFilePath
				.append(cln.get(Calendar.HOUR_OF_DAY)).append(SLASH).toString();

		File objFile = new File(pathTillYear);
		if (!objFile.exists()) {
			objFile.mkdir();// Create a directory with Year
			objFile = null;
			objFile = new File(pathTillMonth);
			objFile.mkdir(); // Create a directory with Year/Month
			objFile = null;
			objFile = new File(pathTillDate);
			objFile.mkdir();// Create a directory with Year/Month/Day
			objFile = null;
			objFile = new File(pathTillHour);
			objFile.mkdir(); // Create a directory with Year/Month/Day/Hour/
			objFile = null;
		} else {
			// Directory with Year already exists
			objFile = null;
			objFile = new File(pathTillMonth);
			if (!objFile.exists()) {
				objFile.mkdir(); // Create a directory with Year/Month
				objFile = null;
				objFile = new File(pathTillDate);
				objFile.mkdir();// Create a directory with Year/Month/Day
				objFile = null;
				objFile = new File(pathTillHour);
				objFile.mkdir(); // Create a directory with Year/Month/Day/Hour/
				objFile = null;
			} else {
				// Directory with Month already exists under Year directory
				objFile = null;
				objFile = new File(pathTillDate);
				if (!objFile.exists()) {
					objFile.mkdir();// Create a directory with Year/Month/Day
					objFile = null;
					objFile = new File(pathTillHour);
					objFile.mkdir(); // Create a directory with
					// Year/Month/Day/Hour/
					objFile = null;
				} else {
					// Directory with Day already exists under Year/Month/
					// directory
					objFile = null;
					objFile = new File(pathTillHour);
					if (!objFile.exists()) {
						objFile.mkdir(); // Create a directory with
						// Year/Month/Day/Hour/
						objFile = null;
					}
				}
			}
		}
		return fullFilePath.toString();
	}

}
