// @formatter:off
package com.everhomes.rest.relocation;

/**
 * <ul>
 * <li>INTELLIGENT("intelligent"): 智能审核</li>
 * <li>TRADITIONAL("traditional"): 手动审核</li>
 * </ul>
 */
public enum RelocationFlowMode {
	INTELLIGENT("intelligent"), TRADITIONAL("traditional");

    private String code;
    private RelocationFlowMode(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RelocationFlowMode fromCode(String code) {
        if(code != null) {
            RelocationFlowMode[] values = RelocationFlowMode.values();
            for(RelocationFlowMode value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
