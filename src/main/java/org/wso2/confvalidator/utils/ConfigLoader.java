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
package org.wso2.confvalidator.utils;

import org.w3c.dom.Document;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigLoader {
    private static Map<String, Boolean> distribution = new HashMap();
    private static DOMBuilder domBuilder;

    /**
     * Goes through the uploaded configs and identifies
     * available profiles.
     * @return
     */
    public static  Map<String, Boolean> identifySetup() {
        for (int i = 0; i < Constants.NODE_PATH_ARRAY.length; i++) {
            String path = Constants.CONF_ROOT + Constants.NODE_PATH_ARRAY[i] + "/conf";
            File directory = new File(path);
            if (directory.exists()) {
                distribution.put(Constants.NODE_NAME_ARRAY[i], true);
            }else{
                distribution.put(Constants.NODE_NAME_ARRAY[i], false);
            }
        }
        return distribution;
    }

    /**
     * Loads all config files from all available nodes
     * @param distribution
     * @return
     */
    public static Map<String, Map<String, Document>> loadConfigs(Map<String, Boolean> distribution){
        //Configuration files for each node
        Map<String, Map<String, Document>> configs = new HashMap();
        Map<String, Document> nodeConfig;
        domBuilder = new DOMBuilder();
        for (Map.Entry<String, Boolean> entry : distribution.entrySet()){
            if(entry.getValue()){
                //load node specific configuration file set
                nodeConfig = domBuilder.loadFiles(entry.getKey());
                configs.put(entry.getKey(), nodeConfig);
            }
        }
        return configs;
    }
}
