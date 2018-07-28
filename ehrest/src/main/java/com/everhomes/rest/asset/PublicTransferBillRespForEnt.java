//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;

/**
 * @author created by yangcx
 * @date 2018年5月23日----下午5:56:48
 */
/**
 *<ul>
 * <li>orderCreateTime:订单创建时间</li>
 * <li>paymentOrderNum:订单编号，如：954650447962984448，订单编号为缴费中交易明细与电商系统中交易明细串联起来的唯一标识。</li>
 * <li>businessType: 业务类型【参数不知道咋定义，待产品决定，预留】</li>
 * <li>orderSource:订单来源，如：物业缴费</li>
 * <li>orderExplain: 订单说明，如：2017-06物业费、2017-06租金</li>
 * <li>orderAmount: 订单金额</li>
 *</ul>
 */
public class PublicTransferBillRespForEnt {
	private String orderCreateTime;
	private String paymentOrderNum;
	private String businessType;
	private String orderSource;
	private String orderExplain;
	private BigDecimal orderAmount;
	
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getPaymentOrderNum() {
		return paymentOrderNum;
	}
	public void setPaymentOrderNum(String paymentOrderNum) {
		this.paymentOrderNum = paymentOrderNum;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	public String getOrderExplain() {
		return orderExplain;
	}
	public void setOrderExplain(String orderExplain) {
		this.orderExplain = orderExplain;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
}
