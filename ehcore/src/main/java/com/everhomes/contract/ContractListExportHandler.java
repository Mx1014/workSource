package com.everhomes.contract;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contract.SearchContractCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;
/**
 * Created by djm on 2018/9/3.
 *
 */

@Component
public class ContractListExportHandler  implements FileDownloadTaskHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContractListExportHandler.class);
	
	@Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

	@Override
	public void execute(Map<String, Object> params) {
		// 前端传过来的所有数据信息
		String userStr = String.valueOf(params.get("UserContext"));
		User user = (User) StringHelper.fromJsonString(userStr, User.class);
		String fileName = (String) params.get("name");
		Long taskId = (Long) params.get("taskId");
		String ListCMDStr = String.valueOf(params.get("ListCMD"));
		SearchContractCommand cmd = (SearchContractCommand) StringHelper.fromJsonString(ListCMDStr, SearchContractCommand.class);
		user.setNamespaceId(cmd.getNamespaceId());
		UserContext.setCurrentUser(user);

		ContractService contractService = getContractService(cmd.getNamespaceId());
		OutputStream outputStream = contractService.exportOutputStreamListByTaskId(cmd, taskId);

		CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
		taskService.processUpdateTask(taskId, fileLocationDTO);
	}

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
    private ContractService getContractService(Integer namespaceId) {
        String handler = configurationProvider.getValue(namespaceId, "contractService", "");
        return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
    }
}
