package com.local.hdfs;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class LocalToHdfs {
	
	static String SLASH = "/";
	static String localpath = "/home/FolderTest/";
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		LocalToHdfs objlocal=new LocalToHdfs();
		File folder = new File(localpath);
		while(true)
		{
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory())
					continue;
			objlocal.copyFileToHDFS(listOfFiles[i].getAbsolutePath().toString());
			File afile = new File(localpath+listOfFiles[i].getName());
			afile.renameTo(new File(localpath+"/Processed/" + afile.getName()));
			}
			Thread.sleep(5000);
			System.out.println("No New File To Copy");
		}
	}

	public void copyFileToHDFS(String localpath) throws IOException {

		Configuration conf = new Configuration();
		conf.addResource(new Path("/home/hadoop/etc/hadoop/core-site.xml"));
		conf.addResource(new Path("/home/hadoop/etc/hadoop/hdfs-site.xml"));
		FileSystem hdfs = FileSystem.get(new Configuration(conf));
		Path newFolderPath = new Path(localpath);
		if (hdfs.exists(newFolderPath)) {
			hdfs.delete(newFolderPath, true); // Delete existing Directory
		}
		hdfs.mkdirs(newFolderPath);
		Path localFilePath = new Path(localpath);
		hdfs.copyFromLocalFile(localFilePath, newFolderPath);
	}

}
