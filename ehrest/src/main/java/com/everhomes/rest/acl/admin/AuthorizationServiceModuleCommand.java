package com.everhomes.rest.acl.admin;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>targetType: 授权对象 部门机构：EhOrganizations 用户：EhUsers</li>
 * <li>targetId: 对象id</li>
 * <li>serviceModuleAuthorizations: 资源授权列表， 参考{@link com.everhomes.rest.acl.admin.AuthorizationServiceModule}</li>
 * </ul>
 */
public class AuthorizationServiceModuleCommand {

	@NotNull
	private Long organizationId;

	@NotNull
	private String targetType;

	@NotNull
	private Long targetId;

	@ItemType(AuthorizationServiceModule.class)
	private List<AuthorizationServiceModule> serviceModuleAuthorizations;


	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public List<AuthorizationServiceModule> getServiceModuleAuthorizations() {
		return serviceModuleAuthorizations;
	}

	public void setServiceModuleAuthorizations(List<AuthorizationServiceModule> serviceModuleAuthorizations) {
		this.serviceModuleAuthorizations = serviceModuleAuthorizations;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
