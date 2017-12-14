package org.wso2.confvalidator;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.ConfigLoader;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.Constants;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.DOMBuilder;
import org.wso2.confvalidator.org.wso2.confvalidator.utils.JSONLoader;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This class initiates the validation
 * Prompt to other classes for further validations
 *
 * Created by nipun on Dec, 2017
 */
public class ConfigValidator {
    private static DOMBuilder domBuilder;
    private static Map<String, Map<String, Document>> configs;
    private static Map<String, Map<String, JSONObject>> jsonKB;
    private static Map<String, Boolean> distribution;
    private static String currentNode;

    public static void main(String[] args) {
        //loads all the configurations from all available nodes. Access by: Node name >> Conf file name
        distribution = ConfigLoader.identifySetup();
        configs = ConfigLoader.loadConfigs(distribution);

        //load Json KB for all nodes. Access by: Node name >> Conf file name
        JSONLoader jsonReader = new JSONLoader();
        jsonKB = jsonReader.loadJsons();

        //validate all existing nodes (profiles)
        for(Map.Entry<String, Boolean> entry : distribution.entrySet()){
            if(entry.getValue()){
                currentNode = entry.getKey();

                //validate current node's confs against xsd
                for(Map.Entry<String, Document> configurationFile : configs.get(currentNode).entrySet()){
                    validateXML(Constants.KB_ROOT + Constants.XSD_KB + Constants.XSD_PATH_MAP.get(configurationFile),
                        Constants.CONF_ROOT + Constants.NODE_PATH_MAP.get(currentNode) + Constants.CONF_PATH_MAP.get(configurationFile));
                }



            }
        }

        //***************************
        domBuilder = new DOMBuilder();
        Map<String, Document> configs = domBuilder.loadFiles();
        Document apiManagerXML = configs.get(Constants.API_MANAGER_XML);
        Document carbonXML = configs.get(Constants.CARBON_XML);
        Document userMgtXML = configs.get(Constants.USER_MGT_XML);

        //validating against XSDs
        for (int i = 0; i < Constants.CONF_PATH_ARRAY.length; i++) {
            System.out.println("Validating " + Constants.CONF_NAME_ARRAY[i]);
            System.out.println(validateXML(Constants.KB_ROOT + Constants.XSD_PATH_ARRAY[i],
                    Constants.CONF_ROOT + Constants.CONF_PATH_ARRAY[i]));
        }

        APIManagerValidator apiManagerValidator = new APIManagerValidator(apiManagerXML, carbonXML, userMgtXML);
        apiManagerValidator.iterator();
    }

    /**
     * Validate against xsd file
     * @param xsdPath
     * @param xmlPath
     * @return
     */
    public static boolean validateXML(String xsdPath, String xmlPath) {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            System.out.println("Exception while validating against " + xsdPath);
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
