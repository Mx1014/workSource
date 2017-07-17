// @formatter:off
package com.everhomes.rest.statistics.event;

/**
 * <ul>
 *     <li>GENERAL_EVENT((byte) 1): 通用事件</li>
 *     <li>LOG_FILE((byte) 4): 异常文件上传</li>
 * </ul>
 */
public enum StatLogUploadScope {

    GENERAL_EVENT((byte) 1),
    LOG_FILE((byte) 2),
    ;

    private Byte code;

    StatLogUploadScope(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    private static StatLogUploadScope fromCode(Byte code) {
        if (code != null) {
            for (StatLogUploadScope strategy : StatLogUploadScope.values()) {
                if (strategy.code.equals(code)) {
                    return strategy;
                }
            }
        }
        return null;
    }
}
