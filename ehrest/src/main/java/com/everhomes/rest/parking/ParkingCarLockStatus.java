package com.everhomes.rest.parking;

import com.everhomes.rest.energy.EnergyMeterStatus;

/**
 * <ul>
 * <li>UNLOCK(0): 未锁车（解锁）</li>
 * <li>LOCK(1): 已锁车（锁车）</li>
 * </ul>
 */
public enum ParkingCarLockStatus {

    UNLOCK((byte) 0), LOCK((byte) 1);

    private Byte code;

    ParkingCarLockStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ParkingCarLockStatus fromCode(Byte code) {
        for (ParkingCarLockStatus type : ParkingCarLockStatus.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
