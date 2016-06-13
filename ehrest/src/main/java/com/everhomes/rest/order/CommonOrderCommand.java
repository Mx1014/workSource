package com.everhomes.rest.order;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
/**
 * <ul>
 * 	<li>orderNo:订单号</li>
 * 	<li>orderType:订单类型,详见{@link com.everhomes.rest.order.OrderType}</li>
 * 	<li>totalFee:订单金额</li>
 * 	<li>subject:订单标题</li>
 * 	<li>body:订单描述内容</li>
 * </ul>
 *
 */
public class CommonOrderCommand {
	@NotNull
	private String orderNo;
	@NotNull
	private String orderType;
	@NotNull
	private BigDecimal totalFee;
	@NotNull
	private String subject;
	@NotNull
	private String body;
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
}
