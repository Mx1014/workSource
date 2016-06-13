// @formatter:off
package com.everhomes.rest.organization.pm;

/**
 * <ul>
 * <li>MANAGER: 物业管理员</li>
 * <li>REPAIR: 维修人员</li>
 * <li>CLEANING: 保洁人员</li>
 * <li>KEFU: 客服人员</li>
 * </ul>
 */
public enum PmMemberGroup {
    MANAGER("manager"), REPAIR("repair"), CLEANING("cleaning"), KEFU("kefu");
    
    private String code;
    private PmMemberGroup(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmMemberGroup fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(MANAGER.getCode())) {
        	return MANAGER;
        }

        if(code.equalsIgnoreCase(REPAIR.getCode())) {
        	return REPAIR;
        }

        if(code.equalsIgnoreCase(CLEANING.getCode())) {
        	return CLEANING;
        }

        if(code.equalsIgnoreCase(KEFU.getCode())) {
        	return KEFU;
        }
        
        return null;
    }
}
