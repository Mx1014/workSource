package com.everhomes.rest.address;

/**
 * 
 * <ul>来自于第三方楼栋的类型，对应eh_buildings表听 namespace_building_type字段
 * <li>JINDIE("jindie"): 金蝶</li>
 * <li>SHENZHOU("shenzhou"): 神州</li>
 * </ul>
 */
public enum NamespaceBuildingType {
	JINDIE("jindie"), SHENZHOU("shenzhou");
    
    private String code;
    private NamespaceBuildingType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static NamespaceBuildingType fromCode(String code) {
        if(code != null) {
        	NamespaceBuildingType[] values = NamespaceBuildingType.values();
            for(NamespaceBuildingType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
