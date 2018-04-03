package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>specificationId：specification表的主键id</li>
 *  <li>ownerId: 规范所属的主体id</li>
 *  <li>ownerType: 规范所属的主体</li>
 *  <li>scopeType: specification可见范围类型 0: all, 1: community</li>
 *  <li>scopeId: 看见范围具体Id，全部为0</li>
 * </ul>
 */
public class DeleteQualitySpecificationCommand {

	@NotNull
	private Long specificationId;
	@NotNull
	private Long ownerId;
	@NotNull
	private String ownerType;
	@NotNull
	private Byte scopeCode;
	  
	private Long scopeId;

	public Long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
