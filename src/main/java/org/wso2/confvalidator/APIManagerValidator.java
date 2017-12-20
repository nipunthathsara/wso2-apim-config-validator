package org.wso2.confvalidator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.XpathEvaluator;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.Iterator;
import java.util.Map;

/**
 * This class validates api-manager.xml based configurations
 * <p>
 * Created by nipun on Dec, 2017
 */
public class APIManagerValidator {
    private static Map<String, Map<String, Document>> configs;
    private static Map<String, JSONObject> jsonKB;
    private static String currentNode;
    private static XpathEvaluator xpathEvaluator;
    final static Logger log = Logger.getLogger(APIManagerValidator.class);

    public APIManagerValidator(Map<String, Map<String, Document>> configs, String currentNode, Map<String, JSONObject> jsonKB) {
        this.configs = configs;
        this.currentNode = currentNode;
        this.jsonKB = jsonKB;
        this.xpathEvaluator = new XpathEvaluator();
        log.info("Validating API-Manager.xml...");
    }

    /**
     * Method to iterate configurations
     */
    public void iterator() {
        JSONObject apiManagerKB = jsonKB.get(Constants.API_MANAGER_XML);
        Iterator<?> keySet = apiManagerKB.keySet().iterator();
        while (keySet.hasNext()) {
            String key = (String) keySet.next();
            if (apiManagerKB.get(key) instanceof JSONObject) {
                validateApiMangerCommonConfigs((JSONObject) apiManagerKB.get(key));
            }
        }
    }

    /**
     * TODO remove overloading. keep only one
     * Method to re-iterate sub subConfigurations
     *
     * @param subConfigurations
     */
    public void iterator(JSONObject subConfigurations) {
        Iterator<?> keySet = subConfigurations.keySet().iterator();
        while (keySet.hasNext()) {
            String key = (String) keySet.next();
            if (subConfigurations.get(key) instanceof JSONObject) {
                validateApiMangerCommonConfigs((JSONObject) subConfigurations.get(key));
            }
        }
    }

    public void validateApiMangerCommonConfigs(JSONObject configuration) {
        String value = null;
        String xpath;

        //Does it contain sub attributes? If so, iterate again
        if (configuration.containsKey("options")) {
            iterator((JSONObject) configuration.get("options"));
            return;
        }

        //xpath should be defined in order to validate the configuration
        if (configuration.containsKey("xpath")) {
            xpath = configuration.get("xpath").toString();
            //Logging what is to be validated
            log.info("Node : " + currentNode + " File : API-Manager.xml" + " Configuration : " + xpath);
        } else {
            return;
        }

        //Mandatory check
        if (configuration.containsKey("mandatory") && configuration.containsKey("xpath")) {
            try {
                doMandatoryCheck(configuration, xpath);
            } catch (XPathExpressionException e) {
                //If xpath evaluation failed, stop further validations
                e.printStackTrace();
                return;
            }
        }

        //Default check
        if (configuration.containsKey("default")) {
            try {
                value = xpathEvaluator.evaluateXpath(configs.get(currentNode).get(Constants.API_MANAGER_XML), xpath, XPathConstants.STRING).toString();
                if(configuration.get("default").equals(value)){
                    log.info(xpath + " is using default value");
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }

        //Cross reference check
        if (configuration.containsKey("crossReference")) {
            JSONObject references = (JSONObject) configuration.get("crossReference");
            Iterator<?> keySet = references.keySet().iterator();
            //iterate through node names in cross references list
            while (keySet.hasNext()) {
                String key = (String) keySet.next();
                if (references.get(key) instanceof JSONArray) {
                    JSONArray referenceArray = (JSONArray) references.get(key);
                    try {
                        doCrossReference(key, referenceArray, value);
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //Parsable values check
        if (configuration.containsKey("parsableValues")) {
            JSONArray parsableValues = (JSONArray) configuration.get("parsableValues");
            boolean acceptableValue = false;
            for (int i = 0; i < parsableValues.size(); i++) {
                if (value.equals(parsableValues.get(i))) {
                    acceptableValue = true;
                    break;
                }
            }
            if(acceptableValue){
                log.info(xpath + " is assigned an acceptable value");
            }else{
                log.error(xpath + " is not assigned an acceptable value");
            }
        }
    }

    public static void doMandatoryCheck(JSONObject configuration, String xpath) throws XPathExpressionException{
        JSONObject mandatoryKB = (JSONObject) configuration.get("mandatory");
        //If configuration is mandatory for given node
        if (mandatoryKB.containsKey(currentNode) && "yes".equals(mandatoryKB.get(currentNode).toString())) {
            try {
                String value = xpathEvaluator.evaluateXpath(configs.get(currentNode).get(Constants.API_MANAGER_XML), xpath, XPathConstants.STRING).toString();
                if ("".equals(value)) {
                    log.error("Mandatory configuration : " + xpath + " is not defined");
                }else {
                    log.info("Mandatory configuration : " + xpath + " is not empty");
                }
            } catch (XPathExpressionException e) {
                //Assumption: xml is commented out
                log.error("Can not find Mandatory configuration : " + xpath + " Check if commented out");
                //Throwing new exception for the caller to know that xpath evaluation has failed
                //therefore, stop further validations
                throw new XPathExpressionException("Can not evaluate xpath");
            }
        }
    }

    public static void doCrossReference(String node, JSONArray referenceArray, String value) throws XPathExpressionException{
        if("currentNode".equals(node)){
            node = currentNode;
        }
        //iterate through cross references array of a given node
        for (int i = 0; i < referenceArray.size(); i++) {
            //split file name and xpath defined in KB - "api-manager.xml //APIManager/DataSourceName/text()"
            String[] fileAndXpath = referenceArray.get(i).toString().split("\\s+");
            if(fileAndXpath.length != 2){
                log.error("Error in KB : cross reference can only contain file name and xpath");
                System.exit(1);
            }

            if (configs.containsKey(node) && configs.get(node).containsKey(fileAndXpath[0])) {
                //Check in all occurrences of xpath
                NodeList remoteValues = (NodeList) xpathEvaluator.evaluateXpath(configs.get(node).get(fileAndXpath[0]), fileAndXpath[1], XPathConstants.NODESET);
                boolean match = false;
                for (int j = 0; j < remoteValues.getLength(); j++) {
                    String remoteValue = remoteValues.item(j).getNodeValue();
                    if (value.equals(remoteValue)) {
                        match = true;
                    }
                }
                if(match){
                    log.info("Cross reference with " + node + "'s " + fileAndXpath[0] + " - " + fileAndXpath[1] + " evaluated okay");
                }else{
                    log.error("Cross reference with " + node + "'s " + fileAndXpath[0] + " - " + fileAndXpath[1] + " did not match");
                }
            }
        }
    }
}
