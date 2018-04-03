package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>0: unread, 1: readed</li>
 * </ul>
 */
public enum TrackingPlanReadStatus {
	UNREAD((byte)0), READED((byte)1);

    private byte code;

    private TrackingPlanReadStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TrackingPlanReadStatus fromCode(Byte code) {
        if(code != null) {
            TrackingPlanReadStatus[] values = TrackingPlanReadStatus.values();
            for(TrackingPlanReadStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
