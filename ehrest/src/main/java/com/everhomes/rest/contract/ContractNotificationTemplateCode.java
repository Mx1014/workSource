package com.everhomes.rest.contract;

/**
 * Created by ying.xiong on 2018/1/5.
 */
public interface ContractNotificationTemplateCode {
    static final String SCOPE = "contract.notification";

    static final int NOTIFY_CONTRACT_EXPIRING = 1; // 通知即将过期
    static final int NOTIFY_CONTRACT_PAY = 2; // 通知付款

}
