// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>PORTAL((byte) 1): 门户</li>
 *     <li>PORTAL_ITEM_GROUP((byte) 2): 门户item_group</li>
 *     <li>BOTTOM_NAVIGATION((byte) 3): 底部导航栏</li>
 *     <li>TOP_NAVIGATION((byte) 4): 顶部导航栏</li>
 * </ul>
 */
public enum StatEventPortalStatType {

    PORTAL((byte) 1), PORTAL_ITEM_GROUP((byte) 2), BOTTOM_NAVIGATION((byte) 3), TOP_NAVIGATION((byte) 4);

    private byte code;

    StatEventPortalStatType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static StatEventPortalStatType fromCode(Byte code) {
        if (code != null) {
            for (StatEventPortalStatType status : StatEventPortalStatType.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
        }
        return null;
    }
}
