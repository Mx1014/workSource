package com.everhomes.rest.acl;

/**
 * <p>业务模块树值类型</p>
 * <ul>
 * <li>MODULE_CATEGORY: 模块分类</li>
 * <li>SERVICE_MODULE: 业务模块</li>
 * <li>PRIVILEGE_CATEGORY: 权限分类</li>
 * <li>PRIVILEGE: 权限</li>
 * </ul>
 */
public enum ServiceModuleTreeVType {

    MODULE_CATEGORY((byte)0), SERVICE_MODULE((byte)1),PRIVILEGE_CATEGORY((byte)2), PRIVILEGE((byte)3);

    private byte code;

    private ServiceModuleTreeVType(byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static ServiceModuleTreeVType fromCode(Byte code) {
    	ServiceModuleTreeVType[] values = ServiceModuleTreeVType.values();
        for(ServiceModuleTreeVType value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        return null;
    }
}