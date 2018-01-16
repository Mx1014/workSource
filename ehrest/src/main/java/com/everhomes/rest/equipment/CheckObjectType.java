package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

public enum CheckObjectType {
    STANDARD_NUM((byte) 0, "设备自编号"), PLAN_NUM((byte) 1, "巡检计划编号");

    private byte code;
    private String name;

    CheckObjectType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public byte getCode() {
        return code;
    }

    public static EquipmentPlanStatus FromCode(byte code) {
        for (EquipmentPlanStatus v : EquipmentPlanStatus.values()) {
            if (v.getCode() == code) {
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
