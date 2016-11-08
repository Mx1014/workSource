package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTemplates;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionTemplates extends EhEquipmentInspectionTemplates{
	
	private static final long serialVersionUID = 8113379724470362015L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
