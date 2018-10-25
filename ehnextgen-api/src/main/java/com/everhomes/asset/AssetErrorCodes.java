//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/11/1.
 */

public class AssetErrorCodes {
    public static final String SCOPE = "assetv2";

    public static final int ERROR_IN_GENERATING = 10001;
    public static final int NO_COMMUNITY_CHOSE = 10002;
    public static final int FAIL_IN_GENERATION = 10003;

    public static final int UNIQUE_BILL_ITEM_CHECK = 10004;
    public static final int CHANGE_SAFE_CHECK = 10005;
    public static final int HTTP_EBEI_ERROR = 10006;
    public static final int HAS_PAID_BILLS = 10007;
    public static final int NOT_CORP_MANAGER = 10008;
    /** 帐单组不存在 */
    public static final int BILL_GROUP_NOT_FOUND = 10009;
    /** 收款方帐号没有配置 */
    public static final int PAYMENT_PAYEE_NOT_CONFIG = 10010;
    /** 缴费订单创建失败 */
    public static final int BILL_ORDER_CREATION_FAILED = 10011;
    /** 催缴短信发送失败-第三方授权异常 */
    public static final int MESSAGE_SEND_FAILED = 10012;
    /** 收费项标准公式不存在 */
    public static final int FORMULA_CANNOT_BE_FOUND = 10013;
    /** 收费项标准类型错误 */
    public static final int STANDARD_BILLING_CYCLE_NOT_FOUND = 10014;
    
    public static final int GROUP_UNIQUE_BILL_ITEM_CHECK = 10020;
    public static final int STANDARD_RELEATE_CONTRACT_CHECK = 10021;
    
    
}
