// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>INACTIVE(0): 无效</li>
 * <li>QUEUEING(1): 排队中</li>
 * <li>notified(2): 已通知</li>
 * <li>ISSUED(3): 已领卡</li>
 * </ul>
 */
public enum ParkingCardRequestStatus {
    INACTIVE((byte)0), QUEUEING((byte)1), NOTIFIED((byte)2), ISSUED((byte)3);
    
    private byte code;
    
    private ParkingCardRequestStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCardRequestStatus fromCode(Byte code) {
        if(code != null) {
            ParkingCardRequestStatus[] values = ParkingCardRequestStatus.values();
            for(ParkingCardRequestStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
