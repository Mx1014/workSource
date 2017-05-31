// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>InCarNum : 在场车辆数</li>
 * </ul>
 *
 *  @author:dengs 2017年5月31日
 */
public class GetParkingCarNumsResponse {
	private String ParkName;
	private Integer CarNum;
	private Integer SpaceNum;
	private Integer InCarNum;
	public String getParkName() {
		return ParkName;
	}
	public void setParkName(String parkName) {
		ParkName = parkName;
	}
	public Integer getCarNum() {
		return CarNum;
	}
	public void setCarNum(Integer carNum) {
		CarNum = carNum;
	}
	public Integer getSpaceNum() {
		return SpaceNum;
	}
	public void setSpaceNum(Integer spaceNum) {
		SpaceNum = spaceNum;
	}
	public Integer getInCarNum() {
		return InCarNum;
	}
	public void setInCarNum(Integer inCarNum) {
		InCarNum = inCarNum;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
