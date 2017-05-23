package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> id: id</li>
 *  <li> ownerType: 拥有者类型 参考 {@link com.everhomes.rest.yellowPage.ServiceAllianceBelongType}</li>
 *  <li> ownerId: 拥有者ID</li>
 * </ul>
 */
public class DeleteServiceAllianceEnterpriseCommand {
	@NotNull
	private Long id;

	private String ownerType;
	
	private Long ownerId;
 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
