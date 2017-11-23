package com.everhomes.rest.community_map;


/**
 * <ul>
 * <li>GAO_DE("GCJ-02")</li>
 * <li>BAI_DU("BD-09")</li>
 * </ul>
 */
public enum CommunityMapGeoType {
	GAO_DE("GCJ-02"),
    BAI_DU("BD-09");

    private String code;

    private CommunityMapGeoType(String code) {
        this.code = code;
    }
       
    public String getCode() {
        return this.code;
    }
       
    public static CommunityMapGeoType fromCode(String code) {
    	CommunityMapGeoType[] values = CommunityMapGeoType.values();
        for(CommunityMapGeoType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
       
        return null;
    }
}
