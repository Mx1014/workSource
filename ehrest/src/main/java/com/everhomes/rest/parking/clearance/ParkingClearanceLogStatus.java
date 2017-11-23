// @formatter:off
package com.everhomes.rest.parking.clearance;

/**
 * <ul>
 *     <li>INACTIVE(0): 删除状态</li>
 *     <li>PROCESSING(1): 处理中</li>
 *     <li>COMPLETED(2): 完成</li>
 *     <li>CANCELLED(3): 取消</li>
 *     <li>PENDING(4): 待处理</li>
 * </ul>
 */
public enum  ParkingClearanceLogStatus {

    INACTIVE     ((byte) 0),
    PROCESSING   ((byte) 1),
    COMPLETED    ((byte) 2),
    CANCELLED    ((byte) 3),
    /*PENDING      ((byte) 4)*/;

    private Byte code;

    ParkingClearanceLogStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ParkingClearanceLogStatus fromCode(Byte code) {
        for (ParkingClearanceLogStatus type : ParkingClearanceLogStatus.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static ParkingClearanceLogStatus fromName(String name) {
        for (ParkingClearanceLogStatus type : ParkingClearanceLogStatus.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
