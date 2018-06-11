package com.everhomes.rest.asset;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface AssetServiceErrorCode {
    static final String SCOPE = "asset";
    static final int ASSET_BILL_NOT_EXIST = 10001;// 账单不存在
    static final int ERROR_CREATE_EXCEL = 10002;// 生成excel信息有问题
    static final String NOTIFY_FEE = "asset.notify.fee";// 通知缴费
    static final int UPLOAD_CERTIFICATES_NUM_ERROR = 10003;//上传缴费凭证的数量不正确
}
