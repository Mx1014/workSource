package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeCubicleDTO;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>longCubicleRentedNums:长租工位已出租数量</li>
 * <li>shortCubicleRentedNums:短租工位已预定数量</li>
 * <li>longRentCloseCubicleNums:长租工位未开放数量</li>
 * <li>rentRates:预定率</li>
 * <li>longCubicleIdleNums:长租工位未预定数量</li>
 * <li>shortCubicleIdleNums:短租工位未预定数量</li>
 * <li>cubicleIdleNums:工位总数</li>
 * </ul>
 */
public class ListOfficeCubicleStatusResponse {

	private Integer longCubicleRentedNums;
	private Integer shortCubicleRentedNums;
	private Integer longRentCloseCubicleNums;
	private Integer rentRates;
	private Integer longCubicleIdleNums;
	private Integer shortCubicleIdleNums;
	private Integer cubicleNums;
	
	public Integer getLongCubicleRentedNums() {
		return longCubicleRentedNums;
	}
	public void setLongCubicleRentedNums(Integer longCubicleRentedNums) {
		this.longCubicleRentedNums = longCubicleRentedNums;
	}
	public Integer getShortCubicleRentedNums() {
		return shortCubicleRentedNums;
	}
	public void setShortCubicleRentedNums(Integer shortCubicleRentedNums) {
		this.shortCubicleRentedNums = shortCubicleRentedNums;
	}


	public Integer getRentRates() {
		return rentRates;
	}
	public void setRentRates(Integer rentRates) {
		this.rentRates = rentRates;
	}
	public Integer getLongCubicleIdleNums() {
		return longCubicleIdleNums;
	}
	public void setLongCubicleIdleNums(Integer longCubicleIdleNums) {
		this.longCubicleIdleNums = longCubicleIdleNums;
	}
	public Integer getShortCubicleIdleNums() {
		return shortCubicleIdleNums;
	}
	public void setShortCubicleIdleNums(Integer shortCubicleIdleNums) {
		this.shortCubicleIdleNums = shortCubicleIdleNums;
	}
	public Integer getLongRentCloseCubicleNums() {
		return longRentCloseCubicleNums;
	}
	public void setLongRentCloseCubicleNums(Integer longRentCloseCubicleNums) {
		this.longRentCloseCubicleNums = longRentCloseCubicleNums;
	}
	public Integer getCubicleNums() {
		return cubicleNums;
	}
	public void setCubicleNums(Integer cubicleNums) {
		this.cubicleNums = cubicleNums;
	}



}
