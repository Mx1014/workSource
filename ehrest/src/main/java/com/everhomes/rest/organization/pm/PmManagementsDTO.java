package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 *  <li>pmName: 物业公司名称</li>
 *  <li>plate: 楼栋门牌</li>
 *  <li>pmId: 物业公司id</li>
 *  <li>buildings: 覆盖楼栋</li>
 *  <li>isAll: 是否全部覆盖 0-全部；1-部分</li>
 * </ul>
 *
 */
public class PmManagementsDTO {

	@ItemType(value = PmBuildingDTO.class)
	private List<PmBuildingDTO> buildings;
	
	private String pmName;
	
	private String plate;
	
	private Long pmId;
	
	private int isAll;

	public int getIsAll() {
		return isAll;
	}

	public void setIsAll(int isAll) {
		this.isAll = isAll;
	}

	public Long getPmId() {
		return pmId;
	}

	public void setPmId(Long pmId) {
		this.pmId = pmId;
	}

	public List<PmBuildingDTO> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<PmBuildingDTO> buildings) {
		this.buildings = buildings;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}
	
}
