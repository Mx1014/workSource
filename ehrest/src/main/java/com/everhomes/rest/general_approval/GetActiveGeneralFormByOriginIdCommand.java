// @formatter:off
package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id : 表单id</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class GetActiveGeneralFormByOriginIdCommand {
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
