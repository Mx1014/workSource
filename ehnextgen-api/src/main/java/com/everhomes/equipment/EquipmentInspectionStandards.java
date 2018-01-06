package com.everhomes.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.util.StringHelper;

import java.util.List;

public class EquipmentInspectionStandards extends EhEquipmentInspectionStandards{
	
	private static final long serialVersionUID = -1192073373709883176L;
	
	@ItemType(RepeatSettings.class)
	private RepeatSettings repeat;
	
	private Integer equipmentsCount;

	@ItemType(Long.class)
	private List<Long> communities;

	private  String  targetName;
	
	@ItemType(EquipmentInspectionStandardGroupMap.class)
	private List<EquipmentInspectionStandardGroupMap> executiveGroup;

	@ItemType(EquipmentInspectionStandardGroupMap.class)
	private List<EquipmentInspectionStandardGroupMap> reviewGroup;
	
	public RepeatSettings getRepeat() {
		return repeat;
	}

	public void setRepeat(RepeatSettings repeat) {
		this.repeat = repeat;
	}

	public Integer getEquipmentsCount() {
		return equipmentsCount;
	}

	public void setEquipmentsCount(Integer equipmentsCount) {
		this.equipmentsCount = equipmentsCount;
	}

	public List<EquipmentInspectionStandardGroupMap> getExecutiveGroup() {
		return executiveGroup;
	}

	public void setExecutiveGroup(
			List<EquipmentInspectionStandardGroupMap> executiveGroup) {
		this.executiveGroup = executiveGroup;
	}

	public List<EquipmentInspectionStandardGroupMap> getReviewGroup() {
		return reviewGroup;
	}

	public void setReviewGroup(List<EquipmentInspectionStandardGroupMap> reviewGroup) {
		this.reviewGroup = reviewGroup;
	}

	public List<Long> getCommunities() {
		return communities;
	}

	public void setCommunities(List<Long> communities) {
		this.communities = communities;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
