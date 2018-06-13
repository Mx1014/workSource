// @formatter:off
package com.everhomes.parking.bee;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/31 14:04
 */
public class TempCardInfo {
    private String carnumber;
    private String datenow;
    private String intime;
    private String iscard;
    private String isparking;
    private String orderid;
    private String outtime;
    private String parktime;
    private String ploname;
    private String orderamount;
    private String discountamount;
    private String prepaymentamount;
    private String receivableprice;
    private String resultcode;
    private String resultmsg;
    private String state;

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getDatenow() {
        return datenow;
    }

    public void setDatenow(String datenow) {
        this.datenow = datenow;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getIscard() {
        return iscard;
    }

    public void setIscard(String iscard) {
        this.iscard = iscard;
    }

    public String getIsparking() {
        return isparking;
    }

    public void setIsparking(String isparking) {
        this.isparking = isparking;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getParktime() {
        return parktime;
    }

    public void setParktime(String parktime) {
        this.parktime = parktime;
    }

    public String getPloname() {
        return ploname;
    }

    public void setPloname(String ploname) {
        this.ploname = ploname;
    }

    public String getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(String orderamount) {
        this.orderamount = orderamount;
    }

    public String getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(String discountamount) {
        this.discountamount = discountamount;
    }

    public String getPrepaymentamount() {
        return prepaymentamount;
    }

    public void setPrepaymentamount(String prepaymentamount) {
        this.prepaymentamount = prepaymentamount;
    }

    public String getReceivableprice() {
        return receivableprice;
    }

    public void setReceivableprice(String receivableprice) {
        this.receivableprice = receivableprice;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResultmsg() {
        return resultmsg;
    }

    public void setResultmsg(String resultmsg) {
        this.resultmsg = resultmsg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isTempCard(){
        return "0".equals(iscard);
    }

    public boolean isNormalState(){
        return "1".equals(state);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public boolean isNotParking() {
        return "0".equals(isparking);////是否有停车记录； 1 有， 0 没有
    }
}
