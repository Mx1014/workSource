package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionCategories;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionCategories extends EhEquipmentInspectionCategories {

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
