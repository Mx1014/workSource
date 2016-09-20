package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>NORMAL(0): 普通联系人</li>
 *     <li>PRIMAL(1): 首要联系人</li>
 * </ul>
 */
public enum OrganizationOwnerOwnerCarPrimaryFlag {
    NORMAL((byte) 0), PRIMARY((byte) 1);

    private Byte code;

    OrganizationOwnerOwnerCarPrimaryFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode(){
        return this.code;
    }

    public static OrganizationOwnerOwnerCarPrimaryFlag fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        switch (code) {
            case 0:
                return NORMAL;
            case 1:
                return PRIMARY;
            default:
                return null;
        }
    }
}
