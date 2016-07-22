package com.everhomes.rest.serviceconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>scopeCode: service可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>pageSize: 每页条数</li>
 * <li>pageAnchor: 分页瞄</li>
 * </ul>
 */
public class ListCommunityServiceCommand {
	private Long ownerId;
	private String ownerType;
	private Byte scopeCode;
	private Long scopeId;
	private Integer pageSize;
	private Long pageAnchor;
	
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
	public Byte getScopeCode() {
		return scopeCode;
	}
	public void setScopeCode(Byte scopeCode) {
		this.scopeCode = scopeCode;
	}
	public Long getScopeId() {
		return scopeId;
	}
	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
