// @formatter:off
package com.everhomes.sms;

/**
 * Created by xq.tian on 2017/7/4.
 */
public interface SmsBlackListProvider {

    void createSmsBlackList(SmsBlackList blackList);

    SmsBlackList findByContactToken(String contactToken);

}
