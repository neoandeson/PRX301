
package mp.generatedObj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="Concentration" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Release">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="1900"/>
 *               &lt;maxExclusive value="2019"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Incense" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://xml.netbeans.org/schema/style}Style" maxOccurs="5"/>
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
    "product"
})
@XmlRootElement(name = "Perfume")
public class Perfume {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "Concentration", required = true)
    protected String concentration;
    @XmlElement(name = "Release")
    protected int release;
    @XmlElement(name = "Incense", required = true)
    protected String incense;
    @XmlElement(name = "Style", namespace = "http://xml.netbeans.org/schema/style", required = true)
    protected List<Style> style;
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
     *     {@link String }
     *     
     */
    public String getConcentration() {
        return concentration;
    }

    /**
     * Sets the value of the concentration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConcentration(String value) {
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the style property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStyle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Style }
     * 
     * 
     */
    public List<Style> getStyle() {
        if (style == null) {
            style = new ArrayList<Style>();
        }
        return this.style;
    }
    
    public void setStyle(ArrayList<Style> styles) {
        this.style = styles;
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
