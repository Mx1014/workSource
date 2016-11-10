package com.everhomes.rest.openapi.techpark;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class CustomerBuilding {
    /** 楼栋名称 */
    private String buildingName;
    
    /** 门牌号（房间号） */
    private String apartmentName;
    
    /** 面积，单位：平方米 */
    private BigDecimal areaSize;
    
    /** 联系人 */
    private String contact;
    
    /** 联系电话 */
    private String contactPhone;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public BigDecimal getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(BigDecimal areaSize) {
        this.areaSize = areaSize;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
