package com.erudite.erudite_node.model;

import java.util.ArrayList;
import java.util.Map;

/**
 * A test model for Instances used for testing async net architecture
 * (Mirror class of the true Instance records returned by test Interface endpoint /instance).
 * Will most likely convert into a real Instance model for nodes & interface to use
 */
public record InstanceTest(String objectName, String className, Map<String, String> attrs, ArrayList<String> behaviorNames) {

}
