package com.everhomes.pmkexing;

/**
 * Created by xq.tian on 2016/12/26.
 */
public interface PmKexingBillConst {

    String API_HOST = "http://120.24.88.192:15902";

    // 查询房间账单
    String API_BILL_LIST = API_HOST + "/tss/rest/chargeRecordInfo/billListForZhenzhong";

    // 查询合计金额
    String API_BILL_COUNT = API_HOST + "/tss/rest/chargeRecordInfo/billCountForZhenzhong";

}
