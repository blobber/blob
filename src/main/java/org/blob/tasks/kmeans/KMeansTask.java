package org.blob.tasks.kmeans;

import org.blob.tasks.TaskIF;
import org.blob.tasks.Task;
import org.blob.tasks.config.Config;
import org.blob.tasks.config.ConfigNode;

public class KMeansTask extends Task implements TaskIF {
  public void run(String app, Config conf) throws Exception {

    for (ConfigNode node: conf.get("kmeans")) {
      System.out.println(node.key + " :: " + node.val);
    }
      
  }
}
