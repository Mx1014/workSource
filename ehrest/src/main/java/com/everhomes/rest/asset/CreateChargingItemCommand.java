//@formatter:off
package com.everhomes.rest.asset;

import javax.validation.constraints.NotNull;

/**
 * @author created by ycx
 * @date 下午9:01:20
 */

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>namespaceId: 域名</li>
 * <li>categoryId: 多入口id</li>
 *</ul>
 */
public class CreateChargingItemCommand {
    private Long ownerId;
    private String ownerType;
    private Integer namespaceId;
    private Long categoryId;
    private String name;
    
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
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
}
