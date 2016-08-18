package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>id:id</li>
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>name：名称</li>
 * <li>description:详情</li>
 * <li>punchGeoPoints: 地点列表{@link com.everhomes.rest.techpark.punch.PunchGeoPointDTO}</li>
 * </ul>
 */
public class PunchLocationRuleDTO {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String name;
    private String description;

	@ItemType(PunchGeoPointDTO.class)
	private  List<PunchGeoPointDTO>  punchGeoPoints;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PunchGeoPointDTO> getPunchGeoPoints() {
		return punchGeoPoints;
	}

	public void setPunchGeoPoints(List<PunchGeoPointDTO> punchGeoPoints) {
		this.punchGeoPoints = punchGeoPoints;
	}
	
}
