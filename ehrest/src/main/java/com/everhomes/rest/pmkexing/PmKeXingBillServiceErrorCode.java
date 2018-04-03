package com.everhomes.rest.pmkexing;

/**
 * Created by xq.tian on 2016/12/27.
 */
public interface PmKeXingBillServiceErrorCode {

    String SCOPE = "pmkexing";

    int ERROR_HTTP_REQUEST = 10001;
    int ERROR_INIT_PMBILL_HANDLER = 10002;
    int ERROR_NO_PMBILL_HANDLER_EXIST = 10003;
}
