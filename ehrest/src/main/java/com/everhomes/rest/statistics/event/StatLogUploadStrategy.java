// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>NO("NO"): 不上传</li>
 *     <li>STARTUP("STARTUP"): 启动时上传</li>
 *     <li>INTERVAL("INTERVAL"): 定时上传</li>
 *     <li>IMMEDIATE("IMMEDIATE"): 即时上传</li>
 * </ul>
 */
public enum StatLogUploadStrategy {

    NO("NO"),
    STARTUP("STARTUP"),
    INTERVAL("INTERVAL"),
    IMMEDIATE("IMMEDIATE"),
    ;

    private String code;

    StatLogUploadStrategy(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private static StatLogUploadStrategy fromCode(String code) {
        if (code != null) {
            for (StatLogUploadStrategy strategy : StatLogUploadStrategy.values()) {
                if (strategy.code.equals(code)) {
                    return strategy;
                }
            }
        }
        return null;
    }
}
