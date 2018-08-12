//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/11/1.
 */

public class AssetErrorCodes {
    static final String SCOPE = "assetv2";

    static final int ERROR_IN_GENERATING = 10001;
    static final int NO_COMMUNITY_CHOSE = 10002;
    static final int FAIL_IN_GENERATION = 10003;

    static final int UNIQUE_BILL_ITEM_CHECK = 10004;
    static final int CHANGE_SAFE_CHECK = 10005;
    static final int HTTP_EBEI_ERROR = 10006;
    static final int HAS_PAID_BILLS = 10007;
    static final int NOT_CORP_MANAGER = 10008;
    /** 帐单组不存在 */
    static final int BILL_GROUP_NOT_FOUND = 10009;
    /** 收款方帐号没有配置 */
    static final int PAYMENT_PAYEE_NOT_CONFIG = 10010;
    /** 缴费订单创建失败 */
    static final int BILL_ORDER_CREATION_FAILED = 10011;

}
