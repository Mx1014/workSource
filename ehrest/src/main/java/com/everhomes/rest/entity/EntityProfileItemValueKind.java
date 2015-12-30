// @formatter:off
package com.everhomes.rest.entity;

public enum EntityProfileItemValueKind {
    OPAQUE_JSON_OBJECT((byte)0), ENTITY_DESCRIPTOR((byte)1);
    
    private byte code;
    
    private EntityProfileItemValueKind(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EntityProfileItemValueKind fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return OPAQUE_JSON_OBJECT;
            
        case 1 :
            return ENTITY_DESCRIPTOR;
                    
        default :
            assert(false);
        }
        
        return null;
    }
}

