package com.everhomes.sms;

import com.everhomes.rest.sms.ListReportLogCommand;
import com.everhomes.rest.sms.ListSmsLogsResponse;
import com.everhomes.rest.sms.SendTestSmsCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xq.tian on 2017/7/11.
 */
public interface SmsService {

    /**
     * 短信报告
     */
    void smsReport(String handlerName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    ListSmsLogsResponse listReportLogs(ListReportLogCommand cmd);

    void sendTestSms(SendTestSmsCommand cmd);
}
