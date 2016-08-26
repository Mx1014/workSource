package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessories;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionAccessories extends EhEquipmentInspectionAccessories{
	
	private static final long serialVersionUID = 7460486719542062801L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
