package com.everhomes.organization.pm.reportForm;

import java.io.OutputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressCmd;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.util.StringHelper;

public class CommunityReportFormExportHandler implements FileDownloadTaskHandler{

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FileDownloadTaskService fileDownloadTaskService;
	
	@Autowired
	private PropertyReportFormService propertyReportFormService;
	
	@Override
	public void beforeExecute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Map<String, Object> params) {
		//前端传过来的所有数据信息
        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        String ListBillStatisticByAddressCmdStr =  String.valueOf(params.get("ListBillStatisticByAddressCmd"));
        ListBillStatisticByAddressCmd cmd = (ListBillStatisticByAddressCmd) 
        		StringHelper.fromJsonString(ListBillStatisticByAddressCmdStr, ListBillStatisticByAddressCmd.class);
//    	OutputStream outputStream = propertyReportFormService.exportOutputStreamBillStatisticByAddress(cmd, taskId);
//      CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
//      taskService.processUpdateTask(taskId, fileLocationDTO);
	}

	@Override
	public void commit(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExecute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
	}

}
