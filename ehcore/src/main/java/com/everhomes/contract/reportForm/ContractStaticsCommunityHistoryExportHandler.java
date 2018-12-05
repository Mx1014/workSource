package com.everhomes.contract.reportForm;

import java.io.OutputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contract.ContractService;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contract.statistic.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

@Component
public class ContractStaticsCommunityHistoryExportHandler implements FileDownloadTaskHandler {

	@Autowired
	private TaskService taskService;

	@Autowired
	private FileDownloadTaskService fileDownloadTaskService;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public void beforeExecute(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void execute(Map<String, Object> params) {
		// 前端传过来的所有数据信息
		String fileName = (String) params.get("name");
		Long taskId = (Long) params.get("taskId");
		String userStr = String.valueOf(params.get("UserContext"));
		User user = (User) StringHelper.fromJsonString(userStr, User.class);

		String getTotalContractStaticsCommandStr = String.valueOf(params.get("GetTotalContractStaticsCommand"));
		SearchContractStaticsListCommand cmd = (SearchContractStaticsListCommand) StringHelper.fromJsonString(getTotalContractStaticsCommandStr, SearchContractStaticsListCommand.class);
		user.setNamespaceId(cmd.getNamespaceId());
		UserContext.setCurrentUser(user);

		ContractService contractService = getContractService(cmd.getNamespaceId());
		OutputStream outputStream = null;
		// 所有项目的汇总历史记录 汇总
		if (cmd.getSearchType() == ContractStatisticSearchType.SUMMARYRECORD.getCode()) {
			outputStream = contractService.exportOutputStreamForContractStaticsTime(cmd, taskId);
		}
		// 各项目的历史合计 明细
		if (cmd.getSearchType() == ContractStatisticSearchType.DETAILRECORD.getCode()) {
			outputStream = contractService.exportOutputStreamContractStaticsCommunityTotal(cmd, taskId);
		}
		
		CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
		taskService.processUpdateTask(taskId, fileLocationDTO);
	}

	@Override
	public void commit(Map<String, Object> params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterExecute(Map<String, Object> params) {
		// TODO Auto-generated method stub

	}

	private ContractService getContractService(Integer namespaceId) {
		String handler = configurationProvider.getValue(namespaceId, "contractService", "");
		return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
	}
}
