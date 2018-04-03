package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>0: invalid, 1: waiting for send out, 2: already sended</li>
 * </ul>
 */
public enum TrackingPlanNotifyStatus {
    INVAILD((byte)0), WAITING_FOR_SEND_OUT((byte)1), ALREADY_SENDED((byte)2);

    private byte code;

    private TrackingPlanNotifyStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TrackingPlanNotifyStatus fromCode(Byte code) {
        if(code != null) {
            TrackingPlanNotifyStatus[] values = TrackingPlanNotifyStatus.values();
            for(TrackingPlanNotifyStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
