// @formatter:off
package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：模块id </li>
 * </ul>
 */
public class DeleteServiceModuleCommand {

	private Long id;

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
