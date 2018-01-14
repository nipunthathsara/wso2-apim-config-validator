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
package org.wso2.confvalidator;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.utils.ConfigLoader;
import org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.utils.JSONLoader;
import org.wso2.confvalidator.validators.APIManagerValidator;
import org.wso2.confvalidator.validators.AxisValidator;
import org.wso2.confvalidator.validators.CarbonValidator;
import org.wso2.confvalidator.validators.MasterDataSourceValidator;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ConfigValidator {
    private static Map<String, Map<String, Document>> configs = new HashMap();
    private static Map<String, JSONObject> jsonKB;
    private static Map<String, Boolean> distribution;
    private static String currentNode;
    private final static Logger log = Logger.getLogger(ConfigValidator.class);

    public static void main(String[] args) {
        //path to the uploads directory created by the script
        Constants.CONF_ROOT = args[0];
        PropertyConfigurator.configure("log4j.properties");
        distribution = ConfigLoader.identifySetup();
        configs = ConfigLoader.loadConfigs(distribution);
        JSONLoader jsonLoader = new JSONLoader();
        jsonKB = jsonLoader.loadJsons();

        //validate all existing nodes (profiles)
        for(Map.Entry<String, Boolean> entry : distribution.entrySet()){
            if(entry.getValue()){
                currentNode = entry.getKey();
                log.info(" Validating " + currentNode + " Node ");
                //TODO - fill XSD kb to use these validations
//                for(Map.Entry<String, Document> configurationFile : configs.get(currentNode).entrySet()){
//                    validateXML(Constants.KB_ROOT + Constants.XSD_KB + Constants.XSD_PATH_MAP.get(configurationFile.getKey()),
//                        Constants.CONF_ROOT + Constants.NODE_PATH_MAP.get(currentNode) + Constants.CONF_PATH_MAP.get(configurationFile.getKey()));
//                }
                APIManagerValidator apiManagerValidator = new APIManagerValidator(configs, currentNode, jsonKB);
                apiManagerValidator.iterator();
                CarbonValidator carbonValidator = new CarbonValidator(configs, currentNode, jsonKB);
                carbonValidator.iterator();
                MasterDataSourceValidator masterDataSourceValidator = new MasterDataSourceValidator(configs, currentNode, jsonKB);
                masterDataSourceValidator.iterator();
                AxisValidator axisValidator = new AxisValidator(configs, currentNode, jsonKB);
                axisValidator.iterator();
            }
        }
    }
}
