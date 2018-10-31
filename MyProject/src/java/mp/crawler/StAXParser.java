/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.crawler;

import com.sun.xml.internal.fastinfoset.stax.events.EndElementEvent;
import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author ASUS
 */
public class StAXParser {

    public static StreamSource getDataFromWeb(String url) throws IOException {
        StringBuffer sb = new StringBuffer();
        URL oracle = new URL(url);
        BufferedReader bf = new BufferedReader(new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = bf.readLine()) != null) {
            inputLine = inputLine.replace("&", "&#30")
                    .replace("&#30;nbsp;", "")
                    .replace("\t", "")
                    .replaceAll("\\w+*\\w+", inputLine);
            sb.append(inputLine.trim() + "\n");
        }
        bf.close();
        InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
        return new StreamSource(is);
    }
    
    private static void cleanXML(StreamSource source, String desElement, String desAttribute, String desAttrValue) throws XMLStreamException {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setEventAllocator(new XMLEventAllocatorImpl());
        
        XMLEventReader reader = null;
        reader = xif.createXMLEventReader(source.getInputStream());
        
        if(reader != null) {
            while(reader.hasNext()) {
                try {
                    XMLEvent event = null;
                    event = reader.nextEvent();
                    if(event != null) {
                        if(event.isStartDocument()) {
                            StartElement startElement = (StartElement) event;
                            if(startElement.getName().toString().equals(desElement)) {
                                Attribute a = startElement.getAttributeByName(new QName(desElement));
                                if(a != null) {
                                    String value = a.getValue();
                                    if(value.equals(desAttrValue)) {
                                        printAllMatchData(reader);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void printAllMatchData(Iterator<XMLEvent> iterator) {
        String result = "";
        while(iterator.hasNext()) {
            XMLEvent event = iterator.next();
            if(event.isStartElement()) {
                StartElement se = (StartElement)event;
                result += "<" + se.getName().toString();
                Iterator childIter = se.getAttributes();
                while (childIter.hasNext()) {
                    Attribute attr = (Attribute) childIter.next();
                    String value = attr.getValue().replace("&", "&#38;");
                    result += " " + attr.getName().toString() + "''\''" + value + "\''";
                }
                result += ">";
            }
            if (event.isCharacters()) {
                Characters chars = (Characters)event;
                if(!chars.isWhiteSpace()) {
                    result += chars.getData().replace("&", "&#30").trim();
                }
            }
            if (event.isEndElement()) {
                EndElement ee = (EndElement)event;
                result += ee.toString();
            }
        }
        System.out.println("Result: " + result);
    }
    
    private static Iterator<XMLEvent> autoAddMissingEndTag(XMLEventReader reader) {
        ArrayList<XMLEvent> lsEvents = new ArrayList<>();
        int endTagMarker = 0;
        while (endTagMarker >= 0) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                String msg = e.getMessage();
                String msgErrorString = "The element type \''";
                if(msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be terminated"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));
                    event = missingTag;
                }
            } catch (NullPointerException exception) {
                break;
            }
            
            if(event != null) {
                if (event.isStartElement()) {
                    endTagMarker++;
                } else if (event.isEndElement()) {
                    endTagMarker--;
                } if (endTagMarker >= 0) {
                    lsEvents.add(event);
                }
            }
        }
        return lsEvents.iterator();
    }
}
