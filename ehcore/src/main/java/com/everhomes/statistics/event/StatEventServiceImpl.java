// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.statistics.event.*;
import com.everhomes.user.OSType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/7/31.
 */
@Service
public class StatEventServiceImpl implements StatEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatEventServiceImpl.class);

    @Autowired
    private StatEventContentLogProvider statEventContentLogProvider;

    @Autowired
    private StatEventDeviceLogProvider statEventDeviceLogProvider;

    @Autowired
    private StatEventUploadStrategyProvider statEventUploadStrategyProvider;

    @Autowired
    private StatEventAppAttachmentLogProvider statEventAppAttachmentLogProvider;

    @Autowired
    private StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    private StatEventStatisticProvider statEventStatisticProvider;

    @Override
    public void postLog(StatPostLogCommand cmd) {
        StatEventLogContent content = new StatEventLogContent();
        content.setNamespaceId(UserContext.getCurrentNamespaceId());
        content.setContent(cmd.getLogs().toString());
        statEventContentLogProvider.createStatEventLogContent(content);
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
            LOGGER.error("InetAddress.getLocalHost().getHostAddress() error", e);
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
            strategies = statEventUploadStrategyProvider.listUploadStrategyByOwner(EntityType.NAMESPACE.getCode(), deviceLog.getNamespaceId().longValue());
        }
        if (strategies == null || strategies.isEmpty()) {
            strategies = statEventUploadStrategyProvider.listUploadStrategyByOwner(EntityType.NAMESPACE.getCode(), (long) Namespace.DEFAULT_NAMESPACE);
        }

        StatPostDeviceResponse response = new StatPostDeviceResponse();

        Map<Byte, List<StatEventUploadStrategy>> logTypeToStrategyMap = strategies.stream().collect(Collectors.groupingBy(StatEventUploadStrategy::getLogType));

        List<StatLogUploadStrategyDTO> dtoList = new ArrayList<>();
        for (Map.Entry<Byte, List<StatEventUploadStrategy>> entry : logTypeToStrategyMap.entrySet()) {
            StatLogUploadStrategyDTO dto = new StatLogUploadStrategyDTO();
            dto.setLogType(entry.getKey());
            dto.setEnvironments(entry.getValue().stream().map(this::toStatLogUploadStrategyEnvironmentDTO).collect(Collectors.toList()));
            dtoList.add(dto);
        }
        response.setUploadStrategy(dtoList);
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
            StatEventAppAttachmentLog attachment = new StatEventAppAttachmentLog();
            attachment.setSessionId(fileDTO.getSessionId());
            attachment.setUid(uid);
            attachment.setContentUri(fileDTO.getUri());
            attachment.setNamespaceId(namespaceId);
            attachment.setContentType("File");
            statEventAppAttachmentLogProvider.createStatEventAppAttachmentLog(attachment);
        }
    }

    @Override
    public ListStatEventPortalStatResponse listEventPortalStat(ListEventPortalStatCommand cmd) {
        ValidatorUtil.validate(cmd);
        Integer namespaceId = cmd.getNamespaceId();
        if (namespaceId == null) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }

        Date startDate = new Date(cmd.getStartDate());
        Date endDate = new Date(cmd.getEndDate());

        List<StatEventPortalStatistic> statistics;
        if (cmd.getParentId() == 0) {
            statistics = statEventPortalStatisticProvider.listEventPortalStatByZeroParentId(
                    namespaceId, cmd.getStatType(), startDate, endDate);
        } else {
            statistics = statEventPortalStatisticProvider.listEventPortalStatByParentId(
                    namespaceId, cmd.getParentId(), cmd.getStatType(), startDate, endDate);
        }

        boolean isPortalItemGroupType = StatEventPortalStatType.fromCode(cmd.getStatType()) == StatEventPortalStatType.PORTAL_ITEM_GROUP;
        List<StatEventPortalStatDTO> list = new ArrayList<>();
        for (StatEventPortalStatistic statistic : statistics) {
            StatEventPortalStatDTO dto = new StatEventPortalStatDTO();
            dto.setId(statistic.getId());
            dto.setParentId(statistic.getParentId());
            dto.setIdentifier(statistic.getIdentifier());
            dto.setDisplayName(statistic.getDisplayName());
            dto.setOwnerType(statistic.getOwnerType());
            dto.setOwnerId(statistic.getOwnerId());
            if (isPortalItemGroupType) {
                processWidget(statistic, dto);
            }
            list.add(dto);
        }
        ListStatEventPortalStatResponse response = new ListStatEventPortalStatResponse();
        list.sort(Comparator.comparingLong(StatEventPortalStatDTO::getId));
        response.setStatList(list);
        return response;
    }

    private void processWidget(StatEventPortalStatistic statistic, StatEventPortalStatDTO dto) {
        String[] split = statistic.getIdentifier().split(":");
        if (split.length >= 2) {
            dto.setWidget(split[1]);
            if (split[1].equals("OPPush")) {
                switch (split[2]) {
                    case "OPPushBiz":
                        dto.setContentType("EhBizs");
                        break;
                    case "OPPushActivity":
                        dto.setContentType("EhActivities");
                        break;
                    case "Gallery":
                        dto.setContentType("EhServiceAlliances");
                        break;
                }
            }
        }
    }

    @Override
    public StatEventStatDTO listEventStat(StatListEventStatCommand cmd) {
        ValidatorUtil.validate(cmd);
        Integer namespaceId = cmd.getNamespaceId();
        if (namespaceId == null) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }

        Date startDate = new Date(cmd.getStartDate());
        Date endDate = new Date(cmd.getEndDate());

        StatEventStatDTO dto = new StatEventStatDTO();

        List<StatEventStatistic> statList;
        if (cmd.getParentId() == 0) {
            statList = statEventStatisticProvider.countAndListEventStatByZeroParentId(namespaceId, cmd.getIdentifier(), startDate, endDate);
        } else {
            statList = statEventStatisticProvider.countAndListEventStatByParentId(namespaceId, cmd.getParentId(), cmd.getIdentifier(), startDate, endDate);
        }

        List<Map<String, Object>> items = new ArrayList<>();
        for (StatEventStatistic stat : statList) {
            Map<String, Object> param = (Map<String, Object>) StringHelper.fromJsonString(stat.getParam(), Map.class);
            param.put("totalCount", stat.getTotalCount());
            items.add(param);
        }
        dto.setItems(items);
        return dto;
    }

    private StatLogUploadStrategyEnvironmentDTO toStatLogUploadStrategyEnvironmentDTO(StatEventUploadStrategy statEventUploadStrategy) {
        return ConvertHelper.convert(statEventUploadStrategy, StatLogUploadStrategyEnvironmentDTO.class);
    }
}
