// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>UNISSUED(0): 未领卡</li>
 * <li>ISSUED(1): 已领卡</li>
 * </ul>
 */
public enum ParkingCardIssueFlag {
    UNISSUED((byte)0), ISSUED((byte)1);
    
    private byte code;
    
    private ParkingCardIssueFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCardIssueFlag fromCode(Byte code) {
        if(code != null) {
            ParkingCardIssueFlag[] values = ParkingCardIssueFlag.values();
            for(ParkingCardIssueFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
