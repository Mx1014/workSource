package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessoryMap;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionAccessoryMap extends EhEquipmentInspectionAccessoryMap{

	private static final long serialVersionUID = 3584105966217070958L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
