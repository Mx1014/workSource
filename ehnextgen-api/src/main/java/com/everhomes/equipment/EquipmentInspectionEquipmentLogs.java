package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by rui.jia  2018/1/18 11 :26
 */

public class EquipmentInspectionEquipmentLogs extends EhEquipmentInspectionEquipmentLogs {

    private static final long serialVersionUID = 4349945158362187165L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
