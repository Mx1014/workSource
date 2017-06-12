// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>ownerId: 所属id</li>
 * <li>organizationId: 管理公司id</li>
 * <li>id: id</li>
 * <li>appFlag: 1是0否，是否为app端调用，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class GetTalentDetailCommand {
	@NotNull
	@Size(min=1)
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long organizationId;
	@NotNull
	private Long id;
	
	private Byte appFlag;

	public GetTalentDetailCommand() {

	}

	public GetTalentDetailCommand(String ownerType, Long ownerId, Long organizationId, Long id) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.organizationId = organizationId;
		this.id = id;
	}

	public Byte getAppFlag() {
		return appFlag;
	}

	public void setAppFlag(Byte appFlag) {
		this.appFlag = appFlag;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
