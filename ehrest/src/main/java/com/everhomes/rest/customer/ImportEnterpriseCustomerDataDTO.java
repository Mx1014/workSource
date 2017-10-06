package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/19.
 */
public class ImportEnterpriseCustomerDataDTO {
    private String name = "";
    private String categoryItemName = "";
    private String levelItemName = "";
    private String contactName = "";
    private String contactMobile = "";
    private String contactPhone = "";
    private String contactAddress = "";

    public String getCategoryItemName() {
        return categoryItemName;
    }

    public void setCategoryItemName(String categoryItemName) {
        this.categoryItemName = categoryItemName;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLevelItemName() {
        return levelItemName;
    }

    public void setLevelItemName(String levelItemName) {
        this.levelItemName = levelItemName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
