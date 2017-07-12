// @formatter:off
package com.everhomes.sms;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.pmtask.PmTaskSearch;
import com.everhomes.pmtask.PmTaskService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.sms.ListSmsLogsCommand;
import com.everhomes.rest.sms.ListSmsLogsResponse;
import com.everhomes.rest.sms.SmsLogDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestDoc(value="Sms controller", site="sms")
@RestController
@RequestMapping("/sms")
public class SmsController extends ControllerBase {

	@Autowired
	private SmsLogProvider smsLogProvider;
    @Autowired
    private ConfigurationProvider configProvider;

	/**
     * <b>URL: /sms/listSmsLogs</b>
     * <p>获取短信log列表</p>
     */
    @RequestMapping("listSmsLogs")
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
    }
	

}