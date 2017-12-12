package org.wso2.confvalidator.org.wso2.confvalidator.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nipun on Dec, 2017
 */
public class JSONReader {

    JSONParser parser = new JSONParser();
    Map<String, JSONObject> jsons = new HashMap();

    public void readJson(String jsonPath){
        JSONObject jsonObject = null;
        String filePath = Constants.KB_ROOT + jsonPath;
        try {
            Object object = parser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all jsons in the KB
     * @return
     */
    public Map<String, JSONObject> loadJsons() {
        JSONObject jsonObject = null;
        for (int i = 0; i < Constants.JSON_PATH_ARRAY.length; i++) {
            String filePath = Constants.KB_ROOT + Constants.JSON_PATH_ARRAY[i];
            try {
                Object object = parser.parse(new FileReader(filePath));
                jsonObject = (JSONObject) object;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            jsons.put(Constants.CONF_NAME_ARRAY[i], jsonObject);
        }
        return jsons;
    }
}
