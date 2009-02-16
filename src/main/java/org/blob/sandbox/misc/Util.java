package org.blob.sandbox.misc;

/**
 * User: alek
 * Date: Feb 15, 2009
 * Time: 9:22:35 PM
 *
 * Misc util methods
 *
 */

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Random;

public class Util {

    public static boolean isInteger(String text) {
        try { Integer.parseInt(text); } catch (NumberFormatException e) { return false; }
        return true;
    }

    public static boolean isLong(String text) {
        try { Long.parseLong(text); } catch (NumberFormatException e) { return false; }
        return true;
    }

    public static boolean isTimestamp(String text) {
        return isInteger(text) ? false : (isLong(text) ? true : false);
    }


    // exec single job
    public static void runJob(String jobName, Class mapper, Class reducer, String inputPath, String outputPath) throws Exception {
        JobConf conf = new JobConf(HistogramGenerator.class);
        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(IntWritable.class);
        conf.setMapperClass(mapper);
        conf.setReducerClass(reducer);
        conf.setCombinerClass(reducer);
        FileInputFormat.setInputPaths(conf, new Path(inputPath));
        FileOutputFormat.setOutputPath(conf, new Path(outputPath));
        JobClient.runJob(conf);
    }

    public static String getHDFSTemporaryDir(FileSystem fs) throws Exception {
        Random rnd = new Random();
        while(true) {
            String path = "tmp-" + rnd.nextInt() + "/";
            if (!fs.exists(new Path(path)))
                return path;
        }
    }

    public static void loadInputToHDFS(FileSystem fs, String localPath, String hdfsPath) throws Exception {
        File file = new File(localPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i=0; i<files.length; i++)
                    copyFileToHDFS(fs, files[i], new Path(hdfsPath+files[i].getName()));
            }
        } else {
          copyFileToHDFS(fs, file, new Path(hdfsPath+file.getName()));  
        }
    }

    public static void copyFileToHDFS(FileSystem fs, File localFile, Path hdfsPath) throws Exception {
        FileInputStream in = new FileInputStream(localFile);
        FSDataOutputStream out = fs.create(hdfsPath);
        byte buffer[] = new byte[256];
        try {
            int bytesRead = 0;
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
    }

    public static void getDataFromHDFS(FileSystem fs, String hdfsPath) throws Exception {

        Path inFile = new Path(hdfsPath);
        FSDataInputStream in = fs.open(inFile);
        byte buffer[] = new byte[256];
        int bytesRead = 0;
        while ((bytesRead = in.read(buffer)) > 0) {
            System.out.write(buffer, 0, bytesRead);
        }
    }

    public static void deleteTemporaryFiles(FileSystem fs, String hdfsPath) throws Exception {
        Path inFile = new Path(hdfsPath);
        if (fs.exists(inFile))
            fs.delete(inFile,true);
    }

}
