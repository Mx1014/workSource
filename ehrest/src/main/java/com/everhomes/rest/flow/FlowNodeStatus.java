package com.everhomes.rest.flow;

public enum FlowNodeStatus {
	INVALID((byte)0), HIDDEN((byte)1), VISIBLE((byte)2);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private FlowNodeStatus(byte code) {
        this.code = code;
    }
    
    public static FlowNodeStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
        		return HIDDEN;
        case 2 :
            return VISIBLE;
        }
        
        return null;
    }
}
