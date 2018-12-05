package com.everhomes.contract;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
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
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.DeleteContractCommand;
import com.everhomes.rest.contract.EbeiContract;
import com.everhomes.rest.contract.EbeiContractRoomInfo;
import com.everhomes.rest.contract.NamespaceContractType;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.EbeiApartment;
import com.everhomes.rest.customer.EbeiJsonEntity;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.SyncFlag;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestStatus;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.userOrganization.UserOrganizationProvider;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/11/22.
 */
@Component(ThirdPartContractHandler.CONTRACT_PREFIX + 999983)
public class EbeiThirdPartContractHandler implements ThirdPartContractHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(EbeiThirdPartContractHandler.class);

    private static final String PAGE_SIZE = "20";
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer NAMESPACE_ID = 999983;

    private static final String SYNC_CONTRACTS = "/rest/LeaseContractChargeInfo/getLeaseContractInfo";

    private static final String GET_OWNER_STATUS = "/rest/LeaseContractChargeInfo/getOwnerStatus";

    private static final String SYNC_APARTMENT_STATUS = "/rest/LeaseContractChargeInfo/getLeaseContractHouseAddrInfo";

    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private CommunityProvider communityProvider;


    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private EnterpriseCustomerProvider customerProvider;

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
    public void syncContractsFromThirdPart(String pageOffset, String version, String communityIdentifier, Long taskId, Long categoryId, Byte contractApplicationScene) {
        Map<String, String> params= new HashMap<String,String>();
        if(communityIdentifier == null) {
            communityIdentifier = "";
        }
        params.put("projectId", communityIdentifier);
        if(pageOffset == null || "".equals(pageOffset)) {
            pageOffset = "1";
        }
        params.put("currentPage", pageOffset);
        params.put("pageSize", PAGE_SIZE);

        if(version == null || "".equals(version)) {
            version = "0";
        }
        params.put("version", version);

        String enterprises = null;
        String url = configurationProvider.getValue("ebei.url", "");
//        String url = "http://183.62.222.87:5902/sf";
        try {
            enterprises = HttpUtils.get(url+SYNC_CONTRACTS, params, 600, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("sync customer from ebei error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync customer from ebei error");
        }

        EbeiJsonEntity<List<EbeiContract>> entity = JSONObject.parseObject(enterprises, new TypeReference<EbeiJsonEntity<List<EbeiContract>>>(){});

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
        }


    }


    private void getOwnerStatus(String communityIdentifier, String ownerIds) {
        Map<String, String> params= new HashMap<String,String>();
        if(communityIdentifier == null) {
            communityIdentifier = "";
        }
        params.put("projectId", communityIdentifier);
        params.put("ownerIds", ownerIds);

        String ownerStatus = null;
        String url = configurationProvider.getValue("ebei.url", "");
//        String url = "http://183.62.222.87:5902/sf";
        try {
            ownerStatus = HttpUtils.post(url+GET_OWNER_STATUS, params, 600, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("sync owner status from ebei error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync owner status from ebei error");
        }

        EbeiJsonEntity<List<Map<String, Long>>> entity = JSONObject.parseObject(ownerStatus, new TypeReference<EbeiJsonEntity<List<Map<String, Long>>>>(){});

        if(SUCCESS_CODE.equals(entity.getResponseCode())) {
            List<Map<String, Long>> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                dtos.forEach(dto -> {
                    for(String key : dto.keySet()){
                        if(dto.get(key) == 0L) {
                            //删除客户及企业
                            deleteEnterprise(key);
                        }

                    }

                });
            }

        }

    }

    private void deleteEnterprise(String key) {
        EnterpriseCustomer customer = customerProvider.findByNamespaceToken(NamespaceCustomerType.EBEI.getCode(), key);
        if(customer == null) {
            return;
        }
        Organization organization = organizationProvider.findOrganizationById(customer.getOrganizationId());
        if(organization == null) {
            return;
        }

        dbProvider.execute((TransactionStatus status) -> {
            //已删除则直接跳出
            if(OrganizationStatus.DELETED.equals(OrganizationStatus.fromCode(organization.getStatus()))) {
                return null;
            }

            //删客户
            customer.setStatus(CommonStatus.INACTIVE.getCode());
            customerProvider.updateEnterpriseCustomer(customer);

            //删企业
            organization.setStatus(OrganizationStatus.DELETED.getCode());
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            organization.setUpdateTime(now);
            organizationProvider.updateOrganization(organization);

            OrganizationCommunityRequest r = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organization.getId());

            if (null != r) {
                r.setMemberStatus(OrganizationCommunityRequestStatus.INACTIVE.getCode());
                r.setUpdateTime(now);
                organizationProvider.updateOrganizationCommunityRequest(r);
            }

            List<OrganizationMember> members = organizationProvider.listOrganizationMembers(organization.getId(), null);
            //把user_organization表中的相应记录更新为失效
            for (OrganizationMember member : members) {
                UserOrganizations userOrganizations = userOrganizationProvider.findUserOrganizations(member.getNamespaceId(), member.getOrganizationId(), member.getTargetId());
                if (userOrganizations != null) {
                    userOrganizationProvider.inactiveUserOrganizations(userOrganizations);
                }
            }

            List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organization.getId());

            if (null != organizationAddresses && 0 != organizationAddresses.size()) {
                for (OrganizationAddress organizationAddress : organizationAddresses) {
                    organizationAddress.setStatus(OrganizationAddressStatus.INACTIVE.getCode());
                    organizationAddress.setUpdateTime(now);
                    organizationProvider.updateOrganizationAddress(organizationAddress);
                }
            }

            //删除organizaiton时，需要把organizaiton下面的所有机构状态置为无效，而且把人员和机构的关系置为无效状态
            List<Organization> underOrganiztions = organizationProvider.findOrganizationByPath(organization.getPath());
            underOrganiztions.stream().map((o) -> {
                //更新机构
                o.setStatus(OrganizationStatus.INACTIVE.getCode());
                o.setUpdateTime(now);
                organizationProvider.updateOrganization(o);
                //更新人员
                List<OrganizationMember> underOrganiztionsMembers = organizationProvider.listOrganizationMembersByOrgIdWithAllStatus(o.getId());
                for (OrganizationMember m : underOrganiztionsMembers) {
                    m.setStatus(OrganizationMemberStatus.INACTIVE.getCode());
                    m.setUpdateTime(now);
                    m.setOperatorUid(1L);
                    organizationProvider.updateOrganizationMember(m);
                    //解除门禁权限
                    doorAccessService.deleteAuthWhenLeaveFromOrg(NAMESPACE_ID, m.getOrganizationId(), m.getTargetId());
                }
                return null;
            }).collect(Collectors.toList());

            organizationSearcher.deleteById(organization.getId());
            return null;
        });
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
            syncDataTaskService.createSyncErrorMsg(NAMESPACE_ID, taskId);

            //万一同步时间太长transaction断掉 在这里也要更新下
//            SyncDataTask task = syncDataTaskProvider.findSyncDataTaskById(taskId);
//            Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.EBEI.getCode(), communityIdentifier);
//            if(community != null) {
//                SyncDataTask task = syncDataTaskProvider.findExecutingSyncDataTask(community.getId(), SyncDataTaskType.fromName(dataType).getCode());
//
//                if(task != null) {
//                    task.setStatus(SyncDataTaskStatus.FINISH.getCode());
//                    task.setResult("同步成功");
//                    task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                    syncDataTaskProvider.updateSyncDataTask(task);
//                }
//            }

            //同步完要再调一个接口更新客户的状态 暂时不做

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("dataType {} get out thread=================", dataType);
        }
//        });
    }

    private void updateAllData(Byte dataType, Integer namespaceId, String communityIdentifier, List<ZjSyncdataBackup> backupList, Long categoryId, Byte contractApplicationScene) {
        DataType ebeiDataType = DataType.fromCode(dataType);
        LOGGER.debug("ebei DataType : {}", ebeiDataType);
        switch (ebeiDataType) {
            case CONTRACT:
                LOGGER.debug("syncDataToDb SYNC CONTRACT");
                syncAllContracts(namespaceId, communityIdentifier, backupList, categoryId, contractApplicationScene);
                break;
            case APARTMENT_LIVING_STATUS:
                syncApartmentLivingStatus(namespaceId, backupList);
                break;
            default:
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "error data type");
        }
    }

    private void syncAllContracts(Integer namespaceId, String communityIdentifier, List<ZjSyncdataBackup> backupList, Long categoryId, Byte contractApplicationScene) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.EBEI.getCode(), communityIdentifier);
        if(community == null) {
            return ;
        }
        List<Contract> myContractList = contractProvider.listContractByNamespaceType(namespaceId, NamespaceCustomerType.EBEI.getCode(), community.getId(), categoryId);

        List<EbeiContract> mergeContractList = mergeBackupList(backupList, EbeiContract.class);

        LOGGER.debug("syncDataToDb namespaceId: {}, myContractList size: {}, theirContractList size: {}",
                namespaceId, myContractList.size(), mergeContractList.size());
        syncAllContracts(namespaceId, community.getId(), myContractList, mergeContractList, categoryId, contractApplicationScene);

        if (mergeContractList.size() > 0) {
            List<String> ownerIdList = mergeContractList.stream().map(EbeiContract::getOwnerId).collect(Collectors.toList());
            String ownerIds = ownerIdList.toString().substring(1, ownerIdList.toString().length()-1);
            getOwnerStatus(communityIdentifier, ownerIds);
        }
    }

    //ebei同步数据规则：两次之间所有的改动会同步过来，所以以一碑同步过来数据作为参照：
    // state为0的数据删除，state为1的数据：我们有，更新；我们没有则新增
    private void syncAllContracts(Integer namespaceId, Long communityId, List<Contract> myContractList, List<EbeiContract> theirContractList, Long categoryId, Byte contractApplicationScene) {
        if (theirContractList != null) {
            for (EbeiContract ebeiContract : theirContractList) {
                if ("0".equals(ebeiContract.getState())) {
                    if (myContractList != null) {
                        for (Contract contract : myContractList) {
                            if (NamespaceContractType.EBEI.getCode().equals(contract.getNamespaceContractType())
                                    && contract.getNamespaceContractToken().equals(ebeiContract.getContractId())) {
//                                dbProvider.execute((s) -> {
                                    deleteContract(contract,ebeiContract.getHouseInfoList());
//                                    return true;
//                                });
                            }
                        }
                    }
                } else if("1".equals(ebeiContract.getState())) {
                    Boolean notdeal = true;
                    if (myContractList != null) {
                        for (Contract contract : myContractList) {
                            if (NamespaceContractType.EBEI.getCode().equals(contract.getNamespaceContractType())
                                    && contract.getNamespaceContractToken().equals(ebeiContract.getContractId())) {
                                notdeal = false;
//                                dbProvider.execute((s) -> {
                                updateContract(contract, communityId, ebeiContract, categoryId, contractApplicationScene);
//                                    return true;
//                                });
                            }
                        }
                    }
                    if(notdeal){
                        // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                        Contract contract = contractProvider.findActiveContractByContractNumber(namespaceId, ebeiContract.getSerialNumber(), categoryId);
                        if (contract == null) {
//                            dbProvider.execute((s) -> {
                                insertContract(NAMESPACE_ID, communityId, ebeiContract, categoryId);
//                                return true;
//                            });
                        }else {
//                            dbProvider.execute((s) -> {
                                updateContract(contract, communityId, ebeiContract, categoryId, contractApplicationScene);
//                                return true;
//                            });
                        }
                    }
                }
            }
        }
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


    private void syncData(EbeiJsonEntity entity, Byte dataType, String communityIdentifier) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("ebei syncData: dataType: {}, communityIdentifier: {}", dataType, communityIdentifier);
        }

        ZjSyncdataBackup backup = new ZjSyncdataBackup();
        backup.setNamespaceId(NAMESPACE_ID);
        backup.setDataType(dataType);
        if("1".equals(entity.getHasNextPag())) {
            backup.setNextPageOffset(entity.getCurrentPage()+1);
        }

        backup.setData(StringHelper.toJsonString(entity.getData()));
        backup.setStatus(CommonStatus.ACTIVE.getCode());
        backup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        backup.setCreatorUid(1L);
        backup.setUpdateTime(backup.getCreateTime());
        backup.setAllFlag(SyncFlag.PART.getCode());
        backup.setUpdateCommunity(communityIdentifier);

        zjSyncdataBackupProvider.createZjSyncdataBackup(backup);
    }

    private void deleteContract(Contract contract,List<EbeiContractRoomInfo> roomInfos) {
        DeleteContractCommand command = new DeleteContractCommand();
        command.setId(contract.getId());
        command.setCommunityId(contract.getCommunityId());
        command.setNamespaceId(contract.getNamespaceId());
        command.setCheckAuth(false);
        getContractService(contract.getNamespaceId()).deleteContract(command);

        if(contract.getCustomerId() != null) {
            EnterpriseCustomer customer = customerProvider.findById(contract.getCustomerId());
            if(customer != null) {
                customer.setContactAddress("");
                customerProvider.updateEnterpriseCustomer(customer);
                List<OrganizationAddress> myOrganizationAddressList = organizationProvider.listOrganizationAddressByOrganizationId(customer.getOrganizationId());
                List<Long> addressIds = listContractRelatedAddresses(roomInfos);
                if (myOrganizationAddressList != null && addressIds != null && addressIds.size() > 0) {
                    List<OrganizationAddress> organizationAddresses = myOrganizationAddressList
                            .stream().filter((r) -> addressIds.contains(r.getAddressId())).collect(Collectors.toList());
                    for (OrganizationAddress organizationAddress : organizationAddresses) {
                        deleteOrganizationAddress(organizationAddress);
                    }
                }
            }
        }

    }

    private List<Long> listContractRelatedAddresses(List<EbeiContractRoomInfo> roomInfos) {
        List<String> addressIds = new ArrayList<>();
        if(roomInfos!=null && roomInfos.size()>0){
            addressIds =  roomInfos.stream().map(EbeiContractRoomInfo::getInfoId).collect(Collectors.toList());
            return addressProvider.listThirdPartRelatedAddresses(NamespaceAddressType.EBEI.getCode(), addressIds);
        }
        return new ArrayList<>();
    }

    private ContractService getContractService(Integer namespaceId) {
        String handler = configurationProvider.getValue(namespaceId, "contractService", "");
        return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
    }

    private Timestamp dateStrToTimestamp(String str) {
        LocalDate localDate = LocalDate.parse(str,dateSF);
        Timestamp ts = new Timestamp(Date.valueOf(localDate).getTime());
        return ts;
    }

    private void insertContract(Integer namespaceId, Long communityId, EbeiContract ebeiContract, Long categoryId) {
        Contract contract = new Contract();
        contract.setNamespaceId(namespaceId);
        contract.setCommunityId(communityId);
        contract.setNamespaceContractType(NamespaceContractType.EBEI.getCode());
        contract.setNamespaceContractToken(ebeiContract.getContractId());
        contract.setContractNumber(ebeiContract.getSerialNumber());
        contract.setName(ebeiContract.getSerialNumber());
        contract.setBuildingRename(ebeiContract.getBuildingRename());
        contract.setCategoryId(categoryId);
        if(StringUtils.isNotBlank(ebeiContract.getStartDate())) {
            contract.setContractStartDate(dateStrToTimestamp(ebeiContract.getStartDate()));
        }
        if(StringUtils.isNotBlank(ebeiContract.getEndDate())) {
            contract.setContractEndDate(dateStrToTimestamp(ebeiContract.getEndDate()));
        }
        if(StringUtils.isNotBlank(ebeiContract.getSignDate())) {
            contract.setSignedTime(dateStrToTimestamp(ebeiContract.getSignDate()));
        }
        if(StringUtils.isNotBlank(ebeiContract.getQuitDate())) {
            contract.setDenunciationTime(dateStrToTimestamp(ebeiContract.getQuitDate()));
        }
        contract.setVersion(ebeiContract.getVersion());
        if(StringUtils.isNotBlank(ebeiContract.getTotalArea())) {
            contract.setRentSize(Double.valueOf(ebeiContract.getTotalArea()));
        }
        contract.setStatus(convertContractStatus(ebeiContract.getContractStatus()));
        contract.setCustomerName(ebeiContract.getCompanyName());
        contract.setCustomerType(CustomerType.ENTERPRISE.getCode());
        EnterpriseCustomer customer = customerProvider.findByNamespaceToken(NamespaceCustomerType.EBEI.getCode(), ebeiContract.getOwnerId());
        if(customer != null) {
            customer.setContactAddress(ebeiContract.getBuildingRename());
            customerProvider.updateEnterpriseCustomer(customer);
            insertOrUpdateOrganizationAddresses(ebeiContract.getHouseInfoList(),customer);
            contract.setCustomerId(customer.getId());
        }
        contract.setCreateUid(UserContext.currentUserId());
        contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        contractProvider.createContract(contract);
        dealContractApartments(contract, ebeiContract.getHouseInfoList());
        contractSearcher.feedDoc(contract);

    }

    private void dealContractApartments(Contract contract, List<EbeiContractRoomInfo> ebeiContractRoomInfos) {
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
        if(ebeiContractRoomInfos != null && ebeiContractRoomInfos.size() > 0) {
            for(EbeiContractRoomInfo ebeiContractRoomInfo : ebeiContractRoomInfos) {
                ContractBuildingMapping mapping = map.get(ebeiContractRoomInfo.getInfoId());
                if(mapping == null) {
                    mapping = new ContractBuildingMapping();
                    mapping.setNamespaceId(contract.getNamespaceId());
                    mapping.setContractId(contract.getId());
                    mapping.setAreaSize(Double.valueOf(ebeiContractRoomInfo.getPaidArea()));
                    mapping.setContractNumber(contract.getContractNumber());
                    Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceAddressType.EBEI.getCode(), ebeiContractRoomInfo.getInfoId());
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
                            addressMapping.setLivingStatus(AddressMappingStatus.SIGNEDUP.getCode());
                            propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
                        } else {
                            address.setLivingStatus(AddressMappingStatus.SIGNEDUP.getCode());
                            addressProvider.updateAddress(address);
                        }

                    }

                } else {
                    map.remove(ebeiContractRoomInfo.getInfoId());
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
                    address.setLivingStatus(AddressMappingStatus.SIGNEDUP.getCode());
                    addressProvider.updateAddress(address);
                }

            });
        }

    }

    private Byte convertContractStatus(String ebeiContractStatus) {
        if(ebeiContractStatus != null) {
            switch (ebeiContractStatus) {
                case "进行中": return ContractStatus.ACTIVE.getCode();
                case "已终止": return ContractStatus.INVALID.getCode();
                case "已超期": return ContractStatus.EXPIRED.getCode();
                case "已归档": return ContractStatus.HISTORY.getCode();

                default: return null;
            }
        }
        return null;
    }

    private void updateContract(Contract contract, Long communityId, EbeiContract ebeiContract, Long categoryId, Byte contractApplicationScene) {
        contract.setCommunityId(communityId);
        contract.setNamespaceContractType(NamespaceContractType.EBEI.getCode());
        contract.setNamespaceContractToken(ebeiContract.getContractId());
        contract.setContractNumber(ebeiContract.getSerialNumber());
        contract.setName(ebeiContract.getSerialNumber());
        contract.setBuildingRename(ebeiContract.getBuildingRename());
        
        contract.setCategoryId(categoryId);
        
        if(StringUtils.isNotBlank(ebeiContract.getStartDate())) {
            contract.setContractStartDate(dateStrToTimestamp(ebeiContract.getStartDate()));
        }
        if(StringUtils.isNotBlank(ebeiContract.getEndDate())) {
            contract.setContractEndDate(dateStrToTimestamp(ebeiContract.getEndDate()));
        }
        if(StringUtils.isNotBlank(ebeiContract.getSignDate())) {
            contract.setSignedTime(dateStrToTimestamp(ebeiContract.getSignDate()));
        }
        if(StringUtils.isNotBlank(ebeiContract.getQuitDate())) {
            contract.setDenunciationTime(dateStrToTimestamp(ebeiContract.getQuitDate()));
        }
        contract.setVersion(ebeiContract.getVersion());
        if(StringUtils.isNotBlank(ebeiContract.getTotalArea())) {
            contract.setRentSize(Double.valueOf(ebeiContract.getTotalArea()));
        }
        contract.setStatus(convertContractStatus(ebeiContract.getContractStatus()));
        contract.setCustomerName(ebeiContract.getCompanyName());
        contract.setCustomerType(CustomerType.ENTERPRISE.getCode());

        EnterpriseCustomer customer = customerProvider.findByNamespaceToken(NamespaceCustomerType.EBEI.getCode(), ebeiContract.getOwnerId());
        if(customer != null) {

            customer.setContactAddress(ebeiContract.getBuildingRename());
            customerProvider.updateEnterpriseCustomer(customer);
            insertOrUpdateOrganizationAddresses(ebeiContract.getHouseInfoList(),customer);
            contract.setCustomerId(customer.getId());
        }
        contractProvider.updateContract(contract);
        dealContractApartments(contract, ebeiContract.getHouseInfoList());
        contractSearcher.feedDoc(contract);
    }

    private void insertOrUpdateOrganizationAddresses(List<EbeiContractRoomInfo> apartmentIdentifier, EnterpriseCustomer customer){
        List<OrganizationAddress> myOrganizationAddressList = organizationProvider.listOrganizationAddressByOrganizationId(customer.getOrganizationId());
        List<EbeiContractRoomInfo> apartments = apartmentIdentifier.stream().collect(Collectors.toList());
        LOGGER.debug("insertOrUpdateOrganizationAddresses customer: {}, myOrganizationAddressList: {}, apartments: {}",
                customer.getName(), StringHelper.toJsonString(myOrganizationAddressList), StringHelper.toJsonString(apartments));
        for (OrganizationAddress organizationAddress : myOrganizationAddressList) {
            Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
            if (address != null && address.getNamespaceAddressType() != null && address.getNamespaceAddressToken() != null) {
                if (address.getNamespaceAddressType().equals(NamespaceAddressType.EBEI.getCode())) {
                    for(EbeiContractRoomInfo identifier : apartmentIdentifier) {
                        if(address.getNamespaceAddressToken().equals(identifier.getInfoId())) {
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

    private void insertOrganizationAddress(EbeiContractRoomInfo apartmentIdentifier, EnterpriseCustomer customer) {
        if (apartmentIdentifier == null) {
            return;
        }
        Long before = System.currentTimeMillis();
        Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceAddressType.EBEI.getCode(), apartmentIdentifier.getInfoId());
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
            //sync to customer entry info 20180523 by jiarui
            EnterpriseCustomer customer =  customerProvider.findByOrganizationId(organizationAddress.getOrganizationId());
            if (customer != null) {
                customerProvider.deleteCustomerEntryInfoByCustomerIdAndAddressId(customer.getId(), organizationAddress.getAddressId());
            }
        }
    }

    //每天凌晨一点半更新
    @Scheduled(cron = "0 30 1 * * ?")
    public void syncApartmentsLivingStatus() {
        List<Community> communities = communityProvider.listNamespaceCommunities(NAMESPACE_ID);
        if(communities != null && communities.size() > 0) {
            for (Community community : communities) {
                String version = addressProvider.findLastVersionByNamespace(NAMESPACE_ID, community.getId());
                syncUserApartmentsLivingStatus(version, community.getNamespaceCommunityToken(), "1");
            }
        }
    }

    private void syncUserApartmentsLivingStatus(String version, String communityToken, String pageOffset) {

        Map<String, String> params= new HashMap<String,String>();
        if(communityToken == null) {
            communityToken = "";
        }
        params.put("projectId", communityToken);
        if(pageOffset == null || "".equals(pageOffset)) {
            pageOffset = "1";
        }
        params.put("currentPage", pageOffset);
        params.put("pageSize", PAGE_SIZE);

        if(version == null || "".equals(version)) {
            version = "0";
        }
        params.put("version", version);

        String apartments = null;
        String url = configurationProvider.getValue("ebei.url", "");
//        String url = "http://183.62.222.87:5902/sf";
        try {
            apartments = HttpUtils.get(url+SYNC_APARTMENT_STATUS, params, 600, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("sync apartment status from ebei error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync apartment status from ebei error");
        }

        EbeiJsonEntity<List<EbeiApartment>> entity = JSONObject.parseObject(apartments, new TypeReference<EbeiJsonEntity<List<EbeiApartment>>>(){});

        if(SUCCESS_CODE.equals(entity.getResponseCode())) {
            List<EbeiApartment> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.APARTMENT_LIVING_STATUS.getCode(), communityToken);

                //数据有下一页则继续请求
                if(entity.getHasNextPag() == 1) {
                    syncUserApartmentsLivingStatus(version, communityToken, String.valueOf(entity.getCurrentPage()+1));
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getHasNextPag() == 0) {
                syncDataToDb(DataType.APARTMENT_LIVING_STATUS.getCode(), communityToken, null, null, null);
            }
        }
    }

    private void syncApartmentLivingStatus(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        //只更新我们系统中有的门牌的状态，如果在我们系统中找不到则不管
        List<EbeiApartment> theirApartmentList = mergeBackupList(backupList, EbeiApartment.class);
        if(theirApartmentList != null && theirApartmentList.size() > 0) {
            theirApartmentList.forEach(apartment -> {
                Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceAddressType.EBEI.getCode(), apartment.getInfoId());
                if(address != null) {
                    //更新地址表
                    Byte livingStatus = convertEbeiApartmentState(apartment.getState());
                    address.setLivingStatus(livingStatus);
                    addressProvider.updateAddress(address);
                    //更新物业公司-地址映射表
                    CommunityAddressMapping addressMapping = this.organizationProvider.findOrganizationAddressMappingByAddressId(address.getId());
                    if(addressMapping != null){
                        updateOrganizationAddressMapping(addressMapping, address);
                    }

                }
            });

        }
    }

    private Byte convertEbeiApartmentState(Byte state) {
//        1-在租——已租状态,2-待租——未租状态
        if(state == 1) {
            return AddressMappingStatus.RENT.getCode();
        } else if(state == 2) {
            return AddressMappingStatus.FREE.getCode();
        }

        return AddressMappingStatus.DEFAULT.getCode();
    }

    private void updateOrganizationAddressMapping(CommunityAddressMapping organizationAddressMapping, Address address) {
        organizationAddressMapping.setOrganizationAddress(address.getAddress());
        organizationAddressMapping.setLivingStatus(address.getLivingStatus());
        organizationProvider.updateOrganizationAddressMapping(organizationAddressMapping);
    }
}
