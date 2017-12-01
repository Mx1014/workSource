package com.everhomes.rest.equipment;
//0:waitting for starting 1: waitting for approving  2: Active 3:inActive


import com.everhomes.util.StringHelper;

public enum EquipmentPlanStatus {
    WAITTING_FOR_STARTING((byte) 0, "待发起"),
    WATTING_FOR_APPOVING((byte) 1, "待审批"),
    QUALIFIED((byte) 2, "审批通过"),
    UN_QUALIFIED((byte) 3, "审批不通过"),
    INACTIVE((byte) 4, "无效");

    private byte code;
    private String name;

    EquipmentPlanStatus(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public byte getCode() {
        return code;
    }

    public static EquipmentPlanStatus fromStatus(byte code) {
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
