package com.everhomes.rest.promotion;

public enum ScheduleTaskStatus {
    CREATING((byte)0), PROCESSING((byte)1), COMPLETE((byte)2), DELETE((byte)3);
    
    private byte code;
    
    private ScheduleTaskStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ScheduleTaskStatus fromCode(Byte code) {
        if(code != null) {
            ScheduleTaskStatus[] values = ScheduleTaskStatus.values();
            for(ScheduleTaskStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
