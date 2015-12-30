package com.everhomes.rest.banner;

public interface BannerServiceErrorCode {
    static final String SCOPE = "banner";
    
    static final int ERROR_BANNER_NOT_EXISTS = 10001;  //banner 不存在
    static final int ERROR_BANNER_EXISTS = 10002;  //banner 已存在
}
