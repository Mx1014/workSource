package com.everhomes.parking.jinyi;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class JinyiClearance {
    /**
     * 卡号
     */
    private String cardno;
    /**
     * 车牌号
     */
    private String plateno;
    /**
     * 车牌颜色：蓝、黄、白、黑、绿、红，默认蓝
     */
    private String platecolor;
    /**
     * 入场时间
     */
    private String entrytime;
    /**
     * 入场编号
     */
    private String entryno;
    /**
     * 出场时间
     */
    private String exittime;
    /**
     * 出口编号
     */
    private String exitno;
    /**
     * 车辆停留时长(单位:分)
     */
    private Integer parkingtime;
    /**
     * 应收金额
     */
    private BigDecimal receivable;
    /**
     * 实收金额
     */
    private BigDecimal paidin;
    /**
     * 折扣金额
     */
    private BigDecimal discount;
    /**
     * 临卡月卡:1001临卡1002月卡
     */
    private Integer parkingtype;
    /**
     * 车辆类型:2001小型车、2002中型车、2003大型车
     */
    private Integer vehicletype;
    /**
     * 通行方式（etc=1301、mtc=1302、vi=1303）
     */
    private Integer throughmode;
    /**
     * 车场名称
     */
    private String parkingname;
    /**
     * 数据ID
     */
    private String id;

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
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

    public String getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    public String getEntryno() {
        return entryno;
    }

    public void setEntryno(String entryno) {
        this.entryno = entryno;
    }

    public String getExittime() {
        return exittime;
    }

    public void setExittime(String exittime) {
        this.exittime = exittime;
    }

    public String getExitno() {
        return exitno;
    }

    public void setExitno(String exitno) {
        this.exitno = exitno;
    }

    public Integer getParkingtime() {
        return parkingtime;
    }

    public void setParkingtime(Integer parkingtime) {
        this.parkingtime = parkingtime;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }

    public BigDecimal getPaidin() {
        return paidin;
    }

    public void setPaidin(BigDecimal paidin) {
        this.paidin = paidin;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getParkingtype() {
        return parkingtype;
    }

    public void setParkingtype(Integer parkingtype) {
        this.parkingtype = parkingtype;
    }

    public Integer getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(Integer vehicletype) {
        this.vehicletype = vehicletype;
    }

    public Integer getThroughmode() {
        return throughmode;
    }

    public void setThroughmode(Integer throughmode) {
        this.throughmode = throughmode;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
