// @formatter:off
package com.everhomes.module;

public enum ServiceModulePrivilegeType {
    ORDINARY((byte) 0), SUPER((byte) 1), ORDINARY_ALL((byte) 2);

    private Byte code;
    private ServiceModulePrivilegeType(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static ServiceModulePrivilegeType fromCode(Byte code) {
    	if(code == null)
            return null;

        ServiceModulePrivilegeType[] values = ServiceModulePrivilegeType.values();

        for (ServiceModulePrivilegeType value: values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
