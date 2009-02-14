package org.blob;

import org.blob.tasks.TaskFactoryIF;
import org.blob.tasks.TaskIF;
import org.blob.tasks.config.Config;
import org.blob.tasks.kmeans.KMeansTaskFactory;
import org.blob.tasks.util.ExceptionUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class Run {
  Logger logger = Logger.getLogger(this.getClass());

  public static void main(String[] args) {
    try {
      new Run().run(args);
    } catch (Exception e) {
      ExceptionUtil.log(e);
    }
  }

  public void run(String[] args) throws Exception {
    TaskIF app = null;
    TaskFactoryIF factory = null;
    Config conf = null;
    HashMap<String, String> params = parseArgs(args);

    if (!params.containsKey("app"))
      throw new Exception("No app defined!");
    if (!params.containsKey("config"))
      throw new Exception("No configuration defined!");

    //
    // PARSE CONFIGURATION
    //

    conf = Config.init(params.get("config"));

    if (conf == null) {
      logger.error("Failed to parse configuration file " + params.get("config"));
      System.exit(1);
    }

    //
    // FACTORY
    //

    if (params.get("app").matches("kmeans"))
      factory = new KMeansTaskFactory();
    else
      throw new Exception("Failed to get the factory application " + params.get("app"));

    //
    // GET AND START APPLICATION
    //

    if ((app = factory.getTask()) == null)
      throw new Exception("Failed to create application " + params.get("app"));

    app.run(params.get("app"), conf);
  }

  //
  // UTIL
  //

  protected HashMap<String, String> parseArgs(String[] args) {
     HashMap<String, String> params = new HashMap<String, String>();

     for (int i = 0; i < args.length; ++i) {
       try {
         if ("-c".equals(args[i]))
           params.put("config", args[++i]);
         else if ("-a".equals(args[i]))
           params.put("app", args[++i]);
         else if ("-h".equals(args[i]))
           usage();
       }
       catch (ArrayIndexOutOfBoundsException e) {
         System.err.println("ERROR: Required parameter missing from " +
                            args[i-1]);
       }
     }

     return params;
   }
  

  public void usage() {
    System.err.println("./bin/blob -a [application name] -c [application config]");
  }
}
