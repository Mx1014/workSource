package com.everhomes.point;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class PointCommodity {

    private String shopNo;
    private String commoNo;
    private String modelNo;
    private String commoName;
    private String defaultPic;
    private Integer deductionIntegral;
    private BigDecimal deductionMoney;
    private BigDecimal price;
    private Integer sellNum;
    private String detailUrl;

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getCommoNo() {
        return commoNo;
    }

    public void setCommoNo(String commoNo) {
        this.commoNo = commoNo;
    }

    public String getCommoName() {
        return commoName;
    }

    public void setCommoName(String commoName) {
        this.commoName = commoName;
    }

    public String getDefaultPic() {
        return defaultPic;
    }

    public void setDefaultPic(String defaultPic) {
        this.defaultPic = defaultPic;
    }

    public Integer getDeductionIntegral() {
        return deductionIntegral;
    }

    public void setDeductionIntegral(Integer deductionIntegral) {
        this.deductionIntegral = deductionIntegral;
    }

    public BigDecimal getDeductionMoney() {
        return deductionMoney;
    }

    public void setDeductionMoney(BigDecimal deductionMoney) {
        this.deductionMoney = deductionMoney;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
