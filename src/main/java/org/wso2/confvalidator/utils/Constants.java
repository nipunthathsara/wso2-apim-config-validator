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

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static String CONF_ROOT;
    public static final String GWW_PATH = "/gw-w";
    public static final String GWM_PATH = "/gw-m";
    public static final String KM_PATH = "/km";
    public static final String PUB_PATH = "/pub";
    public static final String STORE_PATH = "/store";
    public static final String TM_PATH = "/tm";
    public static final String[] NODE_PATH_ARRAY = {GWW_PATH, GWM_PATH, KM_PATH, PUB_PATH, STORE_PATH, TM_PATH};
    public static final Map<String, String> NODE_PATH_MAP = new HashMap();

    public static final String API_MANAGER_XML_PATH = "/conf/api-manager.xml";
    public static final String CARBON_XML_PATH = "/conf/carbon.xml";
    public static final String USER_MGT_XML_PATH = "/conf/user-mgt.xml";
    public static final String MASTER_DATASOURCE_XML_PATH = "/conf/datasources/master-datasources.xml";
    public static final String AXIS2_XML_PATH = "/conf/axis2/axis2.xml";
    public static final String[] CONF_PATH_ARRAY = {API_MANAGER_XML_PATH, CARBON_XML_PATH, USER_MGT_XML_PATH
            , MASTER_DATASOURCE_XML_PATH, AXIS2_XML_PATH};
    public static final Map<String, String> CONF_PATH_MAP = new HashMap();

    public static final String API_MANAGER_XML = "api-manager.xml";
    public static final String CARBON_XML = "carbon.xml";
    public static final String USER_MGT_XML = "user-mgt.xml";
    public static final String MASTER_DATASOURCE_XML = "master-datasources.xml";
    public static final String AXIS2_XML = "axis2.xml";
    public static final String[] CONF_NAME_ARRAY = {API_MANAGER_XML, CARBON_XML, USER_MGT_XML, MASTER_DATASOURCE_XML
            , AXIS2_XML};

    public static final String GWW = "gw-w";
    public static final String GWM = "gw-m";
    public static final String KM = "km";
    public static final String PUB = "pub";
    public static final String STORE = "store";
    public static final String TM = "tm";
    public static final String[] NODE_NAME_ARRAY = {GWW, GWM, KM, PUB, STORE, TM};

    public static final String KB_ROOT = "src/main/resources/kb";
    public static final String JSON_KB = "/json";
    public static final String XSD_KB = "/xsd";
    public static final String API_MANAGER_XSD_PATH = "/api-manager.xsd";
    public static final String CARBON_XSD_PATH = "/carbon.xsd";
    public static final String USER_MGT_XSD_PATH = "/user-mgt.xsd";
    public static final String MASTER_DATASOURCE_XSD_PATH = "/master-datasource.xsd";
    public static final String AXIS2_XSD_PATH = "/axis2.xsd";
    public static final String[] XSD_PATH_ARRAY = {API_MANAGER_XSD_PATH, CARBON_XSD_PATH, USER_MGT_XSD_PATH
            , MASTER_DATASOURCE_XSD_PATH, AXIS2_XSD_PATH};
    public static final Map<String, String> XSD_PATH_MAP = new HashMap();

    public static final String API_MANAGER_JSON_PATH = "/api-manager.json";
    public static final String CARBON_JSON_PATH = "/carbon.json";
    public static final String USER_MGT_JSON_PATH = "/user-mgt.json";
    public static final String MASTER_DATASOURCE_JSON_PATH = "/master-datasource.json";
    public static final String AXIS2_JSON_PATH = "/axis2.json";
    public static final String[] JSON_PATH_ARRAY = {API_MANAGER_JSON_PATH, CARBON_JSON_PATH, USER_MGT_JSON_PATH
            , MASTER_DATASOURCE_JSON_PATH, AXIS2_JSON_PATH};
    public static final Map<String, String> JSON_PATH_MAP = new HashMap();

    static {
        for (int i = 0; i < NODE_NAME_ARRAY.length; i++) {
            NODE_PATH_MAP.put(NODE_NAME_ARRAY[i], NODE_PATH_ARRAY[i]);
        }
        for (int i = 0; i < CONF_NAME_ARRAY.length; i++) {
            CONF_PATH_MAP.put(CONF_NAME_ARRAY[i], CONF_PATH_ARRAY[i]);
            JSON_PATH_MAP.put(CONF_NAME_ARRAY[i], JSON_PATH_ARRAY[i]);
            XSD_PATH_MAP.put(CONF_NAME_ARRAY[i], XSD_PATH_ARRAY[i]);
        }
    }

    public static final String MANDATORY = "mandatory";
    public static final String DEFAULT = "default";
    public static final String CROSS_REFERENCE = "crossReference";
    public static final String XPATH = "xpath";
    public static final String PASSABLE_VALUES = "parsableValues";
    public static final String REGEX = "regex";
    public static final String REGEX_ERROR = "regexError";
}
