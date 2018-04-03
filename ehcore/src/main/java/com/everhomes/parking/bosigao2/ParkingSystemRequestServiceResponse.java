
package com.everhomes.parking.bosigao2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ParkingSystem_RequestServiceResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "parkingSystemRequestServiceResult"
})
@XmlRootElement(name = "ParkingSystem_RequestServiceResponse")
public class ParkingSystemRequestServiceResponse {

    @XmlElement(name = "ParkingSystem_RequestServiceResult")
    protected String parkingSystemRequestServiceResult;

    /**
     * ��ȡparkingSystemRequestServiceResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParkingSystemRequestServiceResult() {
        return parkingSystemRequestServiceResult;
    }

    /**
     * ����parkingSystemRequestServiceResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParkingSystemRequestServiceResult(String value) {
        this.parkingSystemRequestServiceResult = value;
    }

}
