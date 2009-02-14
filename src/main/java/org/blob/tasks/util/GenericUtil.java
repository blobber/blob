package org.blob.tasks.util;

public class GenericUtil {
  public static String toString(Object o) {

    if (o.getClass() == String.class) {
      return (String)o;
    }
    if (o.getClass() == Integer.class) {
      return Integer.toString((Integer)o);
    }
    if (o.getClass() == Long.class) {
      return Long.toString((Long)o);
    }
    if (o.getClass() == Double.class) {
      return Double.toString((Double)o);
    }
    if (o.getClass() == Float.class) {
      return Float.toString((Float)o);
    }

    return null;
  }
}
