
package cn.cpst.rit.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Mail complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Mail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="actionDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="actionInfoOut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mailCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="officeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="relationOfficeDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mail", propOrder = {
    "actionDateTime",
    "actionInfoOut",
    "mailCode",
    "officeName",
    "relationOfficeDesc"
})
public class Mail {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar actionDateTime;
    @XmlElementRef(name = "actionInfoOut", namespace = "http://model.rit.cpst.cn", type = JAXBElement.class, required = false)
    protected JAXBElement<String> actionInfoOut;
    @XmlElementRef(name = "mailCode", namespace = "http://model.rit.cpst.cn", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mailCode;
    @XmlElementRef(name = "officeName", namespace = "http://model.rit.cpst.cn", type = JAXBElement.class, required = false)
    protected JAXBElement<String> officeName;
    @XmlElementRef(name = "relationOfficeDesc", namespace = "http://model.rit.cpst.cn", type = JAXBElement.class, required = false)
    protected JAXBElement<String> relationOfficeDesc;

    /**
     * 获取actionDateTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getActionDateTime() {
        return actionDateTime;
    }

    /**
     * 设置actionDateTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setActionDateTime(XMLGregorianCalendar value) {
        this.actionDateTime = value;
    }

    /**
     * 获取actionInfoOut属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getActionInfoOut() {
        return actionInfoOut;
    }

    /**
     * 设置actionInfoOut属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setActionInfoOut(JAXBElement<String> value) {
        this.actionInfoOut = value;
    }

    /**
     * 获取mailCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMailCode() {
        return mailCode;
    }

    /**
     * 设置mailCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMailCode(JAXBElement<String> value) {
        this.mailCode = value;
    }

    /**
     * 获取officeName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOfficeName() {
        return officeName;
    }

    /**
     * 设置officeName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOfficeName(JAXBElement<String> value) {
        this.officeName = value;
    }

    /**
     * 获取relationOfficeDesc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRelationOfficeDesc() {
        return relationOfficeDesc;
    }

    /**
     * 设置relationOfficeDesc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRelationOfficeDesc(JAXBElement<String> value) {
        this.relationOfficeDesc = value;
    }

}
