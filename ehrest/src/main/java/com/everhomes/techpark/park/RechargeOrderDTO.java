package com.everhomes.techpark.park;

/**
 * <ul>
 *  <li>billId : 订单号</li>
 *  <li>amount : 金额</li>
 *  <li>name : 商品名称</li>
 *	<li>description : 描述</li>
 *  <li>orderType  : 支付类型</li>
 * </ul>
 */
public class RechargeOrderDTO {
	
	private Long billId;
	
	private Double amount;
	
	private String name;
	
	private String description;
	
	private String orderType = "tingche" ;
	
	public String getOrderType() {
		return orderType;
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

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
