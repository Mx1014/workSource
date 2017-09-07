package com.everhomes.rest.community_map;


/**
 * <ul>
 * <li>ORGANIZATION("organization"): </li>
 * <li>BUILDING("building"): </li>
 * <li>SHOP("shop"): </li>
 * <li>ALL("all"): </li>
 * </ul>
 */
public enum CommunityMapSearchContentType {
	ORGANIZATION("organization"),
    BUILDING("building"),
    SHOP("shop"),
	ALL("all");

    private String code;

    private CommunityMapSearchContentType(String code) {
        this.code = code;
    }
       
    public String getCode() {
        return this.code;
    }
       
    public static CommunityMapSearchContentType fromCode(String code) {
    	CommunityMapSearchContentType[] values = CommunityMapSearchContentType.values();
        for(CommunityMapSearchContentType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
       
        return null;
    }
}
