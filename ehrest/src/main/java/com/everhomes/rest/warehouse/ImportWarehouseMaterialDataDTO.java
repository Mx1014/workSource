package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by ying.xiong on 2017/5/22.
 */
public class ImportWarehouseMaterialDataDTO {

    private String name = "";

    private String materialNumber = "";

    private String categoryName = "";

    private String categoryNumber = "";

    private String brand = "";

    private String itemNo = "";

    private String referencePrice = "";

    private String unitName = "";

    private String specificationInformation = "";

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(String categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getSpecificationInformation() {
        return specificationInformation;
    }

    public void setSpecificationInformation(String specificationInformation) {
        this.specificationInformation = specificationInformation;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
