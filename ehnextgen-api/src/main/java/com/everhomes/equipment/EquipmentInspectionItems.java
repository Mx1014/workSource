package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionItems;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionItems extends EhEquipmentInspectionItems{

	private static final long serialVersionUID = 3311826378689906996L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
