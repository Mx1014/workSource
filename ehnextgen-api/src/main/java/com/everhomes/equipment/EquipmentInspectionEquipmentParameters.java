package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentParameters;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionEquipmentParameters extends EhEquipmentInspectionEquipmentParameters{

	private static final long serialVersionUID = 4456178429399704899L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
