// @formatter:off
package com.everhomes.activity;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.activity.StatisticsOrganizationCommand;
import com.everhomes.rest.activity.StatisticsOrganizationDTO;
import com.everhomes.rest.activity.StatisticsOrganizationResponse;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.util.excel.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ActivityOrganizationExportTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    TaskService taskService;

    @Autowired
    ActivityService activityService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        Integer namespaceId = null;
        if(params.get("namespaceId") != null){
            namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        }

        Long categoryId = null;
        if (params.get("categoryId") != null) {
            categoryId = Long.valueOf(String.valueOf(params.get("categoryId")));
        }
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");

        StatisticsOrganizationCommand statisticsOrganizationCommand = new StatisticsOrganizationCommand();
        statisticsOrganizationCommand.setNamespaceId(namespaceId);
        statisticsOrganizationCommand.setCategoryId(categoryId);
        StatisticsOrganizationResponse result = this.activityService.statisticsOrganization(statisticsOrganizationCommand);
        List<StatisticsOrganizationDTO> dtos = result.getList();

        ExcelUtils excelUtils = new ExcelUtils(fileName, "企业报名");
        List<String> propertyNames = new ArrayList<String>(Arrays.asList("orgName", "signPeopleCount", "signActivityCount"));
        List<String> titleNames = new ArrayList<String>(Arrays.asList("企业名称", "报名总人次", "报名活动场数"));
        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 10, 10));

        excelUtils.setNeedSequenceColumn(true);
        OutputStream outputStream = excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, dtos);
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
