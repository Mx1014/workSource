package com.everhomes.sms;

/**
 * <ul>
 *     <li>REPORT_SUCCESS((byte) 1): 报告成功</li>
 *     <li>SEND_SUCCESS((byte) 2): 发送成功</li>
 *     <li>UNKNOWN((byte) 3): 未知</li>
 *     <li>SEND_FAILED((byte) 4): 发送失败</li>
 *     <li>REPORT_FAILED((byte) 5): 报告失败</li>
 * </ul>
 */
public enum SmsLogStatus {

    // 顺序很重要，不能改变
    REPORT_SUCCESS((byte) 1), SEND_SUCCESS((byte) 2), UNKNOWN((byte) 3), SEND_FAILED((byte) 4), REPORT_FAILED((byte) 5);

    private Byte code;

    SmsLogStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static SmsLogStatus fromCode(Byte code) {
        if (code != null) {
            for (SmsLogStatus status : SmsLogStatus.values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
