package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>BILL: 账单项目</li>
 *     <li>SERVICE: 性质</li>
 * </ul>
 */
public enum EnergyStatisticType {

    BILL((byte) 1), SERVICE((byte) 2);

    private Byte code;

    EnergyStatisticType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyStatisticType fromCode(Byte code) {
        for (EnergyStatisticType type : EnergyStatisticType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
