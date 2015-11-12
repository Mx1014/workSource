package com.everhomes.organization;

/**
 * <p>机构类型</p>
 * <ul>
 * <li>PM: 物业</li>
 * <li>GARC: 业委，Government Agency - Resident Committee</li>
 * <li>GANC: 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS: 公安，Government Agency - Police Station</li>
 * <li>GACW: 社区工作站，Government Agency - Community Workstation</li>
 * <li>PARTNER: 园区合作商，一个合作商可能管理多个园区，其结构与机构比较像，故使用机构来实现关联园区、关联人</li>
 * </ul>
 */
public enum OrganizationType {
    PM("PM"), GARC("GARC"), GANC("GANC"), GAPS("GAPS"),GACW("GACW"), PARTNER("PARTNER");
    
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