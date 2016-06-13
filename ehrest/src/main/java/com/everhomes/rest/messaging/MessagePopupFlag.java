package com.everhomes.rest.messaging;

public enum MessagePopupFlag {
    NONE((byte)0), POPUP((byte)1);
    
    private byte code;
    
    private MessagePopupFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static MessagePopupFlag fromCode(Byte code) {
        if(code != null) {
            MessagePopupFlag[] values = MessagePopupFlag.values();
            for(MessagePopupFlag value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
