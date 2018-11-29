package com.everhomes.enterprisepaymentauth;


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
public class EnterprisePaymentAuthExportTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    EnterprisePaymentAuthService enterprisePaymentAuthService;

    @Autowired
    TaskService taskService; 

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
 
        String keyWords = null;
        if (params.get("keyWords") != null) {
        	keyWords = String.valueOf(params.get("keyWords"));
        }
        String orderNo = (String) params.get("orderNo"); 
        Long paymentSceneAppId = null;
        if (params.get("paymentSceneAppId") != null) {
        	paymentSceneAppId = Long.valueOf(String.valueOf(params.get("paymentSceneAppId")));
        }
        Integer namespaceId = null;
        if (params.get("namespaceId") != null) {
        	namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        }
        Long organizationId = null;
        if (params.get("organizationId") != null) {
        	organizationId = Long.valueOf(String.valueOf(params.get("organizationId")));
        }
        Long paymentStartDate = null;
        if (params.get("paymentStartDate") != null) {
        	paymentStartDate = Long.valueOf(String.valueOf(params.get("paymentStartDate")));
        }
        Long paymentEndDate = null;
        if (params.get("paymentEndDate") != null) {
        	paymentEndDate = Long.valueOf(String.valueOf(params.get("paymentEndDate")));
        } 

        String fileName = (String) params.get("name");
        String reportType = (String) params.get("reportType");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream = null;
        if (reportType.equals("exportEnterprisePayLogs")) {
            outputStream = enterprisePaymentAuthService.getEnterprisePayLogsOutputStream(namespaceId, organizationId,paymentSceneAppId,paymentStartDate,paymentEndDate,keyWords,orderNo,taskId);
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
