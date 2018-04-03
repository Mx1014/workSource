package com.everhomes.rest.organization.pm;

/**
 * <ul>
 * 	<li>DEFAULT : 0</li>
 *	<li>LIVING : 1</li>
 *	<li>RENT : 2</li>
 *	<li>FREE : 3</li>
 *	<li>DECORATE : 4</li>
 *	<li>UNSALE : 5</li>
 *</ul>
 *
 */
//已弃用，请使用AddressMappingStatus
@Deprecated
public enum PmAddressMappingStatus {
    DEFAULT((byte)0), LIVING((byte)1), RENT((byte)2), FREE((byte)3), DECORATE((byte)4), UNSALE((byte)5);
    
    private byte code;
    private PmAddressMappingStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmAddressMappingStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return DEFAULT;
            
        case 1:
            return LIVING;
            
        case 2:
            return RENT;
            
        case 3:
            return FREE;
            
        case 4:
            return DECORATE;
            
        case 5:
            return UNSALE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}