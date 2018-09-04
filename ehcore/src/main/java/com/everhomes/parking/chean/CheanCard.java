package com.everhomes.parking.chean;

public class CheanCard {

    private String UserNo;
    private String name;
    private String monthlyrent;
    private String tariffid;
    private String carno;
    private String expirydate;
    private String newexpirydate;
    private String groupID;
    private String groupCardNum;
    private String berth;
    private String chargeBy;

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonthlyrent() {
        return monthlyrent;
    }

    public void setMonthlyrent(String monthlyrent) {
        this.monthlyrent = monthlyrent;
    }

    public String getTariffid() {
        return tariffid;
    }

    public void setTariffid(String tariffid) {
        this.tariffid = tariffid;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getNewexpirydate() {
        return newexpirydate;
    }

    public void setNewexpirydate(String newexpirydate) {
        this.newexpirydate = newexpirydate;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupCardNum() {
        return groupCardNum;
    }

    public void setGroupCardNum(String groupCardNum) {
        this.groupCardNum = groupCardNum;
    }

    public String getBerth() {
        return berth;
    }

    public void setBerth(String berth) {
        this.berth = berth;
    }

    public String getChargeBy() {
        return chargeBy;
    }

    public void setChargeBy(String chargeBy) {
        this.chargeBy = chargeBy;
    }
}
