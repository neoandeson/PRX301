/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.crawler;

import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
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
import mp.generatedObj.Style;

/**
 *
 * @author ASUS
 */
public class OrchardCrawler {

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            crawling(Constant.URL_ORCHARD_FEMALE + i + "/", Constant.PATH_HTML, "", "");
            crawling(Constant.URL_ORCHARD_MALE + i + "/", Constant.PATH_HTML, "", "");
        }
    }

    public static void crawling(String htmlURL, String filePath, String startTagProp, String endTagProp) {
        Parser parser = new Parser() {
            @Override
            public void parsingHTML(String inputFilePath) {
                XMLInputFactory fact = XMLInputFactory.newInstance();
                fact.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
                fact.setProperty(XMLInputFactory.IS_VALIDATING, false);

                XMLEventReader reader = null;

                try {
                    reader = fact.createXMLEventReader(new InputStreamReader(new FileInputStream(inputFilePath), "UTF-8"));
                    boolean inDivImage = false;
                    boolean inTaleInfo = false;
                    boolean inSpan100ML = false;
                    Perfume perfume = new Perfume();
                    Product product = new Product();
                    ArrayList<Style> lst_style = new ArrayList<>();
                    String str_detail = "";
                    String str_infoType = "";

                    while (reader.hasNext()) {
                        XMLEvent event = reader.nextEvent();

                        if (event.isStartElement()) {
                            StartElement ele = (StartElement) event;
                            //Get Name
                            if (ele.getName().toString().equals("h1")) {
                                Attribute attr = ele.getAttributeByName(new QName("id"));

                                if (attr != null) {
                                    if (attr.getValue().equals("product-title")) {
                                        //System.out.println("Name: " + takeAbsoluteContentFromreader(reader));
                                        product.setName(str_detail);
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
                                str_detail = sourceAtt.getValue();
                                //System.out.println("img: " + str_detail);
                                product.setImageURL(str_detail);
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
                                str_detail = takeAbsoluteContentFromreader(reader);
                                switch (str_infoType) {
                                    case "Thương hiệu":
                                        product.setBrand(str_detail);
                                        break;
                                    case "Giới tính":
                                        product.setSex(MyUtils.getBooleanSexValue(str_detail));
                                        break;
                                    case "Xuất xứ":
                                        product.setOrigin(str_detail);
                                        break;
                                    case "Nồng độ":
                                        perfume.setConcentration(str_detail);
                                        break;
                                    case "Năm phát hành":
                                        perfume.setRelease(MyUtils.parseStringToInt(str_detail));
                                        break;
                                    case "Nhóm hương":
                                        perfume.setIncense(str_detail);
                                        break;
                                    case "Phong cách":
                                        Style style = new Style();
                                        style.setContent(str_detail);
                                        lst_style.add(style);
                                        break;
                                }
                            }

                            //Get price
                            if (ele.getName().toString().equals("span")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));

                                if (attr != null) {
                                    if (attr.getValue().equals("variation-price") && inSpan100ML) {
                                        str_detail = takeAbsoluteContentFromreader(reader);
                                        //System.out.println("price: " + str_detail);
                                        product.setPrice(MyUtils.parseStringMoneyToBigInt(str_detail));
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

                            perfume.setType("Perfume");
                            if (ele.getName().toString().equals("table")) {
                                perfume.setStyle(lst_style);
                                inTaleInfo = false;
                            }
                            if(product.getPrice() == null) {
                                product.setPrice(BigInteger.valueOf(1));
                            }
                            perfume.setProduct(product);
                        }//end if end element
                    }

                    boolean isCheckValidate = MyUtils.validateXMLBeforeSaveToDatabase(perfume, Constant.PATH_XML + Constant.NAME_BUFFERED_PAGE_XML,
                            Constant.PATH_SCHEMA + Constant.NAME_SCHEMA_PERFUME);
                    if (isCheckValidate) {
                        MyUtils.savePerfumeDB(perfume);
                    }
                    //TODO test remove
//                    if(!isCheckValidate) {
//                        System.exit(0);
//                    }//END TODO
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XMLStreamException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                    //TODO test remove
                    //System.exit(0);//END TODO
                } catch (IOException ex) {
                    Logger.getLogger(OrchardCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(OrchardCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NamingException ex) {
                    Logger.getLogger(OrchardCrawler.class.getName()).log(Level.SEVERE, null, ex);
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

        Downloader downloader = new Downloader(htmlURL, filePath, startTagProp, endTagProp) {
            @Override
            public void analysMainHTML() {
                URL url = null;
                URLConnection con = null;
                InputStream is = null;
                BufferedReader br = null;
                try {
                    url = new URL(htmlURL);
                    con = url.openConnection();
                    con.addRequestProperty("User-agent", Constant.USER_AGENT);
                    is = con.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                    String inputLine;
                    ArrayList<String> matchedGroup;

                    while ((inputLine = br.readLine()) != null) {
                        matchedGroup = MyUtils.getGroupLineByPattern(inputLine, "<a[a-zA-Z0-9\\:\\//\\.\\-\\\"\\=\\ ]*__link\">");

                        for (String mg : matchedGroup) {
                            //get href content
                            mg = MyUtils.getLineByPattern(mg, "https:.*\\/");
                            downloadHTML(mg, Constant.NAME_ORCHARD_PAGE);
                        }
                    }
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DownloadPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DownloadPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

            @Override
            public void downloadHTML(String pageUrl, String fileName) {
                System.out.println("page URL: " + pageUrl);
                URL url = null;
                URLConnection con = null;
                InputStream is = null;
                BufferedReader br = null;
                Writer writer = null;
                try {
                    url = new URL(pageUrl);
                    con = url.openConnection();
                    con.addRequestProperty("User-agent", Constant.USER_AGENT);
                    is = con.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format("%s\\%s", filePath, fileName)), "UTF-8"));

                    String inputLine;
                    String detail;

                    writer.write("<div>\n");
                    while ((inputLine = br.readLine()) != null) {
                        if (inputLine.contains("<div id=\"content\"")) {
                            inputLine = MyUtils.beautifyHTML(inputLine);
                            detail = MyUtils.getLineByPattern(inputLine, "<div class=\"product-thumb-wrap.*<div id=\"product-single-thumbnail").trim();
                            detail = detail.substring(0, detail.lastIndexOf("<div"));
                            writer.write(detail + "\n");

                            detail = MyUtils.getLineByPattern(inputLine, "<h1 id=\"product-title\".*</h1>").trim();
                            writer.write(detail + "\n");

                            detail = MyUtils.getLineByPattern(inputLine, "<table class=\"shop_attributes.*<div class=\"col-md-6").trim();
                            detail = detail.substring(0, detail.lastIndexOf("</div"));
                            writer.write(detail + "\n");

                            detail = MyUtils.getLineByPattern(inputLine, "<table class=\"product-variation-table.*</table>").trim();
                            detail = detail.replace("<br>", "");
                            writer.write(detail + "\n");
//                            writer.write(inputLine + "\n");
                        }
                    }
                    writer.write("</div>");
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DownloadOrchard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DownloadOrchard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DownloadOrchard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        //Dowload finish
                        //Clean HTML begin
                        parser.cleanHTML(new InputStreamReader(new FileInputStream(Constant.PATH_HTML + Constant.NAME_ORCHARD_PAGE), "UTF-8"),
                                Constant.PATH_HTML + Constant.NAME_BUFFERED_PAGE);
                        //Clean finish Start parse
                        parser.parsingHTML(filePath + Constant.NAME_BUFFERED_PAGE);
                        //Parse finish
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(OrchardCrawler.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(OrchardCrawler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        downloader.analysMainHTML();
    }
}
