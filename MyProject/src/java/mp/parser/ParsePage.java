/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mp.Utils.Constant;
import mp.Utils.MyUtils;

/**
 *
 * @author ASUS
 */
public class ParsePage {

    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            File file = new File(Constant.PATH_HTML + "/" + Constant.NAME_THEGIOINUOCHOA_MALE);
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                MyUtils.getLineByPattern2(st, "<a class=\"product-name pro-color\"[a-zA-Z0-9 \\/\\-\\.\\=\\\"\\>]*</a>");
            }
            

//        Parser parser = new Parser();
//        //parser.parseHTML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE);
//        //parser.parseHTMLToXML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE, "div", "class", "product-item");
//        parser.parseHTML(Constant.HTML_PATH + "/" + Constant.NAME_THEGIOINUOCHOA_MALE, "div", "class", "product-item");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ParsePage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void parseTheGioiNuocHoa() {
        Parser parser = new Parser();
        parser.parseHTML(Constant.PATH_HTML + "/" + Constant.NAME_THEGIOINUOCHOA_MALE, "div", "class", "product-item");
    }
}
