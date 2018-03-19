package com.everhomes.rest.organization;

/**
 * <ul>
 * INACTIVE 0: 无效
 * WAITING_FOR_APPROVAL 1: 审核中
 * WAITING_FOR_ACCEPTANCE 2：待通过
 * ACTIVE 3：正常
 * REJECT 4: 拒绝
 * </ul>
 * @author janson
 *
 */
public enum OrganizationMemberStatus {
	INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), WAITING_FOR_ACCEPTANCE((byte)2),  ACTIVE((byte)3), REJECT((byte)4);
    
    private byte code;
    private OrganizationMemberStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationMemberStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return INACTIVE;
            
        case 1:
            return WAITING_FOR_APPROVAL;
            
        case 2:
            return WAITING_FOR_ACCEPTANCE;
            
        case 3:
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}