// @formatter:off
package com.everhomes.express.guomao;

import com.everhomes.util.StringHelper;

public class GuoMaoChinaPostResponse {
	private String billNo;
	private String orderNo;
	private String sendType;
	private String sendName;
	private String sendPhone;
	private String sendOrganization;
	private String sendProvince;
	private String sendCity;
	private String sendCounty;
	private String sendDetailAddress;
	private String receiveName;
	private String receivePhone;
	private String receiveOrganization;
	private String receiveProvince;
	private String receiveCity;
	private String receiveCounty;
	private String receiveDetailAddress;
	private String internal;
	private String insuredPrice;
	private String status;
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getSendPhone() {
		return sendPhone;
	}
	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}
	public String getSendOrganization() {
		return sendOrganization;
	}
	public void setSendOrganization(String sendOrganization) {
		this.sendOrganization = sendOrganization;
	}
	public String getSendProvince() {
		return sendProvince;
	}
	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}
	public String getSendCity() {
		return sendCity;
	}
	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}
	public String getSendCounty() {
		return sendCounty;
	}
	public void setSendCounty(String sendCounty) {
		this.sendCounty = sendCounty;
	}
	public String getSendDetailAddress() {
		return sendDetailAddress;
	}
	public void setSendDetailAddress(String sendDetailAddress) {
		this.sendDetailAddress = sendDetailAddress;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getReceivePhone() {
		return receivePhone;
	}
	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}
	public String getReceiveOrganization() {
		return receiveOrganization;
	}
	public void setReceiveOrganization(String receiveOrganization) {
		this.receiveOrganization = receiveOrganization;
	}
	public String getReceiveProvince() {
		return receiveProvince;
	}
	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}
	public String getReceiveCity() {
		return receiveCity;
	}
	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}
	public String getReceiveCounty() {
		return receiveCounty;
	}
	public void setReceiveCounty(String receiveCounty) {
		this.receiveCounty = receiveCounty;
	}
	public String getReceiveDetailAddress() {
		return receiveDetailAddress;
	}
	public void setReceiveDetailAddress(String receiveDetailAddress) {
		this.receiveDetailAddress = receiveDetailAddress;
	}
	public String getInternal() {
		return internal;
	}
	public void setInternal(String internal) {
		this.internal = internal;
	}
	public String getInsuredPrice() {
		return insuredPrice;
	}
	public void setInsuredPrice(String insuredPrice) {
		this.insuredPrice = insuredPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
