// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>
 *     <li>HIDE((byte)0): 不可见</li>
 *     <li>SEE_ONLY((byte)1): 仅仅查看</li>
 *     <li>INTERACT((byte)2): 可以交互</li>
 * </ul>
 */
public enum TouristPostPolicyFlag {
    HIDE((byte) 0), SEE_ONLY((byte) 1), INTERACT((byte) 2);

    private byte code;

    private TouristPostPolicyFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TouristPostPolicyFlag fromCode(Byte code) {
        if (code != null) {
            TouristPostPolicyFlag[] values = TouristPostPolicyFlag.values();
            for (TouristPostPolicyFlag value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return INTERACT;
    }
}
