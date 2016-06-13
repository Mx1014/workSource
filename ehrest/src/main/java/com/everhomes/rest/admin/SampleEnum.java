package com.everhomes.rest.admin;

public enum SampleEnum {
    ALL((byte)0), FIRST((byte)1), SECOND((byte)2);
    
    private byte code;
    
    private SampleEnum(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SampleEnum fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return ALL;
            
        case 1 :
            return FIRST;
            
        case 2 :
            return SECOND;
        }
        
        return null;
    }
}
