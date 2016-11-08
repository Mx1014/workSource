package com.everhomes.dbsync;

public enum NDataType {
    STRING("string"), INTEGER("integer"), DOUBLE("double"), DATE("date"), DATETIME("datetime"), BINARY("binary");
    
    private String code;
    private NDataType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static NDataType fromCode(String code) {
        NDataType[] values = NDataType.values();
        for(NDataType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
