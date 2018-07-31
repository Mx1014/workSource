//@formatter:off
package com.everhomes.asset;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.asset.ExemptionItemDTO;

/**
 * @author created by yangcx
 * @date 2018年5月20日----下午9:48:28
 */
/**
 *<ul>
 * <li>defaultOrder:排序数字</li>
 * <li>dateStr:账期，格式为201706，参与排序</li>
 * <li>billGroupId:账单组id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>paymentOrderNum: 订单编号</li>
 * <li>orderAmount:交易金额</li>
 * <li>payerName:缴费人</li>
 * <li>paymentStatus:订单状态:0未支付,1已完成,2挂起,3失败</li>
 * <li>payTime:缴费时间</li>
 * <li>orderRemark2:业务系统自定义字段（对应eh_asset_payment_order表的id）</li>
 *</ul>
 */
public class exportOrdersDetail {
	private String dateStr;
	private String billGroupId;
	private String billGroupName;
	private String paymentOrderNum;
	private String orderAmount;
	private String payerName;
	private String paymentStatus;
	private String payTime;
	private String orderRemark2;
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getBillGroupId() {
		return billGroupId;
	}
	public void setBillGroupId(String billGroupId) {
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
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
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
}