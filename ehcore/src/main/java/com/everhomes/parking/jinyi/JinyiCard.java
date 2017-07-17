package com.everhomes.parking.jinyi;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/7/13.
 */
public class JinyiCard {
    private String calcid;
    private String calcno;
    private String parkingid;
    private String appid;
    private String plateno;
    private String platecolor;
    private String mobile;
    private String ownername;
    private String parkingname;
    private String effectdate;
    private String expiredate;
    private String operatetime;
    private BigDecimal discount;
    private BigDecimal paidin;
    private BigDecimal receivable;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getCalcid() {
        return calcid;
    }

    public void setCalcid(String calcid) {
        this.calcid = calcid;
    }

    public String getCalcno() {
        return calcno;
    }

    public void setCalcno(String calcno) {
        this.calcno = calcno;
    }

    public String getParkingid() {
        return parkingid;
    }

    public void setParkingid(String parkingid) {
        this.parkingid = parkingid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPlateno() {
        return plateno;
    }

    public void setPlateno(String plateno) {
        this.plateno = plateno;
    }

    public String getPlatecolor() {
        return platecolor;
    }

    public void setPlatecolor(String platecolor) {
        this.platecolor = platecolor;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public String getEffectdate() {
        return effectdate;
    }

    public void setEffectdate(String effectdate) {
        this.effectdate = effectdate;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getPaidin() {
        return paidin;
    }

    public void setPaidin(BigDecimal paidin) {
        this.paidin = paidin;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }
}
