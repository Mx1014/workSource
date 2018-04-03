// @formatter:off
package com.everhomes.parking.guangdawegu;

import com.everhomes.parking.handler.GuangDaWeGuParkingVendorHandler;
import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/1/31 15:36
 */
public class GuangDaWeGuTempCarData {
    private String carnum;
    private Integer carattr;
    private Integer objstate;
    private Integer selflock;
    private Integer feestate;
    private Long intime;
    private String parkarea;
    private Integer staydays;
    private Integer stayhours;
    private Integer stayminutes;
    private Double duestotal;
    private Double paidtotal;
    private Double dues;
    private Long oldexittime;
    private Long exittime;

    public boolean canPayTemporaryCar(){
        //feestate	缴费状态	int	0=无数据;2=免缴费;3=已缴费未超时;4=已缴费需续费;5=需要缴费
       return getCarattr()  != null
               && getCarattr() == GuangDaWeGuParkingVendorHandler.TEMPORARY_CAR
               && getDues() != null
               && getDues() > 0
               && (getFeestate()==4 || getFeestate()==5);
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public Integer getCarattr() {
        return carattr;
    }

    public void setCarattr(Integer carattr) {
        this.carattr = carattr;
    }

    public Integer getObjstate() {
        return objstate;
    }

    public void setObjstate(Integer objstate) {
        this.objstate = objstate;
    }

    public Integer getSelflock() {
        return selflock;
    }

    public void setSelflock(Integer selflock) {
        this.selflock = selflock;
    }

    public Integer getFeestate() {
        return feestate;
    }

    public void setFeestate(Integer feestate) {
        this.feestate = feestate;
    }

    public Long getIntime() {
        return intime;
    }

    public void setIntime(Long intime) {
        this.intime = intime;
    }

    public String getParkarea() {
        return parkarea;
    }

    public void setParkarea(String parkarea) {
        this.parkarea = parkarea;
    }

    public Integer getStaydays() {
        return staydays;
    }

    public void setStaydays(Integer staydays) {
        this.staydays = staydays;
    }

    public Integer getStayhours() {
        return stayhours;
    }

    public void setStayhours(Integer stayhours) {
        this.stayhours = stayhours;
    }

    public Integer getStayminutes() {
        return stayminutes;
    }

    public void setStayminutes(Integer stayminutes) {
        this.stayminutes = stayminutes;
    }

    public Double getDuestotal() {
        return duestotal;
    }

    public void setDuestotal(Double duestotal) {
        this.duestotal = duestotal;
    }

    public Double getPaidtotal() {
        return paidtotal;
    }

    public void setPaidtotal(Double paidtotal) {
        this.paidtotal = paidtotal;
    }

    public Double getDues() {
        return dues;
    }

    public void setDues(Double dues) {
        this.dues = dues;
    }

    public Long getOldexittime() {
        return oldexittime;
    }

    public void setOldexittime(Long oldexittime) {
        this.oldexittime = oldexittime;
    }

    public Long getExittime() {
        return exittime;
    }

    public void setExittime(Long exittime) {
        this.exittime = exittime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
