// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>HOURLY("HOURLY"): HOURLY</li>
 *     <li>DAILY("DAILY"): DAILY</li>
 *     <li>WEEKLY("WEEKLY"): WEEKLY</li>
 *     <li>MONTHLY("MONTHLY"): MONTHLY</li>
 * </ul>
 */
public enum StatEventStatTimeInterval {

    HOURLY("HOURLY"), DAILY("DAILY"), WEEKLY("WEEKLY"), MONTHLY("MONTHLY");

    private String code;

    StatEventStatTimeInterval(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static StatEventStatTimeInterval fromCode(String code) {
        if (code != null) {
            for (StatEventStatTimeInterval status : StatEventStatTimeInterval.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
