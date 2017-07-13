package com.everhomes.sms;

import com.everhomes.rest.sms.YzxListReportLogCommand;
import com.everhomes.rest.sms.YzxListSmsReportLogResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xq.tian on 2017/7/11.
 */
public interface SmsService {

    /**
     * 云之讯短信报告
     * @param httpServletRequest
     * @param httpServletResponse
     */
    void yzxSmsReport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    /**
     * 获取云之讯的短信报告记录
     * @param cmd
     * @return
     */
    YzxListSmsReportLogResponse yzxListReportLogs(YzxListReportLogCommand cmd);
}
