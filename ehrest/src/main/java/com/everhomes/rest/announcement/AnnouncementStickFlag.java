// @formatter:off
package com.everhomes.rest.announcement;

/**
 * <ul>
 *     <li>DEFAULT((byte)0): DEFAULT</li>
 *     <li>TOP((byte)1): TOP</li>
 * </ul>
 */
public enum AnnouncementStickFlag {
    DEFAULT((byte) 0), TOP((byte) 1);

    private Byte code;

    private AnnouncementStickFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static AnnouncementStickFlag fromCode(Byte code) {
        if (code != null) {
            AnnouncementStickFlag[] values = AnnouncementStickFlag.values();
            for (AnnouncementStickFlag value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
