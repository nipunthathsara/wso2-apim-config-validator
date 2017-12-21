package org.wso2.confvalidator;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.XpathEvaluator;

import java.util.Map;

/**
 * Created by nipun on Dec, 2017
 */
public class MasterDataSourceValidator extends Validator{
    public MasterDataSourceValidator(Map<String, Map<String, Document>> configs, String currentNode, Map<String, JSONObject> jsonKB) {
        this.configs = configs;
        this.currentNode = currentNode;
        this.jsonKB = jsonKB;
        this.xpathEvaluator = new XpathEvaluator();
        configFileName = Constants.MASTER_DATASOURCE_XML;
        log = Logger.getLogger(MasterDataSourceValidator.class);
        log.info("Validating Master-dataource.xml on " + currentNode + " Node...");
    }

    //TODO
    //Write if any over riding or custom methods is needed
}
