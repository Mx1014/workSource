package com.everhomes.rest.techpark.expansion;

import javax.validation.constraints.NotNull;

public class GetEnterpriseDetailByIdCommand {
	
	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

}
