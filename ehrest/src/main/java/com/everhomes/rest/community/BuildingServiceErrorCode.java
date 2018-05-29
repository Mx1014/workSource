package com.everhomes.rest.community;

public interface BuildingServiceErrorCode {

	static final String SCOPE = "building";
    
    static final int ERROR_BUILDING_DELETED = 10001;
    static final int ERROR_BUILDING_NOT_FOUND = 10002;
    static final int ERROR_BUILDING_HAS_CONTRACT = 10003;
    static final int ERROR_BUILDINGNAME_HASBEENUSED = 10004;
    static final int ERROR_BUILDING_IS_NOT_EXISTS = 10005;//该项目下不存在该楼栋

}
