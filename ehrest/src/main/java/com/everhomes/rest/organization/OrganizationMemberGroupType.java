package com.everhomes.rest.organization;


/**
 * <ul>
 * <li>MANAGER: 管理员</li>
 * <li>REPAIR: 维修人员</li>
 * <li>CLEANING: 保洁人员</li>
 * <li>KEFU: 客服人员</li>
 * <li>HECHA: 核查人员</li>
 * </ul>
 */
public enum OrganizationMemberGroupType {
	MANAGER("manager"), REPAIR("repair"), CLEANING("cleaning"), KEFU("kefu"), HECHA("hecha");
	
	private String code;
    private OrganizationMemberGroupType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationMemberGroupType fromCode(String code) {
    	if (code != null) {
			for (OrganizationMemberGroupType memberGroupType : OrganizationMemberGroupType.values()) {
				if (memberGroupType.code.equalsIgnoreCase(code)) {
					return memberGroupType;
				}
			}
		}
    	
    	return null;
    	
    	//谁写的代码，这么不优雅，update by tt, 20161128
//    	if(code == null) {
//    		return null;
//    	}
//        
//        if(code.equalsIgnoreCase(MANAGER.getCode())) {
//        	return MANAGER;
//        }
//
//        if(code.equalsIgnoreCase(REPAIR.getCode())) {
//        	return REPAIR;
//        }
//
//        if(code.equalsIgnoreCase(CLEANING.getCode())) {
//        	return CLEANING;
//        }
//
//        if(code.equalsIgnoreCase(KEFU.getCode())) {
//        	return KEFU;
//        }
//        
//        return null;
    }

}
