// @formatter:off
package com.everhomes.rest.user;

/**
 * <ul>
 *     <li>FAMILY((byte)0): 家庭</li>
 *     <li>ORGANIZATION((byte)1): 公司</li>
 * </ul>
 */
public enum AddressUserType {
    FAMILY((byte) 0), ORGANIZATION((byte) 1);

    private byte code;

    private AddressUserType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static AddressUserType fromCode(Byte code) {
        if (code != null) {
            AddressUserType[] values = AddressUserType.values();
            for (AddressUserType value : values) {
                if (code.byteValue() == value.getCode()) {
                    return value;
                }
            }
        }

        return null;
    }
}
