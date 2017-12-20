package org.wso2.confvalidator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.XpathEvaluator;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nipun on Dec, 2017
 */
public class Validator {
    private static Map<String, Map<String, Document>> configs;
    private static Map<String, JSONObject> jsonKB;
    private static String currentNode;
    private static XpathEvaluator xpathEvaluator;
    final static Logger log = Logger.getLogger(APIManagerValidator.class);

    public Validator(Map<String, Map<String, Document>> configs, String currentNode, Map<String, JSONObject> jsonKB) {
        this.configs = configs;
        this.currentNode = currentNode;
        this.jsonKB = jsonKB;
        this.xpathEvaluator = new XpathEvaluator();
    }

    public Validator(){}


}
