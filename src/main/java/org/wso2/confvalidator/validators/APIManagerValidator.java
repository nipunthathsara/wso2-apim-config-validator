package org.wso2.confvalidator.validators;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.utils.XpathEvaluator;

import java.util.Map;

/**
 * This class validates api-manager.xml based configurations
 * <p>
 * Created by nipun on Dec, 2017
 */
public class APIManagerValidator extends Validator{
    public APIManagerValidator(Map<String, Map<String, Document>> configs, String currentNode, Map<String, JSONObject> jsonKB) {
        this.configs = configs;
        this.currentNode = currentNode;
        this.jsonKB = jsonKB;
        this.xpathEvaluator = new XpathEvaluator();
        configFileName = Constants.API_MANAGER_XML;
        log = Logger.getLogger(APIManagerValidator.class);
        log.info("Validating " + configFileName);
    }

    //if any over riding or custom methods are needed
}
