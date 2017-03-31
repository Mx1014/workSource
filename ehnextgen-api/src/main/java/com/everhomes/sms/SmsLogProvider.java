package com.everhomes.sms;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */
public interface SmsLogProvider {

    void createSmsLog(SmsLog smsLog);

    List<SmsLog> listSmsLogs(Integer namespaceId, String mobile, Long pageAnchor, Integer pageSize);
}
