package com.everhomes.rest.techpark.expansion;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id：id</li>
 * <li>status：状态</li>
 * </ul>
 */
public class UpdateLeasePromotionStatusCommand {
	@NotNull
	private Long id;
	
	@NotNull
	private Byte status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
