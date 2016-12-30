package com.everhomes.rest.general_approval;

public enum GeneralApprovalSupportType {
	APP((byte)0), WEB((byte)1), APP_AND_WEB((byte)3);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private GeneralApprovalSupportType(byte code) {
        this.code = code;
    }
    
    public static GeneralApprovalSupportType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return APP;
        case 1 :
        		return WEB;
        case 2 :
            return APP_AND_WEB;
        }
        
        return null;
    }
}
