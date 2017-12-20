package com.everhomes.incubator;


import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.rest.incubator.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class IncubatorApplyExportTaskHandler implements FileDownloadTaskHandler {

	@Autowired
	IncubatorService incubatorService;

	@Autowired
	FileDownloadTaskService fileDownloadTaskService;

	@Autowired
	IncubatorProvider incubatorProvider;


	@Override
	public void beforeExecute(Map<String, Object> params) {

	}

	@Override
	public void execute(Map<String, Object> params) {

		ExportIncubatorApplyCommand cmd = null;
		incubatorProvider.listIncubatorApplies(cmd.getNamespaceId(), null, cmd.getKeyWord(), cmd.getApproveStatus(), cmd.getNeedReject(), null, null, cmd.getOrderBy(), cmd.getApplyType());



	}

	@Override
	public void commit(Map<String, Object> params) {

	}

	@Override
	public void afterExecute(Map<String, Object> params) {

	}
}
