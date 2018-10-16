//@formatter:off
package com.everhomes.rest.servicemoduleapp;

import javax.validation.constraints.NotNull;

/**
 * @author created by ycx
 * @date 上午11:46:36
 */

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId: 园区id</li>
 * <li>ownerType: 园区type</li>
 * <li>originId:各个业务应用的originId（公共平台定义的唯一标识）</li>
 * <li>assetCategoryId:缴费的多入口应用ID</li>
 * <li>billGroupId:账单组ID</li>
 * <li>billGroupName:账单组名称</li>
 * <li>chargingItemId:缴费的费项ID</li>
 * <li>chargingItemName:缴费的费项名称</li>
 * <li>creatorId:创建人ID</li>
 *</ul>
 */
public class CreateMappingAndConfigsCommand {
	
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long originId;
    private Long assetCategoryId;
    private Long billGroupId;
    private String billGroupName;
    private Long chargingItemId;
    private String chargingItemName;
    private Long creatorId;
    
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getOriginId() {
		return originId;
	}
	public void setOriginId(Long originId) {
		this.originId = originId;
	}
	public Long getAssetCategoryId() {
		return assetCategoryId;
	}
	public void setAssetCategoryId(Long assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
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
	public Long getChargingItemId() {
		return chargingItemId;
	}
	public void setChargingItemId(Long chargingItemId) {
		this.chargingItemId = chargingItemId;
	}
	public String getChargingItemName() {
		return chargingItemName;
	}
	public void setChargingItemName(String chargingItemName) {
		this.chargingItemName = chargingItemName;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
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
