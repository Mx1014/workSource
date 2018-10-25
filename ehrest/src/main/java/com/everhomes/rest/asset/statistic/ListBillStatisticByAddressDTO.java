//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *<ul>
 * <li>count: 合计总数</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属项目类型</li>
 * <li>ownerId: 所属项目ID</li>
 * <li>addressName:楼宇房源名称</li>
 * <li>areaSize:收费面积</li>
 * <li>targetName:客户名称</li>
 * <li>noticeTel:催缴手机号码</li>
 * <li>dateStrBegin:账单开始时间</li>
 * <li>dateStrEnd:账单结束时间</li>
 * <li>projectChargingItemName:收费项目备注名称</li>
 * <li>amountReceivable:应收含税</li>
 * <li>amountReceivableWithoutTax:应收不含税</li>
 * <li>taxAmount:税额</li>
 * <li>amountReceived:已收含税</li>
 * <li>amountReceivedWithoutTax:已收不含税</li>
 * <li>amountOwed:待收含税</li>
 * <li>amountOwedWithoutTax:待收不含税</li>
 * <li>amountExemption:总减免</li>
 * <li>amountSupplement:总增收</li>
 * <li>dueDayCount:欠费天数</li>
 * <li>noticeTimes:催缴次数</li>
 * <li>collectionRate:收缴率</li>
 *</ul>
 */
public class ListBillStatisticByAddressDTO {
	private BigDecimal count;
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private String addressName;
    private BigDecimal areaSize;
    private String targetName;
    private String noticeTel;
    private String dateStrBegin;
    private String dateStrEnd;
    private String projectChargingItemName;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceivableWithoutTax;
    private BigDecimal taxAmount;
    private BigDecimal amountReceived;
    private BigDecimal amountReceivedWithoutTax;
    private BigDecimal amountOwed;
    private BigDecimal amountOwedWithoutTax;
    private BigDecimal amountExemption;
    private BigDecimal amountSupplement;
    private BigDecimal dueDayCount;
    private BigDecimal noticeTimes;
    private BigDecimal collectionRate;
    
    //调用资产的提供给缴费报表的房源收费面积的接口
    private Long contractId;
    private List<Long> contractIds;
    private  List<String> buildindNames;
    private  List<String> apartmentNames;
    
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public BigDecimal getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(BigDecimal areaSize) {
		this.areaSize = areaSize;
	}
	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}
	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}
	public BigDecimal getAmountReceivableWithoutTax() {
		return amountReceivableWithoutTax;
	}
	public void setAmountReceivableWithoutTax(BigDecimal amountReceivableWithoutTax) {
		this.amountReceivableWithoutTax = amountReceivableWithoutTax;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
	public BigDecimal getAmountReceivedWithoutTax() {
		return amountReceivedWithoutTax;
	}
	public void setAmountReceivedWithoutTax(BigDecimal amountReceivedWithoutTax) {
		this.amountReceivedWithoutTax = amountReceivedWithoutTax;
	}
	public BigDecimal getAmountOwed() {
		return amountOwed;
	}
	public void setAmountOwed(BigDecimal amountOwed) {
		this.amountOwed = amountOwed;
	}
	public BigDecimal getAmountOwedWithoutTax() {
		return amountOwedWithoutTax;
	}
	public void setAmountOwedWithoutTax(BigDecimal amountOwedWithoutTax) {
		this.amountOwedWithoutTax = amountOwedWithoutTax;
	}
	public BigDecimal getAmountExemption() {
		return amountExemption;
	}
	public void setAmountExemption(BigDecimal amountExemption) {
		this.amountExemption = amountExemption;
	}
	public BigDecimal getAmountSupplement() {
		return amountSupplement;
	}
	public void setAmountSupplement(BigDecimal amountSupplement) {
		this.amountSupplement = amountSupplement;
	}
	public BigDecimal getDueDayCount() {
		return dueDayCount;
	}
	public void setDueDayCount(BigDecimal dueDayCount) {
		this.dueDayCount = dueDayCount;
	}
	public BigDecimal getNoticeTimes() {
		return noticeTimes;
	}
	public void setNoticeTimes(BigDecimal noticeTimes) {
		this.noticeTimes = noticeTimes;
	}
	public BigDecimal getCollectionRate() {
		return collectionRate;
	}
	public void setCollectionRate(BigDecimal collectionRate) {
		this.collectionRate = collectionRate;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getNoticeTel() {
		return noticeTel;
	}
	public void setNoticeTel(String noticeTel) {
		this.noticeTel = noticeTel;
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
	public String getProjectChargingItemName() {
		return projectChargingItemName;
	}
	public void setProjectChargingItemName(String projectChargingItemName) {
		this.projectChargingItemName = projectChargingItemName;
	}
	public BigDecimal getCount() {
		return count;
	}
	public void setCount(BigDecimal count) {
		this.count = count;
	}
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public List<Long> getContractIds() {
		return contractIds;
	}
	public void setContractIds(List<Long> contractIds) {
		this.contractIds = contractIds;
	}
	public List<String> getBuildindNames() {
		return buildindNames;
	}
	public void setBuildindNames(List<String> buildindNames) {
		this.buildindNames = buildindNames;
	}
	public List<String> getApartmentNames() {
		return apartmentNames;
	}
	public void setApartmentNames(List<String> apartmentNames) {
		this.apartmentNames = apartmentNames;
	}
    
}
