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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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

public class TGNHCrawler {

    public static void main(String[] args) {
        crawlingTGNH(Constant.GET_URL_THEGIOINUOCHOA_MALE, Constant.PATH_HTML, "", "");
    }

    public static void crawlingTGNH(String htmlURL, String filePath, String startTagProp, String endTagProp) {
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
                    boolean isGetFirstImg = false;
                    boolean isGetFirstPrice = false;

                    while (reader.hasNext()) {
                        XMLEvent event = reader.nextEvent();

                        if (event.isStartElement()) {
                            StartElement ele = (StartElement) event;

                            //Get name
                            if (ele.getName().toString().equals("h3")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));
                                if (attr != null) {
                                    if (attr.getValue().equals("product-title")) {
                                        System.out.println("Name: " + takeAbsoluteContentFromreader(reader));
                                    }
                                }
                            }
                            
                            //Get price
                            if (ele.getName().toString().equals("div")) {
                                Attribute attr = ele.getAttributeByName(new QName("class"));
                                if (attr != null) {
                                    if (attr.getValue().equals("price") && !isGetFirstPrice) {
                                        isGetFirstPrice = true;
                                        System.out.println("Price: " + takeAbsoluteContentFromreader(reader));
                                    }
                                }
                            }
                            
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
                                //only get first img
                                if (detail.matches(".*[.png]") && !isGetFirstImg) {
                                    isGetFirstImg = true;
                                    System.out.println("img: " + detail);
                                }
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
                                switch (detail) {
                                    case "Nhãn hiệu":
                                        System.out.println(takeAbsoluteContentFromTargetReader(reader, 2));
                                        break;
                                    case "Giới tính":
                                        System.out.println(takeAbsoluteContentFromTargetReader(reader, 2));
                                        break;
                                    case "Xuất xứ":
                                        System.out.println(takeAbsoluteContentFromTargetReader(reader, 2));
                                        break;
                                    case "Nồng độ":
                                        System.out.println(takeAbsoluteContentFromTargetReader(reader, 2));
                                        break;
                                    case "Phát hành":
                                        System.out.println(takeAbsoluteContentFromTargetReader(reader, 2));
                                        break;
                                    case "Nhóm hương":
                                        System.out.println(takeAbsoluteContentFromTargetReader(reader, 2));
                                        break;
                                    case "Phong cách":
                                        System.out.println(takeAbsoluteContentFromTargetReader(reader, 2));
                                        break;
                                    default:
                                        break;
                                }
                                //System.out.println("info: " + detail);
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
                                inDivDescription = false;
                            }

                        }//end if start element

                        if (event.isEndElement()) {
                            EndElement ele = (EndElement) event;

                            if (ele.getName().toString().equals("ul")) {
                                inUlInfo = false;
                            }
                        }//end if end element
                    }
                } catch (FileNotFoundException | UnsupportedEncodingException | XMLStreamException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void cleanHTML(InputStreamReader isr, String outputFilePath) {
                Writer writer = null;
                XMLEventReader reader = null;
                try {
                    XMLInputFactory xif = XMLInputFactory.newInstance();
                    xif.setEventAllocator(new XMLEventAllocatorImpl());
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
                } catch (XMLStreamException ex) {
                    Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TGNHCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        writer.close();
                        reader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (XMLStreamException ex) {
                        Logger.getLogger(TGNHCrawler.class.getName()).log(Level.SEVERE, null, ex);
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
                        matchedGroup = MyUtils.getGroupLineByPattern(inputLine, "<a class=\"product-name pro-color\"[a-zA-Z0-9 \\/\\-\\.\\=\\\"\\>]*</a>");
                        int count = 0;
                        for (String mg : matchedGroup) {
                            //get href content
                            mg = MyUtils.getLineByPattern(mg, "[\\/]+.*html");
                            downloadHTML(Constant.GET_PRE_THEGIOINUOCHOA + mg, Constant.NAME_THEGIOINUOCHOA_PAGE);
                            break;//TODO remove
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
                    boolean inTarget = false;

                    while ((inputLine = br.readLine()) != null) {
                        if (inputLine.contains("<!-- End product-detail -->")) {
                            inputLine = inputLine.substring(0, inputLine.lastIndexOf("</section>") + 10).trim();
                            inputLine = MyUtils.modifyToBeautifulHTML(inputLine);
                            writer.write(inputLine);
                            inTarget = false;
                        }
                        if (inputLine.contains("<section class=\"product-detail perfume-detail\">")) {
                            inputLine = inputLine.substring(inputLine.indexOf("<section class=\"product-detail perfume-detail\">"), inputLine.length()).trim();
                            inputLine = MyUtils.modifyToBeautifulHTML(inputLine);
                            inTarget = true;
                        }
                        if (inTarget) {
                            inputLine = MyUtils.modifyToBeautifulHTML(inputLine);
                            writer.write(inputLine.trim());
                        }
                    }
                    writer.close();

                    //Begin parse page to Object--------------------------------
                    //Clean HTML before parse
                    try {
                        parser.cleanHTML(new InputStreamReader(new FileInputStream(Constant.PATH_HTML + "/" + Constant.NAME_THEGIOINUOCHOA_PAGE), "UTF-8"),
                                Constant.PATH_HTML + "/" + Constant.NAME_BUFFERED_PAGE);
                    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                        Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Prasing
                    parser.parsingHTML(Constant.PATH_HTML + "/" + Constant.NAME_BUFFERED_PAGE);
                    //End parsing-----------------------------------------------

                } catch (MalformedURLException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DownloadPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
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

        };

        //analys get detail page hrefs and download detail pages
        downloader.analysMainHTML();

    }
}
