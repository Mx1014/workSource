package com.everhomes.sms;

import com.everhomes.listing.ListingLocator;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27.
 */
public interface SmsLogProvider {

    void createSmsLog(SmsLog smsLog);

    List<SmsLog> listSmsLogs(Integer namespaceId, String handler, String mobile, Byte status, int pageSize, ListingLocator locator);

    Map<String, SmsLog> findLastLogByMobile(Integer namespaceId, String phoneNumber, String[] handlerNames);

    List<SmsLog> findSmsLog(String handler, String mobile, String smsId);

    void updateSmsLog(SmsLog smsLog);
}
