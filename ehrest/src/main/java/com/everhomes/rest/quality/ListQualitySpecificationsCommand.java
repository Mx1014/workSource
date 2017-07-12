package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 类型所属组织等的id</li>
 *  <li>ownerType: 类型所属组织类型，如enterprise</li>
 *  <li>scopeCode: specification可见范围类型 0: all, 1: community </li>
 *  <li>scopeId: 看见范围具体Id，全部为0 </li>
 *  <li>parentId: 父节点id。全要则不填</li>
 *  <li>inspectionType: 规范类型 0: 类型, 1: 规范, 2: 规范事项</li>
 * </ul>
 */
public class ListQualitySpecificationsCommand {

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Byte scopeCode;
	
	private Long scopeId;
	
	private Long parentId;
	
	private Byte inspectionType;
	
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Byte getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(Byte inspectionType) {
		this.inspectionType = inspectionType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
