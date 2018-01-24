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
	IncubatorProvider incubatorProvider;

	@Autowired
	TaskService taskService;


	@Override
	public void beforeExecute(Map<String, Object> params) {

	}

	@Override
	public void execute(Map<String, Object> params) {

		Integer namespaceId = null;
		if(params.get("namespaceId") != null){
			namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
		}

		String keyWord = (String)params.get("keyWord");

		Byte approveStatus = null;
		if(params.get("approveStatus") != null){
			approveStatus = Byte.valueOf(String.valueOf(params.get("approveStatus")));
		}

		Byte needReject = null;
		if(params.get("needReject") != null){
			needReject = Byte.valueOf(String.valueOf(params.get("needReject")));
		}

		Byte orderBy = null;
		if(params.get("orderBy") != null){
			orderBy = Byte.valueOf(String.valueOf(params.get("orderBy")));
		}

		Byte applyType = null;
		if(params.get("applyType") != null){
			applyType = Byte.valueOf(String.valueOf(params.get("applyType")));
		}

		String fileName = (String) params.get("name");
		Long taskId = (Long) params.get("taskId");


		List<IncubatorApply> incubatorApplies = incubatorProvider.listIncubatorApplies(namespaceId, null, keyWord, approveStatus, needReject, null, null, orderBy, applyType);
		List<IncubatorApplyDTO> dtos = new ArrayList<>();
		if(incubatorApplies != null && incubatorApplies.size() > 0){
			incubatorApplies.forEach(r -> {
				IncubatorApplyDTO dto = ConvertHelper.convert(r, IncubatorApplyDTO.class);
//				populateExportString(dto);
				dtos.add(dto);
			});
		}

		ExcelUtils excelUtils = new ExcelUtils(fileName, "入驻申请企业信息");
		List<String> propertyNames = new ArrayList<String>(Arrays.asList("projectName", "projectType", "teamName", "businessLicenceText", "planBookAttachmentText", "chargerName", "chargerPhone", "chargerEmail", "applyTypeText", "createTimeText"));
		List<String> titleNames = new ArrayList<String>(Arrays.asList("项目名称", "项目分类", "申请企业/团队名称", "营业执照扫描件", "创业计划书", "法人代表/负责人姓名", "移动电话", "电子邮箱", "申请类型", "申请时间"));
		List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 10, 20, 10, 10, 10, 10, 20, 10, 10, 10, 10));

		excelUtils.setNeedSequenceColumn(true);
		OutputStream outputStream = excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, dtos);
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
