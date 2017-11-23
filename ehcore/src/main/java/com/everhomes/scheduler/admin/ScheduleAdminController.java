// @formatter:off
package com.everhomes.scheduler.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.scheduler.ScheduleAtTimeCommand;
import com.everhomes.rest.scheduler.ScheduleCronJobCommand;
import com.everhomes.rest.scheduler.ScheduleJobInfoDTO;
import com.everhomes.rest.scheduler.ScheduleRepeatJobCommand;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.StringHelper;

@RestDoc(value="Group admin controller", site="core")
@RestController
@RequestMapping("/admin/schedule")
public class ScheduleAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleAdminController.class);
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    /**
     * <b>URL: /admin/schedule/listJobInfos</b>
     * <p>列出job信息</p>
     */
    @RequestMapping("listJobInfos")
    @RestReturn(value=ScheduleJobInfoDTO.class, collection=true)
    public RestResponse listJobInfos() {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        List<ScheduleJobInfoDTO> jobInfoList = scheduleProvider.listScheduleJobs();
        
        RestResponse response = new RestResponse(jobInfoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/schedule/scheduleAtTime</b>
     * <p>按指定时间执行job</p>
     */
    @RequestMapping("scheduleAtTime")
    @RestReturn(value=String.class)
    public RestResponse scheduleAtTime(ScheduleAtTimeCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        String jobClassName = cmd.getJobClassName();
        String triggerName = cmd.getTriggerName();
        String jobName = cmd.getJobName();
        Date startTime = null;
        if(cmd.getStartTime() != null) {
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cmd.getStartTime());
            } catch (Exception e) {
                LOGGER.error("Failed to parse the time", e);
            }
        }
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Map<String, Object> map = (Map)StringHelper.fromJsonString(cmd.getParameterJson(), HashMap.class);
        scheduleProvider.scheduleSimpleJob(triggerName, jobName, startTime, jobClassName, map);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/schedule/scheduleRepeatJob</b>
     * <p>按指定时间、重复次数和间隔执行job</p>
     */
    @RequestMapping("scheduleRepeatJob")
    @RestReturn(value=String.class)
    public RestResponse scheduleRepeatJob(ScheduleRepeatJobCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        String jobClassName = cmd.getJobClassName();
        String triggerName = cmd.getTriggerName();
        String jobName = cmd.getJobName();
        Date startTime = null;
        if(cmd.getStartTime() != null) {
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cmd.getStartTime());
            } catch (Exception e) {
                LOGGER.error("Failed to parse the time", e);
            }
        }
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Map<String, Object> map = (Map)StringHelper.fromJsonString(cmd.getParameterJson(), HashMap.class);
        scheduleProvider.scheduleRepeatJob(triggerName, jobName, startTime, cmd.getRepeatInterval(), cmd.getRepeatCount(), jobClassName, map);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/schedule/scheduleCronJob</b>
     * <p>按指定周期表达式执行job</p>
     */
    @RequestMapping("scheduleCronJob")
    @RestReturn(value=String.class)
    public RestResponse scheduleCronJob(ScheduleCronJobCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        String jobClassName = cmd.getJobClassName();
        String triggerName = cmd.getTriggerName();
        String jobName = cmd.getJobName();
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Map<String, Object> map = (Map)StringHelper.fromJsonString(cmd.getParameterJson(), HashMap.class);
        scheduleProvider.scheduleCronJob(triggerName, jobName, cmd.getCronExpression(), jobClassName, map);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/schedule/getRunningFlag</b>
     * <p>获取是否执行任务调度flag</p>
     */
    @RequestMapping("getRunningFlag")
    @RestReturn(value=Byte.class)
    @RequireAuthentication(false)
    public RestResponse getRunningFlag() {
        Map<String, Byte> result = new HashMap<>();
        result.put("runningFlag", scheduleProvider.getRunningFlag());
        return new RestResponse(result);
    }

    /**
     * <b>URL: /admin/schedule/setRunningFlag</b>
     * <p>设置任务调度flag</p>
     */
    @RequestMapping("setRunningFlag")
    @RestReturn(value=String.class)
    public RestResponse setRunningFlag(@RequestParam(value="runningFlag", required=true) Byte runningFlag) {
        scheduleProvider.setRunningFlag(runningFlag);
        return new RestResponse();
    }
}
