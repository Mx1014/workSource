package com.everhomes.techpark.park;

/**
 * <ul>
 *  <li>plateNumber: 车牌号</li>
 *  <li>communityId: 园区id</li>
 * </ul>
 */
public class PlateNumberCommand {

	private Long communityId;
	
	private String plateNumber;
	
	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
}
