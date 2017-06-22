package com.everhomes.rest.techpark.expansion;

/**
 * BUILDING("building") 来自楼栋列表申请入住
 * MARKET_ZONE("market_zone") 来自创客空间申请入住
 * FOR_RENT("for_rent") 来自招租信息申请入住
 * OFFICE_CUBICLE("office_cubicle") 来自工位预定申请入住
 * RENEW("renew", "续租申请")
 */
public enum ApplyEntrySourceType {
	
	BUILDING("building", "园区介绍"), MARKET_ZONE("market_zone", "创客入驻"), FOR_RENT("for_rent", "虚位以待"),
    OFFICE_CUBICLE("office_cubicle", "工位预定"), RENEW("renew", "续租申请");
    
    private String code;
    private String description;
    private ApplyEntrySourceType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return this.code;
    }
    
    public static ApplyEntrySourceType fromType(String code) {
        for(ApplyEntrySourceType v : ApplyEntrySourceType.values()) {
            if(v.getCode().equals(code))
                return v;
        }
        return null;
    }
}
