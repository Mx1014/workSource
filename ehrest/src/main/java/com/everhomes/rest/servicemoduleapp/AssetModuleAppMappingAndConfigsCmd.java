//@formatter:off
package com.everhomes.rest.servicemoduleapp;

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
 *</ul>
 */
public class AssetModuleAppMappingAndConfigsCmd {
	
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long originId;
    
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
	public Long getOriginId() {
		return originId;
	}
	public void setOriginId(Long originId) {
		this.originId = originId;
	}
    
}
