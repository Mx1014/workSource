// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>PREVIEW((byte)1): 预览</li>
 *     <li>RELEASE((byte)2): 正式发布</li>
 * </ul>
 */
public enum PortalPublishType {

    PREVIEW((byte) 1), RELEASE((byte) 2);

    private Byte code;

    private PortalPublishType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static PortalPublishType fromCode(Byte code) {
        if (null != code) {
            for (PortalPublishType value : PortalPublishType.values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
