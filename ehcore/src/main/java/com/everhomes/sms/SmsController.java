// @formatter:off
package com.everhomes.sms;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.sms.ListReportLogCommand;
import com.everhomes.rest.sms.ListSmsLogsResponse;
import com.everhomes.rest.sms.SendTestSmsCommand;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestDoc(value = "Sms controller", site = "sms")
@RestController
@RequestMapping("/sms")
public class SmsController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsService smsService;

    /**
     * <b>URL: /sms/listSmsLogs</b>
     * <p>获取短信log列表</p>
     */
    /*@RequestMapping("listSmsLogs")
    @RestReturn(value=ListSmsLogsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listSmsLogs(ListSmsLogsCommand cmd) {
        ListSmsLogsResponse res = new ListSmsLogsResponse();
        if (null == cmd.getNamespaceId()) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid NamespaceId param ");
        }
        Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        List<SmsLog> logs = smsLogProvider.listSmsLogs(cmd.getNamespaceId(), cmd.getMobile(), cmd.getPageAnchor(), pageSize);
        res.setLogs(logs.stream().map(r -> ConvertHelper.convert(r, SmsLogDTO.class)).collect(Collectors.toList()));
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }*/

    /**
     * <b>URL: /sms/sendTestSms</b>
     * <p>发送短信</p>
     */
    @RequestMapping("sendTestSms")
    @RestReturn(value = String.class)
    public RestResponse sendTestSms(SendTestSmsCommand cmd) {
        smsService.sendTestSms(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /sms/listSmsLogs</b>
     * <p>短信状态报告列表</p>
     */
    @RequestMapping("listSmsLogs")
    @RestReturn(value = ListSmsLogsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listReportLogs(ListReportLogCommand cmd) {
        ListSmsLogsResponse resp = smsService.listReportLogs(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /sms/{handler}/report</b>
     * <p>短信状态报告</p>
     */
    @RequestMapping("{handler}/report")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    @SuppressDiscover
    public RestResponse smsReport(@PathVariable("handler") String handlerName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        smsService.smsReport(handlerName, httpServletRequest, httpServletResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}