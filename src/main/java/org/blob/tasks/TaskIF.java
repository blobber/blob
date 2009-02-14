package org.blob.tasks;

import org.blob.tasks.config.Config;

public interface TaskIF {
  public abstract void run(String app, Config conf) throws Exception;
}
