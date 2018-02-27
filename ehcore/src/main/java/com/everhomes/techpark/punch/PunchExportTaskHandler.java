package com.everhomes.techpark.punch;


import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.Task;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.rest.incubator.*;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.excel.ExcelUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PunchExportTaskHandler implements FileDownloadTaskHandler {

	@Autowired
	FileDownloadTaskService fileDownloadTaskService;

	@Autowired
	PunchService punchService;

	@Autowired
	TaskService taskService;


	@Override
	public void beforeExecute(Map<String, Object> params) {

	}

	@Override
	public void execute(Map<String, Object> params) {
        
        String ownerType = null;
		if(params.get("ownerType") != null){
			ownerType =  String.valueOf(params.get("ownerType"));
		}
		String userName = (String)params.get("userName");
		String reportType = (String)params.get("reportType");
		Long ownerId = null;
		if(params.get("ownerId") != null){
			ownerId = Long.valueOf(String.valueOf(params.get("ownerId")));
		} 
		Long startDay = null;
		if(params.get("startDay") != null){
			startDay = Long.valueOf(String.valueOf(params.get("startDay")));
		} 
		Long endDay = null;
		if(params.get("endDay") != null){
			endDay = Long.valueOf(String.valueOf(params.get("endDay")));
		} 
		Byte exceptionStatus = null;
		if(params.get("exceptionStatus") != null){
			exceptionStatus = Byte.valueOf(String.valueOf(params.get("exceptionStatus")));
		} 
		String fileName = (String) params.get("name");
		Long taskId = (Long) params.get("taskId");
		OutputStream outputStream = null;
		if(reportType.equals("exportPunchStatistics")){ 
			outputStream = punchService.getPunchStatisticsOutputStream(startDay, endDay, exceptionStatus, userName, ownerType, ownerId,taskId);
		}else if (reportType.equals("exportPunchDetails")){ 
			outputStream = punchService.getPunchDetailsOutputStream(startDay, endDay, exceptionStatus, userName, ownerType, ownerId,taskId);
		}
		CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream);
		taskService.processUpdateTask(taskId, fileLocationDTO);
	}

	@Override
	public void commit(Map<String, Object> params) {

	}

	@Override
	public void afterExecute(Map<String, Object> params) {

	}
}
