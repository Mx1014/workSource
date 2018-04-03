// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>INACTIVE: 无效的</li>
 * <li>CONFIRMING: 待审核</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum PortalLayoutStatus {
    INACTIVE((byte)0), CONFIRMING((byte)1), ACTIVE((byte)2);

    private byte code;

    private PortalLayoutStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PortalLayoutStatus fromCode(Byte code) {
        if(null != code){
            for (PortalLayoutStatus value: PortalLayoutStatus.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
