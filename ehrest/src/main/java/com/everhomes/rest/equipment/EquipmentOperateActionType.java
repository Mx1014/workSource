package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * Created by rui.jia  2018/1/18 11 :33
 */

public enum EquipmentOperateActionType {
    NONE((byte) 0), INSERT((byte) 1), UPDATE((byte) 2), DELETE((byte) 3);
    private byte code;

    EquipmentOperateActionType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    EquipmentOperateActionType fromCode(byte code) {
        for (EquipmentOperateActionType value : EquipmentOperateActionType.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
