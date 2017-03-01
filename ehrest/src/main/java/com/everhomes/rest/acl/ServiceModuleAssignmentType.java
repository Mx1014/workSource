package com.everhomes.rest.acl;

/**
 * <p>分配类型</p>
 * <ul>
 * <li>All: 全部</li>
 * <li>PORTION: 部分</li>
 * </ul>
 */
public enum ServiceModuleAssignmentType {

	All((byte)0), PORTION((byte)1);

    private byte code;
    private ServiceModuleAssignmentType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ServiceModuleAssignmentType fromCode(Byte code) {
    	ServiceModuleAssignmentType[] values = ServiceModuleAssignmentType.values();
        for(ServiceModuleAssignmentType value : values) {
        	  if(value.code == code.byteValue()) {
                  return value;
              }
        }
        return null;
    }
}