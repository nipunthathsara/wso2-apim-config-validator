package org.wso2.confvalidator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.JSONReader;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.XpathEvaluator;

import javax.xml.xpath.XPathConstants;
import java.util.Iterator;
import java.util.Map;

/**
 * This class validates api-manager.xml based configurations
 * <p>
 * Created by nipun on Dec, 2017
 */
public class APIManagerValidator {
    private Document apiManagerXML;
    private Document carbonXML;
    private Document userMgtXML;
    private Map<String, JSONObject> jsons;

    public APIManagerValidator(Document apiManagerXML, Document carbonXML, Document userMgtXML) {
        this.apiManagerXML = apiManagerXML;
        this.carbonXML = carbonXML;
        this.userMgtXML = userMgtXML;
        jsons = (new JSONReader()).loadJsons();
    }

    /**
     * Method to iterate configurations
     */
    public void iterator() {
        JSONObject apiManagerKB = jsons.get(Constants.API_MANAGER_XML);
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
     * Method to re-iterate sub configurations
     *
     * @param configurations
     */
    public void iterator(JSONObject configurations) {
        Iterator<?> keySet = configurations.keySet().iterator();
        while (keySet.hasNext()) {
            String key = (String) keySet.next();
            if (configurations.get(key) instanceof JSONObject) {
                validateApiMangerCommonConfigs((JSONObject) configurations.get(key));
            }
        }
    }

    public void validateApiMangerCommonConfigs(JSONObject configuration) {
        //Does it contain sub attributes? If so, iterate again
        if (configuration.containsKey("options")) {
            iterator((JSONObject) configuration.get("options"));
            return;
        }
        //Get value in XML
        String xpath = (String) configuration.get("xpath");
        String value = (new XpathEvaluator()).evaluateXpath(this.apiManagerXML, xpath, XPathConstants.STRING).toString();

        //Mandatory Check
        if (configuration.containsKey("mandatory") && "yes".equals(configuration.get("mandatory")) && "".equals(value)) {
            System.out.println(xpath + "Field is required");
        }

        //Default check
        if (configuration.containsKey("default") && configuration.get("default").toString().equals(value)) {
            System.out.println(xpath + "is using DEFAULT value");
        }

        //Cross Reference check
        if (configuration.containsKey("crossReference")) {
            JSONObject referenceArray = (JSONObject) configuration.get("crossReference");
            //TODO cross reference logic here
        }

        //Parsable values
        if (configuration.containsKey("parsableValues")) {
            JSONArray parsableValues = (JSONArray) configuration.get("parsableValues");
            boolean validValue = false;
            for (int i = 0; i < parsableValues.size(); i++) {
                if (value.equals(parsableValues.get(i))) {
                    validValue = true;
                    break;
                }
            }
            System.out.println("Entered value is valid: " + validValue);
        }
    }
}
