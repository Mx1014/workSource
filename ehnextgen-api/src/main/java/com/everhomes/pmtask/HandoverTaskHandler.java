package com.everhomes.pmtask;

/**
 * Created by ying.xiong on 2017/7/17.
 */
public interface HandoverTaskHandler {
    String HANDOVER_VENDOR_PREFIX = "HandoverVendor-";
    int ZJGK = 999971;
//    Integer ZJGK = 9999;

    void handoverTaskToTrd(PmTask task);
}
