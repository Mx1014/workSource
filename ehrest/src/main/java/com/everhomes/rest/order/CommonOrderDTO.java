package com.everhomes.rest.order;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>orderNo:订单号</li>
 * 	<li>orderType:订单类型,详见{@link com.everhomes.rest.order.OrderType}</li>
 * 	<li>totalFee:订单金额</li>
 * 	<li>subject:订单标题</li>
 * 	<li>body:订单描述内容</li>
 * 	<li>appKey:签名公钥</li>
 * 	<li>signature:签名串</li>
 * 	<li>timestamp:时间戳</li>
 * 	<li>randomNum:随机数</li>
 * </ul>
 *
 */
public class CommonOrderDTO {
	private String orderNo;
	private String orderType;
	private BigDecimal totalFee;
	private String subject;
	private String body;
	private String appKey;
	
	private String signature;
	private Long timestamp;
	private Integer randomNum;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
