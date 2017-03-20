package com.everhomes.reserver;

import java.util.Date;

/**
 * Created by sw on 2017/3/1.
 */
public class ReserverDTO {
    private String shopName;
    private String shopPhone;
    private String buyerName;
    private String buyerPhone;
    private Integer status;
    private Integer dinnerNum;
    private String dinnerNote;
    private Date dinnerTime;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDinnerNum() {
        return dinnerNum;
    }

    public void setDinnerNum(Integer dinnerNum) {
        this.dinnerNum = dinnerNum;
    }

    public String getDinnerNote() {
        return dinnerNote;
    }

    public void setDinnerNote(String dinnerNote) {
        this.dinnerNote = dinnerNote;
    }

    public Date getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(Date dinnerTime) {
        this.dinnerTime = dinnerTime;
    }
}
