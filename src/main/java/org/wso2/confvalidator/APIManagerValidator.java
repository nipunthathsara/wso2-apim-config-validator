package org.wso2.confvalidator;

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

    public void iterator() {
        JSONObject apiManagerKB = jsons.get(Constants.API_MANAGER_XML);
        Iterator<?> keySet = apiManagerKB.keySet().iterator();
        while (keySet.hasNext()) {
            String key = (String) keySet.next();
            if ( apiManagerKB.get(key) instanceof JSONObject ) {
                validateApiMangerCommonConfigs((JSONObject) apiManagerKB.get(key));
            }
        }
    }

    public void validateApiMangerCommonConfigs(JSONObject configuration) {
        if(configuration.containsKey("options")){
            return;
        }
        String xpath = (String) configuration.get("xpath");
        String value = (new XpathEvaluator()).evaluateXpath(this.apiManagerXML, xpath, XPathConstants.STRING).toString();

        //Mandatory Check
        if("yes".equals(configuration.get("mandatory")) &&  "".equals(value)){
            System.out.println(xpath + "Field is required");
        }


    }
}
