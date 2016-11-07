package com.everhomes.dbsync;

public enum DataType {
    STRING("string"), INTEGER("integer"), DOUBLE("double"), DATE("date"), DATETIME("datetime"), BINARY("binary");
    
    private String code;
    private DataType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DataType fromCode(String code) {
        DataType[] values = DataType.values();
        for(DataType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
