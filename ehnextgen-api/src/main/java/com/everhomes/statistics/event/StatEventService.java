package com.everhomes.statistics.event;

import com.everhomes.rest.statistics.event.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xq.tian on 2017/7/31.
 */
public interface StatEventService {

    /**
     * 上传日志
     */
    void postLog(HttpServletRequest request);

    /**
     * 上传设备信息，获取上传策略
     */
    StatPostDeviceResponse postDevice(StatPostDeviceCommand cmd, String remoteAddr, String localAddr);

    /**
     * 上传日志文件
     */
    void postLogFile(StatPostLogFileCommand cmd);

    /**
     * 获取门户统计数据
     */
    List<StatEventPortalStatDTO> listEventPortalStat(ListEventPortalStatCommand cmd);

    /**
     * 获取事件统计数据
     */
    StatEventStatDTO listEventStat(StatListEventStatCommand cmd);
}
