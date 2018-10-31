/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.Utils;

import java.io.InputStream;
import java.io.Serializable;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author trant
 */
public class XMLUtils implements Serializable {
    //StAX Cursor

    public static XMLEventReader pareseFileToStAXIterator(InputStream is, String endCoding) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);

        XMLEventReader reader = factory.createXMLEventReader(is, endCoding);

        return reader;
    }
}
