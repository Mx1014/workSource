// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>TOP_NAVIGATION((byte) 1): TOP_NAVIGATION</li>
 *     <li>BOTTOM_NAVIGATION((byte) 2): BOTTOM_NAVIGATION</li>
 * </ul>
 */
public enum StatEventPortalConfigType {

    TOP_NAVIGATION((byte) 1), BOTTOM_NAVIGATION((byte) 2);

    private byte code;

    StatEventPortalConfigType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static StatEventPortalConfigType fromCode(Byte code) {
        if (code != null) {
            for (StatEventPortalConfigType status : StatEventPortalConfigType.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
        }
        return null;
    }
}
