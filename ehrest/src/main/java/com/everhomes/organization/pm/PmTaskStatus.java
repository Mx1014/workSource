package com.everhomes.organization.pm;
public enum PmTaskStatus {
    UNTREATED((byte)0), TREATING((byte)1), TREATED((byte)2);
    
    private byte code;
    private PmTaskStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return UNTREATED;
            
        case 1:
            return TREATING;
            
        case 2:
            return TREATED;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}