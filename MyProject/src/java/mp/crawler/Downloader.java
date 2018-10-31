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
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import mp.Utils.Constant;

/**
 *
 * @author ASUS
 */
public abstract class Downloader {
    public String htmlURL = "";
    public String filePath = "";
    public String startTagProp = "";
    public String endTagProp = "";

    public Downloader(String htmlURL, String filePath, String startTagProp, String endTagProp) {
        this.htmlURL = htmlURL;
        this.filePath = filePath;
        this.startTagProp = startTagProp;
        this.endTagProp = endTagProp;
    }
    
    public abstract void analysMainHTML();
    
    public abstract void downloadHTML(String pageUrl, String fileName);
    
    //TODO sample remove
    public void downloadHTML(String htmlURL, String filePath, String fileName) throws IOException {
        
        URL url = null;
        URLConnection con = null;
        InputStream is = null;
        BufferedReader br = null;
        Writer writer = null;
        try {
            url = new URL(htmlURL);
            con = url.openConnection();
            con.addRequestProperty("User-agent", Constant.USER_AGENT);
            is = con.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format("%s\\%s", filePath, fileName)), "UTF-8"));
            String inputLine;

            boolean isBody = false;

            while ((inputLine = br.readLine()) != null) {
                if (inputLine.trim().matches(".*<body.*")) {
                    isBody = true;
                }
                if (inputLine.trim().matches(".*</html.*")) {
                    isBody = false;
                }

                if (isBody) {
                    inputLine = modifyToBeautifulHTML(inputLine);
                    writer.write(inputLine + "\n");
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
        }

    }

    public void downloadHTML2(String htmlURL, String filePath, String fileName) throws IOException {

        URL url = null;
        URLConnection con = null;
        InputStream is = null;
        BufferedReader br = null;
        Writer writer = null;
        try {
            url = new URL(htmlURL);
            con = url.openConnection();
            con.addRequestProperty("User-agent", Constant.USER_AGENT);
            is = con.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format("%s\\%s", filePath, fileName)), "UTF-8"));
            String inputLine;
            Stack<String> stack = new Stack<>();
            
            boolean isPlace = false;
            boolean isBody = false;
            boolean isScript = false; //dung de koi phai in may doan script ra
            while ((inputLine = br.readLine()) != null) {
                //if (inputLine.contains("class=\"product-list clearfix\"")) {
                if (inputLine.contains("class=\"products row")) {
                    isPlace = true;
                    writer.write("<div>" + "\n");// day la luc khi gap the div co id ="Rao vat"
                }
                if (inputLine.trim().matches(".*<div.*") && isPlace) {
                    isBody = true;
                    stack.push("</div>");//moi khi gap the div mo? thi stack luu the div dong
                } else if (inputLine.contains("</div>") && isBody && isPlace) {
                    isBody = false;
                    writer.write(stack.pop() + "\n"); // khi gap doan co the dong thi no ghi the div dong xuong
                }
                // Theo suy luan cua t la v ak deo bik dc ko chay thu cai. M hieu y t hong :)) hieu khoan
                if (isBody && isPlace) {
                    inputLine = modifyToBeautifulHTML(inputLine);
                    writer.write(inputLine + "\n");
                }
            }
            writer.write("</div>");//co ve v moi dung run thu m
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
        }

    }
    
    public void downloadHTML3(String htmlURL, String filePath, String fileName, String startTagProp, String endTagProp) throws IOException {
        
        URL url = null;
        URLConnection con = null;
        InputStream is = null;
        BufferedReader br = null;
        Writer writer = null;
        try {
            url = new URL(htmlURL);
            con = url.openConnection();
            con.addRequestProperty("User-agent", Constant.USER_AGENT);
            is = con.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format("%s\\%s", filePath, fileName)), "UTF-8"));
            String inputLine;

            boolean isTarget = false;

            while ((inputLine = br.readLine()) != null) {
                if (inputLine.trim().matches(startTagProp)) {
                    isTarget = true;
                }
                if (inputLine.trim().matches(endTagProp)) {
                    isTarget = false;
                    break;
                }

                if (isTarget) {
                    inputLine = modifyToBeautifulHTML(inputLine);
                    writer.write(inputLine + "\n");
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
        }

    }
    //END TODO sample remove
    
    private String modifyToBeautifulHTML(String line) {
        // beautify inputline
        line = line.trim();
        if (line.matches(".*&[^;]+")) {
            line = line.replaceAll("&", "&#38;");
        }

        if (line.contains("&")) {
            line = line.replace("&", "&amp;");
        }
        
        line = line.replace("&raquo;", "");
        line = line.replaceAll("</br>", "&#45;");
        line = line.replaceAll("<br>", "&#45;");
        line = line.replaceAll("<br />", "&#45;");
        line = line.replaceAll("<b>", "&#45;");
        line = line.replaceAll("</b>", "&#45;");

        if (line.contains("<img")) {
            String tmpLine = line.substring(0, line.indexOf("<img"));
            line = line.substring(line.indexOf("<img"));
            line = line.replaceFirst(">", "/>");
            line = line.replaceFirst("//>", "/>");
            line = tmpLine + line;
        }
        if (line.contains("<input")) {
            String tmpLine = line.substring(0, line.indexOf("<input"));
            line = line.substring(line.indexOf("<input"));
            line = line.replaceFirst(">", "/>");
            line = line.replaceFirst("//>", "/>");
            line = tmpLine + line;
        }
        if (line.contains("DIV")) {
            line = line.replace("DIV", "div");
        }

        //lúc crawl về m xóa chỗ modified HTML nè
        line = line.replace("itemscope", "");

        return line;
    }
}
