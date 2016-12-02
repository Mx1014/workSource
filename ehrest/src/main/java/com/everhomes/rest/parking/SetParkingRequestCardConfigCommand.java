package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

public class SetParkingRequestCardConfigCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
    private Integer reserveDay;
	private Integer requestNum;
	private Integer requestMonthCount;
    private Byte requestRechargeType;
}
