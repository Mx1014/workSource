package com.everhomes.salary;


import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.Task;
import com.everhomes.filedownload.TaskService;
import com.everhomes.incubator.IncubatorApply;
import com.everhomes.incubator.IncubatorApplyAttachment;
import com.everhomes.incubator.IncubatorProvider;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.rest.incubator.ApplyType;
import com.everhomes.rest.incubator.IncubatorApplyAttachmentType;
import com.everhomes.rest.incubator.IncubatorApplyDTO;
import com.everhomes.rest.salary.SalaryReportType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.excel.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class SalaryExportTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    SalaryService salaryService;

    @Autowired
    TaskService taskService;


    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {

        Long ownerId = null;
        if (params.get("ownerId") != null) {
            ownerId = Long.valueOf(String.valueOf(params.get("ownerId")));
        }

        String month = null;
        if (params.get("month") != null) {
            month = String.valueOf(params.get("month"));
        }

        Integer namespaceId = Integer.valueOf( (String) params.get("namespaceId"));

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        Byte excelToken = Byte.valueOf((String) params.get("excelToken"));
        OutputStream outputStream = null;
        switch (SalaryReportType.fromCode(excelToken)) {
            case SALARY_DETAIL:
                outputStream = salaryService.getSalaryDetailsOutPut(ownerId, month, taskId,namespaceId);
                break;
            case DPT_STATISTIC:
                outputStream = salaryService.getDepartStatisticsOutPut(ownerId, month, taskId,namespaceId);
                break;
            case SALARY_EMPLOYEE:
                outputStream = salaryService.getEmployeeSalaryOutPut(ownerId, taskId,namespaceId);
                break;
        }
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
