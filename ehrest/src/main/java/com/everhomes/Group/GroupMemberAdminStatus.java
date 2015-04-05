// @formatter:off
package com.everhomes.Group;

public enum GroupMemberAdminStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), WAITING_FOR_ACCEPTANCE((byte)2), ACTIVE((byte)3);
    
    private byte code;
    
    private GroupMemberAdminStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return code;
    }
    
    public static GroupMemberAdminStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INACTIVE;
            
        case 1 :
            return WAITING_FOR_APPROVAL;
            
        case 2 :
            return WAITING_FOR_ACCEPTANCE;
                    
        case 3 :
            return ACTIVE;
                    
        default :
            break;
        }
        
        return null;
    }
}
