// @formatter:off
package com.everhomes.rest.user.admin;

/**
 * <ul>
 *     <li>INACTIVE((byte) 0): 拒绝</li>
 *     <li>WAITING_FOR_APPROVAL((byte) 1): 待认证</li>
 *     <li>ACTIVE((byte) 2): 同意</li>
 * </ul>
 */
public enum UserAppealLogStatus {

    INACTIVE((byte) 0), WAITING_FOR_APPROVAL((byte) 1), ACTIVE((byte) 2);

    private Byte code;

    UserAppealLogStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static UserAppealLogStatus fromCode(Byte code) {
        for (UserAppealLogStatus status : UserAppealLogStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
