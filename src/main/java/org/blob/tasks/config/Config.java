package org.blob.tasks.config;

import org.blob.tasks.util.ExceptionUtil;
import org.blob.tasks.util.GenericUtil;
import org.ho.yaml.Yaml;

import java.util.HashMap;
import java.io.File;

public class Config extends ConfigNode {
  private static Config conf = null;

  //
  // CONSTRUCTOR
  //

  private Config() { }

  //
  // SINGLETON INTERFACE
  //

  public static synchronized Config get() {
    return conf;
  }

  public static synchronized Config init(String file) throws ConfigException {

    if (conf == null)
      conf = load(file);

    return conf;
  }

  //
  // LOAD CONFIGURATION
  //

  public static Config load(String file) throws ConfigException {
    Config conf = new Config();

    // load YAML file
    try {
      conf.key = "root";
      conf.parse(conf, (HashMap) Yaml.load(new File(file)));
    } catch (Exception e) {
      ExceptionUtil.log(e);
      throw new ConfigException("Failed to load " + file + " configuration file.");
    }

    return conf;
  }

  protected void parse(ConfigNode root, HashMap map) throws ConfigException {

    for (Object okey : map.keySet()) {
      Object oval = map.get(okey);
      ConfigNode node = new ConfigNode();
      String skey = GenericUtil.toString(okey);
      String sval = GenericUtil.toString(map.get(okey));

      // key needs to be one of a known class
      if (skey == null) {
        throw new ConfigException("Uknown class(" + okey.getClass().getName() + ") for key " + skey);
      }
      node.key = skey;


      if (sval != null) {
        node.val = sval;
      } else if (oval.getClass() == HashMap.class) {
        parse(node, (HashMap)oval);
      } else {
        throw new ConfigException("Don't know how to handle " + oval.getClass().getName() + "for key " + skey);
      }

      root.add(node);
    }
  }
}
