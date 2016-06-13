package com.everhomes.rest.community;

/**
 * <ul>园区app类型
 * <li>TECHPARK(100000): 科技园app</li>
 * </ul>
 */
public enum CommunityAppType {

	TECHPARK((long)100000);
    
    private long code;
    
    private CommunityAppType(long code) {
        this.code = code;
    }
    
    public long getCode() {
        return this.code;
    }
    
    public static CommunityAppType fromCode(Long code) {
        if(code != null) {
        	CommunityAppType[] values = CommunityAppType.values();
            for(CommunityAppType value : values) {
                if(code.longValue() == value.getCode()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
