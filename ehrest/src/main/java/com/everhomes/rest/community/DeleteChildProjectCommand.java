package com.everhomes.rest.community;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>id: 子项目项目id</li>
 * </ul>
 */
public class DeleteChildProjectCommand {

	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
