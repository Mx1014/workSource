// @formatter:off
package com.everhomes.rest.launchpadbase.indexconfigjson;

/**
 * <ul>
 *     <li>LUCENCY_SHADE((byte)1): 透明渐变样式</li>
 *     <li>OPAQUE_DEFORMATION((byte) 2): 不透明形变样式</li>
 *     <li>OPAQUE_STATIC((byte) 3): 不透明固定样式</li>
 * </ul>
 */
public enum TopBarStyle {

    LUCENCY_SHADE((byte)1), OPAQUE_DEFORMATION((byte) 2),OPAQUE_STATIC((byte) 3);

    private byte code;

    private TopBarStyle(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static TopBarStyle fromCode(Byte code) {
        if (code != null) {
            TopBarStyle[] values = TopBarStyle.values();
            for (TopBarStyle value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
