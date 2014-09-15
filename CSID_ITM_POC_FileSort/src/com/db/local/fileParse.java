package com.db.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


public class fileParse {

//	String strBasePathPER = "D:/Shridhar/FolderTest/";
//	String strOutPath = "D:/Shridhar/FolderTest/Output/";
//	String strrenamePath = "D:/Shridhar/FolderTest/Processed/";

//	 String strBasePathPER = "/home/FolderTest/";
//	
//	 String strOutPath = "/home/FolderTest/Output/";
//	
//	 String strrenamePath = "/home/FolderTest/Processed//";
	 

    

	SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

	static FileWriter fw = null;
	static FileWriter fw_Api = null;
	static FileWriter fw_Threshold_Api = null;

	Date dt = new Date();
	int iSubscribercnt;
	int iAPISuccsess;
	int iAllApi;
	int iThresholdApi;
    long lntime=0l;
	Calendar clnconvert = Calendar.getInstance();
	
	SimpleDateFormat sdfout = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

	
	String strBasePathPER = PropUtil.getPropertyValue(PropUtil.INPUT_FILE_PATH);
	String strOutPath = PropUtil.getPropertyValue(PropUtil.OUTPUT_FILE_PATH);
	String strrenamePath = PropUtil.getPropertyValue(PropUtil.RENAME_FILE_PATH);
	
	File folder = new File(strBasePathPER);

	Map<Long, Integer> hmsubscriber = new HashMap<Long, Integer>();
	Map<Long, Integer> hmApicalls = new HashMap<Long, Integer>();

	Map<String, Integer> hmAllApi = new HashMap<String, Integer>();

	Map<Long, HashMap<String, Integer>> hmAPI = new HashMap<Long, HashMap<String, Integer>>();

	Map<String, Integer> hmThresholdApi = new HashMap<String, Integer>();

	Map<Long, HashMap<String, Integer>> hmThresholdAPI = new HashMap<Long, HashMap<String, Integer>>();
	
	public static Properties propertyFile = new Properties();

	public static void main(String[] args) throws IOException, ParseException,
			InterruptedException {
		// TODO Auto-generated method stub
		
		while (true) {
			fileParse objfile = new fileParse();
			objfile.parse();
			Thread.sleep(60000);
			System.out.println("Slept for 1 min as no files available");

		}
	}

	public void parse() throws IOException, ParseException {
		
  
		File[] listOfFiles = folder.listFiles();
		BufferedReader frfile;
		
		
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory())
				continue;
			frfile = new BufferedReader(new FileReader(strBasePathPER
					+ listOfFiles[i].getName()));
			Date dtFile = new Date();
			
			String currline;
			while ((currline = frfile.readLine()) != null) {
				// System.out.println(currline.split(",")[0]);
				dtFile = sdf.parse(currline.split(",")[0]);
				lntime=dtFile.getTime();
				// dtFile = new Date(currline.split(",")[0]);

				// Subscriber Count for the hour
				if (hmsubscriber.containsKey(lntime)) {
					iSubscribercnt = (Integer) hmsubscriber.get(lntime) + 1;
					hmsubscriber.put(lntime, iSubscribercnt);
				} else {
					iSubscribercnt = 1;
					hmsubscriber.put(lntime, iSubscribercnt);

				}

				// Api Calls

				if (hmApicalls.containsKey(lntime)) {
					iAPISuccsess = (Integer) hmApicalls.get(lntime) + 1;
					hmApicalls.put(lntime, iAPISuccsess);
				} else {
					iAPISuccsess = 1;
					hmApicalls.put(lntime, iAPISuccsess);
				}

				// All the Api calls
				if (hmAllApi.containsKey(currline.split(",")[3])) {
					iAllApi = hmAllApi.get(currline.split(",")[3]) + 1;
					hmAllApi.put(currline.split(",")[3], iAllApi);
				} else {
					iAllApi = 1;
					hmAllApi.put(currline.split(",")[3], iAllApi);
				}

				if (Long.valueOf(currline.split(",")[4]) > 120) {
					// above a Certain value of response time
					if (hmThresholdApi.containsKey(currline.split(",")[3])) {
						iThresholdApi = hmThresholdApi
								.get(currline.split(",")[3]) + 1;
						hmThresholdApi.put(currline.split(",")[3],
								iThresholdApi);
					} else {
						iThresholdApi = 1;
						hmThresholdApi.put(currline.split(",")[3],
								iThresholdApi);
					}
				}

			}

			hmAPI.put(lntime, (HashMap<String, Integer>) hmAllApi);
			hmThresholdAPI.put(lntime,
					(HashMap<String, Integer>) hmThresholdApi);

			File fileout = new File(strOutPath + "Out_"
					+ listOfFiles[i].getName());
			if (!fileout.isFile()) {
				fileout.createNewFile();
			}
			fw = new FileWriter(fileout, true);
			Iterator<Entry<Long, Integer>> it = totalCount();

			File fileout_sub = new File(strOutPath + "Out_Subsriber_"
					+ listOfFiles[i].getName());
			if (!fileout_sub.isFile()) {
				fileout_sub.createNewFile();
			}
			fw_Api = new FileWriter(fileout_sub, true);
			Iterator<Entry<Long, HashMap<String, Integer>>> it_sub = totalIndividualAPI();

			File fileout_sub_threshold = new File(strOutPath
					+ "Out_Subsriber_Threshold_" + listOfFiles[i].getName()
					);
			if (!fileout_sub_threshold.isFile()) {
				fileout_sub_threshold.createNewFile();
			}
			fw_Threshold_Api = new FileWriter(fileout_sub_threshold, true);
			Iterator<Entry<Long, HashMap<String, Integer>>> it_sub_threshold = ThresholdIndividualAPI();

			fw.close();
			hmsubscriber.clear();
			hmApicalls.clear();
			frfile.close();
			fw_Api.close();
			fw_Threshold_Api.close();
			hmAllApi.clear();
			hmAPI.clear();
			hmThresholdApi.clear();
			hmThresholdAPI.clear();
			File afile = new File(strBasePathPER + listOfFiles[i].getName());
			afile.renameTo(new File(strrenamePath + afile.getName()));

		}
		System.out.println("Finished");
	}

	private Iterator<Entry<Long, Integer>> totalCount() throws IOException, ParseException {
		Iterator<Entry<Long, Integer>> it = hmsubscriber.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mpsuscriber = (Map.Entry) it.next();

			int iVal = (Integer) mpsuscriber.getValue();
			// System.out.println(strKey+","+iVal);
			/*Date dt = new Date((Long)mpsuscriber.getKey());
			clnconvert.setTime(dt);*/
			clnconvert.setTimeInMillis((Long)mpsuscriber.getKey());

			fw.write(sdfout.format(clnconvert.getTime())+"," + iVal + ","
					+ hmApicalls.get(mpsuscriber.getKey()));
			/*System.out.println(sdfout.format(clnconvert.getTime())+"," + iVal + ","
					+ hmApicalls.get(mpsuscriber.getKey()));*/
		}
		return it;
	}

	private Iterator<Entry<Long, HashMap<String, Integer>>> totalIndividualAPI()
			throws IOException, ParseException {
		Iterator<Entry<Long, HashMap<String, Integer>>> it = hmAPI.entrySet()
				.iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mpsuscriber = (Map.Entry) it.next();

			HashMap hm = (HashMap) mpsuscriber.getValue();
			Iterator<Entry<String, Integer>> entry = hm.entrySet().iterator();
			while (entry.hasNext()) {
				Map.Entry mpapi = (Map.Entry) entry.next();
//				Date dt = new Date((Long)mpsuscriber.getKey());
//				clnconvert.setTime(dt);
				clnconvert.setTimeInMillis((Long)mpsuscriber.getKey());
	
                    
				fw_Api.write(sdfout.format(clnconvert.getTime())+"," + mpapi.getKey() + ","
						+ hm.get(mpapi.getKey()));
				fw_Api.write("\n");
			}
		}
		return it;
	}

	private Iterator<Entry<Long, HashMap<String, Integer>>> ThresholdIndividualAPI()
			throws IOException, ParseException {
		Iterator<Entry<Long, HashMap<String, Integer>>> it = hmThresholdAPI
				.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mpsuscriber = (Map.Entry) it.next();

			HashMap hm = (HashMap) mpsuscriber.getValue();
			Iterator<Entry<String, Integer>> entry = hm.entrySet().iterator();
			while (entry.hasNext()) {
				Map.Entry mpapi = (Map.Entry) entry.next();
				
				clnconvert.setTimeInMillis((Long)mpsuscriber.getKey());
//				Date dt = new Date((Long)mpsuscriber.getKey());
			
				
				//clnconvert.setTime(dt);
				/*String ktime= clnconvert.get(Calendar.YEAR)+"-"+clnconvert.get(Calendar.MONTH)+"-"+clnconvert.get(Calendar.DATE)+" "+clnconvert.get(Calendar.HOUR_OF_DAY)
						+":"+clnconvert.get(Calendar.MINUTE)+":"+clnconvert.get(Calendar.SECOND);*/
				fw_Threshold_Api.write(sdfout.format(clnconvert.getTime()) + ","
						+ mpapi.getKey() + "," + hm.get(mpapi.getKey()));
				fw_Threshold_Api.write("\n");
			}
		}
		return it;
	}

}
