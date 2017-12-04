package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentPlanMap;
import com.everhomes.util.StringHelper;

/**
 * Date: 2017/12/3 16:53
 *
 * @author jerry.R
 */

public class EquipmentInspectionEquipmentPlanMap extends EhEquipmentInspectionEquipmentPlanMap {

    private static final long serialVersionUID = -7892625796879036483L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
