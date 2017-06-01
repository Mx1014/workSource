package com.everhomes.rest.comment;

/**
 * <ul>园区app类型
 * <li>FORUM(1)</li>
 * <li>NEWS(2)</li>
 * </ul>
 */
public enum SourceType {

	FORUM((long)1),NEWS((long)2);

    private long code;

    private SourceType(long code) {
        this.code = code;
    }
    
    public long getCode() {
        return this.code;
    }
    
    public static SourceType fromCode(Long code) {
        if(code != null) {
        	SourceType[] values = SourceType.values();
            for(SourceType value : values) {
                if(code.longValue() == value.getCode()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
