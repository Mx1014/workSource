package com.everhomes.rest.region;

public interface RegionServiceErrorCode {
    static final String SCOPE = "region";
    
    static final int ERROR_REGION_NOT_EXIST = 10001; //区域不存在

    static final int ERROR_REGIONCODE_CODE_EXISTING = 10011; //区域号已存在
    static final int ERROR_REGIONCODE_NAME_EXISTING = 10011; //区域名称已存在
}
