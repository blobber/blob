package org.blob.tasks.util;

import org.apache.log4j.Logger;

public class ExceptionUtil {
  protected static final Logger logger = Logger.getLogger(ExceptionUtil.class);

  //
  // INTERFACE
  //

  public static void log(Exception e) {
    logger.error("===================== EXCEPTION LOG START =====================");
    logger.error("Message: " + e.getMessage());

    for(StackTraceElement element: e.getStackTrace()) {
      logger.error(element.toString());
    }
    logger.error("=====================  EXCEPTION LOG END  =====================");
  }
}
