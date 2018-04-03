package com.everhomes.scheduler;

public enum RunningFlag {
    FALSE((byte)0), TRUE((byte)1);

    private byte code;

    private RunningFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RunningFlag fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return FALSE;
        case 1 :
            return TRUE;
        default :
            break;
        }
        
        return null;
    }
}
