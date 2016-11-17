package com.everhomes.rest.energy;

/**
 * <ul>
 * <li>INACTIVE(0): 删除</li>
 * <li>ACTIVE(2): 正常</li>
 * <li>DISABLED(3): 禁用(如果是修改抄表提示的开启及禁用状态则传递次参数)</li>
 * </ul>
 */
public enum EnergyCommonStatus {

    INACTIVE((byte) 0), WAITING_FOR_APPROVAL((byte) 1), ACTIVE((byte) 2), DISABLED((byte)3);

    private Byte code;

    EnergyCommonStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyMeterStatus fromCode(Byte code) {
        for (EnergyMeterStatus type : EnergyMeterStatus.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
