package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionItemResults;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionItemResults extends EhEquipmentInspectionItemResults{

	private static final long serialVersionUID = -3566959795034826427L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
