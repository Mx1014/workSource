package com.everhomes.general_approval;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.Task;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.filedownload.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStream;
import java.util.Map;

public class GeneralApprovalExportTaskHandler implements FileDownloadTaskHandler{

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {

        Long organizationId = null;
        if (params.get("organizationId") != null)
            organizationId = (Long) params.get("organizationId");
        Long moduleId = null;
        if (params.get("moduleId") != null)
            moduleId = (Long) params.get("moduleId");
        Long startTime = null;
        if (params.get("startTime") != null)
            startTime = (Long) params.get("startTime");
        Long endTime = null;
        if (params.get("endTime") != null)
            endTime = (Long) params.get("endTime");
        Byte approvalStatus = null;
        if (params.get("approvalStatus") != null)
            approvalStatus = (Byte) params.get("approvalStatus");
        Long approvalId = null;
        if (params.get("approvalId") != null)
            approvalId = (Long) params.get("approvalId");
        Long creatorDepartmentId = null;
        if (params.get("creatorDepartmentId") != null)
            creatorDepartmentId = (Long) params.get("creatorDepartmentId");
        String creatorName = (String) params.get("creatorName");
        Long approvalNo = null;
        if (params.get("approvalNo") != null)
            approvalNo = (Long) params.get("approvalNo");
        OutputStream outputStream = null;
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream);
        taskService.processUpdateTask(taskId, fileLocationDTO);


/*
        params.put("organizationId", cmd.getOrganizationId());
        params.put("moduleId", cmd.getModuleId());
        params.put("startTime", cmd.getStartTime());
        params.put("endTime", cmd.getEndTime());
        params.put("approvalStatus", cmd.getApprovalStatus());
        params.put("approvalId", cmd.getApprovalId());
        params.put("creatorDepartmentId", cmd.getCreatorDepartmentId());
        params.put("creatorName", cmd.getCreatorName());
        params.put("approvalNo", cmd.getApprovalNo());
*/


    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
