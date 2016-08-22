package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipments;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionEquipments extends EhEquipmentInspectionEquipments{

	private static final long serialVersionUID = -8471061039276564577L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
