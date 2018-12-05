package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;

public class RentalPriceClassificationDTO {

    private Byte userPriceType;
    private String classification;
    private BigDecimal workdayPrice;
    private BigDecimal originalPrice;
    private BigDecimal initiatePrice;
    private Byte discountType;
    private BigDecimal fullPrice;
    private BigDecimal cutPrice;
    private Double discountRatio;

    public Byte getUserPriceType() {
        return userPriceType;
    }

    public void setUserPriceType(Byte userPriceType) {
        this.userPriceType = userPriceType;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public BigDecimal getWorkdayPrice() {
        return workdayPrice;
    }

    public void setWorkdayPrice(BigDecimal workdayPrice) {
        this.workdayPrice = workdayPrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getInitiatePrice() {
        return initiatePrice;
    }

    public void setInitiatePrice(BigDecimal initiatePrice) {
        this.initiatePrice = initiatePrice;
    }

    public Byte getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Byte discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public BigDecimal getCutPrice() {
        return cutPrice;
    }

    public void setCutPrice(BigDecimal cutPrice) {
        this.cutPrice = cutPrice;
    }

    public Double getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(Double discountRatio) {
        this.discountRatio = discountRatio;
    }
}
