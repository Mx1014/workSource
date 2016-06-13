package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>billId : 订单号</li>
 *  <li>amount : 金额</li>
 *  <li>name : 商品名称</li>
 *	<li>description : 描述</li>
 *  <li>orderType  : 支付类型</li>
 *  <li>appKey : 公钥</li>
 *	<li>timestamp : 时间戳</li>
 *	<li>randomNum : 随机数</li>
 *	<li>signature : 签名</li>
 * </ul>
 */
public class ConfAccountOrderDTO {

	private Long billId;
	
	private Double amount;
	
	private String name;
	
	private String description;
	
	private String orderType = "tingche" ;
	
	//签名所需参数
	private String appKey;
	private Long timestamp;
	private Integer randomNum;
	private String signature;
	
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


	public String getAppKey() {
		return appKey;
	}


	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}


	public Long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}


	public Integer getRandomNum() {
		return randomNum;
	}


	public void setRandomNum(Integer randomNum) {
		this.randomNum = randomNum;
	}


	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
