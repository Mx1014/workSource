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
 *  <li>invoiceFlag: 是否需要发票 0-dont need 1-need</li>
 *  <li>makeOutFlag: 是否已开票 0-uninvoice 1-invoiced</li>
 *  <li>buyChannel: 购买渠道0-offline 1-online</li>
 *  <li>accountCategoryId: 购买的账号类型</li>
 *  <li>invoice: 发票信息，参考{@link com.everhomes.rest.videoconf.InvoiceDTO}</li>
 * </ul>
 *
 */
public class CreateConfAccountOrderCommand {
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String contactor;
	
	private String mobile;
	
	private Byte buyChannel;
	
	private Integer quantity;
	
	private Integer period;
	
	private BigDecimal amount;
	
	private Byte invoiceFlag;
	
	private Byte makeOutFlag;
	
	private InvoiceDTO invoice;
	
	private Long accountCategoryId;
	
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

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public Byte getMakeOutFlag() {
		return makeOutFlag;
	}

	public void setMakeOutFlag(Byte makeOutFlag) {
		this.makeOutFlag = makeOutFlag;
	}

	public InvoiceDTO getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceDTO invoice) {
		this.invoice = invoice;
	}

	public Long getAccountCategoryId() {
		return accountCategoryId;
	}

	public void setAccountCategoryId(Long accountCategoryId) {
		this.accountCategoryId = accountCategoryId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
