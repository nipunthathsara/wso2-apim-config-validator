package org.wso2.confvalidator.org.wso2.confvalidator.utils;

import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by nipun on Dec, 2017
 */
public class XpathEvaluator {
    XPath xPath = XPathFactory.newInstance().newXPath();

    public Object evaluateXpath(Document doc, String xpath, QName returnType) throws XPathExpressionException{
        String result = null;
        result = (String) xPath.compile(xpath).evaluate(doc, returnType);
        return result;
    }
}
