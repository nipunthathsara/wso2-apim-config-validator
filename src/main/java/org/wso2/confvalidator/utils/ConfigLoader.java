package org.wso2.confvalidator.utils;

import org.w3c.dom.Document;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by nipun on Dec, 2017
 */
public class ConfigLoader {
    private static Map<String, Boolean> distribution = new HashMap();
    private static DOMBuilder domBuilder;

    /**
     * This method goes through the uploaded configs and identifies
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

    /**
     * Takes accessible URLs for available nodes
     * @param distribution
     */
    public static Map<String, String> getNodeURLs(Map<String, Boolean> distribution){
        Map<String, String> nodeURLMap = new HashMap();
        Scanner scanner = new Scanner(System.in);
        for (Map.Entry<String, Boolean> entry: distribution.entrySet()){
            if(entry.getValue() && !entry.getKey().equals(Constants.GW)){
                System.out.println("Enter url for " + entry.getKey() + "node : ");
                nodeURLMap.put(entry.getKey(), scanner.nextLine());
            }else if(entry.getKey().equals(Constants.GW) && entry.getValue()){
                System.out.println("Enter url for GW Manager : ");
                nodeURLMap.put("gwm", scanner.nextLine());
                System.out.println("Enter url for GW Worker : ");
                nodeURLMap.put("gww", scanner.nextLine());
            }
        }
        System.out.println("Enter url for database : ");
        nodeURLMap.put("db", scanner.nextLine());
        System.out.println("Enter url for GW Manager : ");
        return nodeURLMap;
    }
}
