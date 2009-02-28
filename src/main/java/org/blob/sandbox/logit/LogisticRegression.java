package org.blob.sandbox.logit;

/**
 * User: alek
 * Date: Feb 28, 2009
 * Time: 3:05:28 AM
 *
 * Logistic Regression using hadoop prototype
 *
 */


import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.blob.sandbox.misc.Util;


/*
 *  prototype - R-style
 *  val = f(predictors)
 *
 */


public class LogisticRegression {

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, IntWritable> {

        public void map(LongWritable key, Text value, OutputCollector<LongWritable, IntWritable> output, Reporter reporter)
                throws IOException {

        }

    }

    public static class Reduce extends MapReduceBase implements Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {
        public void reduce(LongWritable key, Iterator<IntWritable> values, OutputCollector<LongWritable, IntWritable> output, Reporter reporter)
                throws IOException {


        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);

            String tmpDir = Util.getHDFSTemporaryDir(fs);
            String sourceDir = "source/" + tmpDir;
            String resultDir = "final/" + tmpDir;

            Util.loadInputToHDFS(fs, args[0], sourceDir);
            Util.runJob("test", Map.class, Reduce.class, sourceDir, tmpDir);
            Util.deleteTemporaryFiles(fs, tmpDir);

        } else {
            System.out.println("usage: LogisticRegression [input]");
        }

    }


}

