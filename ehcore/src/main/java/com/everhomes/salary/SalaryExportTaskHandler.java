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

		Long organizationId = null;
		if(params.get("organizationId") != null){
			organizationId = Long.valueOf(String.valueOf(params.get("organizationId")));
		}


		String fileName = (String) params.get("name");
		Long taskId = (Long) params.get("taskId");

		OutputStream outputStream = salaryService.getEmployeeSalaryOutPut(organizationId);
		CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream);


		Task task = taskService.findById(taskId);
		if(fileLocationDTO != null && fileLocationDTO.getUri() != null){
			task.setProcess(100);
			task.setStatus(TaskStatus.SUCCESS.getCode());
			task.setResultString1(fileLocationDTO.getUri());
			if(fileLocationDTO.getSize() != null){
				task.setResultLong1(fileLocationDTO.getSize().longValue());
			}
			taskService.updateTask(task);
		}

	}
	@Override
	public void commit(Map<String, Object> params) {

	}

	@Override
	public void afterExecute(Map<String, Object> params) {

	}
}
