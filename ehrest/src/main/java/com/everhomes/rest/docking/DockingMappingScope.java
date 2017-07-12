// @formatter:off
package com.everhomes.rest.docking;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>EBEI_PM_TASK("EBEI_PM_TASK"): 一碑物业报修对接</li>
 * </ul>
 */
public enum DockingMappingScope {
    EBEI_PM_TASK("EBEI_PM_TASK");

    private String code;
    DockingMappingScope(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DockingMappingScope fromCode(String code) {
        if(code != null) {
            DockingMappingScope[] values = DockingMappingScope.values();
            for(DockingMappingScope value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
