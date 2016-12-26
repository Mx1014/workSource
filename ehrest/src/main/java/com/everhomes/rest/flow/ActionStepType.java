package com.everhomes.rest.flow;

/**
 * <ul> 暂时用不到
 * </ul>
 * @author janson
 *
 */
public enum ActionStepType {
	NO_STEP("no_step"), ENTER("enter"), RUN("run"), LEAVE("leave");
	
	private String code;
    private ActionStepType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ActionStepType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(ActionStepType t : ActionStepType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
