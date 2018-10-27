// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>TEXT((byte)1): 文本</li>
 * <li>SINGLE_SELECTION((byte)2): 单选</li>
 * </ul>
 *
 */
public enum AclinkFormTitlesItemType {
	NODE((byte)0), TEXT((byte)1), SINGLE_SELECTION((byte)2);

    private byte code;

    public byte getCode() {
        return this.code;
    }

    private AclinkFormTitlesItemType(byte code) {
        this.code = code;
    }
    
    public static AclinkFormTitlesItemType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
                return NODE;
        case 1 :
            return TEXT;
            
        case 2 :
            return SINGLE_SELECTION;
        }
        
        return null;
    }
}
