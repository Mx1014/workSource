// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.statistics.event.*;
import com.everhomes.server.schema.tables.pojos.EhStatEventParamStatistics;
import com.everhomes.user.OSType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/7/31.
 */
@Service
public class StatEventServiceImpl implements StatEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatEventServiceImpl.class);

    private final static int MAX_CONTENT_LENGTH = 1024 * 1024 * 10;// 10Mb

    @Autowired
    private StatEventLogContentProvider statEventLogContentProvider;

    @Autowired
    private StatEventDeviceLogProvider statEventDeviceLogProvider;

    @Autowired
    private StatEventUploadStrategyProvider statEventUploadStrategyProvider;

    @Autowired
    private StatEventAppLogAttachmentProvider statEventAppLogAttachmentProvider;

    @Autowired
    private StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    private StatEventStatisticProvider statEventStatisticProvider;

    @Autowired
    private StatEventParamStatisticProvider statEventParamStatisticProvider;

    @Override
    public void postLog(HttpServletRequest request) {
        String header = request.getHeader("Content-Length");
        int contentLength = NumberUtils.toInt(header);
        if (contentLength <= 0 || contentLength > MAX_CONTENT_LENGTH) {
            return;
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(contentLength);
            ServletInputStream in = request.getInputStream();

            int byteCount = 0;
            int bufferLen = 4096;
            byte[] buffer = new byte[bufferLen];
            int bytesRead = -1;

            while ((bytesRead = in.read(buffer)) != -1) {
                if (contentLength - byteCount < bufferLen) {
                    out.write(buffer, 0, contentLength - byteCount);
                    break;
                }
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();

            String content = new String(out.toByteArray(), Charset.forName("UTF-8"));

            StatEventLogContent logContent = new StatEventLogContent();
            logContent.setNamespaceId(UserContext.getCurrentNamespaceId());
            logContent.setContent(content);
            statEventLogContentProvider.createStatEventLogContent(logContent);
        } catch (IOException e) {
            LOGGER.error("read log content error", e);
        }
    }

    @Override
    public StatPostDeviceResponse postDevice(StatPostDeviceCommand cmd, String remoteAddr, String localAddr) {
        StatEventDeviceLog deviceLog = ConvertHelper.convert(cmd, StatEventDeviceLog.class);
        UserContext context = UserContext.current();

        deviceLog.setNamespaceId(UserContext.getCurrentNamespaceId());
        deviceLog.setAppVersion(context.getVersion());
        deviceLog.setClientIp(remoteAddr);
        try {
            deviceLog.setServerIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        deviceLog.setUid(UserContext.currentUserId());
        String versionRealm = context.getVersionRealm();
        deviceLog.setVersionRealm(versionRealm);
        if (versionRealm != null) {
            String osTypeStr = versionRealm.substring(0, versionRealm.indexOf("_"));
            deviceLog.setOsType(OSType.fromString(osTypeStr).getCode());
        }

        statEventDeviceLogProvider.createStatEventDeviceLog(deviceLog);

        List<StatEventUploadStrategy> strategies = statEventUploadStrategyProvider.listUploadStrategyByOwner(EntityType.USER.getCode(),
                UserContext.currentUserId());
        if (strategies == null || strategies.isEmpty()) {
            strategies = statEventUploadStrategyProvider.listUploadStrategyByOwner(EntityType.NAMESPACE.getCode(), (long)deviceLog.getNamespaceId());
        }
        if (strategies == null || strategies.isEmpty()) {
            strategies = statEventUploadStrategyProvider.listUploadStrategyByOwner(EntityType.NAMESPACE.getCode(), (long) Namespace.DEFAULT_NAMESPACE);
        }
        StatPostDeviceResponse response = new StatPostDeviceResponse();
        response.setUploadStrategy(strategies.stream().map(this::toUploadStrategyDTO).collect(Collectors.toList()));
        response.setSessionId(String.valueOf(deviceLog.getId()));
        return response;
    }

    @Override
    public void postLogFile(StatPostLogFileCommand cmd) {
        if (cmd.getFiles() == null) {
            return;
        }
        Long uid = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        for (StatPostLogFileDTO fileDTO : cmd.getFiles()) {
            StatEventAppLogAttachment attachment = new StatEventAppLogAttachment();
            attachment.setSessionId(fileDTO.getSessionId());
            attachment.setUid(uid);
            attachment.setContentUri(fileDTO.getUri());
            attachment.setNamespaceId(namespaceId);
            attachment.setContentType("File");
            statEventAppLogAttachmentProvider.createStatEventAppLogAttachment(attachment);
        }
    }

    @Override
    public List<StatEventPortalStatDTO> listEventPortalStat(ListEventPortalStatCommand cmd) {
        Integer namespaceId = cmd.getNamespaceId();
        if (namespaceId == null) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }

        Date startDate = new Date(cmd.getStartDate());
        Date endDate = new Date(cmd.getEndDate());

        List<StatEventPortalStatistic> statistics = statEventPortalStatisticProvider.listEventPortalStat(
                namespaceId, cmd.getParentId(), cmd.getStatType(), startDate, endDate);

        if (statistics != null) {
            List<StatEventPortalStatDTO> list = new ArrayList<>();
            for (StatEventPortalStatistic statistic : statistics) {
                StatEventPortalStatDTO dto = new StatEventPortalStatDTO();
                dto.setId(statistic.getId());
                dto.setParentId(statistic.getParentId());
                dto.setIdentifier(statistic.getIdentifier());
                dto.setDisplayName(statistic.getDisplayName());
                list.add(dto);
            }
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public StatEventStatDTO listEventStat(StatListEventStatCommand cmd) {
        Integer namespaceId = cmd.getNamespaceId();
        if (namespaceId == null) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }

        Date startDate = new Date(cmd.getStartDate());
        Date endDate = new Date(cmd.getEndDate());

        StatEventStatDTO dto = new StatEventStatDTO();

        List<Long> portalStatIdList = statEventPortalStatisticProvider.listEventPortalStatByIdentifier(
                namespaceId, cmd.getParentId(), cmd.getIdentifier(), startDate, endDate);
        if (portalStatIdList == null || portalStatIdList.isEmpty()) {
            return dto;
        }
        List<StatEventStatistic> eventStatList = statEventStatisticProvider.listEventStatByPortalStatIds(portalStatIdList);
        if (eventStatList == null || portalStatIdList.isEmpty()) {
            return dto;
        }

        Map<Long, List<StatEventStatistic>> idToEventStatMap = eventStatList.stream().collect(Collectors.groupingBy(StatEventStatistic::getId));

        List<Long> eventStatIdList = eventStatList.stream().map(StatEventStatistic::getId).collect(Collectors.toList());
        List<StatEventParamStatistic> paramStatList = statEventParamStatisticProvider.listEventParamStat(eventStatIdList);
        if (paramStatList == null || paramStatList.isEmpty()) {
            return dto;
        }

        Map<Long, List<StatEventParamStatistic>> eventStatIdToEventParamStatMap = paramStatList.stream()
                .collect(Collectors.groupingBy(EhStatEventParamStatistics::getEventStatId));

        List<StatKeyValueListDTO> kvlList = new ArrayList<>();
        eventStatIdToEventParamStatMap.forEach((eventStatId, eventParamStatList) -> {
            StatKeyValueListDTO kvl = new StatKeyValueListDTO();

            List<StatKeyValueDTO> kvList = eventParamStatList.stream().map(r -> {
                StatKeyValueDTO kv = new StatKeyValueDTO();
                kv.setKey(r.getStatKey());
                kv.setValue(r.getStatValue());
                return kv;
            }).collect(Collectors.toList());

            StatKeyValueDTO totalCountKv = new StatKeyValueDTO();
            totalCountKv.setKey("totalCount");
            totalCountKv.setValue("0");
            List<StatEventStatistic> esList = idToEventStatMap.get(eventStatId);
            if (esList != null && esList.size() > 0) {
                totalCountKv.setValue(String.valueOf(esList.get(0).getTotalCount()));
            }
            kvList.add(totalCountKv);
            kvl.setList(kvList);

            kvlList.add(kvl);
        });
        dto.setStat(kvlList);
        return dto;
    }

    private StatLogUploadStrategyDTO toUploadStrategyDTO(StatEventUploadStrategy statEventUploadStrategy) {
        return ConvertHelper.convert(statEventUploadStrategy, StatLogUploadStrategyDTO.class);
    }
}
