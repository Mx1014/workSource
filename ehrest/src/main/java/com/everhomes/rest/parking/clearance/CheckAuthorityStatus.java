package com.everhomes.rest.parking.clearance;

/**
 * <ul>
 *     <li>SUCCESS(1): 成功</li>
 *     <li>FAILURE(0): 失败</li>
 * </ul>
 */
public enum CheckAuthorityStatus {

    SUCCESS((byte) 1), FAILURE((byte) 0);

    private Byte code;

    CheckAuthorityStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static CheckAuthorityStatus fromCode(Byte code) {
        for (CheckAuthorityStatus status : CheckAuthorityStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
