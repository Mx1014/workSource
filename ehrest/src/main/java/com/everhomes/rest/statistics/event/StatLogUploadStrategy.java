// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>NO((byte) 0): 不上传</li>
 *     <li>INTERVAL((byte) 1): 定时上传</li>
 *     <li>IMMEDIATELY((byte) 2): 及时上传</li>
 *     <li>TIMES_PER_DAY((byte) 3): 每天上传多少次</li>
 * </ul>
 */
public enum StatLogUploadStrategy {

    NO((byte) 0),
    INTERVAL((byte) 1),
    IMMEDIATELY((byte) 2),
    TIMES_PER_DAY((byte) 3),
    ;

    private Byte code;

    StatLogUploadStrategy(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static StatLogUploadStrategy fromCode(Byte code) {
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
