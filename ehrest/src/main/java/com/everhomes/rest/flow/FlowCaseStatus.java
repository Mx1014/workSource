package com.everhomes.rest.flow;

public enum FlowCaseStatus {
	INVALID((byte)0), INITIAL((byte)1), PROCESS((byte)2), ABSORTED((byte)3), FINISHED((byte)4), EVALUATE((byte)5);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private FlowCaseStatus(byte code) {
        this.code = code;
    }
    
    public static FlowCaseStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
        		return INITIAL;
        case 2 :
            return PROCESS;
        case 3 :
            return ABSORTED;
        case 4 :
            return FINISHED;
        case 5 :
            return EVALUATE;
        }
        
        return null;
    }
}
