package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTemplateItemMap;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionTemplateItemMap extends EhEquipmentInspectionTemplateItemMap{

	private static final long serialVersionUID = 4466957249550019829L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
