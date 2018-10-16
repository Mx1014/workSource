// @formatter:off
package com.everhomes.rest.announcement;

/**
 * <ul>
 *     <li>UNSUPPORT((byte)0): UNSUPPORT</li>
 *     <li>SUPPORT((byte)1): SUPPORT</li>
 * </ul>
 */
public enum AnnouncementInteractFlag {
    UNSUPPORT((byte) 0), SUPPORT((byte) 1);

    private Byte code;

    private AnnouncementInteractFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static AnnouncementInteractFlag fromCode(Byte code) {
        if (code != null) {
            AnnouncementInteractFlag[] values = AnnouncementInteractFlag.values();
            for (AnnouncementInteractFlag value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return SUPPORT;
    }
}
