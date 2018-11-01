package com.everhomes.organization.pm.reportForm;

public enum PropertyReportFormStatus {
	
	INACTIVE((byte)0), CONFIRMING((byte)1),ACTIVE((byte)2);
    
    private byte code;
    private PropertyReportFormStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PropertyReportFormStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INACTIVE;
            
        case 1 :
            return CONFIRMING;
            
        case 2 :
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }

}
