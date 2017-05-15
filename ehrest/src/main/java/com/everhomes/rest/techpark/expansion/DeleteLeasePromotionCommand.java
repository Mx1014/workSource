package com.everhomes.rest.techpark.expansion;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id：id</li>
 * </ul>
 */
public class DeleteLeasePromotionCommand {

	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
