package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>WATER(1): 自用水表</li>
 *     <li>ELECTRIC(2): 自用电表</li>
 *     <li>ALL(3): 所有</li>
 *     <li>COMMON_WATER(4): 公摊水表</li>
 *     <li>COMMON_ELECTRIC(5): 公摊电表</li>
 * </ul>
 */
public enum EnergyMeterType {

    WATER((byte)1), ELECTRIC((byte)2), ALL((byte)3),COMMON_WATER((byte)4),COMMON_ELECTRIC((byte)5),ADVERTISEMENT((byte)6);

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
