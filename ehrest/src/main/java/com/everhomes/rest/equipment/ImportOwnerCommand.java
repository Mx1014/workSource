package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>ownerId: 所属的主体id</li>
 *  <li>ownerType: 所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 所属管理处id</li>
 *  <li>targetType: 所属管理处类型</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>namespaceId: 域空间id</li>
 *  <li>communityId: 项目id</li>
 *  <li>moduleName: 模块名</li>
 *  <li>groupPath: 所属字段组在系统中的path</li>
 * </ul>
 */
public class ImportOwnerCommand {
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long targetId;
	
	private String targetType;
	
	private Long inspectionCategoryId;

	private Integer namespaceId;

	private Long communityId;

	private String moduleName;

	private String groupPath;
	
	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getGroupPath() {
		return groupPath;
	}

	public void setGroupPath(String groupPath) {
		this.groupPath = groupPath;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
