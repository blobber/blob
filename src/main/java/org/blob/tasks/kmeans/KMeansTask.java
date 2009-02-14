package org.blob.tasks.kmeans;

import org.blob.tasks.TaskIF;
import org.blob.tasks.Task;
import org.blob.tasks.config.Config;
import org.blob.tasks.config.ConfigNode;

public class KMeansTask extends Task implements TaskIF {
  public void run(String app, Config conf) throws Exception {
    System.out.println(conf.get("kmeans/test").val);  
  }
}
