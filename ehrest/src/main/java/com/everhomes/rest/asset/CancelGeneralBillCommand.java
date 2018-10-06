//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
 * <li>ownerType:所属者type</li>
 * <li>ownerId:所属者id</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>billIdList:统一账单id列表</li>
 * <li>merchantOrderId:统一订单定义的唯一标识</li>
 *</ul>
 */
public class CancelGeneralBillCommand {
	private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String sourceType;
    private Long sourceId;
    private List<Long> billIdList;
    private String merchantOrderId;
    
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public List<Long> getBillIdList() {
		return billIdList;
	}
	public void setBillIdList(List<Long> billIdList) {
		this.billIdList = billIdList;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
}
