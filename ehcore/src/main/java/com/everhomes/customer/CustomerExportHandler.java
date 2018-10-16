package com.everhomes.customer;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contract.ContractService;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contract.SearchContractCommand;
import com.everhomes.rest.customer.ExportEnterpriseCustomerCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import scala.Int;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

@Component
public class CustomerExportHandler implements FileDownloadTaskHandler {
    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private CustomerService customerService;


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
            abnormalFlag = Byte.valueOf(String.valueOf(params.get("abnormalFlag")));

        Long addressId = null;
        if(params.get("addressId") != null)
            addressId = (Long) params.get("addressId");

        Byte adminFlag = null;
        if(params.get("adminFlag") != null)
            adminFlag = Byte.valueOf(String.valueOf(params.get("adminFlag")));

        Long buildingId = null;
        if(params.get("buildingId") != null)
            buildingId = (Long) params.get("buildingId");

        Integer type = null;
        if(params.get("type") != null)
            type = Integer.valueOf(String.valueOf(params.get("type")));

        String trackingName = null;
        if(params.get("trackingName") != null)
            trackingName = (String) params.get("trackingName");

        Long corpIndustryItemId = null;
        if(params.get("corpIndustryItemId") != null)
            corpIndustryItemId = (Long) params.get("corpIndustryItemId");

        Byte aptitudeFlagItemId = null;
        if(params.get("aptitudeFlagItemId") != null)
            aptitudeFlagItemId = Byte.valueOf(String.valueOf(params.get("aptitudeFlagItemId")));

        Long customerCategoryId = null;
        if(params.get("customerCategoryId") != null)
            customerCategoryId = (Long) params.get("customerCategoryId");

        String includedGroupIds = null;
        if(params.get("includedGroupIds") != null)
            includedGroupIds = (String) params.get("includedGroupIds");

        Boolean infoFLag = null;
        if(params.get("infoFLag") != null)
            infoFLag = (Boolean) params.get("infoFLag");

        String keyword = null;
        if(params.get("keyword") != null)
            keyword = (String) params.get("keyword");

        Integer lastTrackingTime = null;
        if(params.get("lastTrackingTime") != null)
            lastTrackingTime = Integer.valueOf(String.valueOf(params.get("lastTrackingTime")));

        String levelId = null;
        if(params.get("levelId") != null)
            levelId = (String) params.get("levelId");

        Long ownerId = null;
        if(params.get("ownerId") != null)
            ownerId = (Long) params.get("ownerId");

        String ownerType = null;
        if(params.get("ownerType") != null)
            ownerType = (String) params.get("ownerType");

        String propertyArea = null;
        if(params.get("propertyArea") != null)
            propertyArea = (String) params.get("propertyArea");

        String propertyUnitPrice = null;
        if(params.get("propertyUnitPrice") != null)
            propertyUnitPrice = (String) params.get("propertyUnitPrice");

        String propertyType = null;
        if(params.get("propertyType") != null)
            propertyType = (String)params.get("propertyType");

        String sortField = null;
        if(params.get("sortField") != null)
            sortField = (String) params.get("sortField");

        Integer sortType = null;
        if(params.get("sortType") != null)
            sortType = Integer.valueOf(String.valueOf(params.get("sortType")));

        Byte customerSource = null;
        if(params.get("customerSource") != null)
            customerSource = Byte.valueOf(String.valueOf(params.get("customerSource")));

        BigDecimal requirementMinArea = null;
        if(params.get("requirementMinArea") != null)
            requirementMinArea = BigDecimal.valueOf((Long)params.get("requirementMinArea"));

        BigDecimal requirementMaxArea = null;
        if(params.get("requirementMaxArea") != null)
            requirementMaxArea = BigDecimal.valueOf((Long)params.get("requirementMaxArea"));


        Long entryStatusItemId = null;
        if(params.get("entryStatusItemId") != null)
            entryStatusItemId = (Long) params.get("entryStatusItemId");


        String trackerUids = null;
        if(params.get("trackerUids") != null)
            trackerUids = (String) params.get("trackerUids");

        String customerName = null;
        if(params.get("customerName") != null)
            customerName = (String) params.get("customerName");




        ExportEnterpriseCustomerCommand cmd = new ExportEnterpriseCustomerCommand();
        cmd.setCommunityId(communityId);
        cmd.setTaskId(task_Id);
        cmd.setNamespaceId(namespaceId);
        cmd.setModuleName(moduleName);
        cmd.setTrackingUids(trackingUids);
        cmd.setTrackingUid(trackingUid);
        cmd.setOrgId(orgId);
        cmd.setAbnormalFlag(abnormalFlag);
        cmd.setAddressId(addressId);
        cmd.setAptitudeFlagItemId(aptitudeFlagItemId);
        cmd.setBuildingId(buildingId);
        cmd.setAdminFlag(adminFlag);
        cmd.setType(type);
        cmd.setTrackingName(trackingName);
        cmd.setCorpIndustryItemId(corpIndustryItemId);
        cmd.setCustomerCategoryId(customerCategoryId);
        cmd.setIncludedGroupIds(includedGroupIds);
        cmd.setInfoFLag(infoFLag);
        cmd.setKeyword(keyword);
        cmd.setLastTrackingTime(lastTrackingTime);
        cmd.setLevelId(levelId);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setPropertyArea(propertyArea);
        cmd.setPropertyUnitPrice(propertyUnitPrice);
        cmd.setPropertyType(propertyType);
        cmd.setSortField(sortField);
        cmd.setSortType(sortType);
        cmd.setCustomerSource(customerSource);
        cmd.setRequirementMaxArea(requirementMaxArea);
        cmd.setRequirementMinArea(requirementMinArea);
        cmd.setEntryStatusItemId(entryStatusItemId);
        cmd.setTrackerUids(trackerUids);
        cmd.setCustomerName(customerName);


        Long userId = (Long) params.get("userId");

        //  set the basic data
        User user = new User();
        user.setNamespaceId(namespaceId);
        user.setId(userId);

        UserContext.setCurrentUser(user);
        UserContext.setCurrentNamespaceId(namespaceId);



        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream = customerService.exportEnterpriseCustomerWihtOutPrivilege(cmd);
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
