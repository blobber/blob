package org.blob.tasks;

public interface TaskFactoryIF {
  public static String ID = null;

  public abstract TaskIF getTask();
}
