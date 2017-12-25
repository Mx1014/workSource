package com.everhomes.sms;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.sms.*;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/7/11.
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

    private Map<String, SmsHandler> handlers = new HashMap<>();
    private List<String> handlerNames = new ArrayList();

    @Autowired
    private SmsLogProvider smsLogProvider;

    @Autowired
    private SmsProvider smsProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private AclProvider aclProvider;

    @Autowired
    public void setHandlers(Map<String, SmsHandler> prop) {
        prop.forEach((name, handler) -> handlers.put(name.toLowerCase(), handler));
        prop.forEach((name, handler) -> handlerNames.add(name));
    }

    @Override
    public void smsReport(String handlerName, HttpServletRequest request, HttpServletResponse response) {
        SmsHandler handler = handlers.get(handlerName);
        try (BufferedReader reader = request.getReader()) {
            String line;
            String body = "";
            while ((line = reader.readLine()) != null) {
                body = body + line;
            }
            List<SmsReportDTO> dtoList = handler.report(body);
            if (dtoList != null) {
                for (SmsReportDTO dto : dtoList) {
                    if (dto.getSmsId() == null) {
                        LOGGER.warn("sms report smsId are empty, handlerName = {}, reportBody = {}", handlerName, body);
                        continue;
                    }
                    List<SmsLog> smsLogs = smsLogProvider.findSmsLog(handlerName, dto.getMobile(), dto.getSmsId());
                    if (smsLogs != null && smsLogs.size() > 0) {
                        for (SmsLog smsLog : smsLogs) {
                            smsLog.setStatus(dto.getStatus());
                            smsLog.setReportTime(DateUtils.currentTimestamp());
                            smsLog.setReportText(body);
                            smsLogProvider.updateSmsLog(smsLog);
                        }
                    } else {
                        LOGGER.warn("sms report not found, smsLog by handlerName = {}, smsId = {}, reportBody = {}", handlerName, dto.getSmsId(), body);
                    }
                }
            } else {
                LOGGER.warn("sms report parse error handlerName = {}, reportBody = {}", handlerName, body);
            }
        } catch (Exception e) {
            LOGGER.error("sms report error handlerName = {}", handlerName);
            LOGGER.error("sms report error", e);
        }
    }

    @Override
    public ListSmsLogsResponse listReportLogs(ListReportLogCommand cmd) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
                UserContext.current().getUser().getId(), Privilege.Write, null)) {
            
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
            }
        
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        List<SmsLog> smsLogs = smsLogProvider.listSmsLogs(cmd.getNamespaceId(), cmd.getHandler(), cmd.getMobile(), cmd.getStatus(), pageSize, locator);

        ListSmsLogsResponse response = new ListSmsLogsResponse();
        response.setLogs(smsLogs.stream().map(this::toSmsLogDTO).collect(Collectors.toList()));
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }
    
    @Override
    public SmsHandlerResponse listSmsHandlers() {
        SmsHandlerResponse resp = new SmsHandlerResponse();
        resp.setHandlers(handlerNames);
        return resp;
    }

    @Override
    public void sendTestSms(SendTestSmsCommand cmd) {
        String[] mobiles = cmd.getMobile().split(",");
        String locale = Locale.CHINA.toString();

        List<Tuple<String, Object>> tuples = new ArrayList<>();
        if (cmd.getVariables() != null) {
            Map<String, String> map = (Map<String, String>) StringHelper.fromJsonString(cmd.getVariables(), Map.class);
            map.forEach((k, v) -> smsProvider.addToTupleList(tuples, k, v));
        }

        smsProvider.sendSms(
                cmd.getHandler(),
                cmd.getNamespaceId(),
                mobiles,
                SmsTemplateCode.SCOPE,
                cmd.getTemplateCode() != null ? cmd.getTemplateCode() : -1,
                locale,
                tuples
        );
    }

    private SmsLogDTO toSmsLogDTO(SmsLog smsLog) {
        SmsLogDTO dto = ConvertHelper.convert(smsLog, SmsLogDTO.class);
        dto.setCreateTime(smsLog.getCreateTime().toLocalDateTime().toString());
        return dto;
    }
}
