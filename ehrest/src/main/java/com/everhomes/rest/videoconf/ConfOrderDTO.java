package com.everhomes.rest.videoconf;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>enterpriseId: 企业id </li>
 *  <li>enterpriseName: 企业名称</li>
 *  <li>contactor: 企业联系人</li>
 *  <li>mobile: 手机号</li>
 *  <li>createTime: 下单时间 </li>
 *  <li>quantity: 购买数量</li>
 *  <li>assignedQuantity: 已分配账号数</li>
 *  <li>period: 包月时间</li>
 *  <li>amount: 金额</li>
 *  <li>accountCategoryId: 购买视频会议账号的类型</li>
 *  <li>invoiceFlag: 是否需要发票 0-dont need 1-need</li>
 *  <li>makeOutFlag: 是否已开票 0-uninvoice 1-invoiced</li>
 *  <li>buyChannel: 购买渠道 0-offline 1-online</li>
 * </ul>
 *
 */

public class ConfOrderDTO {

	private Long id;
	
	private Long enterpriseId;
	
	private String enterpriseName;
	
	private String contactor;
	
	private String mobile;
	
	private Timestamp createTime;
	
	private Byte buyChannel;
	
	private Integer quantity;
	
	private Integer assignedQuantity;
	
	private Integer period;
	
	private BigDecimal amount;
	
	private Long accountCategoryId;
	
	private Byte invoiceFlag;
	
	private Byte makeOutFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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

	public Integer getAssignedQuantity() {
		return assignedQuantity;
	}

	public void setAssignedQuantity(Integer assignedQuantity) {
		this.assignedQuantity = assignedQuantity;
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

	public Long getAccountCategoryId() {
		return accountCategoryId;
	}

	public void setAccountCategoryId(Long accountCategoryId) {
		this.accountCategoryId = accountCategoryId;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
