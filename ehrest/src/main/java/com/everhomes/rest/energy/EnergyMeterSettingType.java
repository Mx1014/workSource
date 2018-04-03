package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>PRICE((byte)1): 价格</li>
 *     <li>RATE((byte)2): 倍率</li>
 *     <li>AMOUNT_FORMULA((byte)3): 用量计算公式</li>
 *     <li>COST_FORMULA((byte)4): 费用计算公式</li>
 *     <li>PROMPT((byte)5): 每日抄表提示</li>
 *     <li>MONTH_PROMPT((byte)6): 每月抄表提示</li>
 * </ul>
 */
public enum EnergyMeterSettingType {

    PRICE((byte)1), RATE((byte)2), AMOUNT_FORMULA((byte)3), COST_FORMULA((byte)4), DAY_PROMPT((byte)5), MONTH_PROMPT((byte)6);

    private Byte code;

    EnergyMeterSettingType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyMeterSettingType fromCode(Byte code) {
        for (EnergyMeterSettingType type : EnergyMeterSettingType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
