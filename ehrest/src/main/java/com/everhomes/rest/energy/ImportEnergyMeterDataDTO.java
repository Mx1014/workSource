package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by ying.xiong on 2017/11/10.
 */
public class ImportEnergyMeterDataDTO {
    private String name = "";
    private String meterNumber = "";
    private String meterType = "";
    private String billCategory = "";
    private String serviceCategory = "";
    @ItemType(String.class)
    private List<String> buildingName = null;
    @ItemType(String.class)
    private List<String> apartmentName = null;
    private List<String>  burdenRate = null;
    private String maxReading = "";
    private String startReading = "";
    private String Rate = "";
    private String amountFormula = "";

    public String getAmountFormula() {
        return amountFormula;
    }

    public void setAmountFormula(String amountFormula) {
        this.amountFormula = amountFormula;
    }

    public String getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(String billCategory) {
        this.billCategory = billCategory;
    }

    public List<String> getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(List<String> buildingName) {
        this.buildingName = buildingName;
    }

    public List<String> getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(List<String> apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getMaxReading() {
        return maxReading;
    }

    public void setMaxReading(String maxReading) {
        this.maxReading = maxReading;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getStartReading() {
        return startReading;
    }

    public void setStartReading(String startReading) {
        this.startReading = startReading;
    }

    public List<String> getBurdenRate() {
        return burdenRate;
    }

    public void setBurdenRate(List<String> burdenRate) {
        this.burdenRate = burdenRate;
    }
}
