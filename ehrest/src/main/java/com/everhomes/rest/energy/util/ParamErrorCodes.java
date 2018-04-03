package com.everhomes.rest.energy.util;

/**
 * 参数检查错误 code
 * Created by xq.tian on 2016/12/16.
 */
public interface ParamErrorCodes {

    String SCOPE = "parameters.error";

    int ERROR_OVER_LENGTH = 10001;
    int ERROR_UNDER_LENGTH = 10002;
    int ERROR_EMPTY_STRING = 10003;
    
}
