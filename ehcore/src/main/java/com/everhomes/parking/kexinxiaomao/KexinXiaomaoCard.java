package com.everhomes.parking.kexinxiaomao;

import com.everhomes.util.StringHelper;

public class KexinXiaomaoCard{
    private Integer flag;
    private String message;
    private String parkId;
    private String licenseNumber;
    private String parkName;
    private String memberType;
    private String createTime;
    private String beginTime;
    private String endTime;
    private String unitMoney;
    private String standardType;
    private String userName;
    
    public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getParkId() {
		return parkId;
	}

	public void setParkId(String parkId) {
		this.parkId = parkId;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUnitMoney() {
		return unitMoney;
	}

	public void setUnitMoney(String unitMoney) {
		this.unitMoney = unitMoney;
	}

	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public boolean isSuccess() {
		return flag!=null && flag==1;
	}
}
