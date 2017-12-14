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

//    /**
//     * Load all jsons in the KB
//     * @return
//     */
//    public Map<String, JSONObject> loadJsons() {
//        JSONObject jsonObject = null;
//        for (int i = 0; i < Constants.JSON_PATH_ARRAY.length; i++) {
//            String filePath = Constants.KB_ROOT + Constants.JSON_PATH_ARRAY[i];
//            try {
//                Object object = parser.parse(new FileReader(filePath));
//                jsonObject = (JSONObject) object;
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            jsons.put(Constants.CONF_NAME_ARRAY[i], jsonObject);
//        }
//        return jsons;
//    }

    /**
     * Load all Json knowledge bases
     * for every node
     * @return
     */
    public Map<String, Map<String, JSONObject>> loadJsons(){
        Map<String, Map<String, JSONObject>> jasonKB = new HashMap();
        Map<String, JSONObject> nodeKb = new HashMap();
        JSONObject jsonObject = null;
        for (int i = 0; i < Constants.NODE_NAME_ARRAY.length; i++) {
            for(Map.Entry<String, String> entry : Constants.JSON_PATH_MAP.entrySet()){
                jsonObject = this.readJson(Constants.KB_ROOT + Constants.JSON_KB + entry.getValue());
                nodeKb.put(entry.getKey(), jsonObject);
            }
            jasonKB.put(Constants.NODE_NAME_ARRAY[i], jsonObject);
        }
        return jasonKB;
    }
}
