// @formatter:off
package com.everhomes.activity;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.activity.ExportActivityDTO;
import com.everhomes.rest.activity.StatisticsActivityCommand;
import com.everhomes.rest.activity.StatisticsActivityDTO;
import com.everhomes.rest.activity.StatisticsActivityResponse;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.excel.ExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ActivityApplyExportTaskHandler implements FileDownloadTaskHandler {

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

        Long startTime = null;
        if(params.get("startTime") != null){
            startTime = Long.valueOf(String.valueOf(params.get("startTime")));
        }

        Long endTime = null;
        if(params.get("endTime") != null){
            endTime = Long.valueOf(String.valueOf(params.get("endTime")));
        }

        Long categoryId = null;
        if (params.get("categoryId") != null) {
            categoryId = Long.valueOf(String.valueOf(params.get("categoryId")));
        }
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");

        StatisticsActivityCommand statisticsActivityCommand = new StatisticsActivityCommand();
        statisticsActivityCommand.setNamespaceId(namespaceId);
        statisticsActivityCommand.setStartTime(startTime);
        statisticsActivityCommand.setEndTime(endTime);
        statisticsActivityCommand.setCategoryId(categoryId);
        StatisticsActivityResponse result = this.activityService.statisticsActivity(statisticsActivityCommand);
        List<StatisticsActivityDTO> statisticsActivityDTOS = result.getList();
        List<ExportActivityDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(statisticsActivityDTOS)) {
            statisticsActivityDTOS.forEach(r -> {
                ExportActivityDTO exportActivityDTO = ConvertHelper.convert(r,ExportActivityDTO.class);
                if(r.getCreateTime() != null){
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    exportActivityDTO.setCreateTimeText(f.format(r.getCreateTime()));
                }
                dtos.add(exportActivityDTO);
            });
        }

        ExcelUtils excelUtils = new ExcelUtils(fileName, "活动报名");
        List<String> propertyNames = new ArrayList<String>(Arrays.asList("subject", "enrollUserCount", "createTimeText"));
        List<String> titleNames = new ArrayList<String>(Arrays.asList("活动标题", "报名人数", "发布时间"));
        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 10, 20));

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
