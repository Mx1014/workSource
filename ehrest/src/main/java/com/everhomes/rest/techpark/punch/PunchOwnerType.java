package com.everhomes.rest.techpark.punch;
 
/**
 * <ul>
 * <li>User : 个人 </li>
 * <li>ORGANIZATION : 机构-公司-部门</li>
 * </ul>
 */
public enum PunchOwnerType {
   
    User("user"),ORGANIZATION("organization");
    
    private String code;
    private PunchOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PunchOwnerType fromCode(String code) {
        for(PunchOwnerType t : PunchOwnerType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
