package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id: 订单id</li>
 * </ul>
 */
public class DeleteParkingRechargeOrderCommand {
	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
