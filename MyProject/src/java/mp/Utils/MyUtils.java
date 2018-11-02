/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.NamingException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import mp.DAO.Perfum_has_StyleDAO;
import mp.DAO.PerfumeDAO;
import mp.DAO.StyleDAO;
import mp.generatedObj.Perfume;
import mp.generatedObj.Product;
import mp.generatedObj.Style;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author ASUS
 */
public class MyUtils {

    /**
     * Returns String that is the end tag of html line input </tag>
     *
     * @param String inputHTMLLine
     * @return the string end tag
     * @see String
     */
    //TODO unnecessary remove
    public static String getCloseTag(String inputHTMLLine) {
        String result = "";
        Pattern p = Pattern.compile("^[<]\\w+[ ]");
        Matcher m = p.matcher(inputHTMLLine);

        if (m.find()) {
            result = m.group(0).trim().replace("<", "</").concat(">");
            return result;
        }
        return result;
    }

    public static String getLineByPattern(String input, String pattern) {
        String result = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);

        if (m.find()) {
            result = m.group(0).trim();
            //System.out.println("getLineByPattern result: " + result);
            return result;
        }
        return result;
    }

    public static String getLineByPattern(String input, String pattern, String removeString) {
        String result = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);

        if (m.find()) {
            result = m.group(0).trim().replace(removeString, "");
            //System.out.println("getLineByPattern() result: " + result);
            return result;
        }
        return result;
    }

    //TODO find remove
    public static String getLineByPattern2(String input, String pattern) {
        String result = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);

        while (m.find()) {
            System.out.println("result: " + m.group());
        }

        return result;
    }

    /**
     * Returns ArrayList<String> that contains substring match the pattern
     * </tag>
     *
     * @param String input
     * @param String pattern
     * @return the ArrayList<String>
     * @see ArrayList<String>
     */
    public static ArrayList<String> getGroupLineByPattern(String input, String pattern) {
        ArrayList<String> results = new ArrayList<>();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);

        while (m.find()) {
            results.add(m.group());
            //System.out.println("result: " + m.group());
        }

        return results;
    }

    public static String modifyToBeautifulHTML(String line) {
        // beautify inputline
        line = line.trim();

//        if (line.matches(".*&[^;]+")) {
//            line = line.replaceAll("&", "&#38;");
//        }
        if (line.contains("&amp;")) {
            line = line.replaceAll("&amp;", "&");
        }
        if (line.contains("&")) {
            line = line.replaceAll("&", "&amp;");
        }

        line = line.replace("&raquo;", "");
        line = line.replaceAll("</br>", "&#45;");
        line = line.replaceAll("<br/>", "&#45;");
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

        if (line.contains("required")) {
            line = line.replace("required", "");
        }

        if (line.contains("aria-=\"true\"")) {
            line = line.replace("aria-=\"true\"", "");
        }

        if (line.contains("class=\"input-box\"")) {
            line = line.replace("class=\"input-box\"", "");
        }

        return line;
    }

    public static String beautifyHTML(String line) {
//        if (line.contains("<br>")) {
//            line = line.replaceAll("<br>", "");
//        }
        if(line.contains("& ")){
            line = line.replaceAll("& ", "và ");
        } 
        if(line.contains(" &amp; ")){
            line = line.replaceAll(" &amp; ", " n ");
        }
        if (line.contains("&#8217;")) {
            line = line.replaceAll("&#8217;", "`");
        }
        if (line.contains("&#038;")) {
            line = line.replace("&#038;", "n");
        }
        if (line.contains("D&amp;G")) {
            line = line.replace("D&amp;G", "DnG");
        }
        if (line.contains("Dolce &#038; Gabbana")) {
            line = line.replace("Dolce &#038; Gabbana", "Dolce n Gabbana");
        }
        if (line.contains("Dolce &amp; Gabbana")) {
            line = line.replace("Dolce &amp; Gabbana", "Dolce n Gabbana");
        }
        if (line.contains("&#8220;")) {
            line = line.replace("&#8220;", "(");
            line = line.replace("&#8221;", ")");
        }
        
        if (line.contains("&amp;")) {
            line = line.replaceAll("&amp;", "&");
        } 
        if (line.contains("&")) {
            line = line.replaceAll("&", "&amp;amp;");
        }
        
        return line;
    }

    public static <T> boolean validateXMLBeforeSaveToDatabase(T obj, String xmlFilePath, String xsdPath) throws IOException {
        try {
            marshall(obj, xmlFilePath);

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File(xsdPath));
            InputSource source = new InputSource(new BufferedReader(new FileReader(xmlFilePath)));
            Validator validator = schema.newValidator();
            validator.validate(new SAXSource(source));
            return true;
        } catch (SAXException ex) {
            //Logger.getLogger(MyUtils.class.getName()).log(Level.SEVERE, null, ex);
            //TODO remove
//            Perfume p = (Perfume)obj;
//            System.out.println("imgURL: " + p.getProduct().getImageURL());
            System.out.println("\nERROR validateXMLBeforeSaveToDatabase---------------------------");
            System.out.println(ex.getMessage() + "\n");
            return false;
        }
    }

    public static void savePerfumeDB(Perfume perfume) throws SQLException, NamingException {
        StyleDAO styleDAO = null;
        int perfumeId = 0;
        int styleId = 0;

        PerfumeDAO perfumeDAO = new PerfumeDAO();
        Product product = perfume.getProduct();
        Perfum_has_StyleDAO p_h_sDAO;

        //Insert perfume
        if (!perfumeDAO.isExist(product.getBrand(), product.getName(), product.isSex(),
                product.getPrice().intValue(), perfume.getRelease(), perfume.getIncense())) {
            perfumeDAO.insert(perfume);
        }
        //Insert style
        styleDAO = new StyleDAO();
        for (Style style : perfume.getStyle()) {
            if (!styleDAO.isExist(style.getContent())) {
                styleDAO.insert(style);
            }

            //Check perfumeId and styleId isExist and != 0
            perfumeId = perfumeDAO.getId(product.getBrand(), product.getName(), product.isSex(),
                    product.getPrice().intValue(), perfume.getRelease(), perfume.getIncense());
            styleId = styleDAO.getId(style.getContent());
            if (perfumeId != 0 && styleId != 0) {
                p_h_sDAO = new Perfum_has_StyleDAO();
                if (!p_h_sDAO.isExist(perfumeId, styleId)) {
                    p_h_sDAO.insert(perfumeId, styleId);
                }
            }
        }
    }

    public static <T> void marshall(T obj, String outputFilePath) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
            Marshaller mar = ctx.createMarshaller();
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.marshal(obj, new File(outputFilePath));
        } catch (JAXBException ex) {
            Logger.getLogger(Marshaller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static <T> void unmarshall(T obj, String inputFilePath) {
        try {
            JAXBContext jc = JAXBContext.newInstance(obj.getClass());
            Unmarshaller u = jc.createUnmarshaller();
            File f = new File(inputFilePath);
            T tmp = (T) u.unmarshal(f);
            //TODO doing with object tmp
        } catch (JAXBException ex) {
            Logger.getLogger(Marshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BigInteger parseStringMoneyToBigInt(String strMoney) throws NumberFormatException {
        strMoney = strMoney.replaceAll("[.]", "");
        strMoney = strMoney.replaceAll("[VNĐ]", "");
        return BigInteger.valueOf(Integer.parseInt(strMoney.trim()));
    }

    public static int parseStringToInt(String str) {
        try {
            int tmp = Integer.parseInt(str);
            return tmp;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean getBooleanSexValue(String strSex) {
        return strSex.toLowerCase().equals("nam") ? true : false;
    }
}
