/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.crawler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import mp.Utils.Constant;

/**
 *
 * @author ASUS
 */
public class ParsePage {

    public static void main(String[] args) {
//        Parser parser = new Parser();
//        //parser.parseHTML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE);
//        //parser.parseHTMLToXML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE, "div", "class", "product-item");
//        parser.parseHTML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE, "div", "class", "product-item");
        parseTheGioiNuocHoa();
    }

    public static void parseTheGioiNuocHoa() {
        Parser parser = new Parser() {
            @Override
            public void parsingHTML(String filePath) {
                XMLInputFactory fact = XMLInputFactory.newInstance();
                fact.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
                fact.setProperty(XMLInputFactory.IS_VALIDATING, false);

                XMLEventReader reader = null;
                String detail = "";

                try {
                    reader = fact.createXMLEventReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
                    boolean inDivImage = false;
                    boolean inUlInfo = false;
                    boolean inDivDescription = false;

                    while (reader.hasNext()) {
                        XMLEvent event = reader.nextEvent();

                        if (event.isStartElement()) {
                            StartElement ele = (StartElement) event;
                            
                            //Get image
                            if (ele.getName().toString().equals("div")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));

                                if (attr != null) {
                                    if (attr.getValue().equals("item")) {
                                        inDivImage = true;
                                    }
                                }//end if check desAttribute
                                if (attr == null) {
                                    inDivImage = false;
                                }
                            }
                            if (ele.getName().toString().equals("img") && inDivImage) {
                                Attribute sourceAtt = ele.getAttributeByName(new QName("src"));
                                detail = sourceAtt.getValue();
                                System.out.println("img: " + detail);
                                inDivImage = false;
                            }

                            //Get info
                            if (ele.getName().toString().equals("ul")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));

                                if (attr != null) {
                                    if (attr.getValue().equals("border-bottom")) {
                                        inUlInfo = true;
                                    }
                                }
                            }
                            if (ele.getName().toString().equals("span") && inUlInfo) {
                                detail = takeAbsoluteContentFromreader(reader);
                                System.out.println("info: " + detail);
                            }

                            
                            //Get desc
                            if (ele.getName().toString().equals("div")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));
                                
                                if (attr != null) {
                                    if (attr.getValue().equals("desc")) {
                                        inDivDescription = true;
                                    }
                                }
                            }
                            if (ele.getName().toString().equals("p") && inDivDescription) {
                                detail = takeAbsoluteContentFromreader(reader);
                                System.out.println("desc: " + detail);
                                inUlInfo = false;
                            }
                            
                        }//end if start element
                        
                        
                        if (event.isEndElement()) {
                            EndElement ele = (EndElement) event;
                            
                            if (ele.getName().toString().equals("ul")) {
                                inUlInfo = false;
                            }
                        }//end if end element
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XMLStreamException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        parser.parsingHTML(Constant.PATH_HTML + "/" + "cho.html");
        //Check ok them tag
        //parser.addMissingEndTag("src/java/mp/htmlFiles/tgnhMale0.html", "src/java/mp/htmlFiles");
        
        //OK TODO: simplify
//        try {
//            parser.cleanHTML(new InputStreamReader(new FileInputStream("src/java/mp/htmlFiles/tgnhMale0.html"), "UTF-8"));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (XMLStreamException ex) {
//            Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
}
