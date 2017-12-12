package com.everhomes.rest.videoconf;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>enterpriseId: 企业id </li>
 *  <li>enterpriseName: 企业名称</li>
 *  <li>contactor: 企业联系人</li>
 *  <li>mobile: 手机号</li>
 *  <li>quantity: 账号数量</li>
 *  <li>period: 每个账号可使用几个月</li>
 *  <li>amount: 金额</li>
 *  <li>invoiceReqFlag: 是否需要发票 0-dont need 1-need</li>
 *  <li>buyChannel: 购买渠道0-offline 1-online</li>
 *  <li>confCapacity: 会议容量 0-25方 1-100方 2-6方 3-50方 </li>
 *  <li>confType: 是否支持电话 0-否 1-是</li>
 *  <li>mailAddress: 邮箱地址</li>
 *     <li>clientAppName: Realm值，app客户端必传</li>
 * </ul>
 *
 */
public class CreateConfAccountOrderOnlineV2Command {

	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String contactor;
	
	private String mobile;
	
	private Byte buyChannel;
	
	private Integer quantity;
	
	private Integer period;
	
	private BigDecimal amount;
	
	private Byte invoiceReqFlag;
	
	private Byte confCapacity;
	
	private Byte confType;
	
	private String mailAddress;
	private String clientAppName;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Byte getBuyChannel() {
		return buyChannel;
	}

	public void setBuyChannel(Byte buyChannel) {
		this.buyChannel = buyChannel;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Byte getInvoiceReqFlag() {
		return invoiceReqFlag;
	}

	public void setInvoiceReqFlag(Byte invoiceReqFlag) {
		this.invoiceReqFlag = invoiceReqFlag;
	}

	public Byte getConfCapacity() {
		return confCapacity;
	}

	public void setConfCapacity(Byte confCapacity) {
		this.confCapacity = confCapacity;
	}

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}
	
	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}
}
