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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

@Component
public class ContractExportHandler  implements FileDownloadTaskHandler {
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


        Long communityId = null;
        if(params.get("communityId") != null)
            communityId = (Long) params.get("communityId");
        Long categoryId = null;
        if(params.get("categoryId") != null)
            categoryId = (Long) params.get("categoryId");
        Long task_Id = null;
        if(params.get("task_Id") != null)
            task_Id = (Long) params.get("task_Id");
        Integer namespaceId = null;
        if(params.get("namespaceId") != null){
            namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        }

        Long userId = (Long) params.get("userId");

        //  set the basic data
        User user = new User();
        user.setNamespaceId(namespaceId);
        user.setId(userId);

        UserContext.setCurrentUser(user);
        UserContext.setCurrentNamespaceId(namespaceId);


        SearchContractCommand cmd = new SearchContractCommand();
        cmd.setCommunityId(communityId);
        cmd.setCategoryId(categoryId);
        cmd.setTaskId(task_Id);
        cmd.setNamespaceId(namespaceId);

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        ContractService contractService = getContractService(cmd.getNamespaceId());
        OutputStream outputStream = contractService.exportOutputStreamContractListByContractList(cmd, taskId);
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
