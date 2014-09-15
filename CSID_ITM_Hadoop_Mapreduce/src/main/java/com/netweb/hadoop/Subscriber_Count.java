package com.netweb.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.lib.MultipleOutputs;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Subscriber_Count {

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {

		Configuration conf = new Configuration();

		Job job = new Job(conf);

		job.setJobName("Subscriber_Analytics");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Subscriber_CountMapper.class);
		job.setReducerClass(Subscriber_CountReducer.class);

		MultipleOutputs.addNamedOutput(job, "countbyapisuccess",
				TextOutputFormat.class, Text.class, IntWritable.class);
		MultipleOutputs.addNamedOutput(job, "countbysubscriber",
				TextOutputFormat.class, Text.class, IntWritable.class);
		MultipleOutputs.addNamedOutput(job, "countbythresholdapi",
				TextOutputFormat.class, Text.class, IntWritable.class);
		MultipleOutputs.addNamedOutput(job, "default", TextOutputFormat.class,
				Text.class, IntWritable.class);

		job.setJarByClass(Subscriber_CountMapper.class);
		job.setJarByClass(Subscriber_CountReducer.class);

		TextInputFormat.setInputPaths(job, args[0]);
		TextOutputFormat.setOutputPath(job, new Path(args[1]));
		System.out.println("Job About to start");
		job.waitForCompletion(true);
		System.out.println("Job End");

	}
}