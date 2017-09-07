// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>INACTIVE: 无效的</li>
 * <li>CONFIRMING: 待审核</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum PortalLayoutTemplateStatus {
    INACTIVE((byte)0), CONFIRMING((byte)1), ACTIVE((byte)2);

    private byte code;

    private PortalLayoutTemplateStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PortalLayoutTemplateStatus fromCode(Byte code) {
        if(null != code){
            for (PortalLayoutTemplateStatus value: PortalLayoutTemplateStatus.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
