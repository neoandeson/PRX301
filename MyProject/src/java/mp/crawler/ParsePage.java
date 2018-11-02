/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.crawler;

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
import mp.Utils.MyUtils;
import mp.generatedObj.Perfume;
import mp.generatedObj.Product;

/**
 *
 * @author ASUS
 */
public class ParsePage {

    public static void main(String[] args) {
        try {
            //        Parser parser = new Parser();
//        //parser.parseHTML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE);
//        //parser.parseHTMLToXML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE, "div", "class", "product-item");
//        parser.parseHTML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE, "div", "class", "product-item");

            Perfume perfume = new Perfume();
            Product product = new Product();
            perfume.setProduct(product);
            perfume.setType("Perfume");
            perfume.getProduct().setName("GucciGang");
            perfume.getProduct().setPrice(MyUtils.parseStringMoneyToBigInt("2.000.000"));
            perfume.getProduct().setImageURL("abc/def.com");
            perfume.getProduct().setBrand("Gucci");
            perfume.getProduct().setSex(MyUtils.getBooleanSexValue("Nam"));
            perfume.getProduct().setOrigin("France");
            perfume.setConcentration("Eau de parfum");
            perfume.setRelease(MyUtils.parseStringToInt("1999"));
            perfume.setIncense("Nhóm gỗ");
            //perfume.setStyle("Thanh lịch");

            MyUtils.validateXMLBeforeSaveToDatabase(perfume, Constant.PATH_XML + "/" + "temp.xml", Constant.PATH_SCHEMA + "Perfume.xsd");
//OK TODO uncomment
//parseTheGioiNuocHoa();
        } catch (IOException ex) {
            Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

            @Override
            public void cleanHTML(InputStreamReader isr, String outputFilePath) {
                Writer writer = null;
                try {
                    XMLInputFactory xif = XMLInputFactory.newInstance();
                    xif.setEventAllocator(new XMLEventAllocatorImpl());
                    XMLEventReader reader = null;
                    reader = xif.createXMLEventReader(isr);
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "UTF-8"));

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
                                        } else if (inScriptTag) {
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
                                        System.out.println("missingTagName2: " + missingTagName);
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
                } catch (XMLStreamException ex) {
                    Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        };

        //Clean HTML before parse
        try {
            parser.cleanHTML(new InputStreamReader(new FileInputStream(Constant.PATH_HTML + "/" + Constant.NAME_THEGIOINUOCHOA_PAGE), "UTF-8"),
                    Constant.PATH_HTML + "/" + Constant.NAME_BUFFERED_PAGE);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
        }

        parser.parsingHTML(Constant.PATH_HTML + "/" + Constant.NAME_BUFFERED_PAGE);
    }

}
