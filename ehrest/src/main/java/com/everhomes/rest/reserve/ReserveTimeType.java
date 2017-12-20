package com.everhomes.rest.reserve;

/**
 * <ul>预定时间类型
 * <li>HOUR(1): 按小时</li>
 * <li>HALF_DAY(2): 按半天</li>
 * <li>HALF_DAY_CONTAIN_NIGHT(3): 按半天 (支持晚上)</li>
 * <li>DAY(4): 按天</li>
 * <li>MONTH(5): 按月</li>
 * </ul>
 */
public enum ReserveTimeType {

    HOUR((byte)1), HALF_DAY((byte)2), HALF_DAY_CONTAIN_NIGHT((byte)3), DAY((byte)4), MONTH((byte)5);

    private byte code;
    private ReserveTimeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveTimeType fromCode(byte code) {
        for(ReserveTimeType t : ReserveTimeType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
