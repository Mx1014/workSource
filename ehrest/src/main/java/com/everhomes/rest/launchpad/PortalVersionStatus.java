// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>
 *     <li>PREVIEW((byte) 1): PREVIEW</li>
 *     <li>RELEASE((byte) 2): RELEASE</li>
 * </ul>
 */
public enum PortalVersionStatus {
    PREVIEW((byte) 1), RELEASE((byte) 2);

    private byte code;

    private PortalVersionStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static PortalVersionStatus fromCode(Byte code) {

        if(code == null){
            return null;
        }

        PortalVersionStatus[] values = PortalVersionStatus.values();
        for (PortalVersionStatus value : values) {
            if (code.equals(value.code)) {
                return value;
            }
        }
        return null;
    }
}
