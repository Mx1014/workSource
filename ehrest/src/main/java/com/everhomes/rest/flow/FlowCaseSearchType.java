package com.everhomes.rest.flow;

public enum FlowCaseSearchType {
	APPLIER((byte)0), TODO_LIST((byte)1), DONE_LIST((byte)2), SUPERVISOR((byte)3), ADMIN((byte)4);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private FlowCaseSearchType(byte code) {
        this.code = code;
    }
    
    public static FlowCaseSearchType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return APPLIER;
        case 1 :
        		return TODO_LIST;
        case 2 :
            return DONE_LIST;
        case 3 :
            return SUPERVISOR;
        }
        
        return null;
    }
}
