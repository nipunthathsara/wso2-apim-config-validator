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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DOMBuilder {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder;
    Document document;

    /**
     * Returns all configuration files for a given node
     * @param profile
     * @return
     */
    public Map<String, Document> loadFiles(String profile) {
        Map<String, Document> configs = new HashMap();
        for (int i = 0; i < Constants.CONF_PATH_ARRAY.length; i++) {
            String filePath = Constants.CONF_ROOT + Constants.NODE_PATH_MAP.get(profile) + Constants.CONF_PATH_ARRAY[i];
            File configFile = new File(filePath);
            try {
                this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
                document = documentBuilder.parse(configFile);
                document.getDocumentElement().normalize();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            configs.put(Constants.CONF_NAME_ARRAY[i], document);
        }
        return configs;
    }
}
