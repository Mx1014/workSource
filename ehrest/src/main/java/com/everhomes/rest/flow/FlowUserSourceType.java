package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>source_user: 用户选择源为普通用户</li>
 * <li>source_department: 用户选择源为部门</li>
 * <li>source_position: 用户选择源为职位</li>
 * <li>source_nodeid: 目标节点 id</li>
 * <li>source_unlimited_department: 不限部门</li>
 * <li>source_business_department: 业务责任部门</li>
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

    /**
     * @see FlowUserSourceType#SOURCE_BUSINESS_DEPARTMENT
     * @deprecated
     */
    SOURCE_DUTY_DEPARTMENT("source_duty_department"),

    /**
     * @see FlowUserSourceType#SOURCE_BUSINESS_DEPARTMENT
     * @deprecated
     */
    SOURCE_DUTY_MANAGER("source_duty_manager"),

    /**
     * 不限部门类型
     */
    SOURCE_UNLIMITED_DEPARTMENT("source_unlimited_department"),

    /**
     * 业务责任部门类型
     */
    SOURCE_BUSINESS_DEPARTMENT("source_business_department"),
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
