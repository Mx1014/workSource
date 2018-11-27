package com.everhomes.techpark.punch;


import com.alibaba.fastjson.JSONArray;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingEmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Component
public class PunchExportTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    PunchService punchService;

    @Autowired
    TaskService taskService;
    @Autowired
    private PunchVacationBalanceService punchVacationBalanceService;


    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {

        String ownerType = null;
        if (params.get("ownerType") != null) {
            ownerType = String.valueOf(params.get("ownerType"));
        }

        List<Long> departmentIds = null;
        if (params.get("departmentIds") != null) {
            departmentIds = (List<Long>) params.get("departmentIds");
        }

        String userName = null;
        if (params.get("userName") != null) {
            userName = String.valueOf(params.get("userName"));
        }
        String reportType = (String) params.get("reportType");
        Long ownerId = null;
        if (params.get("ownerId") != null) {
            ownerId = Long.valueOf(String.valueOf(params.get("ownerId")));
        }
        Long monthReportId = null;
        if (params.get("monthReportId") != null) {
        	monthReportId = Long.valueOf(String.valueOf(params.get("monthReportId")));
        }
        Long startDay = null;
        if (params.get("startDay") != null) {
            startDay = Long.valueOf(String.valueOf(params.get("startDay")));
        }
        Long userId = null;
        if (params.get("userId") != null) {
            userId = Long.valueOf(String.valueOf(params.get("userId")));
        }
        Long endDay = null;
        if (params.get("endDay") != null) {
            endDay = Long.valueOf(String.valueOf(params.get("endDay")));
        }
        Byte exceptionStatus = null;
        if (params.get("exceptionStatus") != null) {
            exceptionStatus = Byte.valueOf(String.valueOf(params.get("exceptionStatus")));
        }
        Long queryTime = null;
        if (params.get("queryTime") != null) {
            queryTime = Long.valueOf(String.valueOf(params.get("queryTime")));
        }

        List<PunchSchedulingEmployeeDTO> employees = null;
        if (params.get("employees") != null) {
            employees = JSONArray.parseArray(String.valueOf(params.get("employees")), PunchSchedulingEmployeeDTO.class);
        }
        List<PunchTimeRuleDTO> timeRules = null;
        if (params.get("timeRules") != null) {
            timeRules = JSONArray.parseArray(String.valueOf(params.get("timeRules")), PunchTimeRuleDTO.class);
        }


        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream = null;
        if (reportType.equals("exportPunchStatistics")) {
            outputStream = punchService.getPunchStatisticsOutputStream(startDay, endDay, exceptionStatus, userName, ownerType, ownerId, departmentIds, taskId, monthReportId);
        } else if (reportType.equals("exportPunchDetails")) {
            outputStream = punchService.getPunchDetailsOutputStream(startDay, endDay, exceptionStatus, userName, ownerType, ownerId, taskId, userId);
        } else if (reportType.equals("exportPunchScheduling")) {
            outputStream = punchService.getPunchSchedulingOutputStream(queryTime, employees, timeRules, taskId);
        }else if (reportType.equals("exportVacationBalances")) {
            outputStream = punchVacationBalanceService.getVacationBalanceOutputStream(ownerId, taskId);
        }
        taskService.updateTaskProcess(taskId, 99);
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
