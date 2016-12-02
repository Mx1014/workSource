package com.everhomes.rest.parking;

import com.everhomes.rest.energy.EnergyMeterStatus;

/**
 * <ul>
 * <li>INACTIVE(0): 删除</li>
 * <li>ACTIVE(2): 正常</li>
 * </ul>
 */
public enum ParkingCommonStatus {

    INACTIVE((byte) 0), WAITING_FOR_APPROVAL((byte) 1), ACTIVE((byte) 2);

    private Byte code;

    ParkingCommonStatus(Byte code) {
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
