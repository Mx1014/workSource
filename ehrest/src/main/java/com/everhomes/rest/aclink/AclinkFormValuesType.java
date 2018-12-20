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
 * <li>VISITOR_NOTICE((byte)8): 访客来访提示</li>
 * <li>IP((byte)9)第三方url</li>
 * <li>USERNAME((byte)10)第三方用户名</li>
 * <li>PASSWORD((byte)11)第三方密码</li>
 * </ul>
 *
 */
public enum AclinkFormValuesType {
	CUSTOM_FIELD((byte)2), AUTH_PRIORITY_DOOR((byte)3), AUTH_PRIORITY_GROUP((byte)4), DEFAULT_MAX_DURATION((byte)5), DEFAULT_MAX_COUNT((byte)6), HOTLINE((byte)7), VISITOR_NOTICE((byte)8),IP((byte)9),USERNAME((byte)10),PASSWORD((byte)11);

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

            case 8:
                return VISITOR_NOTICE;

            case 9:
                return IP;

            case 10:
                return USERNAME;

            case 11:
                return PASSWORD;
        }
        
        return null;
    }
}
