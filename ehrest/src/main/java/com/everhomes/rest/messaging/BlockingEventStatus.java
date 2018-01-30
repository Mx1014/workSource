package com.everhomes.rest.messaging;

/**
 * <p>阻塞事件状态</p>
 * <ul>
 * <li>BLOCKING("blocking"): 阻塞</li>
 * <li>CONTINUTE("continute"): 通过</li>
 * <li>TIMEOUT("timeout"): 超时</li>
 * <li>ERROR("error"): 错误</li>
 * </ul>
 */
public enum BlockingEventStatus {
    BLOCKING("blocking"), CONTINUTE("continute"), TIMEOUT("timeout"), ERROR("error");
    private String code;

    private BlockingEventStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static BlockingEventStatus fromCode(String code) {
        if (code != null) {
            BlockingEventStatus[] values = BlockingEventStatus.values();
            for (BlockingEventStatus value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
