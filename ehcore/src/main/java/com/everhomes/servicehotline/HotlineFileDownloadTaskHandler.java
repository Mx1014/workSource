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

// 下载中心备用
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
		
	}
	

	@Override
	public void commit(Map<String, Object> params) {

	}

	@Override
	public void afterExecute(Map<String, Object> params) {

	}
}

