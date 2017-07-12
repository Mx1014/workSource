package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>source_user: 用户选择源为普通用户</li>
 * <li>source_department: 用户选择源为部门</li>
 * <li>source_position: 用户选择源为职位</li>
 * <li>source_nodeid: 目标节点 id</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowUserSourceType {
	SOURCE_USER("source_user"),
    SOURCE_DEPARTMENT("source_department"),
    SOURCE_POSITION("source_position"),
    SOURCE_NODEID("source_nodeid"),
    SOURCE_VARIABLE("source_variable"),
    SOURCE_DUTY_DEPARTMENT("source_duty_department"),
    SOURCE_DUTY_MANAGER("source_duty_manager"),
    ;
	
	private String code;
    private FlowUserSourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowUserSourceType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowUserSourceType t : FlowUserSourceType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
