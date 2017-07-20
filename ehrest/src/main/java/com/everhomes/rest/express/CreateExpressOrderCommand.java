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
 * <li>receiveAddressId: 收件地址id</li>
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

	private Long receiveAddressId;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
