// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.util.StringHelper;

public class ParkingLot extends EhParkingLots {
    private static final long serialVersionUID = 4551895615872424333L;

    public ParkingLot() {
    }

    //是否支持过期充值
    private Byte expiredRechargeFlag;
    //支持过期充值时，最多过期天数
    private Integer maxExpiredDay;
    //支持过期充值时，至少充值几个月
    private Integer expiredRechargeMonthCount;
    //支持过期充值时，按照什么模式充值 {@link ParkingCardExpiredRechargeType}
    private Byte expiredRechargeType;

    //是否支持临时车缴费
    private Byte tempfeeFlag;
    //是否支持添加/删除费率
    private Byte rateFlag;
    //是否支持锁车
    private Byte lockCarFlag;
    //是否支持寻车
    private Byte searchCarFlag;
    //显示当前在场车/当前剩余车位
    private Byte currentInfoType;
    //停车场客服联系方式
    private String contact;

    private Byte monthlyDiscountFlag;
    private String monthlyDiscount;

    private Byte tempFeeDiscountFlag;
    private String tempFeeDiscount;

    private Byte invoiceFlag;
    private Byte vipParkingFlag;
    private Byte monthRechargeFlag;
    private Byte businessLicenseFlag;
    private Byte identityCardFlag;
    private Byte monthCardFlag;//是否支持月卡申请，用于应用创建。
    private Integer flowMode;
    private Byte noticeFlag;

    public Integer getFlowMode() {
        return flowMode;
    }

    public void setFlowMode(Integer flowMode) {
        this.flowMode = flowMode;
    }

    public Byte getMonthCardFlag() {
        return monthCardFlag;
    }

    public void setMonthCardFlag(Byte monthCardFlag) {
        this.monthCardFlag = monthCardFlag;
    }

    public Byte getIdentityCardFlag() {
        return identityCardFlag;
    }

    public void setIdentityCardFlag(Byte identityCardFlag) {
        this.identityCardFlag = identityCardFlag;
    }
    public Byte getVipParkingFlag() {
        return vipParkingFlag;
    }

    public void setVipParkingFlag(Byte vipParkingFlag) {
        this.vipParkingFlag = vipParkingFlag;
    }

    public Byte getBusinessLicenseFlag() {
        return businessLicenseFlag;
    }

    public void setBusinessLicenseFlag(Byte businessLicenseFlag) {
        this.businessLicenseFlag = businessLicenseFlag;
    }

    public Byte getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Byte invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public Byte getMonthlyDiscountFlag() {
        return monthlyDiscountFlag;
    }

    public void setMonthlyDiscountFlag(Byte monthlyDiscountFlag) {
        this.monthlyDiscountFlag = monthlyDiscountFlag;
    }

    public String getMonthlyDiscount() {
        return monthlyDiscount;
    }

    public void setMonthlyDiscount(String monthlyDiscount) {
        this.monthlyDiscount = monthlyDiscount;
    }

    public String getTempFeeDiscount() {
        return tempFeeDiscount;
    }

    public void setTempFeeDiscount(String tempFeeDiscount) {
        this.tempFeeDiscount = tempFeeDiscount;
    }

    public Byte getTempFeeDiscountFlag() {
        return tempFeeDiscountFlag;
    }

    public void setTempFeeDiscountFlag(Byte tempFeeDiscountFlag) {
        this.tempFeeDiscountFlag = tempFeeDiscountFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Byte getExpiredRechargeFlag() {
        return expiredRechargeFlag;
    }

    public void setExpiredRechargeFlag(Byte expiredRechargeFlag) {
        this.expiredRechargeFlag = expiredRechargeFlag;
    }

    public Integer getMaxExpiredDay() {
        return maxExpiredDay;
    }

    public void setMaxExpiredDay(Integer maxExpiredDay) {
        this.maxExpiredDay = maxExpiredDay;
    }

    public Integer getExpiredRechargeMonthCount() {
        return expiredRechargeMonthCount;
    }

    public void setExpiredRechargeMonthCount(Integer expiredRechargeMonthCount) {
        this.expiredRechargeMonthCount = expiredRechargeMonthCount;
    }

    public Byte getExpiredRechargeType() {
        return expiredRechargeType;
    }

    public void setExpiredRechargeType(Byte expiredRechargeType) {
        this.expiredRechargeType = expiredRechargeType;
    }

    public Byte getTempfeeFlag() {
        return tempfeeFlag;
    }

    public void setTempfeeFlag(Byte tempfeeFlag) {
        this.tempfeeFlag = tempfeeFlag;
    }

    public Byte getRateFlag() {
        return rateFlag;
    }

    public void setRateFlag(Byte rateFlag) {
        this.rateFlag = rateFlag;
    }

    public Byte getLockCarFlag() {
        return lockCarFlag;
    }

    public void setLockCarFlag(Byte lockCarFlag) {
        this.lockCarFlag = lockCarFlag;
    }

    public Byte getSearchCarFlag() {
        return searchCarFlag;
    }

    public void setSearchCarFlag(Byte searchCarFlag) {
        this.searchCarFlag = searchCarFlag;
    }

    public Byte getCurrentInfoType() {
        return currentInfoType;
    }

    public void setCurrentInfoType(Byte currentInfoType) {
        this.currentInfoType = currentInfoType;
    }

    public Byte getMonthRechargeFlag() {
        return monthRechargeFlag;
    }

    public void setMonthRechargeFlag(Byte monthRechargeFlag) {
        this.monthRechargeFlag = monthRechargeFlag;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    
	public Byte getNoticeFlag() {
		return noticeFlag;
	}

	public void setNoticeFlag(Byte noticeFlag) {
		this.noticeFlag = noticeFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
