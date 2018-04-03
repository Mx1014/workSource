package com.everhomes.rest.acl;

/**
 * <ul>
 *     <li>NO((byte)0): NO</li>
 *     <li>YES((byte)1): YES</li>
 * </ul>
 */
public enum WebMenuSelectedFlag {

    NO((byte) 0), YES((byte) 1);

    private byte code;

    private WebMenuSelectedFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WebMenuSelectedFlag fromCode(Byte code) {
        if(null == code){
            return null;
        }
        for (WebMenuSelectedFlag flag : WebMenuSelectedFlag.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}