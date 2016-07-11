package com.everhomes.forum;


public enum HotPostModifyType {
	
	VIEW((byte)0), LIKE((byte)1);
    
    private byte code;
    
    private HotPostModifyType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static HotPostModifyType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return VIEW;
        case 1 :
            return LIKE;
        default :
            break;
        }
        
        return null;
    }

}
