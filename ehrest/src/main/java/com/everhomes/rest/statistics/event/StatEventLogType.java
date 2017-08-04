// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>GENERAL_EVENT((byte) 1): 通用事件</li>
 *     <li>CRASH_LOG((byte) 2): 崩溃</li>
 *     <li>ERROR_LOG((byte) 3): 错误</li>
 * </ul>
 */
public enum StatEventLogType {

    GENERAL_EVENT((byte) 1),
    CRASH_LOG((byte) 2),
    ERROR_LOG((byte) 3),;

    private Byte code;

    StatEventLogType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static StatEventLogType fromCode(Byte code) {
        if (code != null) {
            for (StatEventLogType strategy : StatEventLogType.values()) {
                if (strategy.code.equals(code)) {
                    return strategy;
                }
            }
        }
        return null;
    }
}
