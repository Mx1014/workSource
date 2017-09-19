package com.everhomes.rest.parking;

/**
 * @author sw on 2017/9/19.
 */
public class ParkingLotConfig {

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

    private Byte invoiceFlag;

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

    public Byte getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Byte invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }
}
