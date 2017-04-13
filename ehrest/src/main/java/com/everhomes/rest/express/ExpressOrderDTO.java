// @formatter:off
package com.everhomes.rest.express;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>sendName: 寄件人姓名</li>
 * <li>sendPhone: 寄件人手机</li>
 * <li>expressCompanyName: 快递公司</li>
 * <li>billNo: 快递单号</li>
 * <li>sendType: 寄件类型</li>
 * <li>payType: 付款方式</li>
 * <li>status: 订单状态</li>
 * <li>paySummary: 付费总计</li>
 * <li>列表只包含以上字段</li>
 * <li>sendOrganization: 寄件人公司</li>
 * <li>sendAddress: 寄件地址</li>
 * <li>receiveName: 收件人姓名</li>
 * <li>receivePhone: 收件人手机</li>
 * <li>receiveOrganization: 收件人公司</li>
 * <li>receiveAddress: 收件人地址</li>
 * <li>orderNo: 订单编号</li>
 * <li>createTime: 订单时间</li>
 * <li>serviceAddress: 自寄地址</li>
 * <li>internal: 内件物品</li>
 * <li>insuredPrice: 保价金额</li>
 * </ul>
 */
public class ExpressOrderDTO {
	private Long id;
	private String sendName;
	private String sendPhone;
	private String expressCompanyName;
	private String billNo;
	private Byte sendType;
	private Byte payType;
	private Byte status;
	private BigDecimal paySummary;
	
	private String sendOrganization;
	private String sendAddress;
	private String receiveName;
	private String receivePhone;
	private String receiveOrganization;
	private String receiveAddress;
	private String orderNo;
	private Timestamp createTime;
	private String serviceAddress;
	private String internal;
	private BigDecimal insuredPrice;
	
	public String getSendOrganization() {
		return sendOrganization;
	}

	public void setSendOrganization(String sendOrganization) {
		this.sendOrganization = sendOrganization;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
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

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getInternal() {
		return internal;
	}

	public void setInternal(String internal) {
		this.internal = internal;
	}

	public BigDecimal getInsuredPrice() {
		return insuredPrice;
	}

	public void setInsuredPrice(BigDecimal insuredPrice) {
		this.insuredPrice = insuredPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Byte getSendType() {
		return sendType;
	}

	public void setSendType(Byte sendType) {
		this.sendType = sendType;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public BigDecimal getPaySummary() {
		return paySummary;
	}

	public void setPaySummary(BigDecimal paySummary) {
		this.paySummary = paySummary;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
