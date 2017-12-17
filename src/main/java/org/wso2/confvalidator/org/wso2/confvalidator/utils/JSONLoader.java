package org.wso2.confvalidator.org.wso2.confvalidator.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nipun on Dec, 2017
 */
public class JSONLoader {

    JSONParser parser = new JSONParser();
    Map<String, JSONObject> jsons = new HashMap();

    /**
     * Read a single Json
     * @param jsonPath
     * @return
     */
    public JSONObject readJson(String jsonPath){
        JSONObject jsonObject = null;
        String filePath = jsonPath;
        try {
            Object object = parser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Loads all Jsons from knowledge base
     * for every node
     * @return
     */
    public Map<String, Map<String, JSONObject>> loadJsons(){
        Map<String, Map<String, JSONObject>> jasonKB = new HashMap();
        JSONObject jsonObject = null;
        for (int i = 0; i < Constants.NODE_NAME_ARRAY.length; i++) {
            Map<String, JSONObject> nodeKb = new HashMap();
            for(Map.Entry<String, String> entry : Constants.JSON_PATH_MAP.entrySet()){
                jsonObject = this.readJson(Constants.KB_ROOT + Constants.JSON_KB +
                        Constants.NODE_PATH_MAP.get(Constants.NODE_NAME_ARRAY[i]) + entry.getValue());
                nodeKb.put(entry.getKey(), jsonObject);
            }
            jasonKB.put(Constants.NODE_NAME_ARRAY[i], nodeKb);
        }
        return jasonKB;
    }
}
