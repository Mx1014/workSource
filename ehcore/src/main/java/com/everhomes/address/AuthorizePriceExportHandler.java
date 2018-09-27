package com.everhomes.address;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

@Component
public class AuthorizePriceExportHandler  implements FileDownloadTaskHandler {
    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
	PropertyMgrService propertyMgrService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

	@Override
	public void execute(Map<String, Object> params) {
		String userStr = String.valueOf(params.get("UserContext"));
		User user = (User) StringHelper.fromJsonString(userStr, User.class);
		String fileName = (String) params.get("name");
		Long taskId = (Long) params.get("taskId");
		String listPropApartmentsByKeywordCommand = String.valueOf(params.get("CommandCMD"));

		ListPropApartmentsByKeywordCommand cmd = (ListPropApartmentsByKeywordCommand) StringHelper
				.fromJsonString(listPropApartmentsByKeywordCommand, ListPropApartmentsByKeywordCommand.class);
		user.setNamespaceId(cmd.getNamespaceId());
		UserContext.setCurrentUser(user);

		OutputStream outputStream = propertyMgrService.exportOutputStreamAuthorizePriceList(cmd, taskId);

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
