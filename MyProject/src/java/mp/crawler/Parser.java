/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.crawler;

import com.sun.xml.internal.fastinfoset.stax.events.EndElementEvent;
import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
import mp.Utils.Constant;

/**
 *
 * @author ASUS
 */
public abstract class Parser {

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

    /**
     * Read HTML file to convert
     * TODO: implement to each parser
     * @param String inputFilePath
     */
    public abstract void parsingHTML(String filePath);

    /**
     * Processing and writing well-formed temporary file
     * TODO: implement to each parser
     * @param InputStreamReader isr
     * @param String outputFilePath
     */
    public abstract void cleanHTML(InputStreamReader isr, String outputFilePath);
    

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

    /**
     * Get absolute content from tag 
     * 
     * @param XMLEventReader eventReader
     * @return String
     */
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

    //TODO Unecessary remove
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

    //TODO OK simplifying if ok remove duplicated
    public void cleanHTML1(InputStreamReader isr) throws XMLStreamException {
        Writer writer = null;
        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            xif.setEventAllocator(new XMLEventAllocatorImpl());
            XMLEventReader reader = null;
            reader = xif.createXMLEventReader(isr);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format("%s\\%s", Constant.PATH_HTML, "cho.html")), "UTF-8"));

            boolean inPTag = false;
            boolean inScriptTag = false;
            String pContent = "";

            if (reader != null) {
                while (reader.hasNext()) {
                    try {
                        XMLEvent event = null;
                        event = reader.nextEvent();
                        if (event != null) {
                            //System.out.println("event: " + event.toString());
                            if (event.toString().contains("<?xml version")) {
                                writer.write("<?xml version=\"1.0\" encoding='UTF-8' standalone='no'?>" + "\n");
                            } else if (event.isStartElement()) {
                                StartElement ele = (StartElement) event;
                                if (ele.getName().toString().equals("p")) {
                                    inPTag = true;
                                    writer.write(event.toString());
                                } else if (ele.getName().toString().equals("script")) {
                                    inScriptTag = true;
                                } else {
                                    writer.write(event.toString() + "\n");
                                }
                            } else if (event.isEndElement()) {
                                EndElement ele = (EndElement) event;
                                if (ele.getName().toString().equals("p")) {
                                    writer.write(pContent);
                                    writer.write(event.toString());
                                    pContent = "";
                                    inPTag = false;
                                } else if (ele.getName().toString().equals("script")) {
                                    inScriptTag = false;
                                } else {
                                    writer.write(event.toString() + "\n");
                                }
                            } else if (event.isCharacters()) {
                                if (inPTag) {
                                    pContent += event.asCharacters().getData();
                                } else if(inScriptTag) {
                                    //Not write
                                } else {
                                    writer.write(event.toString() + "\n");
                                }
                            }

                        }
                    } catch (XMLStreamException e) {
                        String msg = e.getMessage();
                        String msgErrorString = "The element type \"";
                        if (msg.contains(msgErrorString)) {
                            String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be terminated"));
                            try {
                                writer.write("</" + missingTagName + ">");
                            } catch (IOException ex) {
                                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NullPointerException e) {
                        //End of file
                        break;
                    }
                }
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
