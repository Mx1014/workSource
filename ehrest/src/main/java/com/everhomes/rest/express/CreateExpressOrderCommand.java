// @formatter:off
package com.everhomes.rest.express;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>sendAddressId: 寄件地址id</li>
 * <li>sendName: 寄件人名称</li>
 * <li>sendPhone: 寄件人电话</li>
 * <li>sendOrganization: 寄件公司名称</li>
 * <li>sendProvince: 寄件省份</li>
 * <li>sendCity: 寄件城市</li>
 * <li>sendCounty: 寄件县城</li>
 * <li>sendDetailAddress: 寄件详细地址</li>
 * <li>receiveAddressId: 收件地址id</li>
 * <li>receiveName: 收件人名称</li>
 * <li>receivePhone: 收件人电话</li>
 * <li>receiveOrganization: 收件人公司名称</li>
 * <li>receiveProvince: 收件省份</li>
 * <li>receiveCity: 收件城市</li>
 * <li>receiveCounty: 收件县城</li>
 * <li>receiveDetailAddress: 收件详细地址</li>
 * <li>expressCompanyId: 快递公司id</li>
 * <li>sendType: 寄件类型，1标准快递，参考{@link com.everhomes.rest.express.ExpressSendType}</li>
 * <li>sendMode: 寄件方式，1服务点自寄，参考{@link com.everhomes.rest.express.ExpressSendMode}</li>
 * <li>serviceAddressId: 服务地址id</li>
 * <li>payType: 1寄付现结，参考{@link com.everhomes.rest.express.ExpressPayType}</li>
 * <li>internal: 内件物品</li>
 * <li>insuredPrice: 保价金额</li>
 * <li>packageType: 封装类型，参考{@link com.everhomes.rest.express.ExpressPackageType}</li>
 * <li>invoiceHead: 发票抬头</li>
 * <li>invoiceFlag: 需要发票，参考{@link com.everhomes.rest.express.ExpressInvoiceFlagType}</li>
 * </ul>
 */
public class CreateExpressOrderCommand {

	private String ownerType;

	private Long ownerId;

	private Long sendAddressId;
	private String sendName;
	private String sendPhone;
	private String sendOrganization;
	private String sendProvince;
	private String sendCity;
	private String sendCounty;
	private String sendDetailAddress;
	private Long receiveAddressId;
	private String receiveName;
	private String receivePhone;
	private String receiveOrganization;
	private String receiveProvince;
	private String receiveCity;
	private String receiveCounty;
	private String receiveDetailAddress;

	private Long expressCompanyId;

	private Byte sendType;

	private Byte sendMode;

	private Long serviceAddressId;

	private Byte payType;

	private String internal;

	private BigDecimal insuredPrice;
	
	private Byte packageType;
	
	private String invoiceHead;
	
	private Byte invoiceFlag;

	public CreateExpressOrderCommand() {

	}

	public CreateExpressOrderCommand(String ownerType, Long ownerId, Long sendAddressId, Long receiveAddressId, Long expressCompanyId, Byte sendType, Byte sendMode, Long serviceAddressId, Byte payType, String internal, BigDecimal insuredPrice) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.sendAddressId = sendAddressId;
		this.receiveAddressId = receiveAddressId;
		this.expressCompanyId = expressCompanyId;
		this.sendType = sendType;
		this.sendMode = sendMode;
		this.serviceAddressId = serviceAddressId;
		this.payType = payType;
		this.internal = internal;
		this.insuredPrice = insuredPrice;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getSendAddressId() {
		return sendAddressId;
	}

	public void setSendAddressId(Long sendAddressId) {
		this.sendAddressId = sendAddressId;
	}

	public Long getReceiveAddressId() {
		return receiveAddressId;
	}

	public void setReceiveAddressId(Long receiveAddressId) {
		this.receiveAddressId = receiveAddressId;
	}

	public Long getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(Long expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}

	public Byte getSendType() {
		return sendType;
	}

	public void setSendType(Byte sendType) {
		this.sendType = sendType;
	}

	public Byte getSendMode() {
		return sendMode;
	}

	public void setSendMode(Byte sendMode) {
		this.sendMode = sendMode;
	}

	public Long getServiceAddressId() {
		return serviceAddressId;
	}

	public void setServiceAddressId(Long serviceAddressId) {
		this.serviceAddressId = serviceAddressId;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
