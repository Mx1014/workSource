// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>HOTLINE((byte)1): 热线电话</li>
 * </ul>
 *
 */
public enum AclinkExtraActionsItemType {
	HOTLINE((byte)1);

    private byte code;

    public byte getCode() {
        return this.code;
    }

    private AclinkExtraActionsItemType(byte code) {
        this.code = code;
    }
    
    public static AclinkExtraActionsItemType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return null;

        case 1 :
            return HOTLINE;
        }

        return null;
    }
}
