package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>WATER(1): 水表</li>
 *     <li>ELECTRIC(2): 电表</li>
 *     <li>ALL(3): 所有</li>
 * </ul>
 */
public enum EnergyMeterType {

    WATER((byte)1), ELECTRIC((byte)2), ALL((byte)3);

    private Byte code;

    EnergyMeterType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyMeterType fromCode(Byte code) {
        for (EnergyMeterType type : EnergyMeterType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
