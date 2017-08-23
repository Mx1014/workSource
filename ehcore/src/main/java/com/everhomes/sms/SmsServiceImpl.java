package com.everhomes.sms;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.sms.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/7/11.
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

    private Map<String, SmsHandler> handlers = new HashMap<>();

    @Autowired
    private SmsLogProvider smsLogProvider;

    @Autowired
    private SmsProvider smsProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    public void setHandlers(Map<String, SmsHandler> prop) {
        prop.forEach((name, handler) -> handlers.put(name.toLowerCase(), handler));
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
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        List<SmsLog> smsLogs = smsLogProvider.listSmsLogs(cmd.getNamespaceId(), cmd.getHandler(), cmd.getMobile(), cmd.getStatus(), pageSize, locator);

        ListSmsLogsResponse response = new ListSmsLogsResponse();
        response.setLogs(smsLogs.stream().map(this::toSmsLogDTO).collect(Collectors.toList()));
        return response;
    }

    @Override
    public void sendTestSms(SendTestSmsCommand cmd) {
        String[] mobiles = cmd.getMobile().split(",");
        String locale = Locale.CHINESE.toString();

        smsProvider.sendSms(
                cmd.getHandler(),
                cmd.getNamespaceId(),
                mobiles,
                SmsTemplateCode.SCOPE,
                cmd.getTemplateCode() != null ? cmd.getTemplateCode() : -1,
                locale,
                null
        );
    }

    private SmsLogDTO toSmsLogDTO(SmsLog smsLog) {
        return ConvertHelper.convert(smsLog, SmsLogDTO.class);
    }
}
