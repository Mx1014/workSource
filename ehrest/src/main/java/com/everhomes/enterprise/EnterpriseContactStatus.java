package com.everhomes.enterprise;

/**
 * <ul>
 * <li>INACTIVE(0): 已无效</li>
 * <li>WAITING_AUTH(1): 待认证</li>
 * <li>AUTHENTICATED(2): 已认证</li>
 * </ul>
 */
public enum EnterpriseContactStatus {
    INACTIVE((byte)0), WAITING_AUTH((byte)1), AUTHENTICATED((byte)2);
    
    private byte code;
    private EnterpriseContactStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseContactStatus fromCode(Byte code) {
        if(code != null) {
            for(EnterpriseContactStatus value : EnterpriseContactStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
