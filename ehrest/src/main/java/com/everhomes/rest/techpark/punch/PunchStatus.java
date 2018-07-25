package com.everhomes.rest.techpark.punch;

/**
 * <ul>打卡的状态
 * <li>NO_ASSIGN_PUNCH_SCHEDULED((byte) -2): 未安排班次</li>
 * <li>NO_ASSIGN_PUNCH_RULE((byte) -1): 未设置规则</li>
 *<li>NOTWORKDAY(17): 非工作日无需打卡</li>
 *<li>NONENTRY(16): 未入职</li>
 *<li>RESIGNED(15): 已离职</li>
 * <li>NOTWORKDAY(17): 非工作日无需打卡</li>
 * <li>NONENTRY(16): 未入职</li>
 * <li>RESIGNED(15): 已离职</li>
 * <li>FORGOT_OFF_DUTY((byte)19): 下班缺卡</li>
 * <li>FORGOT_ON_DUTY(14): 上班缺卡</li>
 * <li>BLANDLE(4): 迟到且早退</li>
 * <li>UNPUNCH(3): 未打卡-缺勤</li>
 * <li>LEAVEEARLY(2): 早退</li>
 * <li>BELATE(1): 迟到</li>
 * <li>NORMAL(0): 正常</li>
 * </ul>
 */
public enum PunchStatus {
    NO_ASSIGN_PUNCH_SCHEDULED((byte) -2),
    NO_ASSIGN_PUNCH_RULE((byte) -1),
    FORGOT_OFF_DUTY((byte) 19),
    // 迟到且缺卡
    BELATE_AND_FORGOT((byte) 18),
    NOTWORKDAY((byte) 17),
    NONENTRY((byte) 16),
    RESIGNED((byte) 15),
    FORGOT_ON_DUTY((byte) 14),
    /**
     * BLANDLE(4): 迟到且早退
     */
    BLANDLE((byte) 4),
    /**
     * UNPUNCH(3): 未打卡
     */
    UNPUNCH((byte) 3),
    /**
     * LEAVEEARLY(2): 早退
     */
    LEAVEEARLY((byte) 2),
    /*** <li>BELATE(1): 迟到</li>*/
    BELATE((byte) 1),
    /**
     * <li>NORMAL(0): 正常</li>
     */
    NORMAL((byte) 0);

    private byte code;

    private PunchStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PunchStatus fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for (PunchStatus t : PunchStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
