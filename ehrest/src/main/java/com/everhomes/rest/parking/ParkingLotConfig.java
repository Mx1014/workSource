package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/19.
 */
public class ParkingLotConfig {

    //是否支持临时车缴费
    private Byte tempfeeFlag = 0;
    //是否支持添加/删除费率
    private Byte rateFlag = 0;
    //是否支持锁车
    private Byte lockCarFlag = 0;
    //是否支持寻车
    private Byte searchCarFlag = 0;
    //显示当前在场车/当前剩余车位
    private Byte currentInfoType = 0;
    //停车场客服联系方式
    private String contact;

    private Byte invoiceFlag = 0;

    private Byte businessLicenseFlag = 0;
    private Byte vipParkingFlag = 0;
    private Byte monthRechargeFlag = 1;
    private Byte identityCardFlag = 0;
    private Byte monthCardFlag = 0;
    private Integer flowMode;
    private Byte noticeFlag = 0;

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Byte getMonthRechargeFlag() {
        return monthRechargeFlag;
    }

    public void setMonthRechargeFlag(Byte monthRechargeFlag) {
        this.monthRechargeFlag = monthRechargeFlag;
    }


	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
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
