package com.everhomes.rest.flow;

import com.everhomes.rest.aclink.DoorAccessOwnerType;

public enum FlowStatusType {
	INVALID((byte)0), CONFIG((byte)1), RUNNING((byte)2), STOP((byte)3);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private FlowStatusType(byte code) {
        this.code = code;
    }
    
    public static FlowStatusType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
            return CONFIG;
        case 2:
            return RUNNING;
        case 3:
        	   return STOP;
        }
        
        return null;
    }
}
