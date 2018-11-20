// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>CUSTOM_FIELD((byte)2): 临时授权自定义字段</li>
 * <li>AUTH_PRIORITY_DOOR((byte)3): 临时授权优先门禁</li>
 * <li>AUTH_PRIORITY_GROUP((byte)4): 临时授权优先门禁组</li>
 * <li>DEFAULT_MAX_DURATION((byte)5): 临时授权默认允许最大时长</li>
 * <li>DEFAULT_MAX_COUNT((byte)6): 临时授权默认允许最大按次授权</li>
 * <li>HOTLINE((byte)7): 服务热线</li>
 * </ul>
 *
 */
public enum AclinkFormValuesType {
	CUSTOM_FIELD((byte)2), AUTH_PRIORITY_DOOR((byte)3), AUTH_PRIORITY_GROUP((byte)4), DEFAULT_MAX_DURATION((byte)5), DEFAULT_MAX_COUNT((byte)6), HOTLINE((byte)7);

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
            return AUTH_PRIORITY_DOOR;
            
        case 4 :
            return AUTH_PRIORITY_GROUP;

            case 5:
                return DEFAULT_MAX_DURATION;

            case 6:
                return DEFAULT_MAX_COUNT;

            case 7:
                return HOTLINE;
        }
        
        return null;
    }
}
