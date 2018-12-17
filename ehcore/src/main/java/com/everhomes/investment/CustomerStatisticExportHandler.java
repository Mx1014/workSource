package com.everhomes.investment;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.investment.ExportCustomerStatisticsCommand;
import com.everhomes.rest.investment.GetCustomerStatisticsCommand;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

@Component
public class CustomerStatisticExportHandler implements FileDownloadTaskHandler {

    @Autowired
    private InvitedCustomerService invitedCustomerService;

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;
    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        String ExportCustomerStatisticsCommandStr =  String.valueOf(params.get("ExportCustomerStatisticsCommand"));
        ExportCustomerStatisticsCommand cmd = (ExportCustomerStatisticsCommand)
                StringHelper.fromJsonString(ExportCustomerStatisticsCommandStr, ExportCustomerStatisticsCommand.class);
        OutputStream outputStream = invitedCustomerService.exportOutputStreamCustomerStatistic(cmd, taskId, cmd.getExportType());
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
