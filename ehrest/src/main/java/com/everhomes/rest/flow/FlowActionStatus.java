package com.everhomes.rest.flow;

public enum FlowActionStatus {
	INVALID((byte)0), DISABLED((byte)1), ENABLED((byte)2);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private FlowActionStatus(byte code) {
        this.code = code;
    }
    
    public static FlowActionStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
        		return DISABLED;
        case 2 :
            return ENABLED;
        }
        
        return null;
    }
}
