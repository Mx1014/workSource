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
 * <li>dateStr:账单时间</li>
 * <li>payTime:交易时间（缴费时间）</li>
 * <li>paymentType:支付方式，微信/支付宝/对公转账</li>
 * <li>paymentStatus:支付状态: 已完成/订单异常</li>
 * <li>payerName:缴费人</li>
 * <li>payerTel:缴费人电话</li>
 * <li>billGroupName:账单组名称</li>
 * <li>targetName:客户名称</li>
 * <li>targetType:客户类型</li>
 * <li>amountReceivable:应收(元),应收=应收-减免+增收</li>
 * <li>amountReceived:实收(元)</li>
 * <li>amoutExemption:减免(元)</li>
 * <li>amountSupplement:增收(元)</li>
 * <li>addresses[String]:组装的多个楼栋门牌，如：金融基地/1205,金融基地/1206</li>
 * <li>billItemListMsg:组装所有的收费项信息，如：物业费：100元</li>
 * <li>paymentOrderNum[String]:订单编号，如：954650447962984448，订单编号为缴费中交易明细与电商系统中交易明细串联起来的唯一标识。</li>
 *</ul>
 */
public class exportPaymentOrdersDetail {
    private String payTime;
    private String paymentType;
    private String paymentStatus;
    private String payerName;
    private String payerTel;
    private String dateStr;
    private String billGroupName;
    private String targetName;
    private String targetType;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amoutExemption;
    private BigDecimal amountSupplement;
    private String addresses;
    private String billItemListMsg;
    private String paymentOrderNum;
    
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerTel() {
		return payerTel;
	}
	public void setPayerTel(String payerTel) {
		this.payerTel = payerTel;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getBillGroupName() {
		return billGroupName;
	}
	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}
	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
	public BigDecimal getAmoutExemption() {
		return amoutExemption;
	}
	public void setAmoutExemption(BigDecimal amoutExemption) {
		this.amoutExemption = amoutExemption;
	}
	public BigDecimal getAmountSupplement() {
		return amountSupplement;
	}
	public void setAmountSupplement(BigDecimal amountSupplement) {
		this.amountSupplement = amountSupplement;
	}
	public String getBillItemListMsg() {
		return billItemListMsg;
	}
	public void setBillItemListMsg(String billItemListMsg) {
		this.billItemListMsg = billItemListMsg;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentOrderNum() {
		return paymentOrderNum;
	}
	public void setPaymentOrderNum(String paymentOrderNum) {
		this.paymentOrderNum = paymentOrderNum;
	}
	public String getAddresses() {
		return addresses;
	}
	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}
    
}
