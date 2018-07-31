//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author created by yangcx
 * @date 2018年5月23日----上午11:09:59
 */
/**
 *<ul>
 * <li>dateStr:账期，格式为201706，参与排序</li>
 * <li>dateStrBegin:账单开始时间，参与排序</li>
 * <li>dateStrEnd:账单结束时间，参与排序</li>
 * <li>billGroupId:账单组id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>targetName:客户名称</li>
 * <li>paymentOrderNum: 订单编号</li>
 * <li>orderAmount:交易金额</li>
 * <li>payerName:缴费人</li>
 * <li>paymentStatus:订单状态:0未支付,1已完成,2挂起,3失败</li>
 * <li>payTime:缴费时间</li>
 * <li>orderRemark2:业务系统自定义字段（对应eh_asset_payment_order表的id）</li>
 * <li>state:前端触发事件使用，1是外层，2是内层(children代表内层)</li>
 *</ul>
 */
public class PaymentOrderBillForEntDTO implements Cloneable{
	private String dateStr;
	private String dateStrBegin;
	private String dateStrEnd;
	private Long billGroupId;
	private String billGroupName;
	private String targetName;
	private String paymentOrderNum;
	private BigDecimal orderAmount;
	private String payerName;
	private Integer paymentStatus;
	private String payTime;
	private String orderRemark2;
	private Byte state;
	
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public Long getBillGroupId() {
		return billGroupId;
	}
	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}
	public String getBillGroupName() {
		return billGroupName;
	}
	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}
	public String getPaymentOrderNum() {
		return paymentOrderNum;
	}
	public void setPaymentOrderNum(String paymentOrderNum) {
		this.paymentOrderNum = paymentOrderNum;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public Integer getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getOrderRemark2() {
		return orderRemark2;
	}
	public void setOrderRemark2(String orderRemark2) {
		this.orderRemark2 = orderRemark2;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public String getDateStrBegin() {
		return dateStrBegin;
	}
	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}
	public String getDateStrEnd() {
		return dateStrEnd;
	}
	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	
}
