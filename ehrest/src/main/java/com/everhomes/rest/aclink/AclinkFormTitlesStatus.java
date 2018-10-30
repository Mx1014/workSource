// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>TEXT((byte)1): 文本</li>
 * <li>SINGLE_SELECTION((byte)2): 单选</li>
 * </ul>
 *
 */
public enum AclinkFormTitlesStatus {
	DELETE((byte)0), NECESSARY((byte)1),OPTIONAL((byte)2),INVALID_NECESSARY((byte)3),INVALID_OPTIONAL((byte)4);

    private byte code;

    public byte getCode() {
        return this.code;
    }

    private AclinkFormTitlesStatus(byte code) {
        this.code = code;
    }
    
    public static AclinkFormTitlesStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
            case 0 :
                return DELETE;
            case 1 :
                return NECESSARY;
            case 2 :
                return  OPTIONAL;
            case 3 :
                return INVALID_NECESSARY;
            case 4:
                return  INVALID_OPTIONAL;
        }
        
        return null;
    }
}
