package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>0:未抄；1：已抄; 2: 无效; 3: 到期未完成</li>
 * </ul>
 * Created by ying.xiong on 2017/10/27.
 */
public enum EnergyTaskStatus {
    NON_READ((byte)0), READ((byte)1), INACTIVE((byte)2), NON_READ_DELAY((byte)3);

    private Byte code;

    EnergyTaskStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyTaskStatus fromCode(Byte code) {
        for (EnergyTaskStatus type : EnergyTaskStatus.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
