package org.wso2.confvalidator.org.wso2.confvalidator.utils;

import org.w3c.dom.Document;

import java.io.File;
import java.util.Map;

/**
 * Created by nipun on Dec, 2017
 */
public class ConfigLoader {
    private static Map<String, Boolean> distribution;
    private static DOMBuilder domBuilder;

    /**
     * This method goes through the uploaded configs and identifies
     * available profiles.
     * @return
     */
    public static  Map<String, Boolean> identifySetup() {
        for (int i = 0; i < Constants.NODE_PATH_ARRAY.length; i++) {
            String path = Constants.CONF_ROOT + Constants.NODE_PATH_ARRAY[i];
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
        Map<String, Map<String, Document>> configs = null;
        domBuilder = new DOMBuilder();
        for (Map.Entry<String, Boolean> entry : distribution.entrySet()){
            if(entry.getValue()){
                //load node specific configuration file set
                Map<String, Document> nodeConfig = domBuilder.loadFiles(entry.getKey());
                configs.put(entry.getKey(), nodeConfig);
            }
        }
        return configs;
    }
}
