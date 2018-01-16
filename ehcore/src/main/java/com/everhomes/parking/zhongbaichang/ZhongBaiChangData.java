// @formatter:off
package com.everhomes.parking.zhongbaichang;

import com.everhomes.util.StringHelper;

public class ZhongBaiChangData {
	private String memberUuid;
	private String memberName;
	private String carNo;
	private String startTime;
	private String endTime;
	private String createTime;
	private String telephone;
	private String company;
	private String cardUuid;
	private String carTypeUuid;
	private String remark;
	
	private String effective;
	private String state;
	private String money;
	private String attr1;
	private String attr2;
	private String attr3;
	private String begainTime;
	private String cardName;
	private String carTypeName;
	private String parkCode;
	private String newCarNo;
	private String operateId;
	private String operateName;
	private String chargeBackMoney;
	private String alertContext;
	private String chargeBackDelete;
	
	private String version;
	private String operate;
	private String carType;
	public String getMemberUuid() {
		return memberUuid;
	}
	public void setMemberUuid(String memberUuid) {
		this.memberUuid = memberUuid;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCardUuid() {
		return cardUuid;
	}
	public void setCardUuid(String cardUuid) {
		this.cardUuid = cardUuid;
	}
	public String getCarTypeUuid() {
		return carTypeUuid;
	}
	public void setCarTypeUuid(String carTypeUuid) {
		this.carTypeUuid = carTypeUuid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEffective() {
		return effective;
	}
	public void setEffective(String effective) {
		this.effective = effective;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public String getAttr3() {
		return attr3;
	}
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	public String getBegainTime() {
		return begainTime;
	}
	public void setBegainTime(String begainTime) {
		this.begainTime = begainTime;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCarTypeName() {
		return carTypeName;
	}
	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}
	public String getParkCode() {
		return parkCode;
	}
	public void setParkCode(String parkCode) {
		this.parkCode = parkCode;
	}
	public String getNewCarNo() {
		return newCarNo;
	}
	public void setNewCarNo(String newCarNo) {
		this.newCarNo = newCarNo;
	}
	public String getOperateId() {
		return operateId;
	}
	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getChargeBackMoney() {
		return chargeBackMoney;
	}
	public void setChargeBackMoney(String chargeBackMoney) {
		this.chargeBackMoney = chargeBackMoney;
	}
	public String getAlertContext() {
		return alertContext;
	}
	public void setAlertContext(String alertContext) {
		this.alertContext = alertContext;
	}
	public String getChargeBackDelete() {
		return chargeBackDelete;
	}
	public void setChargeBackDelete(String chargeBackDelete) {
		this.chargeBackDelete = chargeBackDelete;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
