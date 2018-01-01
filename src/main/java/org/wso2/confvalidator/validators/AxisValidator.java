package org.wso2.confvalidator.validators;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.utils.XpathEvaluator;

import java.util.Map;

/**
 * Created by nipun on Jan, 2018
 */
public class AxisValidator extends Validator {
    public AxisValidator(Map<String, Map<String, Document>> configs, String currentNode, Map<String, JSONObject> jsonKB) {
        this.configs = configs;
        this.currentNode = currentNode;
        this.jsonKB = jsonKB;
        this.xpathEvaluator = new XpathEvaluator();
        configFileName = Constants.AXIS2_XML;
        log = Logger.getLogger(CarbonValidator.class);
        log.info("Validating " + configFileName);
    }
    //Write if any over riding or custom methods is needed
}
