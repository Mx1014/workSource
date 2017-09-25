package com.everhomes.rest.flow;

/**
 * 
 * <ul>
 * <li>any-module : 任何模块</li>
 * <li>service_alliance : 服务联盟应用模块</li>
 * </ul>
 *
 *  @author:dengs
 */
public enum FlowModuleType {

	NO_MODULE("any-module"),REPAIR_MODULE("repair"),
	SERVICE_ALLIANCE("service_alliance");
	
	private String code;
    private FlowModuleType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowModuleType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
    	for(FlowModuleType t : FlowModuleType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
