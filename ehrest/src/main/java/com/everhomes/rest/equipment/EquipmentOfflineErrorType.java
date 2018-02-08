package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/2/8 09 :59
 */

public enum  EquipmentOfflineErrorType {
    INEPECT_TASK((byte)0,"巡检任务"),REPAIR_TASK((byte)1,"维修任务");
    private byte code;
    private String name;

    EquipmentOfflineErrorType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private EquipmentOfflineErrorType fromCode(Byte code) {
        for (EquipmentOfflineErrorType v : EquipmentOfflineErrorType.values()) {
            if (v.code == code) {
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
