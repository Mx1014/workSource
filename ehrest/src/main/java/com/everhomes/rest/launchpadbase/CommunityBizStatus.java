// @formatter:off
package com.everhomes.rest.launchpadbase;

/**
 * <ul>
 *     <li>DELETE((byte) 0): 删除</li>
 *     <li>DISABLE((byte) 1): 未启用</li>
 *     <li>ENABLE((byte) 2): 启用</li>
 * </ul>
 */
public enum CommunityBizStatus {
    DELETE((byte) 0), DISABLE((byte) 1), ENABLE((byte) 2);

    private byte code;

    private CommunityBizStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static CommunityBizStatus fromCode(Byte code) {
        if (code != null) {
            CommunityBizStatus[] values = CommunityBizStatus.values();
            for (CommunityBizStatus value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
