package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>id: 模板id</li>
 *  <li>ownerId: 模板所属组织等的id</li>
 *  <li>ownerType: 模板所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 标准所属项目id</li>
 *  <li>targetType: 标准所属项目类型</li>
 * </ul>
 */
public class DeleteInspectionTemplateCommand {
	
	private Long id;
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private  Long targetId;

	private String targetType;

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

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
