// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值订单是否删除
 * <li>NORMAL(0): 未删除</li>
 * <li>DELETED(1): 已删除</li>
 * </ul>
 */
public enum ParkingOrderDeleteFlag {
    NORMAL((byte)0), DELETED((byte)1);
    
    private byte code;
    
    private ParkingOrderDeleteFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingOrderDeleteFlag fromCode(Byte code) {
        if(code != null) {
            ParkingOrderDeleteFlag[] values = ParkingOrderDeleteFlag.values();
            for(ParkingOrderDeleteFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
