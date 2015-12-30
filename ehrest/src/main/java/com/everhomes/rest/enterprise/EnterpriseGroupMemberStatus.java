package com.everhomes.rest.enterprise;

/**
 * <ul>公司部门成员状态
 * <li>INACTIVE(0): 无效的</li>
 * <li>WAITING_FOR_APPROVAL(1): 等待批准加入</li>
 * <li>ACTIVE(2): 正常成员</li>
 * </ul>
 */
public enum EnterpriseGroupMemberStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), ACTIVE((byte)2), ;
    
    private byte code;
    private EnterpriseGroupMemberStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseGroupMemberStatus fromCode(Byte code) {
        if(code != null) {
            for(EnterpriseGroupMemberStatus value : EnterpriseGroupMemberStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
