package com.everhomes.customer;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contract.ContractService;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contract.SearchContractCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.Int;

import java.io.OutputStream;
import java.util.Map;

@Component
public class CustomerExportHandler implements FileDownloadTaskHandler {
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
        Long task_Id = null;
        if(params.get("task_Id") != null)
            task_Id = (Long) params.get("task_Id");
        Integer namespaceId = null;
        if(params.get("namespaceId") != null){
            namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        }

        String moduleName = null;
        if(params.get("moduleName") != null)
            moduleName = (String) params.get("moduleName");

        String trackingUids = null;
        if(params.get("trackingUids") != null)
            trackingUids = (String) params.get("trackingUids");

        Long trackingUid = null;
        if(params.get("trackingUid") != null)
            trackingUid = (Long) params.get("trackingUid");

        Long orgId = null;
        if(params.get("orgId") != null)
            orgId = (Long) params.get("orgId");

        Byte abnormalFlag = null;
        if(params.get("abnormalFlag") != null)
            abnormalFlag = (Byte) params.get("abnormalFlag");

        Long addressId = null;
        if(params.get("addressId") != null)
            addressId = (Long) params.get("addressId");

        Byte adminFlag = null;
        if(params.get("adminFlag") != null)
            adminFlag = (Byte) params.get("adminFlag");

        Long buildingId = null;
        if(params.get("buildingId") != null)
            buildingId = (Long) params.get("buildingId");

        Integer type = null;
        if(params.get("type") != null)
            type = (int) params.get("type");

        String trackingName = null;
        if(params.get("trackingName") != null)
            trackingName = (String) params.get("trackingName");

        Long corpIndustryItemId = null;
        if(params.get("corpIndustryItemId") != null)
            corpIndustryItemId = (Long) params.get("corpIndustryItemId");

        Long customerCategoryId = null;
        if(params.get("customerCategoryId") != null)
            customerCategoryId = (Long) params.get("customerCategoryId");

        Long cncludedGroupIds = null;
        if(params.get("cncludedGroupIds") != null)
            cncludedGroupIds = (Long) params.get("cncludedGroupIds");

        Byte infoFLag = null;
        if(params.get("infoFLag") != null)
            infoFLag = (Byte) params.get("infoFLag");

        String keyword = null;
        if(params.get("keyword") != null)
            keyword = (String) params.get("keyword");

        Integer lastTrackingTime = null;
        if(params.get("lastTrackingTime") != null)
            lastTrackingTime = (Integer) params.get("lastTrackingTime");

        Long levelId = null;
        if(params.get("levelId") != null)
            levelId = (Long) params.get("levelId");

        /*if(params.get("ownerId") != null)
        if(params.get("ownerType") != null)
        if(params.get("propertyArea") != null)
        if(params.get("propertyUnitPrice") != null)
        if(params.get("propertyType") != null)
        if(params.get("sortField") != null)
        if(params.get("sortType") != null)*/


        Long userId = (Long) params.get("userId");

        //  set the basic data
        User user = new User();
        user.setNamespaceId(namespaceId);
        user.setId(userId);

        UserContext.setCurrentUser(user);
        UserContext.setCurrentNamespaceId(namespaceId);




        SearchContractCommand cmd = new SearchContractCommand();
        cmd.setCommunityId(communityId);
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
