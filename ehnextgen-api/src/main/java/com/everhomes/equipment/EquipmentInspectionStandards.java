package com.everhomes.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.util.StringHelper;

import java.util.List;

public class EquipmentInspectionStandards extends EhEquipmentInspectionStandards{
	
	private static final long serialVersionUID = -1192073373709883176L;

	@Deprecated
	@ItemType(RepeatSettings.class)
	private RepeatSettings repeat;
	
	private Integer equipmentsCount;

	//add in PM.V3.2
	@ItemType(EquipmentInspectionEquipments.class)
	private  List<EquipmentInspectionEquipments> equipments;

	//add in PM.V3.2
	@ItemType(EquipmentInspectionItems.class)
	private  List<EquipmentInspectionItems> items;

	@Deprecated
	@ItemType(EquipmentInspectionStandardGroupMap.class)
	private List<EquipmentInspectionStandardGroupMap> executiveGroup;

	@Deprecated
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



	public List<EquipmentInspectionItems> getItems() {
		return items;
	}

	public void setItems(List<EquipmentInspectionItems> items) {
		this.items = items;
	}

	public List<EquipmentInspectionEquipments> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentInspectionEquipments> equipments) {
		this.equipments = equipments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
