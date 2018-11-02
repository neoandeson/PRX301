/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.generator;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.ErrorListener;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 *
 * @author ASUS
 */
public class XJCGenerateJavaObj {

    public static void main(String[] args) {
        String[] schemas = new String[]{"Product.xsd", "Perfume.xsd", "Outfit.xsd", "Style.xsd"};
        try {
            String output = "src/java";
            SchemaCompiler sc = XJC.createSchemaCompiler();
            sc.setErrorListener(new ErrorListener() {
                @Override
                public void error(SAXParseException saxpe) {
                    System.out.println("Error: " + saxpe.getMessage());
                }

                @Override
                public void fatalError(SAXParseException saxpe) {
                    System.out.println("Fatal: " + saxpe.getMessage());
                }

                @Override
                public void warning(SAXParseException saxpe) {
                    System.out.println("Warning: " + saxpe.getMessage());
                }

                @Override
                public void info(SAXParseException saxpe) {
                    System.out.println("Info: " + saxpe.getMessage());
                }
            });
            sc.forcePackageName("mp.generatedObj");
            File schema;
            for (String scname : schemas) {
                schema = new File("src/java/mp/schemas/" + scname);
                InputSource is = new InputSource(schema.toURI().toString());
                sc.parseSchema(is);
                S2JJAXBModel mode = sc.bind();
                JCodeModel code = mode.generateCode(null, null);
                code.build(new File(output));
            }

            System.out.println("Finished generate");
        } catch (IOException ex) {
            Logger.getLogger(XJCGenerateJavaObj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
