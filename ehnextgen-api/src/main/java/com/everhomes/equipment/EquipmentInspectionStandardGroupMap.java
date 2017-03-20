package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandardGroupMap;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionStandardGroupMap extends EhEquipmentInspectionStandardGroupMap {

	private static final long serialVersionUID = 3627479667483523301L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
