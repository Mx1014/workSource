package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTasks;
import com.everhomes.util.StringHelper;


public class EquipmentInspectionTasks extends EhEquipmentInspectionTasks{

	private static final long serialVersionUID = 7870710501912354391L;

	private Byte standardType;

	public Byte getStandardType() {
		return standardType;
	}

	public void setStandardType(Byte standardType) {
		this.standardType = standardType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
