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
 * <li>expressCompanyId: 快递公司id</li>
 * <li>expressCompanyName: 快递公司</li>
 * <li>billNo: 快递单号</li>
 * <li>sendType: 寄件类型，参考{@link com.everhomes.rest.express.ExpressSendType}</li>
 * <li>quantityAndWeight: 数量及重量</li>
 * <li>packageType: 封装类型，参考{@link com.everhomes.rest.express.ExpressPackageType}</li>
 * <li>invoiceHead: 发票抬头</li>
 * <li>invoiceFlag: 需要发票，参考{@link com.everhomes.rest.express.ExpressInvoiceFlagType}</li>
 * <li>payType: 付款方式，参考{@link com.everhomes.rest.express.ExpressPayType}</li>
 * <li>status: 订单状态，参考{@link com.everhomes.rest.express.ExpressOrderStatus}</li>
 * <li>statusDesc: 订单状态描述</li>
 * <li>paySummary: 付费总计</li>
 * <li>sendOrganization: 寄件人公司</li>
 * <li>sendProvince: 寄件省</li>
 * <li>sendCity: 寄件市</li>
 * <li>sendCounty: 寄件区县</li>
 * <li>sendDetailAddress: 寄件详细地址</li>
 * <li>receiveName: 收件人姓名</li>
 * <li>receivePhone: 收件人手机</li>
 * <li>receiveOrganization: 收件人公司</li>
 * <li>receiveProvince: 收件人省</li>
 * <li>receiveCity: 收件人市</li>
 * <li>receiveCounty: 收件人区县</li>
 * <li>receiveDetailAddress: 收件人详细地址</li>
 * <li>orderNo: 订单编号</li>
 * <li>createTime: 订单时间</li>
 * <li>sendMode: 寄件方式，参考{@link com.everhomes.rest.express.ExpressSendMode}</li>
 * <li>serviceAddress: 自寄地址</li>
 * <li>internal: 内件物品</li>
 * <li>insuredPrice: 保价金额</li>
 * <li>paidFlag: 1是0否，表示是否点击过支付，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ExpressOrderDTO {
	private Long id;
	private String sendName;
	private String sendPhone;
	private Long expressCompanyId;
	private String expressCompanyName;
	private String billNo;
	private Byte sendType;
	private String quantityAndWeight;
	private Byte packageType;
	private String invoiceHead;
	private Byte invoiceFlag;
	private Byte payType;
	private Byte status;
	private String statusDesc;
	private BigDecimal paySummary;
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
	private String orderNo;
	private Timestamp createTime;
	private Byte sendMode;
	private String serviceAddress;
	private String internal;
	private BigDecimal insuredPrice;
	private String expressLogoUrl;
	private Byte paidFlag;

	public Byte getPaidFlag() {
		return paidFlag;
	}

	public void setPaidFlag(Byte paidFlag) {
		this.paidFlag = paidFlag;
	}

	public String getExpressLogoUrl() {
		return expressLogoUrl;
	}

	public void setExpressLogoUrl(String expressLogoUrl) {
		this.expressLogoUrl = expressLogoUrl;
	}

	public Long getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(Long expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
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

	public Byte getSendMode() {
		return sendMode;
	}

	public void setSendMode(Byte sendMode) {
		this.sendMode = sendMode;
	}

	public String getSendOrganization() {
		return sendOrganization;
	}

	public void setSendOrganization(String sendOrganization) {
		this.sendOrganization = sendOrganization;
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

	public String getQuantityAndWeight() {
		return quantityAndWeight;
	}

	public void setQuantityAndWeight(String quantityAndWeight) {
		this.quantityAndWeight = quantityAndWeight;
	}

	public Byte getPackageType() {
		return packageType;
	}

	public void setPackageType(Byte packageType) {
		this.packageType = packageType;
	}

	public String getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(String invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
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

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
