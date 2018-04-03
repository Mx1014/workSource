package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>STANDARD : 0 model表标准</li>
 * <li>TEMPLATE : 1 model表模板</li>
 * </ul>
 */
public enum EquipmentModelType {
    STANDARD((byte) 0), TEMPLATE((byte) 1);

    private byte code;

    EquipmentModelType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public static EquipmentModelType fromStatus(byte code) {
        for (EquipmentModelType v : EquipmentModelType.values()) {
            if (v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
