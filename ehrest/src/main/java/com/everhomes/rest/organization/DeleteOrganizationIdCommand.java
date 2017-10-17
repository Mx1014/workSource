// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>scopeType：删除人员范围{@link com.everhomes.rest.organization.DeleteOrganizationContactScopeType}</li>
 * <li>enterpriseId: 当前总公司的名称</li>
 * </ul>
 */
public class DeleteOrganizationIdCommand {
	@NotNull
	private Long  id;

	private String scopeType;

	private Long enterpriseId;
	
	public DeleteOrganizationIdCommand() {
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
}
