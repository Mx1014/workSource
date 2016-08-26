package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>id: 备品配件id</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * </ul>
 */
public class DeleteEquipmentAccessoriesCommand {
	
	private Long id;
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
