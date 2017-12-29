package org.wso2.confvalidator.validators;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.wso2.confvalidator.utils.XpathEvaluator;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nipun on Dec, 2017
 */
public class Validator {
    protected static Map<String, Map<String, Document>> configs;
    protected static Map<String, JSONObject> jsonKB;
    protected static String currentNode;
    protected static XpathEvaluator xpathEvaluator;
    protected static Logger log;
    protected static String configFileName;

    /**
     * Method to iterate configurations in configFileName
     * pass name of the relevant JSON configFileName
     */
    public void iterator() {
        JSONObject configKB = jsonKB.get(configFileName);
        Iterator<?> keySet = configKB.keySet().iterator();
        while (keySet.hasNext()) {
            String key = (String) keySet.next();
            if (configKB.get(key) instanceof JSONObject) {
                validateCommonConfigs((JSONObject) configKB.get(key));
            }
        }
    }

    /**
     * TODO remove overloading. keep only one
     * Method to re-iterate sub subConfigurations
     * @param subConfigurations
     */
    public void iterator(JSONObject subConfigurations) {
        //JSONObject configKB = jsonKB.get(configFileName);
        Iterator<?> keySet = subConfigurations.keySet().iterator();
        while (keySet.hasNext()) {
            String key = (String) keySet.next();
            if (subConfigurations.get(key) instanceof JSONObject) {
                validateCommonConfigs((JSONObject) subConfigurations.get(key));
            }
        }
    }

    /**
     * Define all common validations/validation calls here
     * @param configuration
     */
    public void validateCommonConfigs(JSONObject configuration) {
        String value = null;
        String xpath;

        //Does it contain sub configurations? If so, iterate again
        if (configuration.containsKey("subConfigurations")) {
            iterator((JSONObject) configuration.get("subConfigurations"));
            return;
        }

        //xpath should be defined in order to validate the configuration
        if (configuration.containsKey("xpath")) {
            xpath = configuration.get("xpath").toString();
            //Logging what is to be validated
            log.info("Config : " + xpath);
        } else {
            return;
        }

        //Mandatory check
        if (configuration.containsKey("mandatory") && configuration.containsKey("xpath")) {
            try {
                doMandatoryCheck(configuration, xpath);
            } catch (XPathExpressionException e) {
                //If xpath evaluation failed, stop further validations
                log.error("Failed obtaining value from XML, Aborting validation");
                e.printStackTrace();
                return;
            }
        }

        //Default check
        if (configuration.containsKey("default")) {
            try {
                value = xpathEvaluator.evaluateXpath(configs.get(currentNode).get(configFileName), xpath, XPathConstants.STRING).toString();
                if(configuration.get("default").equals(value)){
                    log.info("Using default value");
                }else {
                    log.info("Not default value");
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
                log.info("Acceptable value");
            }else{
                log.error("Not an acceptable value");
            }
        }

        //Regex validation
        if(configuration.containsKey("regex")){
            boolean match = value.matches(configuration.get("regex").toString());
            if(match){
                log.info("Regex validation okay" );
            }else{
                log.error("Regex validation failed" + configuration.get("regex").toString());
            }
        }
    }

    public static void doMandatoryCheck(JSONObject configuration, String xpath) throws XPathExpressionException{
        JSONObject mandatoryKB = (JSONObject) configuration.get("mandatory");
        //If configuration is mandatory for given node
        if (mandatoryKB.containsKey(currentNode) && "yes".equals(mandatoryKB.get(currentNode).toString())) {
            try {
                String value = xpathEvaluator.evaluateXpath(configs.get(currentNode).get(configFileName), xpath, XPathConstants.STRING).toString();
                if ("".equals(value)) {
                    log.error("Mandatory value is empty");
                }else {
                    log.info("Mandatory value defined");
                }
            } catch (XPathExpressionException e) {
                //Assumption: xml is commented out
                log.error("Mandatory config commented out");
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
