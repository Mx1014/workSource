package com.everhomes.rest.activity;

public enum VideoState {
    UN_READY((byte)0), DEBUG((byte)1), LIVE((byte)2), RECORDING((byte)3), EXCEPTION((byte)4), INVALID((byte)5), CREATING_FILE((byte)6);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private VideoState(byte code) {
        this.code = code;
    }
    
    public static VideoState fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return UN_READY;
        case 1 :
            return DEBUG;
        case 2:
            return LIVE;
        case 3:
            return RECORDING;
        case 4:
            return EXCEPTION;
        case 5:
            return INVALID;
        case 6:
            return CREATING_FILE;
        }
        
        return null;
    }
}
