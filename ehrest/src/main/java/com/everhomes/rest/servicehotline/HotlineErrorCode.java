package com.everhomes.rest.servicehotline;

/**
 * Created by Administrator on 2017/12/18.
 */
public interface HotlineErrorCode {
    String SCOPE = "hotline";

    int ERROR_DUPLICATE_PHONE = 10001; // 电话号码重复
    int ERROR_HOTLINE_SERVICER_KEY_INVALID = 10002; // 查询记录时未指定客服
    int ERROR_HOTLINE_NOT_FOUND = 10003; // 未找到相应客服
}
