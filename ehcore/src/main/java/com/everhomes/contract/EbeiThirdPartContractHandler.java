package com.everhomes.contract;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.openapi.*;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.EbeiJsonEntity;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.SyncFlag;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.search.ContractSearcher;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ContractService contractService;

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

    @Override
    public void syncContractsFromThirdPart(String pageOffset, String version, String communityIdentifier) {
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
            enterprises = HttpUtils.get(url+SYNC_CONTRACTS, params);
        } catch (IOException e) {
            LOGGER.error("sync customer from ebei error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync customer from ebei error");
        }

        EbeiJsonEntity<List<EbeiContract>> entity = JSONObject.parseObject(enterprises, new TypeReference<EbeiJsonEntity<List<EbeiContract>>>(){});

        if(SUCCESS_CODE.equals(entity.getResponseCode())) {
            List<EbeiContract> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.CONTRACT.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if("1".equals(entity.getHasNextPag())) {
                    syncContractsFromThirdPart(String.valueOf(entity.getCurrentPage()+1), version, communityIdentifier);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if("0".equals(entity.getHasNextPag())) {
                syncDataToDb(DataType.CONTRACT.getCode(), communityIdentifier);
            }
        }

    }

    private void syncDataToDb(Byte dataType, String communityIdentifier) {
//        queueThreadPool.execute(()->{
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("dataType {} enter into thread=================", dataType);
//        }

        List<ZjSyncdataBackup> backupList = zjSyncdataBackupProvider.listZjSyncdataBackupByParam(NAMESPACE_ID, communityIdentifier, dataType);

        if (backupList == null || backupList.isEmpty()) {
            LOGGER.debug("syncDataToDb backupList is empty, NAMESPACE_ID: {}, dataType: {}", NAMESPACE_ID, dataType);
            return ;
        }
        try {
            LOGGER.debug("syncDataToDb backupList size：{}", backupList.size());
            updateAllData(dataType, NAMESPACE_ID, communityIdentifier, backupList);
        } finally {
            zjSyncdataBackupProvider.updateZjSyncdataBackupInactive(backupList);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("dataType {} get out thread=================", dataType);
        }
//        });
    }

    private void updateAllData(Byte dataType, Integer namespaceId, String communityIdentifier, List<ZjSyncdataBackup> backupList) {
        DataType ebeiDataType = DataType.fromCode(dataType);
        LOGGER.debug("ebei DataType : {}", ebeiDataType);
        switch (ebeiDataType) {
            case CONTRACT:
                LOGGER.debug("syncDataToDb SYNC CONTRACT");
                syncAllContracts(namespaceId, communityIdentifier, backupList);
                break;

            default:
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "error data type");
        }
    }

    private void syncAllContracts(Integer namespaceId, String communityIdentifier, List<ZjSyncdataBackup> backupList) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.EBEI.getCode(), communityIdentifier);
        if(community == null) {
            return ;
        }
        List<Contract> myContractList = contractProvider.listContractByNamespaceType(namespaceId, NamespaceCustomerType.EBEI.getCode(), community.getId());

        List<EbeiContract> mergeContractList = mergeBackupList(backupList, EbeiContract.class);

        LOGGER.debug("syncDataToDb namespaceId: {}, myContractList size: {}, theirContractList size: {}",
                namespaceId, myContractList.size(), mergeContractList.size());
        dbProvider.execute(s->{
            syncAllContracts(namespaceId, community.getId(), myContractList, mergeContractList);
            return true;
        });
    }

    //ebei同步数据规则：两次之间所有的改动会同步过来，所以以一碑同步过来数据作为参照：
    // state为0的数据删除，state为1的数据：我们有，更新；我们没有则新增
    private void syncAllContracts(Integer namespaceId, Long communityId, List<Contract> myContractList, List<EbeiContract> theirContractList) {
        if (theirContractList != null) {
            for (EbeiContract ebeiContract : theirContractList) {
                if("0".equals(ebeiContract.getState())) {
                    if (myContractList != null) {
                        for (Contract contract : myContractList) {
                            if (NamespaceContractType.EBEI.getCode().equals(contract.getNamespaceContractType())
                                    && contract.getNamespaceContractToken().equals(ebeiContract.getContractId())) {
                                deleteContract(contract);
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
                                updateContract(contract, communityId, ebeiContract);
                            }
                        }
                    }
                    if(notdeal){
                        // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                        Contract contract = contractProvider.findActiveContractByContractNumber(namespaceId, ebeiContract.getSerialNumber());
                        if (contract == null) {
                            insertContract(NAMESPACE_ID, communityId, ebeiContract);
                        }else {
                            updateContract(contract, communityId, ebeiContract);
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

    private void deleteContract(Contract contract) {
        DeleteContractCommand command = new DeleteContractCommand();
        command.setId(contract.getId());
        contractService.deleteContract(command);
    }

    private Timestamp dateStrToTimestamp(String str) {
        LocalDate localDate = LocalDate.parse(str,dateSF);
        Timestamp ts = new Timestamp(Date.valueOf(localDate).getTime());
        return ts;
    }

    private void insertContract(Integer namespaceId, Long communityId, EbeiContract ebeiContract) {
        Contract contract = new Contract();
        contract.setNamespaceId(namespaceId);
        contract.setCommunityId(communityId);
        contract.setNamespaceContractType(NamespaceContractType.EBEI.getCode());
        contract.setNamespaceContractToken(ebeiContract.getContractId());
        contract.setContractNumber(ebeiContract.getSerialNumber());
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
            contract.setCustomerId(customer.getId());
        }
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
                        addressMapping.setLivingStatus(AddressMappingStatus.OCCUPIED.getCode());
                        propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
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
                addressMapping.setLivingStatus(AddressMappingStatus.FREE.getCode());
                propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
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

    private void updateContract(Contract contract, Long communityId, EbeiContract ebeiContract) {
        contract.setCommunityId(communityId);
        contract.setNamespaceContractType(NamespaceContractType.EBEI.getCode());
        contract.setNamespaceContractToken(ebeiContract.getContractId());
        contract.setContractNumber(ebeiContract.getSerialNumber());
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
            contract.setCustomerId(customer.getId());
        }
        contractProvider.updateContract(contract);
        dealContractApartments(contract, ebeiContract.getHouseInfoList());
        contractSearcher.feedDoc(contract);
    }
}
