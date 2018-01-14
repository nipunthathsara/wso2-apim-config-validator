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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * Loads JSON Knowledge Base
     * @return
     */
    public Map<String, JSONObject> loadJsons(){
        Map<String, JSONObject> jsonKB = new HashMap();
        JSONObject jsonObject = null;
        for(Map.Entry<String, String> entry : Constants.JSON_PATH_MAP.entrySet()){
            jsonObject = this.readJson(Constants.KB_ROOT + Constants.JSON_KB + entry.getValue());
            jsonKB.put(entry.getKey(), jsonObject);
        }
        return jsonKB;
    }
}
