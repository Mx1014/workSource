package com.everhomes.servicehotline;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.servicehotline.ChatRecordDTO;
import com.everhomes.rest.servicehotline.GetChatRecordListCommand;
import com.everhomes.rest.servicehotline.GetChatRecordListResponse;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.util.excel.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.*;

@Component
public class HotlineFileDownloadTaskHandler implements FileDownloadTaskHandler {

	@Autowired
	FileDownloadTaskService fileDownloadTaskService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	private HotlineService hotlineService;


	@Override
	public void beforeExecute(Map<String, Object> params) {

	}

	@Override
	public void execute(Map<String, Object> params) {

		Long taskId = null;
		
//		GetChatRecordListCommand cmd = new GetChatRecordListCommand();
//		GetChatRecordListResponse rsp = hotlineService.getChatRecordList(cmd);
//		List<ChatRecordDTO> chatRecordList = rsp.getChatRecordList();
		
		System.out.println("---*----*--*----*--------it is in the hmb_upload Test!!******************************************");
		System.out.println("params:"+params.get("test"));
		
		String fileName = "mingboTest";
		ExcelUtils excelUtils = new ExcelUtils(fileName, "入驻申请企业信息");
		List<String> propertyNames = new ArrayList<String>(Arrays.asList("senderName", "message", "messageType", "sendTime"));
		List<String> titleNames = new ArrayList<String>(Arrays.asList("发送人", "信息", "类型", "发送时间"));
		List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 30, 10, 30));

		excelUtils.setNeedSequenceColumn(true);
		OutputStream outputStream = excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, null);
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

