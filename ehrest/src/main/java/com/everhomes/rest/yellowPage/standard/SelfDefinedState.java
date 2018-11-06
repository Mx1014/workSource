package com.everhomes.rest.yellowPage.standard;


public enum SelfDefinedState {
	ENABLE((byte)1), DISABLE((byte)0);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private SelfDefinedState(byte code) {
        this.code = code;
    }
    
    public static SelfDefinedState fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return DISABLE;
            
        case 1 :
            return ENABLE;
        }
        
        return null;
    }
}
