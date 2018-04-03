// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>carNum : 在场车辆数</li>
 * </ul>
 *
 *  @author:dengs 2017年5月31日
 */
public class GetParkingCarNumsResponse {
	private String parkName;
	private Integer allCarNum;
	private Integer emptyCarNum;
	private Integer carNum;
	
	
	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public Integer getAllCarNum() {
		return allCarNum;
	}

	public void setAllCarNum(Integer allCarNum) {
		this.allCarNum = allCarNum;
	}

	public Integer getEmptyCarNum() {
		return emptyCarNum;
	}

	public void setEmptyCarNum(Integer emptyCarNum) {
		this.emptyCarNum = emptyCarNum;
	}

	public Integer getCarNum() {
		return carNum;
	}

	public void setCarNum(Integer carNum) {
		this.carNum = carNum;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
