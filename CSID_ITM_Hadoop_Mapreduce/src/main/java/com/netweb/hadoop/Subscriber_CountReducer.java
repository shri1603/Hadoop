package com.netweb.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class Subscriber_CountReducer extends
		Reducer<Text, IntWritable, Text, IntWritable> {

	private MultipleOutputs mos;
	private IntWritable outputValue = new IntWritable();

	public void setup(Context context) {
		mos = new MultipleOutputs(context);
	}

	protected void reduce(Text key, Iterable<IntWritable> values,
			Context context) throws IOException, InterruptedException {

		if (key.toString().contains("#Success#")
				|| key.toString().contains("#Error#")
				|| key.toString().contains("#Failure#")
				|| key.toString().contains("#ServiceError#")) {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			outputValue.set(sum);
			mos.write("countbyapisuccess",key,outputValue);
		}

		else if ((key.toString().contains("Total Subscribers"))
				|| (key.toString().contains("Distinct Subscribers"))) {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			outputValue.set(sum);
			mos.write("countbysubscriber",key,outputValue);
		}

		else if (key.toString().endsWith("500")) {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			outputValue.set(sum);
			mos.write("countbythresholdapi",key,outputValue);
		}

		else {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			outputValue.set(sum);
			mos.write("default",key,outputValue);
		}

	}

	public void cleanup(Context context) throws IOException,
			InterruptedException {
		mos.close();
	}

}
