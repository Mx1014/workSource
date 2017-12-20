package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>text: 大段文字类型</li>
 * <li>image: 图片类型</li>
 *
 * </ul>*
 */
public enum ReserveOrderContentEntityType {
	TEXT("text"), IMAGE("image");
	
	private String code;
    private ReserveOrderContentEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ReserveOrderContentEntityType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(ReserveOrderContentEntityType t : ReserveOrderContentEntityType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
