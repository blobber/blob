package org.blob.tasks.kmeans;

import org.blob.tasks.TaskFactoryIF;
import org.blob.tasks.TaskIF;

public class KMeansTaskFactory implements TaskFactoryIF {
  public static String ID = "k-means";

  //
  // FACTORY
  //

  public TaskIF getTask() {
    return new KMeansTask();
  }
}
