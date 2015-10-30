package com.everhomes.techpark.park;

/**
 * <ul>
 *  <li>plateNumber: 车牌号</li>
 *  <li>isvalid: 停车场卡数据查询接口返回的参数，true为车牌已有月卡；false为失效月卡车牌；无返回值为无月卡车牌</li>
 * </ul>
 */
public class PlateNumberCommand {

	private String plateNumber;
	
	private String isvalid;
	
	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
}
