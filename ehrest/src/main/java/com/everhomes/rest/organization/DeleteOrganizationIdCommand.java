// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>scopeType：删除人员范围{@link com.everhomes.rest.organization.DeleteOrganizationContactScopeType}</li>
 * </ul>
 */
public class DeleteOrganizationIdCommand {
	@NotNull
	private Long  id;

	private String scopeType;
	
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
}
