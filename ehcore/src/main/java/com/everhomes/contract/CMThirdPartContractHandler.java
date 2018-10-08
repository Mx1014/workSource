package com.everhomes.contract;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.asset.AssetService;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.customer.SyncDataTaskProvider;
import com.everhomes.customer.SyncDataTaskService;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.openapi.ZjSyncdataBackup;
import com.everhomes.openapi.ZjSyncdataBackupProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.EbeiCustomer;
import com.everhomes.rest.customer.EbeiJsonEntity;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.SyncFlag;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.EnterpriseSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.userOrganization.UserOrganizationProvider;
import com.everhomes.util.*;
import com.everhomes.util.xml.XMLToJSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlObject;
import org.elasticsearch.index.mapper.MapperBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pengyu.huang on 2018/08/21.
 */
@Component(ThirdPartContractHandler.CONTRACT_PREFIX + "999929")
public class CMThirdPartContractHandler implements ThirdPartContractHandler{

    private final static Logger LOGGER = LoggerFactory.getLogger(EbeiThirdPartContractHandler.class);

    private static final String PAGE_SIZE = "20";
    private static final String SUCCESS_CODE = "0";
    private static final Integer NAMESPACE_ID = 999929;

    private static final String DispContract = "/DispContract";


    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;
    
    @Autowired
    private AssetService assetService;
	
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ContractProvider contractProvider;
    
    @Autowired
    private EnterpriseCustomerProvider customerProvider;

    @Autowired
    private EnterpriseSearcher customerSearcher;
    
    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private BuildingProvider buildingProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserOrganizationProvider userOrganizationProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private DoorAccessService doorAccessService;

    @Autowired
    private SyncDataTaskProvider syncDataTaskProvider;

    @Autowired
    private SyncDataTaskService syncDataTaskService;

    @Override
    public void syncContractsFromThirdPart(String pageOffset, String date, String communityIdentifier, Long taskId, Long categoryId, Byte contractApplicationScene){
        Map<String, String> params= new HashMap<>();
        Map<String, String> codeString= new HashMap<>();


        Integer currentPage = 0;
        Integer totalPage = 2147483647;

        List<CMSyncObject> cmSyncObjects = new ArrayList<>();

        while(currentPage < totalPage){
            //同步CM数据到物业账单表
            if(date == null || "".equals(date)) {
                date = "1970-01-01";
            }
            params.put("Date", date);
            params.put("CurrentPage", pageOffset);

            String enterprises = null;
            String url = configurationProvider.getValue("RuiAnCM.sync.url", "");

            try {
                String codeStr = JSONObject.toJSONString(params);
                codeString.put("CodeStr", codeStr);
                LOGGER.info("sync customer from RuiAnCM param: {}", codeString);
                enterprises = HttpUtils.get(url+DispContract, codeString, 600, "UTF-8");
            } catch (Exception e) {
                LOGGER.error("sync customer from RuiAnCM error: {}", e);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ContractErrorCode.ERROR_CONTRACT_SYNC_UNKNOW_ERROR, "sync customer from RuiAnCM error");
            }
            enterprises = enterprises.substring(enterprises.indexOf(">{")+1, enterprises.indexOf("</string>"));

            CMSyncObject cmSyncObject =
                    (CMSyncObject) StringHelper.fromJsonString(enterprises, CMSyncObject.class);




            if(cmSyncObject != null) {
                currentPage = Integer.valueOf(cmSyncObject.getCurrentpage());
                totalPage = Integer.valueOf(cmSyncObject.getTotalpage());
                pageOffset = String.valueOf(currentPage + 1);
                cmSyncObjects.add(cmSyncObject);
                syncData(cmSyncObject, DataType.CONTRACT.getCode(), communityIdentifier);
                if(SUCCESS_CODE.equals(cmSyncObject.getErrorCode())) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = new Date(System.currentTimeMillis());
                    String nowStr = sdf.format(now);
                    if(date.compareTo(nowStr) < 0) {
                        //同步到记录表
                        syncData(cmSyncObject, DataType.CONTRACT.getCode(), communityIdentifier);

                        date = getNextDay(date, sdf);
                    }

                }else{
                    LOGGER.error("sync from RuiAnCM error: {}", cmSyncObject.getErrorCode());

                }
            }else{
                LOGGER.error("sync data from RuiAnCM is fail" );
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ContractErrorCode.ERROR_CONTRACT_SYNC_UNKNOW_ERROR, "sync data from RuiAnCM is fail");

            }

        }


        //存储瑞安合同
        syncDataToDb(DataType.CONTRACT.getCode(), communityIdentifier, taskId, categoryId, contractApplicationScene);

        //同步CM数据到物业账单表
        assetService.syncRuiAnCMBillToZuolin(cmSyncObjects, 999929, categoryId);

        //String url = "http://183.62.222.87:5902/sf";
        






/*        if(SUCCESS_CODE.equals(cmSyncObject.getErrorCode())) {
            List<CMDataObject> dtos = cmSyncObject.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(cmSyncObject, DataType.CONTRACT.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if(!cmSyncObject.getTotalpage().equals(cmSyncObject.getCurrentpage())) {
                    syncContractsFromThirdPart(String.valueOf(Integer.valueOf(cmSyncObject.getCurrentpage()) + 1), date, communityIdentifier, taskId, categoryId, contractApplicationScene);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(cmSyncObject.getTotalpage().equals(cmSyncObject.getCurrentpage())) {
                //syncDataToDb(DataType.CONTRACT.getCode(), communityIdentifier, taskId, categoryId, contractApplicationScene);
            }
        }*/
        /*EbeiJsonEntity<List<EbeiContract>> entity = JSONObject.parseObject(enterprises, new TypeReference<EbeiJsonEntity<List<EbeiContract>>>(){});
        if(SUCCESS_CODE.equals(entity.getResponseCode())) {
            List<EbeiContract> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.CONTRACT.getCode(), communityIdentifier);
                //数据有下一页则继续请求
                if(entity.getHasNextPag() == 1) {
                    syncContractsFromThirdPart(String.valueOf(entity.getCurrentPage()+1), version, communityIdentifier, taskId, categoryId, contractApplicationScene);
                }
            }
            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getHasNextPag() == 0) {
                syncDataToDb(DataType.CONTRACT.getCode(), communityIdentifier, taskId, categoryId, contractApplicationScene);
            }
        }*/
    }
    public static void main(String[] args) {
        System.out.println(String.valueOf(Integer.valueOf("1") + 1));
    }

    
    private void syncDataToDb(Byte dataType, String communityIdentifier, Long taskId, Long categoryId, Byte contractApplicationScene) {
        List<ZjSyncdataBackup> backupList = zjSyncdataBackupProvider.listZjSyncdataBackupByParam(NAMESPACE_ID, communityIdentifier, dataType);
        if (backupList == null || backupList.isEmpty()) {
            LOGGER.debug("syncDataToDb backupList is empty, NAMESPACE_ID: {}, dataType: {}", NAMESPACE_ID, dataType);
            return ;
        }
        try {
            LOGGER.debug("syncDataToDb backupList size：{}", backupList.size());
            updateAllData(dataType, NAMESPACE_ID, communityIdentifier, backupList, categoryId, contractApplicationScene);
        } finally {
            zjSyncdataBackupProvider.updateZjSyncdataBackupInactive(backupList);
            //syncDataTaskService.createSyncErrorMsg(NAMESPACE_ID, taskId);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("dataType {} get out thread=================", dataType);
        }
    }
    


    private String getNextDay(String date, SimpleDateFormat sdf){
        try {
            java.util.Date oldDate = sdf.parse(date);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(oldDate);
            calendar.add(Calendar.DATE, 1);
            oldDate = calendar.getTime();
            date = sdf.format(oldDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void syncData(CMSyncObject cmObj, Byte dataType, String communityIdentifier) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("ebei syncData: dataType: {}, communityIdentifier: {}", dataType, communityIdentifier);
        }
        ZjSyncdataBackup backup = new ZjSyncdataBackup();
        backup.setNamespaceId(NAMESPACE_ID);
        backup.setDataType(dataType);
        backup.setData(StringHelper.toJsonString(cmObj.getData()));
        backup.setStatus(CommonStatus.ACTIVE.getCode());
        backup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        backup.setCreatorUid(1L);
        backup.setUpdateTime(backup.getCreateTime());
        backup.setAllFlag(SyncFlag.PART.getCode());
        backup.setUpdateCommunity(communityIdentifier);
        zjSyncdataBackupProvider.createZjSyncdataBackup(backup);
    }
    
    private void updateAllData(Byte dataType, Integer namespaceId, String communityIdentifier, List<ZjSyncdataBackup> backupList, Long categoryId, Byte contractApplicationScene) {
        DataType cmDataType = DataType.fromCode(dataType);
        LOGGER.debug("CM DataType : {}", cmDataType);
        switch (cmDataType) {
            case CONTRACT:
                LOGGER.debug("syncDataToDb SYNC CONTRACT");
                syncAllContracts(namespaceId, communityIdentifier, backupList, categoryId, contractApplicationScene);
                break;
            case APARTMENT_LIVING_STATUS:
                //syncApartmentLivingStatus(namespaceId, backupList);
                break;
            default:
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "error data type");
        }
    }
    
    private void syncAllContracts(Integer namespaceId, String communityIdentifier, List<ZjSyncdataBackup> backupList, Long categoryId, Byte contractApplicationScene) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.RUIAN_CM.getCode(), communityIdentifier);
        if(community == null) {
            return ;
        }
        
        List<CMDataObject> mergeContractList = mergeBackupList(backupList, CMDataObject.class);
        //这部分已经传过来
        
        
        List<Contract> myContractList = contractProvider.listContractByNamespaceType(namespaceId, NamespaceCommunityType.RUIAN_CM.getCode(), community.getId(), categoryId);

        

        LOGGER.debug("syncDataToDb namespaceId: {}, myContractList size: {}, theirContractList size: {}",
                namespaceId, myContractList.size(), mergeContractList.size());
        syncAllContracts(namespaceId, community.getId(), myContractList, mergeContractList, categoryId, contractApplicationScene);

        /*if (mergeContractList.size() > 0) {
            List<String> ownerIdList = mergeContractList.stream().map(EbeiContract::getOwnerId).collect(Collectors.toList());
            String ownerIds = ownerIdList.toString().substring(1, ownerIdList.toString().length()-1);
            //getOwnerStatus(communityIdentifier, ownerIds);
        }*/
    }
    
    private void syncAllContracts(Integer namespaceId, Long communityId, List<Contract> myContractList, List<CMDataObject> theirContractList, Long categoryId, Byte contractApplicationScene) {
        if (theirContractList != null) {
            for (CMDataObject cmContract : theirContractList) {
            	//删除状态暂不考虑
                if ("0".equals(convertContractStatus(cmContract.getContractHeader().getRecordstatus()))) {
                    if (myContractList != null) {
                        for (Contract contract : myContractList) {
                            if (NamespaceContractType.RUIAN_CM.getCode().equals(contract.getNamespaceContractType())
                                    && contract.getNamespaceContractToken().equals(cmContract.getContractHeader().getRentalID())) {
                                    //deleteContract(contract,ebeiContract.getHouseInfoList());
                            }
                        }
                    }
                //待发起 未提交
                } else if("1".equals(convertContractStatus(cmContract.getContractHeader().getRecordstatus()))) {
                    Boolean notdeal = true;
                    if (myContractList != null) {
                        for (Contract contract : myContractList) {
                            if (NamespaceContractType.RUIAN_CM.getCode().equals(contract.getNamespaceContractType())
                                    && contract.getNamespaceContractToken().equals(cmContract.getContractHeader().getRentalID())) {
                                notdeal = false;
                                updateContract(contract, communityId, cmContract, categoryId, contractApplicationScene);
                            }
                        }
                    }
                    if(notdeal){
                        // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                        Contract contract = contractProvider.findActiveContractByContractNumber(namespaceId, cmContract.getContractHeader().getContractNo(), categoryId);
                        if (contract == null) {
                                insertContract(NAMESPACE_ID, communityId, cmContract, categoryId);
                        }else {
                                updateContract(contract, communityId, cmContract, categoryId, contractApplicationScene);
                        }
                    }
                //3 审批中
                } else {
                    Boolean notdeal = true;
                    if (myContractList != null) {
                        for (Contract contract : myContractList) {
                            if (NamespaceContractType.RUIAN_CM.getCode().equals(contract.getNamespaceContractType())
                                    && contract.getNamespaceContractToken().equals(cmContract.getContractHeader().getRentalID())) {
                                notdeal = false;
                                updateContract(contract, communityId, cmContract, categoryId, contractApplicationScene);
                            }
                        }
                    }
                    if(notdeal){
                        // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                        Contract contract = contractProvider.findActiveContractByContractNumber(namespaceId, cmContract.getContractHeader().getContractNo(), categoryId);
                        if (contract == null) {
                                insertContract(NAMESPACE_ID, communityId, cmContract, categoryId);
                        }else {
                                updateContract(contract, communityId, cmContract, categoryId, contractApplicationScene);
                        }
                    }
                }
            }
        }
    }
    
    private void insertContract(Integer namespaceId, Long communityId, CMDataObject cmContractData, Long categoryId) {
        Contract contract = new Contract();
        contract.setNamespaceId(namespaceId);
        contract.setCommunityId(communityId);
        contract.setNamespaceContractType(NamespaceContractType.RUIAN_CM.getCode());
        contract.setNamespaceContractToken(cmContractData.getContractHeader().getRentalID());
        contract.setContractNumber(cmContractData.getContractHeader().getContractNo());
        contract.setName("瑞安合同");
        //contract.setBuildingRename(cmContractData.getBuildingRename());
        contract.setCategoryId(categoryId);
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getStartDate())) {
            contract.setContractStartDate(dateStrToTimestamp(cmContractData.getContractHeader().getStartDate()));
        }
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getEndDate())) {
            contract.setContractEndDate(dateStrToTimestamp(cmContractData.getContractHeader().getEndDate()));
        }
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getSignDate())) {
            contract.setSignedTime(dateStrToTimestamp(cmContractData.getContractHeader().getSignDate()));
        }
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getTerminateDate())) {
            contract.setDenunciationTime(dateStrToTimestamp(cmContractData.getContractHeader().getTerminateDate()));
        }
        //contract.setVersion(cmContractData.getVersion());
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getLFA())) {
            contract.setRentSize(Double.valueOf(cmContractData.getContractHeader().getLFA()));
        }
        contract.setStatus(convertContractStatus(cmContractData.getContractHeader().getRecordstatus()));
        contract.setCustomerName(cmContractData.getContractHeader().getAccountName());
        //contract.setCustomerId(Long.parseLong(cmContractData.getContractHeader().getAccountID()));
        //处理过的客户ID
        contract.setCustomerId(cmContractData.getCustomerId());
        contract.setCustomerType(CustomerType.ENTERPRISE.getCode());
        
        EnterpriseCustomer customer = customerProvider.findByNamespaceToken(NamespaceContractType.RUIAN_CM.getCode(), cmContractData.getContractHeader().getAccountID());
        if(customer != null) {
            //customer.setContactAddress(cmContractData.getBuildingRename());
            customerProvider.updateEnterpriseCustomer(customer);
            insertOrUpdateOrganizationAddresses(cmContractData.getContractUnit(),customer);
            contract.setCustomerId(customer.getId());
        }
        
        contract.setCreateUid(UserContext.currentUserId());
        contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        contractProvider.createContract(contract);
        dealContractApartments(contract, cmContractData.getContractUnit());
        contractSearcher.feedDoc(contract);
    }
    
    private void insertOrUpdateOrganizationAddresses(List<CMContractUnit> apartmentIdentifier, EnterpriseCustomer customer){
        List<OrganizationAddress> myOrganizationAddressList = organizationProvider.listOrganizationAddressByOrganizationId(customer.getOrganizationId());
        List<CMContractUnit> apartments = apartmentIdentifier.stream().collect(Collectors.toList());
        LOGGER.debug("insertOrUpdateOrganizationAddresses customer: {}, myOrganizationAddressList: {}, apartments: {}",
                customer.getName(), StringHelper.toJsonString(myOrganizationAddressList), StringHelper.toJsonString(apartments));
        for (OrganizationAddress organizationAddress : myOrganizationAddressList) {
            Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
            if (address != null && address.getNamespaceAddressType() != null && address.getNamespaceAddressToken() != null) {
                if (address.getNamespaceAddressType().equals(NamespaceContractType.RUIAN_CM.getCode())) {
                    for(CMContractUnit identifier : apartmentIdentifier) {
                        if(address.getNamespaceAddressToken().equals(identifier.getUnitID())) {
                            apartments.remove(identifier);
                        }
                    }
                }
            } else {
                deleteOrganizationAddress(organizationAddress);
            }
        }
        LOGGER.debug("insertOrUpdateOrganizationAddresses after remove apartments: {}", StringHelper.toJsonString(apartmentIdentifier));
        if(apartments != null && apartments.size() > 0) {
            apartments.forEach(apartment -> {
                insertOrganizationAddress(apartment, customer);
            });
        }

    }
    
    private void insertOrganizationAddress(CMContractUnit apartmentIdentifier, EnterpriseCustomer customer) {
        if (apartmentIdentifier == null) {
            return;
        }
        Long before = System.currentTimeMillis();
        Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceContractType.RUIAN_CM.getCode(), apartmentIdentifier.getUnitID());
        Long after = System.currentTimeMillis();
        LOGGER.info("address time before index:{}", after-before);
        if (address == null) {
            return;
        }
        Building building = buildingProvider.findBuildingByName(customer.getNamespaceId(), address.getCommunityId(), address.getBuildingName());

        OrganizationAddress organizationAddress = new OrganizationAddress();
        organizationAddress.setOrganizationId(customer.getOrganizationId());
        organizationAddress.setStatus(OrganizationAddressStatus.ACTIVE.getCode());
        organizationAddress.setCreatorUid(1L);
        organizationAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationAddress.setOperatorUid(1L);
        organizationAddress.setUpdateTime(organizationAddress.getCreateTime());
        organizationAddress.setAddressId(address.getId());
        if (building == null) {
            organizationAddress.setBuildingId(0L);
        }else {
            organizationAddress.setBuildingId(building.getId());
            organizationAddress.setBuildingName(building.getName());
        }

        organizationProvider.createOrganizationAddress(organizationAddress);
        // add sync to customer entry info  20180523
        organizationService.updateCustomerEntryInfo(customer, organizationAddress);
    }
    
    private void deleteOrganizationAddress(OrganizationAddress organizationAddress) {
        if (OrganizationAddressStatus.fromCode(organizationAddress.getStatus()) != OrganizationAddressStatus.INACTIVE) {
            organizationAddress.setStatus(OrganizationAddressStatus.INACTIVE.getCode());
            organizationAddress.setOperatorUid(1L);
            organizationAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organizationProvider.updateOrganizationAddress(organizationAddress);
            EnterpriseCustomer customer =  customerProvider.findByOrganizationId(organizationAddress.getOrganizationId());
            if (customer != null) {
                customerProvider.deleteCustomerEntryInfoByCustomerIdAndAddressId(customer.getId(), organizationAddress.getAddressId());
            }
        }
    }
    
    private void updateContract(Contract contract, Long communityId, CMDataObject cmContractData, Long categoryId, Byte contractApplicationScene) {
        contract.setCommunityId(communityId);
        contract.setNamespaceContractType(NamespaceContractType.RUIAN_CM.getCode());
        contract.setNamespaceContractToken(cmContractData.getContractHeader().getRentalID());
        contract.setContractNumber(cmContractData.getContractHeader().getContractNo());
        contract.setName("瑞安合同");
        //contract.setBuildingRename(cmContractData.getBuildingRename());
        
        contract.setCategoryId(categoryId);
        
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getStartDate())) {
            contract.setContractStartDate(dateStrToTimestamp(cmContractData.getContractHeader().getStartDate()));
        }
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getEndDate())) {
            contract.setContractEndDate(dateStrToTimestamp(cmContractData.getContractHeader().getEndDate()));
        }
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getSignDate())) {
            contract.setSignedTime(dateStrToTimestamp(cmContractData.getContractHeader().getSignDate()));
        }
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getTerminateDate())) {
            contract.setDenunciationTime(dateStrToTimestamp(cmContractData.getContractHeader().getTerminateDate()));
        }
        //contract.setVersion(cmContractData.getContractHeader().getVersion());
        if(StringUtils.isNotBlank(cmContractData.getContractHeader().getLFA())) {
            contract.setRentSize(Double.valueOf(cmContractData.getContractHeader().getLFA()));
        }
        contract.setStatus(convertContractStatus(cmContractData.getContractHeader().getRecordstatus()));
        contract.setCustomerName(cmContractData.getContractHeader().getAccountName());
        //contract.setCustomerId(Long.parseLong(cmContractData.getContractHeader().getAccountID()));
        //处理过的客户ID
        contract.setCustomerId(cmContractData.getCustomerId());
        
        contract.setCustomerType(CustomerType.ENTERPRISE.getCode());
        contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        EnterpriseCustomer customer = customerProvider.findByNamespaceToken(NamespaceContractType.RUIAN_CM.getCode(), cmContractData.getContractHeader().getAccountID());
        if(customer != null) {
            //customer.setContactAddress(cmContractData.getBuildingRename());
            customerProvider.updateEnterpriseCustomer(customer);
            insertOrUpdateOrganizationAddresses(cmContractData.getContractUnit(),customer);
            contract.setCustomerId(customer.getId());
        }
         
        contractProvider.updateContract(contract);
        dealContractApartments(contract, cmContractData.getContractUnit());
        contractSearcher.feedDoc(contract);
    }
    
    private void dealContractApartments(Contract contract, List<CMContractUnit> cmContractRoomInfos) {
        List<ContractBuildingMapping> existApartments = contractBuildingMappingProvider.listByContract(contract.getId());
        Map<String, ContractBuildingMapping> map = new HashMap<>();
        if(existApartments != null && existApartments.size() > 0) {
            existApartments.forEach(apartment -> {
                if(apartment.getAddressId() != null) {
                    Address address = addressProvider.findAddressById(apartment.getAddressId());
                    map.put(address.getNamespaceAddressToken(), apartment);
                }

            });
        }

        Double totalSize = 0.0;
        if(cmContractRoomInfos != null && cmContractRoomInfos.size() > 0) {
            for(CMContractUnit cmContractRoomInfo : cmContractRoomInfos) {
                ContractBuildingMapping mapping = map.get(cmContractRoomInfo.getUnitID());
                if(mapping == null) {
                    mapping = new ContractBuildingMapping();
                    mapping.setNamespaceId(contract.getNamespaceId());
                    mapping.setContractId(contract.getId());
                    mapping.setAreaSize(Double.valueOf(cmContractRoomInfo.getLFA()));
                    mapping.setContractNumber(contract.getContractNumber());
                    Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceAddressType.RUIAN_CM.getCode(), cmContractRoomInfo.getUnitID());
                    if(address != null) {
                        mapping.setAddressId(address.getId());
                        mapping.setBuildingName(address.getBuildingName());
                        mapping.setApartmentName(address.getApartmentName());
                    }

                    mapping.setStatus(CommonStatus.ACTIVE.getCode());
                    mapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    contractBuildingMappingProvider.createContractBuildingMapping(mapping);

                    if(address != null) {
                        CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(address.getId());
                        if(addressMapping != null) {
                            addressMapping.setLivingStatus(AddressMappingStatus.OCCUPIED.getCode());
                            propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
                        } else {
                            address.setLivingStatus(AddressMappingStatus.OCCUPIED.getCode());
                            addressProvider.updateAddress(address);
                        }

                    }

                } else {
                    map.remove(cmContractRoomInfo.getUnitID());
                }
            }
        }
        if(map.size() > 0) {
            map.forEach((namespaceAddressToken, apartment) -> {
                contractBuildingMappingProvider.deleteContractBuildingMapping(apartment);

                CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(apartment.getAddressId());
                if(addressMapping != null) {
                    addressMapping.setLivingStatus(AddressMappingStatus.FREE.getCode());
                    propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
                } else {
                    Address address = addressProvider.findAddressById(apartment.getAddressId());
                    address.setLivingStatus(AddressMappingStatus.OCCUPIED.getCode());
                    addressProvider.updateAddress(address);
                }

            });
        }

    }
    
    private Byte convertContractStatus(String ebeiContractStatus) {
        if(ebeiContractStatus != null) {
            switch (ebeiContractStatus) {
                case "已结束": return ContractStatus.HISTORY.getCode();
                case "未提交": return ContractStatus.DRAFT.getCode();
                case "待审批": return ContractStatus.WAITING_FOR_APPROVAL.getCode();
                case "已审批": return ContractStatus.ACTIVE.getCode();
                case "已终止": return ContractStatus.DENUNCIATION.getCode();
                case "已驳回": return ContractStatus.APPROVE_NOT_QUALITIED.getCode();

                default: return null;
            }
        }
        return null;
    }
    
    private <T> List<T> mergeBackupList(List<ZjSyncdataBackup> backupList, Class<T> targetClz) {
        List<T> resultList = new ArrayList<>();
        for (ZjSyncdataBackup syncdataBackup : backupList) {
            if (StringUtils.isNotBlank(syncdataBackup.getData())) {
                List<T> list = JSONObject.parseArray(syncdataBackup.getData(), targetClz);
                if (list != null) {
                    resultList.addAll(list);
                }
            }
        }
        return resultList;
    }
    
    private Timestamp dateStrToTimestamp(String str) {
        LocalDate localDate = LocalDate.parse(str,dateSF);
        Timestamp ts = new Timestamp(Date.valueOf(localDate).getTime());
        return ts;
    }

/*
    private List<CMSyncObject> syncAllEnterprises(Integer namespaceId, String communityIdentifier, List<CMSyncObject> syncObjects) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.EBEI.getCode(), communityIdentifier);
        if(community == null) {
            return null;
        }
        List<EnterpriseCustomer> myEnterpriseCustomerList = customerProvider.listEnterpriseCustomerByNamespaceType(namespaceId, NamespaceCustomerType.CM.getCode(), community.getId());

        //List<EbeiCustomer> mergeEnterpriseList = mergeBackupList(backupList, EbeiCustomer.class);

        LOGGER.debug("syncDataToDb namespaceId: {}, myEnterpriseCustomerList size: {}, theirEnterpriseList size: {}",
                namespaceId, myEnterpriseCustomerList.size(), syncObjects.size());

        syncObjects.forEach(r -> {
            r.getData().forEach(r -> {
                r.getContractHeader().getPropertyID();
            });
        });

        syncAllEnterprises(namespaceId, community.getId(), myEnterpriseCustomerList, contractHeaders);

    }

    //ebei同步数据规则：两次之间所有的改动会同步过来，所以以一碑同步过来数据作为参照：
    // state为0的数据删除，state为1的数据：我们有，更新；我们没有则新增
    private List<CMDataObject> syncAllEnterprises(Integer namespaceId, Long communityId, List<EnterpriseCustomer> myEnterpriseCustomerList, List<CMContractHeader> theirEnterpriseList) {
        if (theirEnterpriseList != null) {
            for (CMContractHeader cmContractHeader : theirEnterpriseList) {

                Boolean notdeal = true;
                if (myEnterpriseCustomerList != null) {
                    for (EnterpriseCustomer enterpriseCustomer : myEnterpriseCustomerList) {
                        if (NamespaceCustomerType.CM.getCode().equals(enterpriseCustomer.getNamespaceCustomerType())
                                && enterpriseCustomer.getNamespaceCustomerToken().equals(cmContractHeader.getAccountID())) {
                            notdeal = false;
                            updateEnterpriseCustomer(enterpriseCustomer, communityId, cmContractHeader);
                        }
                    }
                }
                if(notdeal){
                    // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                    List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId, ebeiCustomer.getCompanyName());
                    if (customers == null || customers.size() == 0) {
                        insertEnterpriseCustomer(NAMESPACE_ID, communityId, ebeiCustomer);
                    } else {
                        updateEnterpriseCustomer(customers.get(0), communityId, ebeiCustomer);
                    }
                }

            }

        }
    }


    private void updateEnterpriseCustomer(EnterpriseCustomer customer, Long communityId, CMContractHeader contractHeader) {
        LOGGER.debug("syncDataToDb updateEnterpriseCustomer customer: {}, ebeiCustomer: {}",
                StringHelper.toJsonString(customer), StringHelper.toJsonString(contractHeader));
//        this.dbProvider.execute((TransactionStatus status) -> {
        customer.setCommunityId(communityId);
        customer.setNamespaceCustomerType(NamespaceCustomerType.CM.getCode());
        customer.setNamespaceCustomerToken(contractHeader.getAccountID());
        customer.setName(contractHeader.getAccountName());
        customer.setNickName(contractHeader.getAccountName());
        customer.setContactName(contractHeader.getConnector());
        customer.setContactPhone(contractHeader.getConnectorPhone());
        customer.setStatus(CommonStatus.ACTIVE.getCode());
        customer.setOperatorUid(1L);
        customer.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        customer.setVersion(ebeiCustomer.getVersion());
//            if(customer.getTrackingUid() == null) {
//                customer.setTrackingUid(-1L);
//            }
        customerProvider.updateEnterpriseCustomer(customer);
        customerSearcher.feedDoc(customer);

        //查找对应的企业账号
        Organization organization = organizationProvider.findOrganizationById(customer.getOrganizationId());
        if (organization == null) {
            organization = insertOrganization(customer);
            customer.setOrganizationId(organization.getId());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        } else if (CommonStatus.fromCode(organization.getStatus()) != CommonStatus.ACTIVE || !organization.getName().equals(customer.getName())) {
            organization.setName(customer.getName());
            organization.setStatus(CommonStatus.ACTIVE.getCode());
            organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organization.setOperatorUid(1L);
            organizationProvider.updateOrganization(organization);
        }

        insertOrUpdateOrganizationDetail(organization, customer);
        insertOrUpdateOrganizationCommunityRequest(communityId, organization);
        insertOrUpdateOrganizationMembers(customer.getNamespaceId(), organization, customer.getContactName(), customer.getContactPhone());
        organizationSearcher.feedDoc(organization);
//            return null;
//        });
    }

*/
}


