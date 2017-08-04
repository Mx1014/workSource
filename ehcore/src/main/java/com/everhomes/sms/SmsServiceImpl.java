package com.everhomes.sms;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.sms.YzxListReportLogCommand;
import com.everhomes.rest.sms.YzxListSmsReportLogResponse;
import com.everhomes.rest.sms.YzxSmsLogDTO;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/7/11.
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private YzxSmsLogProvider yzxSmsLogProvider;

    @Override
    public void yzxSmsReport(HttpServletRequest request, HttpServletResponse response) {
        try (
                ServletOutputStream outputStream = response.getOutputStream();
                BufferedReader reader = request.getReader()
        ) {
            String line;
            String body = "";
            while ((line = reader.readLine()) != null) {
                body = body + line;
            }

            if (LOGGER.isDebugEnabled()) {
                body = body.replaceAll("\\s+", "").replaceAll("\\n", "");
                LOGGER.debug("YZX sms report body: {}", body);
            }

            YzxSmsReport report = xmlToBean(body, YzxSmsReport.class);
            String smsId = report.getSmsId();
            if (smsId == null) {
                LOGGER.error("YZX sms report smsId is null, report = {}", report);
                return;
            }
            YzxSmsLog log = yzxSmsLogProvider.findBySmsId(smsId);
            if (log != null) {
                log.setType(report.getType());
                log.setDesc(report.getDesc());
                log.setStatus(Byte.valueOf(report.getStatus()));
                log.setReportTime(Timestamp.from(Instant.now()));
                yzxSmsLogProvider.updateYzxSmsLog(log);

                if (!Objects.equals(log.getStatus(), 0)) {
                    // sendEmail();
                }
            } else {
                LOGGER.warn("YZX sms report not found, smsId = \"{}\", maybe a test server send.", smsId);
            }

            response.setHeader("Content-Type", "text/xml;charset=utf-8 ");
            outputStream
                    .write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<response>\n<retcode>0</retcode>\n</response>"
                            .getBytes("utf-8"));
        } catch (IOException e) {
            LOGGER.error("YZX sms report error", e);
            // e.printStackTrace();
        }
    }

    @Override
    public YzxListSmsReportLogResponse yzxListReportLogs(YzxListReportLogCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<YzxSmsLog> logList = yzxSmsLogProvider.listReportLogs(cmd.getNamespaceId(), cmd.getMobile(), cmd.getStatus(), cmd.getFailure(), locator, cmd.getPageSize());
        List<YzxSmsLogDTO> dtoList = new ArrayList<>();
        if (logList != null) {
            dtoList = logList.stream().map(this::toYzxReportLogDTO).collect(Collectors.toList());
        }
        YzxListSmsReportLogResponse response = new YzxListSmsReportLogResponse();
        response.setLogs(dtoList);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private YzxSmsLogDTO toYzxReportLogDTO(YzxSmsLog log) {
        return ConvertHelper.convert(log, YzxSmsLogDTO.class);
    }

    private static <T> T xmlToBean(String xml, Class<T> c) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
