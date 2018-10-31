/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mp.Utils.Constant;
import mp.Utils.MyUtils;

/**
 *
 * @author ASUS
 */
public class DownloadPage {

    public static void main(String[] args) {
        //DOING: chinh sua ket hop bo parse trong luc crawl luon luu dung 1 file 
        downloadingTheGioiNuocHoa2(Constant.GET_URL_THEGIOINUOCHOA_MALE, Constant.PATH_HTML, "", "");
    }

    public static void downloadingTheGioiNuocHoa2(String htmlURL, String filePath, String startTagProp, String endTagProp) {
        String[] detailPages = new String[]{};

        Downloader crawler = new Downloader(htmlURL, filePath, startTagProp, endTagProp) {
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
                            downloadHTML(Constant.GET_PRE_THEGIOINUOCHOA + mg, "tgnhMale.html");
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
        crawler.analysMainHTML();
    }
}
