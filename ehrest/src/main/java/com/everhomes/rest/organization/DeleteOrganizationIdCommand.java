// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * </ul>
 */
public class DeleteOrganizationIdCommand {
	@NotNull
	private Long  id;
	
	public DeleteOrganizationIdCommand() {
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
