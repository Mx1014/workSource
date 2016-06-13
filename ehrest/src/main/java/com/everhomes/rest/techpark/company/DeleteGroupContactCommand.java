package com.everhomes.rest.techpark.company;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class DeleteGroupContactCommand {
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
