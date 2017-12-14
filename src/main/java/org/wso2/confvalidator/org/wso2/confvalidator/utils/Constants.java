package org.wso2.confvalidator.org.wso2.confvalidator.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nipun on Dec, 2017
 */
public class Constants {

    //    public static final String CONF_ROOT = "/home/nipun/data/task/confvalidator/src/resources";
    public static final String CONF_ROOT = "/home/nipun/data/task/confvalidator/configurations";

    public static final String GW_PATH = "/gw";
    public static final String KM_PATH = "/km";
    public static final String PUB_PATH = "/pub";
    public static final String STORE_PATH = "/store";
    public static final String TM_PATH = "/tm";
    public static final String[] NODE_PATH_ARRAY = {GW_PATH, KM_PATH, PUB_PATH, STORE_PATH, TM_PATH};
    public static final Map<String, String> NODE_PATH_MAP = new HashMap<String, String>();

    //TODO Take CONF_ROOT as a JVM argument
    public static final String API_MANAGER_XML_PATH = "/conf/api-manager.xml";
    public static final String CARBON_XML_PATH = "/conf/carbon.xml";
    public static final String USER_MGT_XML_PATH = "/conf/user-mgt.xml";
    public static final String[] CONF_PATH_ARRAY = {API_MANAGER_XML_PATH, CARBON_XML_PATH, USER_MGT_XML_PATH};
    public static final Map<String, String> CONF_PATH_MAP = new HashMap<String, String>();

    public static final String API_MANAGER_XML = "api-manager.xml";
    public static final String CARBON_XML = "carbon.xml";
    public static final String USER_MGT_XML = "user-mgt.xml";
    public static final String[] CONF_NAME_ARRAY = {API_MANAGER_XML, CARBON_XML, USER_MGT_XML};

    public static final String GW = "gw";
    public static final String KM = "km";
    public static final String PUB = "pub";
    public static final String STORE = "store";
    public static final String TM = "tm";
    public static final String[] NODE_NAME_ARRAY = {GW, KM, PUB, STORE, TM};

    public static final String KB_ROOT = "/home/nipun/data/task/confvalidator/src/resources/kb";
    public static final String JSON_KB = "/json";
    public static final String XSD_KB = "/xsd";
    public static final String API_MANAGER_XSD_PATH = "/xsd/api-manager.xsd";
    public static final String CARBON_XSD_PATH = "/xsd/carbon.xsd";
    public static final String USER_MGT_XSD_PATH = "/xsd/user-mgt.xsd";
    public static final String[] XSD_PATH_ARRAY = {API_MANAGER_XSD_PATH, CARBON_XSD_PATH, USER_MGT_XSD_PATH};
    public static final Map<String, String> XSD_PATH_MAP = new HashMap();

    public static final String API_MANAGER_JSON_PATH = "/api-manager.json";
    public static final String CARBON_JSON_PATH = "/carbon.json";
    public static final String USER_MGT_JSON_PATH = "/user-mgt.json";
    public static final String[] JSON_PATH_ARRAY = {API_MANAGER_JSON_PATH, CARBON_JSON_PATH, USER_MGT_JSON_PATH};
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

}
