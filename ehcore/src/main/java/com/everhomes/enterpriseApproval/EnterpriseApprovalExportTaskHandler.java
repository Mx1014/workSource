package com.everhomes.enterpriseApproval;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.enterpriseApproval.ApprovalFilter;
import com.everhomes.rest.enterpriseApproval.ListApprovalFlowRecordsCommand;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

@Component
public class EnterpriseApprovalExportTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService;

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
            approvalStatus = Byte.valueOf(String.valueOf(params.get("approvalStatus")));
        ApprovalFilter filter = null;
        if (params.get("filter") != null)
            filter = (ApprovalFilter) StringHelper.fromJsonString((String) params.get("filter"), ApprovalFilter.class);
        Long creatorDepartmentId = null;
        if (params.get("creatorDepartmentId") != null)
            creatorDepartmentId = (Long) params.get("creatorDepartmentId");
        String creatorName = (String) params.get("creatorName");
        Long approvalNo = null;
        if (params.get("approvalNo") != null)
            approvalNo = (Long) params.get("approvalNo");
        Integer namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));

        ListApprovalFlowRecordsCommand cmd = new ListApprovalFlowRecordsCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setOrganizationId(organizationId);
        cmd.setModuleId(moduleId);
        cmd.setStartTime(startTime);
        cmd.setEndTime(endTime);
        cmd.setApprovalStatus(approvalStatus);
        cmd.setFilter(filter);
        cmd.setCreatorDepartmentId(creatorDepartmentId);
        cmd.setCreatorName(creatorName);
        cmd.setApprovalNo(approvalNo);
        cmd.setPageAnchor(null);
        cmd.setPageSize(Integer.MAX_VALUE - 1);

        //  get the out put stream before start download
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream = enterpriseApprovalService.getEnterpriseApprovalOutputStream(cmd, taskId);
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
