package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>FAILED(0): 失败</li>
 * <li>SUCCEED(1): 成功</li>
 * </ul>
 */
public enum ParkingRechargeResultStatus {

    FAILED((byte) 0), SUCCEED((byte) 1);

    private Byte code;

    ParkingRechargeResultStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ParkingRechargeResultStatus fromCode(Byte code) {
        for (ParkingRechargeResultStatus type : ParkingRechargeResultStatus.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
