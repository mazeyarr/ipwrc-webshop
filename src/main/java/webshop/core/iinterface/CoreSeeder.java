package webshop.core.iinterface;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public abstract class CoreSeeder {
    DocumentBuilderFactory docFactory;
    DocumentBuilder docBuilder;

    public CoreSeeder() {
        try {
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public DocumentBuilderFactory getDocFactory() {
        return docFactory;
    }

    public DocumentBuilder getDocBuilder() {
        return docBuilder;
    }
}
