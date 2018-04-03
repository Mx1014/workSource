// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>INACTIVE: 无效的</li>
 * <li>CONFIRMING: 待审核</li>
 * <li>ACTIVE: 正常</li>
 * <li>DISABLE: 禁用</li>
 * </ul>
 */
public enum PortalNavigationBarStatus {
    INACTIVE((byte)0), CONFIRMING((byte)1), ACTIVE((byte)2), DISABLE((byte)3);

    private byte code;

    private PortalNavigationBarStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PortalNavigationBarStatus fromCode(Byte code) {
        if(null != code){
            for (PortalNavigationBarStatus value: PortalNavigationBarStatus.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
