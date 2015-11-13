package com.everhomes.techpark.park;

/**
 * <ul>
 *  <li>plateNumber: 车牌号</li>
 *  <li>enterpriseCommunityId: 园区id</li>
 * </ul>
 */
public class PlateNumberCommand {

	private Long enterpriseCommunityId;
	
	private String plateNumber;
	
	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
}
