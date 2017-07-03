package com.everhomes.rest.community;

public interface CommunityServiceErrorCode {
    static final String SCOPE = "community";
    
    
    
    static final int ERROR_COMMUNITY_NOT_EXIST = 10001; //小区不存在
    static final int ERROR_COMMUNITY_GEOPOIN_NOT_EXIST = 10002; //小区经纬不存在
    static final int ERROR_COMMUNITY_CITY_NOT_EXIST = 10003; //小区城市信息不存在
    
    static final int ERROR_BUILDING_NOT_EXIST = 10004; //楼栋不存在
    static final int ERROR_BUILDING_COMMUNITY_NOT_EXIST = 10005; //楼栋小区信息不存在
    
    static final int ERROR_INVALID_PARAMETER = 10010; //楼栋小区信息不存在
    
    
    
    int ERROR_BUILDING_NAME_EMPTY = 20001;
    int ERROR_ADDRESS_EMPTY = 20002;
    int ERROR_CONTACTOR_EMPTY = 20003;
    int ERROR_PHONE_EMPTY = 20004;
    int ERROR_LATITUDE_LONGITUDE = 20005;

}
