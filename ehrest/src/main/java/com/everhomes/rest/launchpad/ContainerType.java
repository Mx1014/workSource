// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>
 *     <li>NAVIGATOR((byte)0): 服务广场二级容器</li>
 *     <li>TAB((byte)1): 社群Tab样式二级容器</li>
 * </ul>
 */
public enum ContainerType {
    NAVIGATOR((byte) 0), TAB((byte) 1);

    private Byte code;

    private ContainerType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static ContainerType fromCode(Byte code) {
        ContainerType[] values = ContainerType.values();
        for (ContainerType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
