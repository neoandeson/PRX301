
package mp.generatedObj;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Concentration" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="Release">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="1900"/>
 *               &lt;maxExclusive value="2018"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Incense" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Style" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://xml.netbeans.org/schema/product}Product"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "type",
    "concentration",
    "release",
    "incense",
    "style",
    "description",
    "product"
})
@XmlRootElement(name = "Outfit", namespace = "http://xml.netbeans.org/schema/outfit")
public class Outfit {

    @XmlElement(name = "Type", namespace = "http://xml.netbeans.org/schema/outfit", required = true)
    protected String type;
    @XmlElement(name = "Concentration", namespace = "http://xml.netbeans.org/schema/outfit", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger concentration;
    @XmlElement(name = "Release", namespace = "http://xml.netbeans.org/schema/outfit")
    protected int release;
    @XmlElement(name = "Incense", namespace = "http://xml.netbeans.org/schema/outfit", required = true)
    protected String incense;
    @XmlElement(name = "Style", namespace = "http://xml.netbeans.org/schema/outfit", required = true)
    protected String style;
    @XmlElement(name = "Description", namespace = "http://xml.netbeans.org/schema/outfit", required = true)
    protected String description;
    @XmlElement(name = "Product", namespace = "http://xml.netbeans.org/schema/product", required = true)
    protected Product product;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the concentration property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getConcentration() {
        return concentration;
    }

    /**
     * Sets the value of the concentration property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setConcentration(BigInteger value) {
        this.concentration = value;
    }

    /**
     * Gets the value of the release property.
     * 
     */
    public int getRelease() {
        return release;
    }

    /**
     * Sets the value of the release property.
     * 
     */
    public void setRelease(int value) {
        this.release = value;
    }

    /**
     * Gets the value of the incense property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncense() {
        return incense;
    }

    /**
     * Sets the value of the incense property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncense(String value) {
        this.incense = value;
    }

    /**
     * Gets the value of the style property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the value of the style property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStyle(String value) {
        this.style = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link Product }
     *     
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link Product }
     *     
     */
    public void setProduct(Product value) {
        this.product = value;
    }

}
