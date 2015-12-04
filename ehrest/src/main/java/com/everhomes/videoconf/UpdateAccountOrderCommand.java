package com.everhomes.videoconf;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>orderId: 订单号</li>
 *  <li>enterpriseId: 企业id </li>
 *  <li>enterpriseName: 企业名称</li>
 *  <li>contactor: 企业联系人</li>
 *  <li>mobile: 手机号</li>
 *  <li>quantity: 购买数量</li>
 *  <li>monthlyPackage: 包月套餐</li>
 *  <li>amount: 金额</li>
 *  <li>invoiceFlag: 是否需要发票 0-dont need 1-need</li>
 *  <li>makeOutFlag: 是否已开票 0-uninvoice 1-invoiced</li>
 *  <li>buyChannel: 购买渠道 0-offline 1-online</li>
 *  <li>invoice: 发票信息，参考{@link com.everhomes.videoconf.InvoiceDTO}</li>
 * </ul>
 *
 */
public class UpdateAccountOrderCommand {

	private Long id;
	
	private Long orderId;
	
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String contactor;
	
	private String mobile;
	
	private Byte buyChannel;
	
	private Integer quantity;
	
	private String monthlyPackage;
	
	private BigDecimal amount;
	
	private Byte invoiceFlag;
	
	private Byte makeOutFlag;
	
	private InvoiceDTO invoice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getMonthlyPackage() {
		return monthlyPackage;
	}

	public void setMonthlyPackage(String monthlyPackage) {
		this.monthlyPackage = monthlyPackage;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
