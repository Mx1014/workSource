package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

public enum EquipmentOperateObjectType {
    EQUIPMENT("equipment");
    String operateObjectType;

    EquipmentOperateObjectType(String operateObjectType) {
        this.operateObjectType = operateObjectType;
    }

    public String getOperateObjectType() {
        return operateObjectType;
    }

    EquipmentOperateObjectType fromCode(String operateObjectType) {
        for (EquipmentOperateObjectType value : EquipmentOperateObjectType.values()) {
            if (value.operateObjectType.equals(operateObjectType)) {
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
