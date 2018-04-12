package com.everhomes.rest.acl;

/**
 * <ul>
 *     <li>APPLICATION((byte)1): 根据引用配置</li>
 *     <li>NAMESPACE((byte)2): 根据域空间配置</li>
 * </ul>
 */
public enum WebMenuConfigType {

    APPLICATION((byte) 1), NAMESPACE((byte) 2);

    private Byte code;

    private WebMenuConfigType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static WebMenuConfigType fromCode(Byte code) {
        WebMenuConfigType[] values = WebMenuConfigType.values();
        for (WebMenuConfigType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return APPLICATION;
    }
}