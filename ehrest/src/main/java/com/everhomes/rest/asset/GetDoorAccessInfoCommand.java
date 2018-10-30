//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>projectId:园区id</li>
 * <li>projectType:类型</li>
 * <li>ownerId:企业id，或者用户id</li>
 * <li>ownerType:所属者类型</li>
 * <li>orgId: 管理公司id</li>
 * <li>categoryId: 缴费应用id</li>
 *</ul>
 */

public class GetDoorAccessInfoCommand {
    private Integer namespaceId;
    //private Long projectId;
    //private String projectType;
    private Long ownerId;
    //private String ownerType;
    //private Long orgId;
    //private Long categoryId;
    
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
