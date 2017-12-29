package org.wso2.confvalidator;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.wso2.confvalidator.utils.ConfigLoader;
import org.wso2.confvalidator.utils.JSONLoader;
import org.wso2.confvalidator.validators.APIManagerValidator;
import org.wso2.confvalidator.validators.CarbonValidator;
import org.wso2.confvalidator.validators.MasterDataSourceValidator;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class initiates the validation
 * Prompts to other classes for further validations
 *
 * Created by nipun on Dec, 2017
 */
public class ConfigValidator {
    private static Map<String, Map<String, Document>> configs = new HashMap();
    private static Map<String, JSONObject> jsonKB;
    private static Map<String, Boolean> distribution;
    private static String currentNode;
    final static Logger log = Logger.getLogger(ConfigValidator.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

        //loads all the configurations from all available nodes. Access by: Node name >> Conf file name
        distribution = ConfigLoader.identifySetup();
        //ConfigLoader.getNodeURLs(distribution);
        configs = ConfigLoader.loadConfigs(distribution);

        //load Json KB. Access by: Conf file name Ex: api-manager.xml
        JSONLoader jsonLoader = new JSONLoader();
        jsonKB = jsonLoader.loadJsons();

        //validate all existing nodes (profiles)
        for(Map.Entry<String, Boolean> entry : distribution.entrySet()){
            if(entry.getValue()){
                currentNode = entry.getKey();
                log.info("****************" + " Validating " + currentNode + " Node " + "****************");

                //validate current node's confs against xsd
                for(Map.Entry<String, Document> configurationFile : configs.get(currentNode).entrySet()){
//                    validateXML(Constants.KB_ROOT + Constants.XSD_KB + Constants.XSD_PATH_MAP.get(configurationFile.getKey()),
//                        Constants.CONF_ROOT + Constants.NODE_PATH_MAP.get(currentNode) + Constants.CONF_PATH_MAP.get(configurationFile.getKey()));
                }

                APIManagerValidator apiManagerValidator = new APIManagerValidator(configs, currentNode, jsonKB);
                apiManagerValidator.iterator();

                CarbonValidator carbonValidator = new CarbonValidator(configs, currentNode, jsonKB);
                carbonValidator.iterator();

                MasterDataSourceValidator masterDataSourceValidator = new MasterDataSourceValidator(configs, currentNode, jsonKB);
                masterDataSourceValidator.iterator();
            }
        }
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
