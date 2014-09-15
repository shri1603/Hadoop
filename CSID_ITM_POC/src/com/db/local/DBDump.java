package com.db.local;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class DBDump {

	static String strBasePath = "D:/Shridhar/FolderTest/Output/";
	
	
	public static void main(String[] args) throws IOException {
		DBase db = new DBase();
		File folder = new File(strBasePath);
		//String strfilename ;
		
		Connection conn = db.connect("jdbc:mysql://192.168.1.72:3306/ITM",
				"root", "netweb12");
		File[] listOfFiles = folder.listFiles();
	    
		for (int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isDirectory())
				continue;
//		   strfilename = listOfFiles[i].getAbsolutePath();
//		   System.out.println(strfilename);
//		   
		}
		String strfilename = "D:\\Shridhar\\FolderTest\\Output\\Out_ItmtuResponsestatus_2012_1_18_15.csv";
				   db.importData(conn, strfilename);
		
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

	public void importData(Connection conn, String filename) {
		Statement stmt;
		String query = "";

		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
            if(filename.contains("Out_It"))
            {
			query = "LOAD DATA LOCAL INFILE '" + filename
					+ "' INTO TABLE subscriber_count FIELDS TERMINATED BY ','  LINES TERMINATED BY '\n ';";
            }
//            else
//            {
//            	query = "LOAD DATA LOCAL INFILE '" + filename
//    					+ "' INTO TABLE API_count FIELDS FILEDS TERMINATED BY ','  LINES TERMINATED BY '\n '; ";
//            }
            System.out.println(query);
            
			stmt.executeUpdate(query);

		} catch (Exception e) {
			e.printStackTrace();
			stmt = null;
		}
	}
};
