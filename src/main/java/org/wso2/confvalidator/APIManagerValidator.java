package org.wso2.confvalidator;

import org.w3c.dom.Document;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.JSONReader;

/**
 * This class validates api-manager.xml based configurations
 *
 * Created by nipun on Dec, 2017
 */
public class APIManagerValidator {
    private Document apiManagerXML;
    private Document carbonXML;
    private Document userMgtXML;

    public APIManagerValidator(Document apiManagerXML, Document carbonXML, Document userMgtXML) {
        this.apiManagerXML = apiManagerXML;
        this.carbonXML = carbonXML;
        this.userMgtXML = userMgtXML;
        JSONReader jsonReader = new JSONReader();

    }

    public void validateApiMangerCommonConfigs(){

    }
}
