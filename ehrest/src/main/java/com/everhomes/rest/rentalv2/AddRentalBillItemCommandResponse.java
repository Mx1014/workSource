package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderNo：支付订单id</li>   
 * <li>amount：支付金额</li> 
 * <li>name：name</li> 
 * <li>description：详情</li> 
 * <li>orderType：orderType</li> 
 * <li>appKey：appKey </li> 
 * <li>randomNum：randomNum </li> 
 * <li>timestamp： timestamp</li> 
 * <li>signature：signature </li>  
 * <li>flowCaseUrl : payMode 为1线下支付的时候,需要url跳转工作流 </li>  
 * </ul>
 */
public class AddRentalBillItemCommandResponse {
	private String orderNo;
	private java.math.BigDecimal amount;
	private String name;
	private String description;
	private String orderType;
	
	//签名所需参数
	private String appKey;
	private Integer randomNum;
	private Long timestamp;
	private String signature;
	private String flowCaseUrl;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	 
	public java.math.BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(java.math.BigDecimal amount) {
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Integer getRandomNum() {
		return randomNum;
	}
	public void setRandomNum(Integer randomNum) {
		this.randomNum = randomNum;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getFlowCaseUrl() {
		return flowCaseUrl;
	}
	public void setFlowCaseUrl(String flowCaseUrl) {
		this.flowCaseUrl = flowCaseUrl;
	} 
}
