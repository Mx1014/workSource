// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>CUSTOM_FIELD((byte)2): 临时授权自定义字段</li>
 * <li>AUTH_PRIORITY((byte)3): 临时授权优先门禁</li>
 * <li>DEFAULT_MAX_DURATION((byte)4): 临时授权默认允许最大时长</li>
 * <li>DEFAULT_MAX_COUNT((byte)5): 临时授权默认允许最大按次授权</li>
 * </ul>
 *
 */
public enum AclinkFormValuesType {
	CUSTOM_FIELD((byte)2), AUTH_PRIORITY((byte)3), DEFAULT_MAX_DURATION((byte)4), DEFAULT_MAX_COUNT((byte)5);

    private byte code;

    public byte getCode() {
        return this.code;
    }

    private AclinkFormValuesType(byte code) {
        this.code = code;
    }
    
    public static AclinkFormValuesType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 2:
                return CUSTOM_FIELD;
        case 3 :
            return AUTH_PRIORITY;
            
        case 4 :
            return DEFAULT_MAX_DURATION;

            case 5:
                return DEFAULT_MAX_COUNT;
        }
        
        return null;
    }
}
