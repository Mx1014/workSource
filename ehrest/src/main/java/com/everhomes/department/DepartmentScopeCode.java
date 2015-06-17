package com.everhomes.department;
public enum DepartmentScopeCode {
    GARC((byte)0), GAPS((byte)1), GANC((byte)2);
    
    private byte code;
    private DepartmentScopeCode(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DepartmentScopeCode fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return GARC;
            
        case 1:
            return GAPS;
            
        case 2:
            return GANC;
            
      
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}