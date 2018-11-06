package com.everhomes.rest.category;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * 
 * <ul>
 * 	<li>id : category的Id</li>
 * </ul>
 *
 */
public class GetCategoryCommand {
	@NotNull
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
