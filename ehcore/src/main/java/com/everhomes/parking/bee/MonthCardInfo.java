// @formatter:off
package com.everhomes.parking.bee;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 15:33
 */
public class MonthCardInfo {
    private String cardcode;
    private String carddate;
    private String cardstatus;
    private String carid;
    private String carnumber;
    private String carnumberlist;
    private String disabletime;
    private String enddate;
    private String id;
    private String identitynum;
    private String mebtel;
    private String mebtypename;
    private String operatorname;
    private String price;
    private String realname;
    private String sex;
    private String typename;
    private String userid;
    private String cardtypeid;

    public String getCardcode() {
        return cardcode;
    }

    public void setCardcode(String cardcode) {
        this.cardcode = cardcode;
    }

    public String getCarddate() {
        return carddate;
    }

    public void setCarddate(String carddate) {
        this.carddate = carddate;
    }

    public String getCardstatus() {
        return cardstatus;
    }

    public void setCardstatus(String cardstatus) {
        this.cardstatus = cardstatus;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getCarnumberlist() {
        return carnumberlist;
    }

    public void setCarnumberlist(String carnumberlist) {
        this.carnumberlist = carnumberlist;
    }

    public String getDisabletime() {
        return disabletime;
    }

    public void setDisabletime(String disabletime) {
        this.disabletime = disabletime;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentitynum() {
        return identitynum;
    }

    public void setIdentitynum(String identitynum) {
        this.identitynum = identitynum;
    }

    public String getMebtel() {
        return mebtel;
    }

    public void setMebtel(String mebtel) {
        this.mebtel = mebtel;
    }

    public String getMebtypename() {
        return mebtypename;
    }

    public void setMebtypename(String mebtypename) {
        this.mebtypename = mebtypename;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCardtypeid() {
        return cardtypeid;
    }

    public void setCardtypeid(String cardtypeid) {
        this.cardtypeid = cardtypeid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public boolean isNormalCarStatus() {
        return "1".equals(cardstatus);
    }
}
