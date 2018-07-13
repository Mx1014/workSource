//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;

/**
 * @author created by yangcx
 * @date 2018年5月22日----上午10:57:58
 */
/**
 *<ul>
 * <li>defaultOrder:排序数字</li>
 * <li>dateStr:账期，格式为2017-06，参与排序</li>
 * <li>dateStrBegin:账期开始时间</li>
 * <li>dateStrEnd:账期结束时间</li>
 * <li>billId:账单id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户类型</li>
 * <li>contractId:合同id</li>
 * <li>contractNum:合同编号</li>
 * <li>noticeTel:催缴手机号</li>
 * <li>amountReceivable:应收(元)</li>
 * <li>amountReceived:实收(元)</li>
 * <li>amountOwed:欠收(元)</li>
 * <li>billStatus:账单状态，0:待缴;1:已缴</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 *</ul>
 */
public class ListBillsDTOForEnt {
    private Integer defaultOrder;
    private String dateStr;
    private String dateStrBegin;
    private String dateStrEnd;
    private String billId;
    private String billGroupName;
    private String targetName;
    private String targetId;
    private String targetType;
    private String contractId;
    private String contractNum;
    private String noticeTel;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amountOwed;
    private Byte billStatus;
    private String ownerId;
    private String ownerType;
    private String invoiceNum;
    
	public Integer getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
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
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
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
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getNoticeTel() {
		return noticeTel;
	}
	public void setNoticeTel(String noticeTel) {
		this.noticeTel = noticeTel;
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
	public BigDecimal getAmountOwed() {
		return amountOwed;
	}
	public void setAmountOwed(BigDecimal amountOwed) {
		this.amountOwed = amountOwed;
	}
	public Byte getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
    
}
