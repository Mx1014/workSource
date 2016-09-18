package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>id: 主键id</li>
 *  <li>status: 启用状态</li>
 * </ul>
 */
public class SetNotifyTargetStatusCommand {

	@NotNull
	private Long id;
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private Byte status;

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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
