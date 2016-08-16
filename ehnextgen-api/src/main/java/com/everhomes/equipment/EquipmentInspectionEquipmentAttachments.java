package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentAttachments;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionEquipmentAttachments extends EhEquipmentInspectionEquipmentAttachments{

	private static final long serialVersionUID = -6012667336121457364L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
