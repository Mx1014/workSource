package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionPlanGroupMap;
import com.everhomes.util.StringHelper;

/**
 * Created by rui.jia  2017/12/28 14 :07
 */

public class EquipmentInspectionPlanGroupMap extends EhEquipmentInspectionPlanGroupMap {
    private static final long serialVersionUID = 6501370259675746205L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
