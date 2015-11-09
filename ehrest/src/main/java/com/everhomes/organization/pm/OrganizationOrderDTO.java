package com.everhomes.organization.pm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>orderNo : 订单号</li>
 *	<li>amount : 订单金额</li>
 *	<li>name : 商品名称</li>
 *	<li>description : 描述</li>
 *	<li>type : 支付方式</li>
 *</ul>
 *
 */
public class OrganizationOrderDTO {
	
	private String orderNo;
	private BigDecimal amount;
	private String name;
	private String description;
	private String type = "wuye";
	
	
	public String getType() {
		return type;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
