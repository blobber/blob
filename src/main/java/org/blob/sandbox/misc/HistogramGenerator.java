package org.blob.sandbox.misc;

/**
 * User: alek
 * Date: Feb 14, 2009
 * Time: 11:54:19 PM
 *
 * Simple histogram generator  
 *
 */

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class HistogramGenerator {

    private static String   FIELD_DELIMITER = "\t";     // file field delimiter
    private static int      MILLISECOND_WINDOW = 1000;  // windowing interval    

    // calculating frequency of each key
    public static class FreqMap extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, IntWritable> {
        public void map(LongWritable key, Text value, OutputCollector<LongWritable, IntWritable> output, Reporter reporter)
                       throws IOException {
            String line = value.toString();
            String[] parts = line.split(FIELD_DELIMITER);
            if (parts.length > 0) {
                String keyText = parts[0];
                if (Util.isTimestamp(keyText)) {
                    try {
                        // map timestamps into buckets of MILLISECOND_WINDOW size
                        Long keyVal = (Long.parseLong(keyText))/MILLISECOND_WINDOW;
                        output.collect(new LongWritable(keyVal),new IntWritable(1));
                    } catch (NumberFormatException e) {}
                }
            }
        }
    }

    // getting the histogram
    public static class HistogramMap extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, IntWritable> {
        public void map(LongWritable key, Text value, OutputCollector<LongWritable, IntWritable> output, Reporter reporter)
                       throws IOException {
            String line = value.toString();
            String[] parts = line.split(FIELD_DELIMITER);
            if (parts.length > 1) {
                String keyText = parts[1];
                if (Util.isLong(keyText))
                    output.collect(new LongWritable(Long.parseLong(keyText)),new IntWritable(1));
            }
        }
    }

    // summing per-key frequencies
    public static class Reduce extends MapReduceBase implements Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {

        public void reduce(LongWritable key, Iterator<IntWritable> values, OutputCollector<LongWritable, IntWritable> output, Reporter reporter)
                throws IOException {

            int sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }
            output.collect(key, new IntWritable(sum));
        }
    }

    // main
    public static void main(String[] args) throws Exception {

        if (args.length == 1) {

            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            
            String tmpDir = Util.getHDFSTemporaryDir(fs);
            String sourceDir = "source/" + tmpDir;
            String resultDir = "final/" + tmpDir;

            Util.loadInputToHDFS(fs,args[0], sourceDir);

            Util.runJob("get_key_freq", FreqMap.class, Reduce.class, sourceDir,tmpDir);
            Util.runJob("get_histogram", HistogramMap.class, Reduce.class, tmpDir, resultDir);

            System.out.println("================================");
            System.out.println("histogram: ");
            System.out.println("================================");
            Util.getDataFromHDFS(fs,(resultDir + "/part-00000"));
            Util.deleteTemporaryFiles(fs,tmpDir);

        } else {
            System.out.println("usage: HistogramGenerator [input]");
        }

    }
    
}