package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentStandardMap;
import com.everhomes.util.StringHelper;

public class EquipmentStandardMap  extends EhEquipmentInspectionEquipmentStandardMap{

	private static final long serialVersionUID = 3375336858016434936L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
