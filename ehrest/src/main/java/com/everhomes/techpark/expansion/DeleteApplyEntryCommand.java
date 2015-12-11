package com.everhomes.techpark.expansion;

import javax.validation.constraints.NotNull;

public class DeleteApplyEntryCommand {

	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
