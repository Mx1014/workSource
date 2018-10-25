// @formatter:off
package com.everhomes.rest.launchpadbase;

/**
 * <ul>
 *     <li>NAV_LUCENCY((byte) 1): 不透明导航栏</li>
 *     <li>NAV_OPAQUE((byte) 2): 透明导航栏</li>
 *     <li>TAB((byte) 3): TAB页签</li>
 *     <li>WORKPLATFORM((byte) 4): 工作台</li>
 *     <li>COMMUNITY((byte) 5): 园区广场</li>
 * </ul>
 */
public enum LayoutType {
    NAV_LUCENCY((byte) 1), NAV_OPAQUE((byte) 2), TAB((byte) 3), WORKPLATFORM((byte) 4), COMMUNITY((byte) 5);

    private byte code;

    private LayoutType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static LayoutType fromCode(Byte code) {
        if (code != null) {
            LayoutType[] values = LayoutType.values();
            for (LayoutType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
