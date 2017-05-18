// @formatter:off
package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>targetId：对象id</li>
 * <li>targetType：对象类型，Eh_Users, Eh_Organizations,{@link com.everhomes.rest.common.EntityType}</li>
 * <li>contactType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>targetName：对象名称</li>
 * <li>identifierType：账号类型</li>
 * <li>identifierToken：手机号码</li>
 * <li>allFlag：是否全部业务模块 1：是 0：否</li>
 * <li>modules：模块列表，{@link com.everhomes.rest.acl.ServiceModuleDTO}</li>
 * <li>projects：项目列表，{@link com.everhomes.rest.acl.ProjectDTO}</li>
 * </ul>
 */
public class ServiceModuleAuthorizationsDTO {

	private String ownerType;

	private Long ownerId;

	private Long targetId;

	private String targetType;

	private String   targetName;

	private Byte     identifierType;

	private String   identifierToken;

	private Byte allFlag;

	@ItemType(ServiceModuleDTO.class)
	private List<ServiceModuleDTO> modules;

	@ItemType(ProjectDTO.class)
	private List<ProjectDTO> projects;

	public ServiceModuleAuthorizationsDTO() {
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

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public Byte getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(Byte identifierType) {
		this.identifierType = identifierType;
	}

	public String getIdentifierToken() {
		return identifierToken;
	}

	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}

	public List<ServiceModuleDTO> getModules() {
		return modules;
	}

	public void setModules(List<ServiceModuleDTO> modules) {
		this.modules = modules;
	}

	public List<ProjectDTO> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectDTO> projects) {
		this.projects = projects;
	}

	public Byte getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(Byte allFlag) {
		this.allFlag = allFlag;
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
