package com.everhomes.rest.organization;

/**
 * <p>机构类型</p>
 * <ul>
 * <li>PM: 物业</li>
 * <li>GARC: 业委，Government Agency - Resident Committee</li>
 * <li>GANC: 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS: 公安，Government Agency - Police Station</li>
 * <li>GACW: 社区工作站，Government Agency - Community Workstation</li>
 * <li>ENTERPRISE: 普通企业</li>
 * </ul>
 */
public enum OrganizationType {
    PM("PM"), GARC("GARC"), GANC("GANC"), GAPS("GAPS"),GACW("GACW"),ENTERPRISE("ENTERPRISE");
    
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
    
    /**
     * 对于物业、居委、业委、公安等是特殊机构/公司，提供该方法方便判断
     * @param code 当前的机构/公司类型
     * @return 是否是特殊机构/公司
     */
    public static boolean isGovAgencyOrganization(String code) {
        OrganizationType tempType = fromCode(code);
        if(tempType != null) {
            switch(tempType) {
            case PM:
            case GARC:
            case GANC:
            case GAPS:
            case GACW:
                return true;
            default:
                return false;
            }
        } else {
            return false;
        }
    }
}