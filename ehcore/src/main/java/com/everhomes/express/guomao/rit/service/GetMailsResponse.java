
package com.everhomes.express.guomao.rit.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.everhomes.express.guomao.rit.model.ArrayOfMail;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="out" type="{http://model.rit.cpst.cn}ArrayOfMail"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "out"
})
@XmlRootElement(name = "getMailsResponse")
public class GetMailsResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfMail out;

    /**
     * 获取out属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMail }
     *     
     */
    public ArrayOfMail getOut() {
        return out;
    }

    /**
     * 设置out属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMail }
     *     
     */
    public void setOut(ArrayOfMail value) {
        this.out = value;
    }

}
