package org.knot.tests;

import org.testng.annotations.Test;
import org.knot.config.ConfigNode;

/**
 * User: Igor Bogicevic
 * Date: Jan 12, 2009 7:15:18 PM
 */

public class KnotConfigurationTest {

  @Test(groups = { "knot_node_test" })
  public void ConfigNodeTest() throws Exception {
    ConfigNode config = new ConfigNode("base", null);

    config.add(new ConfigNode("sub_node_1", "sub_value_1"));
    config.add(new ConfigNode("sub_node_2", "sub_value_2"));
    config.add(new ConfigNode("sub_node_3", "sub_value_3"));

    assert config.contains("sub_node_1")
      : "Failed to found node by-key in ConfigNode container.";
    assert config.contains(new ConfigNode("sub_node_2", "sub_value_2"))
      : "Failed to found node by-value in ConfigNode container.";

    assert config.size() == 3
      : "Failed to validate number of elements.";
  }

  @Test(groups = { "knot_node_key_test" })
  public void ConfigNodeKeyTest() throws Exception {
    ConfigNode config = new ConfigNode("base", null);

    assert config.validateKey("good_and.valid_key")
      : "Failed to validate key 'good_and.valid_key'";
    assert !config.validateKey("bad_and/invalid_key")
      : "Failed to invalidate key 'good_and/valid_key'";
  }

  @Test(groups = { "knot_node_lookup_test" })
  public void ConfigNodeLookupTest() throws Exception {
    ConfigNode config = new ConfigNode("base", null);

    // sub-nodes
    ConfigNode node1 = new ConfigNode("depth_1", "value_1");
    ConfigNode node2 = new ConfigNode("depth_2", "value_2");

    // create structure
    config.add(node1);
    node1.add(node2);

    assert config.get("depth_1/depth_2").equals(new ConfigNode("depth_2", "value_2"))
      : "Failed to validate in-depth node lookup";
    assert config.get("depth_1/depth_2/depth_3") == null
      : "Failed to invalidate in-depth node lookup";
  }
}
