package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>STANDING_CHARGE_TARIFF(0): 固定费用</li>
 *     <li>BLOCK_TARIFF(1): 阶梯收费</li>
 * </ul>
 * Created by ying.xiong on 2017/3/16.
 */
public enum PriceCalculationType {
    STANDING_CHARGE_TARIFF((byte) 0), BLOCK_TARIFF((byte) 1);

    private Byte code;

    PriceCalculationType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PriceCalculationType fromCode(Byte code) {
        for (PriceCalculationType type : PriceCalculationType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
