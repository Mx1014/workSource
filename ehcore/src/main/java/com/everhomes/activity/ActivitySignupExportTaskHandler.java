// @formatter:off
package com.everhomes.activity;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.activity.ExportActivityCommand;
import com.everhomes.rest.activity.ExportSignupInfoCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

@Component
public class ActivitySignupExportTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;
    @Autowired
    private TaskService taskService;
    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {

        ExportSignupInfoCommand exportSignupInfoCommand = new ExportSignupInfoCommand();
        if (params.get("activityId") != null) {
            exportSignupInfoCommand.setActivityId(Long.valueOf(params.get("activityId").toString()));
        }
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");

        OutputStream outputStream = this.activityService.getActivitySignupExportStream(exportSignupInfoCommand,taskId);
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
