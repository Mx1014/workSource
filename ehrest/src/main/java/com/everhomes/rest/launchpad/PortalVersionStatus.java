// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>
 *     <li>INIT((byte)0): 初始化</li>
 *     <li>EDIT((byte)1): 编辑</li>
 *     <li>PULISH_SUCCESS((byte)2): 发布成功</li>
 *     <li>PUBLISH_FAIL((byte)3): 发布失败</li>
 * </ul>
 */
public enum PortalVersionStatus {
    INIT((byte) 0), EDIT((byte) 1), PULISH_SUCCESS((byte) 2), PUBLISH_FAIL((byte) 3);

    private byte code;

    private PortalVersionStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static PortalVersionStatus fromCode(byte code) {
        PortalVersionStatus[] values = PortalVersionStatus.values();
        for (PortalVersionStatus value : values) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
