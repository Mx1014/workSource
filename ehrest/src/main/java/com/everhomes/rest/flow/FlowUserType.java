package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>node_applier: 申请者</li>
 * <li>processor: 处理者</li>
 * <li>supervisor: 督办者</li>
 * <li> 注意： 不能随便改 FlowUserType 的参数值与顺序 </li>
 * </ul>
 * @author janson
 *
 */
public enum FlowUserType {
	NO_USER("no_user"), APPLIER("node_applier"), PROCESSOR("processor"), SUPERVISOR("supervisor");
	
	private String code;
    private FlowUserType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public int getCodeInt() {
    	int i = 0;
    	for(FlowUserType t : FlowUserType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return i;
    		}
    		i++;
    	}
    	
    	return i;
    }
    
    public static FlowUserType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowUserType t : FlowUserType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
