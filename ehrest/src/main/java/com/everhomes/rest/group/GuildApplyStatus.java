// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>
 *     <li>APPLYING((byte) 0): APPLYING</li>
 *     <li>REJECT((byte) 1): REJECT</li>
 *     <li>AGREE((byte) 2): AGREE</li>
 * </ul>
 */
public enum GuildApplyStatus {
    APPLYING((byte) 0), REJECT((byte) 1), AGREE((byte) 2);

    private byte code;

    private GuildApplyStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static GuildApplyStatus fromCode(Byte code) {
        if (code != null) {
            GuildApplyStatus[] values = GuildApplyStatus.values();
            for (GuildApplyStatus value : values) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
