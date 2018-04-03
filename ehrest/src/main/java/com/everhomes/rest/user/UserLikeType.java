// @formatter:off
package com.everhomes.rest.user;

/**
 * <ul>
 *     <li>NONE((byte)0): NONE</li>
 *     <li>DISLIKE((byte)1): DISLIKE</li>
 *     <li>LIKE((byte)2): LIKE</li>
 * </ul>
 */
public enum UserLikeType {
    NONE((byte) 0), DISLIKE((byte) 1), LIKE((byte) 2);

    private byte code;

    private UserLikeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static UserLikeType fromCode(Byte code) {
        if (code != null) {
            UserLikeType[] values = UserLikeType.values();
            for (UserLikeType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
