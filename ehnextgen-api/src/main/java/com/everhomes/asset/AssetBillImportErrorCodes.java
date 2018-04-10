//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2018/4/2.
 */

public interface AssetBillImportErrorCodes {
    String SCOPE = "assetBillImport";

    Integer CUSTOM_TYPE_ERROR = 1001;
    Integer DATE_STR_EMPTY_ERROR = 1002;
    Integer CUSTOM_NAME_EMPTY_ERROR = 1003;
    Integer NOTICE_TEL_EMPTY_ERROR = 1004;
    Integer CHARGING_ITEM_NAME_ERROR = 1005;
    Integer AMOUNT_INCORRECT = 1006;
    Integer USER_CUSTOMER_TEL_ERROR = 1007;
}
