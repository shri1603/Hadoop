package com.netweb.hadoop;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Subscriber_CountMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {
	// private Text word = new Text();
	// private Text outputKey = new Text();
	static HashSet<String> hsdistinct = new HashSet<String>();

	SimpleDateFormat sdfout = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
	Calendar clnconvert = Calendar.getInstance();
	Date dt = new Date();

	// private final static IntWritable one = new IntWritable(1);

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] columns = line.split(",");
		String totalsubs = "";
		String distinctsubs = "";
		String strdate = "";

		try {
			dt = sdfout.parse(columns[0].toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		strdate = sdfout.format(dt);

		if (!columns[1].isEmpty()) {
			totalsubs = "Total Subscribers";
		}

		if (!hsdistinct.contains(columns[1])) {
			distinctsubs = "Distinct Subscribers";
			hsdistinct.add(columns[1]);
		}
		context.write(new Text(strdate + "#" + totalsubs), new IntWritable(1));
		context.write(new Text(strdate + "#" + columns[2] + "#" + columns[3]),
				new IntWritable(1));
		context.write(new Text(strdate + "#" + distinctsubs), new IntWritable(1));
		if (Integer.valueOf(columns[4]) > 500) {
			context.write(new Text(strdate + "#" + columns[3] + "#" + columns[4]+"#500"),
					new IntWritable(1));
		}

	}
}
