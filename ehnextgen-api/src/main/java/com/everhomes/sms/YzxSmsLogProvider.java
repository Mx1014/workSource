package com.everhomes.sms;

import com.everhomes.listing.ListingLocator;

import java.util.List;

/**
 * Created by xq.tian on 2017/7/11.
 */
public interface YzxSmsLogProvider {

    void createYzxSmsLog(YzxSmsLog log);

    void updateYzxSmsLog(YzxSmsLog log);

    YzxSmsLog findBySmsId(String smsId);

    List<YzxSmsLog> listReportLogs(Integer namespaceId, String mobile, Byte status, Byte failure, ListingLocator locator, Integer pageSize);
}
