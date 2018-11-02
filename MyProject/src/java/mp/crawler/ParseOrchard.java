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
public class ParseOrchard {

    public static void main(String[] args) {
        parseOrchard();
    }

    public static void parseOrchard() {
        Parser parser = new Parser() {
            @Override
            public void parsingHTML(String filePath) {
                XMLInputFactory fact = XMLInputFactory.newInstance();
                fact.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
                fact.setProperty(XMLInputFactory.IS_VALIDATING, false);

                XMLEventReader reader = null;
                String detail = "";
                String str_infoType = "";

                try {
                    reader = fact.createXMLEventReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
                    boolean inDivImage = false;
                    boolean inTaleInfo = false;
                    boolean inSpan100ML = false;

                    while (reader.hasNext()) {
                        XMLEvent event = reader.nextEvent();

                        if (event.isStartElement()) {
                            StartElement ele = (StartElement) event;
                            //Get Name
                            if (ele.getName().toString().equals("h1")) {
                                Attribute attr = ele.getAttributeByName(new QName("id"));

                                if (attr != null) {
                                    if (attr.getValue().equals("product-title")) {
                                        System.out.println("Name: " + takeAbsoluteContentFromreader(reader));
                                    }
                                }
                            }

                            //Get image
                            if (ele.getName().toString().equals("div")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));

                                if (attr != null) {
                                    if (attr.getValue().equals("woocommerce-product-gallery__image")) {
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
                            if (ele.getName().toString().equals("table")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));

                                if (attr != null) {
                                    if (attr.getValue().equals("shop_attributes")) {
                                        inTaleInfo = true;
                                    }
                                }
                            }
                            if (ele.getName().toString().equals("th") && inTaleInfo) {
                                str_infoType = takeAbsoluteContentFromreader(reader);
                            }
                            if (ele.getName().toString().equals("a") && inTaleInfo) {
                                detail = takeAbsoluteContentFromreader(reader);
                                switch (str_infoType) {
                                    case "Thương hiệu":
                                        System.out.println("Brand: " + detail);
                                        break;
                                    case "Giới tính":
                                        System.out.println("Sex: " + detail);
                                        break;
                                    case "Xuất xứ":
                                        System.out.println("Origin: " + detail);
                                        break;
                                    case "Nồng độ":
                                        System.out.println("Concentration: " + detail);
                                        break;
                                    case "Năm phát hành":
                                        System.out.println("Release: " + detail);
                                        break;
                                    case "Nhóm hương":
                                        System.out.println("Incense: " + detail);
                                        break;
                                    case "Phong cách":
                                        System.out.println("Style: " + detail);
                                        break;
                                }
                            }

                            //Get desc
//                            if (ele.getName().toString().equals("span")) {
//                                Attribute attr = ele.getAttributeByName(new QName("class"));
//
//                                if (attr != null) {
//                                    if (attr.getValue().equals("variation-price")) {
//                                        inSpanPrice = true;
//                                    }
//                                }
//                            }
//                            if (ele.getName().toString().equals("span") && inSpanPrice) {
//                                detail = takeAbsoluteContentFromreader(reader);
//                                System.out.println("price: " + detail);
//                                inSpanPrice = false;
//                            }
                            //Get price
                            if (ele.getName().toString().equals("span")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));

                                if (attr != null) {
                                    if (attr.getValue().equals("variation-price") && inSpan100ML) {
                                        detail = takeAbsoluteContentFromreader(reader);
                                        System.out.println("price: " + detail);
                                        inSpan100ML = false;
                                    }
                                }
                            }
                            if (ele.getName().toString().equals("span")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));

                                if (attr != null) {
                                    if (attr.getValue().equals("variation-name")) {
                                        str_infoType = takeAbsoluteContentFromreader(reader);
                                        if (str_infoType.contains("100")) {
                                            inSpan100ML = true;
                                        }
                                    }
                                }
                            }

                        }//end if start element

                        if (event.isEndElement()) {
                            EndElement ele = (EndElement) event;

                            if (ele.getName().toString().equals("table")) {
                                inTaleInfo = false;
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
                    XMLEventReader reader = null;
                    XMLInputFactory xif = XMLInputFactory.newInstance();
                    xif.setEventAllocator(new XMLEventAllocatorImpl());
                    reader = xif.createXMLEventReader(isr);
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "UTF-8"));

                    boolean inPTag = false;
                    boolean inScriptTag = false;
                    boolean isFirstSection = false;
                    String pContent = "";

                    if (reader != null) {
                        while (reader.hasNext()) {
                            XMLEvent event = null;
                            event = reader.nextEvent();

                            if (event != null) {
                                //System.out.println("event: " + event.toString());
                                if (event.isEndDocument()) {
                                    break;
                                } else if (event.toString().contains("<?xml version")) {
                                    writer.write("<?xml version=\"1.0\" encoding='UTF-8' standalone='no'?>" + "\n");
                                } else {
                                    writer.write(event.toString() + "\n");
                                }
                            }
                        }

                    }
                } catch (XMLStreamException ex) {
                    Logger.getLogger(ParseOrchard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ParseOrchard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ParseOrchard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ParseOrchard.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ParseOrchard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        };

        //Clean HTML before parse
//        try {
//            parser.cleanHTML(new InputStreamReader(new FileInputStream(Constant.PATH_HTML + Constant.NAME_ORCHARD_PAGE), "UTF-8"),
//                    Constant.PATH_HTML + Constant.NAME_BUFFERED_PAGE);
//        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
//            Logger.getLogger(ParseOrchard.class.getName()).log(Level.SEVERE, null, ex);
//        }
        parser.parsingHTML(Constant.PATH_HTML + "/" + Constant.NAME_BUFFERED_PAGE);
    }

}
