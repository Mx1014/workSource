package com.everhomes.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionStandards extends EhEquipmentInspectionStandards{
	
	private static final long serialVersionUID = -1192073373709883176L;
	
	@ItemType(RepeatSettings.class)
	private RepeatSettings repeat;
	
	private Integer equipmentsCount;
	
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
