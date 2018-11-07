package com.everhomes.rest.community;

public interface CommunityServiceErrorCode {
    static final String SCOPE = "community";
    
    static final int ERROR_COMMUNITY_NOT_EXIST = 10001; //小区不存在
    static final int ERROR_COMMUNITY_GEOPOIN_NOT_EXIST = 10002; //小区经纬不存在
    static final int ERROR_COMMUNITY_CITY_NOT_EXIST = 10003; //小区城市信息不存在
    
    static final int ERROR_BUILDING_NOT_EXIST = 10004; //楼栋不存在
    static final int ERROR_BUILDING_COMMUNITY_NOT_EXIST = 10005; //楼栋小区信息不存在
    
    static final int ERROR_INVALID_PARAMETER = 10010; //楼栋小区信息不存在
    static final int ERROR_COMMUNITY_NUMBER_EXIST = 10011; //项目编号已存在
    static final int ERROR_BUILDING_NUMBER_EXIST = 10012; //楼栋编号已存在
    static final int ERROR_COMMUNITY_NAME_EMPTY = 10013; //项目名称不能为空
    static final int ERROR_ADDRESS_LENGTH = 10014; //项目地址超过指定长度
    static final int ERROR_PROVINCECITYAREA_EMPTY = 10015; //省市区不能为空
    static final int ERROR_COMMUNITY_NAME_EXIST = 10016; //项目名称已存在
    
    static final int ERROR_PROVINCE_NAME = 10017; //省填写格式错误
    static final int ERROR_CITY_NAME = 10018; //市填写格式错误
    static final int ERROR_AREA_NAME = 10019; //区填写格式错误
    
    static final int ERROR_COMMUNITY_LONGITUDE_EMPTY = 10101;//经度不能为空
	static final int ERROR_LONGITUDE_FORMAT = 10102;//经度填写格式错误
	static final int ERROR_LONGITUDE_RANGE = 10103;//经度不在中国经度范围内

	static final int ERROR_COMMUNITY_LATITUDE_EMPTY = 10104;//纬度不能为空
	static final int ERROR_LATITUDE_FORMAT = 10105;//纬度填写格式错误
	static final int ERROR_LATITUDE_RANGE = 10106;//纬度不在中国纬度范围内

	static final int ERROR_COMMUNITY_TYPE = 10107;//园区分类不能为空
	static final int ERROR_COMMUNITY_TYPE_CONTENT = 10108;//园区分类内容填写错误

	static final int ERROR_BUILDING_NAME_EXIST = 10201;//楼宇名称已存在

	static final int ERROR_BUILDING_NAME_OVER_FLOW = 10213;//楼栋名称不能超过20个汉字
    static final int ERROR_BUILDING_NAME_REPEATED = 10214;//楼栋名称不能重复

    int ERROR_BUILDING_NAME_EMPTY = 20001;
    int ERROR_ADDRESS_EMPTY = 20002;
    int ERROR_CONTACTOR_EMPTY = 20003;
    int ERROR_PHONE_EMPTY = 20004;
    int ERROR_LATITUDE_LONGITUDE = 20005;
    int ERROR_AREASIZE_FORMAT = 20006;

    int ERROR_ORGANIZATION_COMMUNITY_NOT_EXIST = 30001; //管理管关系不存在
    int ERROR_FLOORNUMBER_FORMAT = 20007;//楼层数填写格式错误
}
