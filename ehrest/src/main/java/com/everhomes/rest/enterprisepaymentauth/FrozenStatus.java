package com.everhomes.rest.enterprisepaymentauth;

/**
 * <ul>
 * <li>FROZEN((byte) 1): 冻结中</li>
 * <li>FROZEN_CONFIRM((byte) 2): 冻结已确认</li>
 * <li>UN_FROZEN((byte) 3): 取消冻结</li>
 * </ul>
 */
public enum FrozenStatus {
    FROZEN((byte) 1), FROZEN_CONFIRM((byte) 2), UN_FROZEN((byte) 3);

    private byte code;

    FrozenStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static FrozenStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (FrozenStatus status : FrozenStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
