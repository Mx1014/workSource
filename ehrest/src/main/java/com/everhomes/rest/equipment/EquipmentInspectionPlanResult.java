package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

public enum EquipmentInspectionPlanResult {
    NONE((byte) 0, ""),
    QUALIFIED((byte) 1, "审批通过"),
    UNQUALIDIED((byte) 2, "审批不通过"),
    REVIEW_DELAY((byte) 3, "审批延迟"),
    INVALID((byte) 4, "无效");

    private byte code;
    private String name;

    EquipmentInspectionPlanResult(byte code, String name) {
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
