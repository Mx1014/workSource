// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 *
 * <ul>
 * <li>BOOKING_MANAGEMENT((byte)0): 预约管理</li>
 * <li>VISITOR_MANAGEMENT((byte)1): 访客管理</li>
 * </ul>
 */
public enum VisitorsysSearchFlagType {
    BOOKING_MANAGEMENT((byte)0),
    VISITOR_MANAGEMENT((byte)1),
    SYNCHRONIZATION((byte)2);//同步搜索引擎的时候，使用这个

    private byte code;

    private VisitorsysSearchFlagType(byte code) {
        this.code = code;
    }

    public byte getCode(){
        return code;
    }

    public static VisitorsysSearchFlagType fromCode(Byte code) {
        if (code != null) {
            for (VisitorsysSearchFlagType status : VisitorsysSearchFlagType.values()) {
                if (status.code == code.byteValue()) {
                    return status;
                }
            }
        }
        return null;
    }
}
