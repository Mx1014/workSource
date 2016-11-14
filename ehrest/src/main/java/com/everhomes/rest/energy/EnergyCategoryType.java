package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>BILL: 账单项目</li>
 *     <li>SERVICE: 性质</li>
 * </ul>
 */
public enum EnergyCategoryType {

    BILL((byte) 1), SERVICE((byte) 2);

    private Byte code;

    EnergyCategoryType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyCategoryType fromCode(Byte code) {
        for (EnergyCategoryType type : EnergyCategoryType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
