package com.db.local;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBDump {

	static String strBasePath = "/home/FolderTest/Output/";
	//static String strBasePath = "D:/Shridhar/FolderTest/Output";

	public static void main(String[] args) throws IOException,
			InterruptedException, SQLException {

		while (true) {
			DBase db = new DBase();
			File folder = new File(strBasePath);
			String strfilename;

			Connection conn = db.connect("jdbc:mysql://192.168.1.72:3306/ITM",
					"root", "netweb12");
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				
				if (listOfFiles[i].isDirectory())
					continue;
				strfilename = listOfFiles[i].getAbsolutePath();
			    db.importData(conn, strfilename);
			    
			    File afile = new File(strBasePath + listOfFiles[i].getName());
				afile.renameTo(new File(strBasePath + "/Dump/"
						+ afile.getName()));


			}
			Thread.sleep(1000);
			System.out.println("Sleeping for 1 sec");	
		}
		
	}

}

class DBase {
	public DBase() {
	}

	public Connection connect(String db_connect_str, String db_userid,
			String db_password) {
		Connection conn;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection(db_connect_str, db_userid,
					db_password);

		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}

		return conn;
	}

	public void importData(Connection conn, String filename) throws SQLException {
		Statement stmt;
		String query = "";
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		
			
			if (filename.contains("Out_It")) {

				query = "LOAD DATA LOCAL INFILE '" + filename
						+ "' INTO TABLE subscriber_count "
						+ "FIELDS TERMINATED BY ',' "
						+ "(@Date,Subscribercnt,APICalls) "
						+ "SET Date = STR_TO_DATE(@Date,'%Y-%c-%d %k:%i:%s');";

				System.out.println(query);
				stmt.executeUpdate(query);
			} else if (filename.contains("Out_Subsriber") && !filename.contains("Threshold")) {
				query = "LOAD DATA LOCAL INFILE '" + filename
						+ "' INTO TABLE API_count "
						+ "FIELDS TERMINATED BY ',' "
						+ "(@Date,APIName,APICalls) "
						+ "SET Date = STR_TO_DATE(@Date,'%Y-%c-%d %k:%i:%s');";
				System.out.println(query);
				stmt.executeUpdate(query);
				
			}

			else if (filename.contains("Threshold")) {
				query = "LOAD DATA LOCAL INFILE '" + filename
						+ "' INTO TABLE API_Mod_Threshold "
						+ "FIELDS TERMINATED BY ',' "
						+ "(@Date,APIModule,Count) "
						+ "SET Date = STR_TO_DATE(@Date,'%Y-%c-%d %k:%i:%s');";
				System.out.println(query);
				stmt.executeUpdate(query);
				
		

		} 

			}
}
