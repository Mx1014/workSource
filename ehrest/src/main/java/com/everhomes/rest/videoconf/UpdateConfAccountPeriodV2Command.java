package com.everhomes.rest.videoconf;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountIds: 账号id列表</li>
 *  <li>months: 延期月数</li>
 *  <li>enterpriseId: 企业id </li>
 *  <li>enterpriseName: 企业名称</li>
 *  <li>contactor: 企业联系人</li>
 *  <li>mobile: 手机号</li>
 *  <li>amount: 金额</li>
 *  <li>invoiceFlag: 是否需要发票 0-dont need 1-need</li>
 *  <li>buyChannel: 购买渠道0-offline 1-online</li>
 *  <li>mailAddress: 邮箱地址</li>
 *  <li>clientAppName:  Realm值，app客户端必传</li>
 * </ul>
 *
 */
public class UpdateConfAccountPeriodV2Command {
	
	@NotNull
	@ItemType(Long.class)
	private List<Long> accountIds;
	
	@NotNull
	private int months;
	
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String contactor;
	
	private String mobile;
	
	private Byte buyChannel;
	
	private BigDecimal amount;
	
	private Byte invoiceFlag;
	
	private String mailAddress;
	private String clientAppName;
	
	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
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
