/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.confvalidator.validators;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.utils.XpathEvaluator;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.Iterator;
import java.util.Map;

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
     * Define all common validations/validation calls here
     * @param configuration
     */
    public void validateCommonConfigs(JSONObject configuration) {
        String value = null;
        String xpath;

        //xpath should be defined in order to validate the configuration
        if (configuration.containsKey(Constants.XPATH)) {
            xpath = configuration.get(Constants.XPATH).toString();
            log.info("Config : " + xpath);
        } else {
            return;
        }

        //Mandatory check
        if (configuration.containsKey(Constants.MANDATORY)) {
            try {
                doMandatoryCheck(configuration, xpath);
            } catch (XPathExpressionException e) {
                //If xpath evaluation failed, stop further validations
                log.error("Skipping mandatory config...check if commented out");
                e.printStackTrace();
                return;
            }
        }

        //Default check
        if (configuration.containsKey(Constants.DEFAULT)) {
            try {
                value = xpathEvaluator.evaluateXpath(configs.get(currentNode).get(configFileName), xpath,
                        XPathConstants.STRING).toString();
                if(configuration.get(Constants.DEFAULT).equals(value)){
                    log.info("Using default value");
                }else {
                    log.info("Not default value : " + value);
                }
            } catch (XPathExpressionException e) {
                log.warn("Skipping config...check if commented out");
                return;
            }
        }

        //Regex validation
        if(configuration.containsKey(Constants.REGEX)){
            boolean match = value.matches(configuration.get(Constants.REGEX).toString());
            if(match){
                log.info("Regex validation okay" );
            }else{
                log.error("Regex validation failed : " + configuration.get(Constants.REGEX_ERROR).toString());
            }
        }

        //Parsable values check
        if (configuration.containsKey(Constants.PASSABLE_VALUES)) {
            JSONArray parsableValues = (JSONArray) configuration.get(Constants.PASSABLE_VALUES);
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
                log.error("Not an acceptable value : " + value);
            }
        }

        //Cross reference check
        if (configuration.containsKey(Constants.CROSS_REFERENCE)) {
            JSONObject references = (JSONObject) configuration.get(Constants.CROSS_REFERENCE);
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
    }

    public static void doMandatoryCheck(JSONObject configuration, String xpath) throws XPathExpressionException{
        JSONArray mandatoryNodes = (JSONArray) configuration.get(Constants.MANDATORY);
        //does the configuration is mandatory for current node
        if (mandatoryNodes.contains(currentNode)) {
            try {
                String value = xpathEvaluator.evaluateXpath(configs.get(currentNode).get(configFileName), xpath,
                        XPathConstants.STRING).toString();
                if ("".equals(value)) {
                    log.error("Mandatory value is empty");
                }else {
                    log.info("Mandatory value defined");
                }
            } catch (XPathExpressionException e) {
                //Assumption: xml is commented out
                log.error("Mandatory config commented out");
                //Throwing new exception for the caller to know that xpath evaluation has failed. stop validating
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
                //Check in all occurrences of xpath - eg: master-datasource.xml
                NodeList remoteValues = (NodeList) xpathEvaluator.evaluateXpath(configs.get(node).get(fileAndXpath[0]),
                        fileAndXpath[1], XPathConstants.NODESET);
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
