/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.parser;

import com.sun.xml.internal.fastinfoset.stax.events.EndElementEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mp.crawler.Downloader;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author ASUS
 */
public class Parser {

    public void parseHTMLToXML(String filePath, String desTag, String desAttr, String desAttrContent) {

        XMLInputFactory fact = XMLInputFactory.newInstance();
        fact.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        fact.setProperty(XMLInputFactory.IS_VALIDATING, false);

        //XMLEventReader reader;
        XMLEventReader reader = null;
        String detail = "";

        try {
            reader = fact.createXMLEventReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            boolean inProductTag = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    StartElement ele = (StartElement) event;
                    if (ele.getName().toString().equals(desTag)) {
                        System.out.println("ele: " + ele);
                        Attribute attr = ele.getAttributeByName(new QName(desAttr));
                        System.out.println("attr" + attr);

                        if (attr != null) {
                            if (attr.getValue().equals(desAttrContent)) {
                                inProductTag = true;

                                if (ele.getName().toString().equals("img") && inProductTag) {
                                    Attribute sourceAtt = ele.getAttributeByName(new QName("src"));
                                    detail = sourceAtt.getValue();
                                    System.out.println("detail: " + detail);
                                }

                                if (ele.getName().toString().equals("a") && inProductTag) {
                                    Attribute sourceAtt = ele.getAttributeByName(new QName("title"));
                                    detail = sourceAtt.getValue();
                                    System.out.println(detail);
                                }
                                System.out.println("----");
                            }
                        }//end if check desAttribute
                    }//end if check desTag
                }//end if start element
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addMissingEndTag() {
        try {
            // Load file into memory
            String xml = new String(Files.readAllBytes(Paths.get("test.xml")), StandardCharsets.UTF_8);

            // Apply magic to add missing end-tags
            xml = xml.replaceAll("(?m)^(\\s*)<(\\w+)>([^<]+)$", "$1<$2>$3</$2>");

            // Parse then print the XML, to ensure there are no errors
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));
            TransformerFactory.newInstance().newTransformer()
                    .transform(new DOMSource(document), new StreamResult(System.out));
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Iterator<XMLEvent> autoAddMissingEndTag(XMLEventReader reader) {
        ArrayList<XMLEvent> lEvents = new ArrayList<>();
        int endTagMarker = 0;
        while (endTagMarker >= 0) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                String msg = e.getMessage();
                String msgErrorString = "The element type \"";
                if (msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be terminated"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));
                    event = missingTag;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                break;
            }

            if (event != null) {
                if (event.isStartElement()) {
                    endTagMarker++;
                } else if (event.isEndElement()) {
                    endTagMarker--;
                }
                if (endTagMarker >= 0) {
                    lEvents.add(event);
                }
            }
        }
        return lEvents.iterator();
    }

    public void parseHTML(String filePath) {
        XMLInputFactory fact = XMLInputFactory.newInstance();
        fact.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        fact.setProperty(XMLInputFactory.IS_VALIDATING, false);

        //XMLEventReader reader;
        XMLEventReader reader = null;
        String detail = "";

        try {
            reader = fact.createXMLEventReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            detail = takeAbsoluteContentFromreader(reader);
            System.out.println("Content: " + detail);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected String takeAbsoluteContentFromreader(XMLEventReader eventReader) {
        String result = "";
        int depth = 0;
        try {

            while (eventReader.hasNext()) {
                // peek event
                XMLEvent xmlEvent;
                xmlEvent = eventReader.peek();

                if (xmlEvent.isStartElement()) {
                    ++depth;
                } else if (xmlEvent.isEndElement()) {
                    --depth;
                    // reached END_ELEMENT tag?
                    // break loop, leave event in stream
                    if (depth < 0) {
                        break;
                    }
                }
                // skip first element event and next to second eslement
                xmlEvent = eventReader.nextEvent();
                // write src to list

                if (xmlEvent.isCharacters()) {
                    Characters se = xmlEvent.asCharacters();
                    if (!se.getData().trim().isEmpty()) {
                        //TODO uncomment
                        //result += se.getData().trim() + "|";
                        //TODO remove
                        result += se.getData().trim() + "\n";
                    }
                }
            }
        } catch (XMLStreamException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (result.isEmpty()) {
            return "";
        }
        return result.substring(0, result.length() - 1);
    }

    public void parseHTML(String filePath, String desTag, String desAttr, String desAttrContent) {
        XMLInputFactory fact = XMLInputFactory.newInstance();
        fact.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        fact.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLEventReader reader = null;
        String detail = "";

        try {
            reader = fact.createXMLEventReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            boolean inProductTag = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    StartElement ele = (StartElement) event;
                    if (ele.getName().toString().equals(desTag)) {
                        Attribute attr = ele.getAttributeByName(new QName(desAttr));

                        if (attr != null) {
                            if (attr.getValue().equals(desAttrContent)) {
                                inProductTag = true;
                            }
                        }//end if check desAttribute
                        if (attr == null) {
                            inProductTag = false;
                        }
                    }
                    if (ele.getName().toString().equals("img") && inProductTag) {
                        Attribute sourceAtt = ele.getAttributeByName(new QName("data-original"));
                        detail = sourceAtt.getValue();
                        System.out.println("img: " + detail);
                    }

                    if (ele.getName().toString().equals("a") && inProductTag) {
                        Attribute attr = ele.getAttributeByName(new QName("class"));
                        String productName = attr.getValue();
                        if ("product-name pro-color".equals(productName)) {
                            attr = ele.getAttributeByName(new QName("href"));
                            detail = attr.getValue();
                            event = reader.nextEvent();
                            Characters characters = (Characters) event;
                            String content = characters.getData();
                            System.out.println("a: " + detail);
                            System.out.println("content: " + content);
                        }
                    }//end if check desTag
                    if (ele.getName().toString().equals("div") && inProductTag) {
                        Attribute attr = ele.getAttributeByName(new QName("class"));
                        String productName = attr.getValue();
                        if ("product-price row".equals(productName)) {
                            detail = takeAbsoluteContentFromreader(reader);
                            System.out.println("price: " + detail);
                        }
                        
                    }
                }//end if start element
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
