package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>WIFI("WIFI"): WIFI</li>
 *     <li>GSM("GSM"): GSM</li>
 * </ul>
 */
public enum StatEventLogUploadStrategyAccess {

    WIFI("WIFI"), GSM("GSM");

    private String code;

    StatEventLogUploadStrategyAccess(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static StatEventLogUploadStrategyAccess fromCode(String code) {
        if (code != null) {
            for (StatEventLogUploadStrategyAccess status : StatEventLogUploadStrategyAccess.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
