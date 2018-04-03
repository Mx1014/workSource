package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>AMOUNT((byte)1): 用量</li>
 *     <li>COST((byte)2): 费用</li>
 *     <li>ACTUAL_BURDEN_AMOUNT((byte)3): 实际负担总用量</li>
 *     <li>ACTUAL_BURDEN_COST((byte)4): 实际负担总费用</li>
 * </ul>
 */
public enum EnergyStatBill {

    AMOUNT((byte)1), COST((byte)2), ACTUAL_BURDEN_AMOUNT((byte)3), ACTUAL_BURDEN_COST((byte)4);

    private Byte code;

    EnergyStatBill(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyStatBill fromCode(Byte code) {
        for (EnergyStatBill project : EnergyStatBill.values()) {
            if (project.getCode().equals(code)) {
                return project;
            }
        }
        return null;
    }
}
