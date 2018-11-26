package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeCubicleDTO;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 */
public class ListOfficeCubicleStatusResponse {

	private Integer longCubicleRentedNums;
	private Integer shortCubicleRentedNums;
	private Integer idleCubicleNums;
	private Integer idleRates;
	private Integer longCubicleIdleNums;
	private Integer shortCubicleIdleNums;
	@ItemType(OfficeCubicleDTO.class)
	private List<OfficeCubicleDTO> officeCubicleDTO;
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
	public Integer getIdleCubicleNums() {
		return idleCubicleNums;
	}
	public void setIdleCubicleNums(Integer idleCubicleNums) {
		this.idleCubicleNums = idleCubicleNums;
	}
	public Integer getIdleRates() {
		return idleRates;
	}
	public void setIdleRates(Integer idleRates) {
		this.idleRates = idleRates;
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
	public List<OfficeCubicleDTO> getOfficeCubicleDTO() {
		return officeCubicleDTO;
	}
	public void setOfficeCubicleDTO(List<OfficeCubicleDTO> officeCubicleDTO) {
		this.officeCubicleDTO = officeCubicleDTO;
	}


}
