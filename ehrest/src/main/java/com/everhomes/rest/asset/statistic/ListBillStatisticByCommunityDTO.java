//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属项目类型</li>
 * <li>ownerId: 所属项目ID</li>
 * <li>projectName:项目名称</li>
 * <li>projectClassify:项目分类</li>
 * <li>addressCount:楼宇总数</li>
 * <li>addressArea:建筑面积</li>
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
public class ListBillStatisticByCommunityDTO {
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
    private String projectName;
    private String projectClassify;
    private Long addressCount;
    private Long addressArea;
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
    
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectClassify() {
		return projectClassify;
	}
	public void setProjectClassify(String projectClassify) {
		this.projectClassify = projectClassify;
	}
	public Long getAddressCount() {
		return addressCount;
	}
	public void setAddressCount(Long addressCount) {
		this.addressCount = addressCount;
	}
	public Long getAddressArea() {
		return addressArea;
	}
	public void setAddressArea(Long addressArea) {
		this.addressArea = addressArea;
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
    
    
}
