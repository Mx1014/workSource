package com.everhomes.rest.energy;

/**
 * <ul>
 * <li>INACTIVE(0): 删除</li>
 * <li>ACTIVE(2): 正常</li>
 * <li>OBSOLETE(3): 报废</li>
 * </ul>
 */
public enum EnergyMeterStatus {

    INACTIVE((byte) 0), WAITING_FOR_APPROVAL((byte) 1), ACTIVE((byte) 2), OBSOLETE((byte) 3);

    private Byte code;

    EnergyMeterStatus(Byte code) {
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
