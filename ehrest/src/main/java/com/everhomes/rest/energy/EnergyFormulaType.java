package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>AMOUNT(1): 用量</li>
 *     <li>COST(2): 费用</li>
 * </ul>
 */
public enum EnergyFormulaType {

    AMOUNT((byte)1), COST((byte)2);

    private Byte code;

    EnergyFormulaType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyFormulaType fromCode(Byte code) {
        for (EnergyFormulaType type : EnergyFormulaType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
