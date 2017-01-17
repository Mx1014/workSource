package com.everhomes.rest.acl;

/**
 * <p>业务模块树值类型</p>
 * <ul>
 * <li>SERVICE_MODULE: 业务模块</li>
 * <li>PRIVILEGE: 园区web系统菜单</li>
 * </ul>
 */
public enum ServiceModuleTreeVType {

    SERVICE_MODULE((byte)0), PRIVILEGE((byte)1);

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