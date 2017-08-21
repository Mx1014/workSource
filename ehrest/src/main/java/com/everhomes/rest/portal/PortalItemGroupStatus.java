// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>INACTIVE: 无效的</li>
 * <li>CONFIRMING: 待审核</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum PortalItemGroupStatus {
    INACTIVE((byte)0), CONFIRMING((byte)1), ACTIVE((byte)2);

    private byte code;

    private PortalItemGroupStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PortalItemGroupStatus fromCode(Byte code) {
        if(null != code){
            for (PortalItemGroupStatus value: PortalItemGroupStatus.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
