package com.everhomes.rest.organization.pm;

/**
 * <ul>organizationOwnerAddress认证状态
 * <li>INACTIVE: 未认证</li>
 * <li>ACTIVE: 已认证</li>
 * <li>REJECT: 被驳回</li>
 * </ul>
 */
public enum OrganizationOwnerAddressAuthType {
    INACTIVE((byte) 0), ACTIVE((byte) 1), REJECT((byte) 2);

    private byte code;

    OrganizationOwnerAddressAuthType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static OrganizationOwnerAddressAuthType fromCode(Byte code) {
        if (code == null)
            return null;
        switch (code) {
            case 0:
                return INACTIVE;
            case 1:
                return ACTIVE;
            case 2:
                return REJECT;
            default:
                break;
        }
        return null;
    }
}
