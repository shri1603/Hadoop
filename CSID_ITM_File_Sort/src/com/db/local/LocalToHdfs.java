package com.db.local;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class LocalToHdfs {
	static String SLASH = "/";
	String DBFileName = "ItmtuResponsestatus.csv";

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
