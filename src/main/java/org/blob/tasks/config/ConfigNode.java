package org.blob.tasks.config;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class ConfigNode {
  public String key = null;
  public String val = null;

  private Iterator<ConfigNode> iterator = null;
  private LinkedHashMap<String, ConfigNode> nodes = new LinkedHashMap<String, ConfigNode>();

  protected static Pattern keyPattern = Pattern.compile("^([a-zA-Z_0-9.]+)$");

  //
  // CONSTRUCTOR
  //

  public ConfigNode() {
    this(null, null);
  }

  public ConfigNode(String key, String val) {
    this.key = key;
    this.val = val;

    iterator = nodes.values().iterator();
  }

  //
  // ITERABLE INTERFACE
  //

  public boolean hasNext() {
    if (iterator.hasNext())
      return true;

    return false;
  }

  public ConfigNode next() {
    if (!iterator.hasNext())
      throw new NoSuchElementException();

    return iterator.next();
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  public Iterator<ConfigNode> iterator() {
    return nodes.values().iterator();
  }

  //
  // USER INTERFACE
  //

  public ConfigNode get(String str) {
    ConfigNode node = null;
    String[] keys = null;

    if (str == null)
      return null;

    keys = str.split("/", -1);

    for(String key: keys) {
      if (node == null)
        node = nodes.get(key);
      else
        node = node.get(key);

      // sanity check - drop lookups if no such key
      if (node == null)
        return null;
    }

    return node;
  }

  public void add(ConfigNode node) {
    if (node.key == null)
      return;

    this.nodes.put(node.key, node);
  }

  public Boolean contains(String key) {
    return nodes.containsKey(key);
  }

  public Boolean contains(ConfigNode node) {
    return nodes.containsValue(node);
  }

  public Integer size() {
    return nodes.size();
  }

  //
  // COMPARISON INTERFACE
  //

  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if((obj == null) || (obj.getClass() != this.getClass()))
      return false;

    ConfigNode node = (ConfigNode)obj;

    if (key.equals(node.key) && val.equals(node.val))
      return true;

    return false;
  }

  public int hashCode() {
    int hash = 1;
    if (key != null) hash = hash * 31 + key.hashCode();
    if (val != null) hash = hash * 29 + val.hashCode();

    return hash;
  }

  //
  // UTIL
  //

  public Boolean validateKey(String key) {

    if (keyPattern.matcher(key).matches())
      return true;

    return false;
  }

  //
  // DEBUG
  //

  public String toString() {
    StringBuilder result = new StringBuilder();

    result . append("\"") . append(key) . append("\" => \"") . append(val) . append("\"");

    return result.toString();
  }

  public String inspect() {
    return inspect(0);
  }

  protected String inspect(int depth) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < depth; i++)
      result . append("\t");
    result . append(this.toString()) . append("\n");

    for (ConfigNode subNode: nodes.values())
      result . append(subNode.inspect(depth + 1));

    return result.toString();
  }  
}

