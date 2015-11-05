package com.everhomes.enterprise;

/**
 * <ul>
 * <li>INACTIVE(0): 已无效</li>
 * <li>WAITING_AUTH: 待认证</li>
 * <li>AUTHENTICATED: 已认证</li>
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
    
    public static EnterpriseContactStatus fromCode(byte code) {
        for(EnterpriseContactStatus t : EnterpriseContactStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
