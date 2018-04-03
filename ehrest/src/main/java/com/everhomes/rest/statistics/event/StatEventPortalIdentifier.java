// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>BOTTOM_NAVIGATION("BottomNavigation"): BOTTOM_NAVIGATION</li>
 *     <li>TOP_NAVIGATION("TopNavigation"): TOP_NAVIGATION</li>
 * </ul>
 */
public enum StatEventPortalIdentifier {

    BOTTOM_NAVIGATION("BottomNavigation"), TOP_NAVIGATION("TopNavigation");

    private String code;

    StatEventPortalIdentifier(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static StatEventPortalIdentifier fromCode(String code) {
        if (code != null) {
            for (StatEventPortalIdentifier status : StatEventPortalIdentifier.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
