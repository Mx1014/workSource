package com.everhomes.rest.organization;

/**
 * 
 * <ul>来自于第三方楼栋的类型，对应eh_organizations表听 namespace_organization_type字段
 * <li>JINDIE("jindie"): 金蝶</li>
 * <li>SHENZHOU("shenzhou"): 神州数码</li>
 * <li>EBEI("ebei"): 一碑</li>
 * </ul>
 */
public enum NamespaceOrganizationType {
	JINDIE("jindie"), SHENZHOU("shenzhou"), EBEI("ebei"), CM("cm");
    
    private String code;
    private NamespaceOrganizationType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static NamespaceOrganizationType fromCode(String code) {
        if(code != null) {
        	NamespaceOrganizationType[] values = NamespaceOrganizationType.values();
            for(NamespaceOrganizationType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
