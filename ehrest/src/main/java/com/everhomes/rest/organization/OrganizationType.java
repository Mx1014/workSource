package com.everhomes.rest.organization;

/**
 * <p>机构类型</p>
 * <ul>
 * <li>PM: 物业</li>
 * <li>GARC: 业委，Government Agency - Resident Committee</li>
 * <li>GANC: 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS: 公安，Government Agency - Police Station</li>
 * <li>GACW: 社区工作站，Government Agency - Community Workstation</li>
 * </ul>
 */
public enum OrganizationType {
    PM("PM"), GARC("GARC"), GANC("GANC"), GAPS("GAPS"),GACW("GACW");
    
    private String code;
    private OrganizationType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationType fromCode(String code) {
    	OrganizationType[] values = OrganizationType.values();
        for(OrganizationType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}