// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id：主键id</li>
 * </ul>
 */
public class DeleteOrganizationJobPositionCommand {

	@NotNull
	private Long  id;

	public DeleteOrganizationJobPositionCommand() {
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
