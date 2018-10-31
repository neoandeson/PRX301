/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.Utils;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.ErrorListener;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 *
 * @author trant
 */
public class JAXBUtils {

    public static final String BRAND_SCHEMA_FILE_PATH = "web/WEB-INF/schemas/Company.xsd";

    public static void generateObject(String schemaFilePath, String packageName, String outputPath) throws IOException {
        SchemaCompiler sc = XJC.createSchemaCompiler();
        sc.setErrorListener(new ErrorListener() {
            @Override
            public void error(SAXParseException saxpe) {
                System.out.println("error" + saxpe.getMessage());
            }

            @Override
            public void fatalError(SAXParseException saxpe) {
                System.out.println("fatal" + saxpe.getMessage());
            }

            @Override
            public void warning(SAXParseException saxpe) {
                System.out.println("warning" + saxpe.getMessage());
            }

            @Override
            public void info(SAXParseException saxpe) {
                System.out.println("info" + saxpe.getMessage());
            }
        });
        if (packageName != null && !packageName.trim().isEmpty()) {
            sc.forcePackageName(packageName);
        }
        File schema = new File(schemaFilePath);
        InputSource is = new InputSource(schema.toURI().toString());
        sc.parseSchema(is);
        S2JJAXBModel model = sc.bind();
        JCodeModel code = model.generateCode(null, null);
        code.build(new File(outputPath));
        System.out.println("Finished");

    }

    public static <T> void marshallXML(String xmlFilePath, T entity) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(entity.getClass());
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(entity, new File(xmlFilePath));
    }
}
