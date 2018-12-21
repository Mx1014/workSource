package com.everhomes.customer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.asset.AssetService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.openapi.ZjSyncdataBackup;
import com.everhomes.openapi.ZjSyncdataBackupProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CustomerAptitudeFlag;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.customer.EbeiCustomer;
import com.everhomes.rest.customer.EbeiJsonEntity;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.SyncFlag;
import com.everhomes.rest.organization.DeleteOrganizationIdCommand;
import com.everhomes.rest.organization.NamespaceOrganizationType;
import com.everhomes.rest.organization.OrganizationCommunityRequestStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/11/21.
 */
@Component(CustomerHandle.CUSTOMER_PREFIX + 999983)
public class EbeiCustomerHandle implements CustomerHandle {
    private final static Logger LOGGER = LoggerFactory.getLogger(EbeiCustomerHandle.class);

    private static final String PAGE_SIZE = "20";
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer NAMESPACE_ID = 999983;

    private static final String SYNC_ENTERPRISES = "/rest/LeaseContractChargeInfo/getLeaseContractOwnerInfo";

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private SyncDataTaskProvider syncDataTaskProvider;

    @Autowired
    private AssetService assetService;

    @Override
    public void syncEnterprises(String pageOffset, String version, String communityIdentifier, Long taskId) {
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
        try {
            enterprises = HttpUtils.get(url+SYNC_ENTERPRISES, params, 600, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("sync customer from ebei error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync customer from ebei error");
        }


        EbeiJsonEntity<List<EbeiCustomer>> entity = JSONObject.parseObject(enterprises, new TypeReference<EbeiJsonEntity<List<EbeiCustomer>>>(){});

        if(SUCCESS_CODE.equals(entity.getResponseCode())) {
            List<EbeiCustomer> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.ENTERPRISE.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if(entity.getHasNextPag() == 1) {
                    syncEnterprises(String.valueOf(entity.getCurrentPage()+1), version, communityIdentifier, taskId);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getHasNextPag() == 0) {
                syncDataToDb(DataType.ENTERPRISE.getCode(), communityIdentifier, taskId);
            }
        }
    }

    @Override
    public void syncIndividuals(String pageOffset, String version, String communityIdentifier) {

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

    private void syncDataToDb(Byte dataType, String communityIdentifier, Long taskId) {
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

            //万一同步时间太长transaction断掉 在这里也要更新下
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
            case ENTERPRISE:
                LOGGER.debug("syncDataToDb SYNC ENTERPRISE");
                syncAllEnterprises(namespaceId, communityIdentifier, backupList);
                break;

            default:
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "error data type");
        }
    }

    private void syncAllEnterprises(Integer namespaceId, String communityIdentifier, List<ZjSyncdataBackup> backupList) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.EBEI.getCode(), communityIdentifier);
        if(community == null) {
            return ;
        }
        List<EnterpriseCustomer> myEnterpriseCustomerList = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceType(namespaceId, NamespaceCustomerType.EBEI.getCode(), community.getId());

        List<EbeiCustomer> mergeEnterpriseList = mergeBackupList(backupList, EbeiCustomer.class);

        LOGGER.debug("syncDataToDb namespaceId: {}, myEnterpriseCustomerList size: {}, theirEnterpriseList size: {}",
                namespaceId, myEnterpriseCustomerList.size(), mergeEnterpriseList.size());
        syncAllEnterprises(namespaceId, community.getId(), myEnterpriseCustomerList, mergeEnterpriseList);

    }

    //ebei同步数据规则：两次之间所有的改动会同步过来，所以以一碑同步过来数据作为参照：
    // state为0的数据删除，state为1的数据：我们有，更新；我们没有则新增
    private void syncAllEnterprises(Integer namespaceId, Long communityId, List<EnterpriseCustomer> myEnterpriseCustomerList, List<EbeiCustomer> theirEnterpriseList) {
        if (theirEnterpriseList != null) {
            for (EbeiCustomer ebeiCustomer : theirEnterpriseList) {
                if("0".equals(ebeiCustomer.getState())) {
                    if (myEnterpriseCustomerList != null) {
                        for (EnterpriseCustomer enterpriseCustomer : myEnterpriseCustomerList) {
                            if (NamespaceCustomerType.EBEI.getCode().equals(enterpriseCustomer.getNamespaceCustomerType())
                                    && enterpriseCustomer.getNamespaceCustomerToken().equals(ebeiCustomer.getOwnerId())) {
                                deleteEnterpriseCustomer(enterpriseCustomer);
                            }
                        }
                    }
                } else if("1".equals(ebeiCustomer.getState())) {
                    Boolean notdeal = true;
                    if (myEnterpriseCustomerList != null) {
                        for (EnterpriseCustomer enterpriseCustomer : myEnterpriseCustomerList) {
                            if (NamespaceCustomerType.EBEI.getCode().equals(enterpriseCustomer.getNamespaceCustomerType())
                                    && enterpriseCustomer.getNamespaceCustomerToken().equals(ebeiCustomer.getOwnerId())) {
                                notdeal = false;
                                updateEnterpriseCustomer(enterpriseCustomer, communityId, ebeiCustomer);
                            }
                        }
                    }
                    if(notdeal){
                        // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId, communityId, ebeiCustomer.getCompanyName());
                        if (customers == null || customers.size() == 0) {
                            insertEnterpriseCustomer(NAMESPACE_ID, communityId, ebeiCustomer);
                        } else {
                            updateEnterpriseCustomer(customers.get(0), communityId, ebeiCustomer);
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

    private void insertEnterpriseCustomer(Integer namespaceId, Long communityId, EbeiCustomer ebeiCustomer) {
        LOGGER.debug("syncDataToDb insertEnterpriseCustomer namespaceId: {}, zjEnterprise: {}",
                namespaceId, StringHelper.toJsonString(ebeiCustomer));
//        this.dbProvider.execute((TransactionStatus status) -> {
            EnterpriseCustomer customer = new EnterpriseCustomer();
            customer.setCommunityId(communityId);
            customer.setNamespaceId(namespaceId);
            customer.setAptitudeFlagItemId((long)CustomerAptitudeFlag.APTITUDE.getCode());
            customer.setNamespaceCustomerType(NamespaceCustomerType.EBEI.getCode());
            customer.setNamespaceCustomerToken(ebeiCustomer.getOwnerId());
            customer.setName(ebeiCustomer.getCompanyName());
            customer.setNickName(ebeiCustomer.getCompanyName());
            customer.setContactName(ebeiCustomer.getContactPerson());
            customer.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
            customer.setContactPhone(ebeiCustomer.getContactPhone());
            customer.setStatus(CommonStatus.ACTIVE.getCode());
            customer.setCreatorUid(1L);
//            customer.setTrackingUid(-1L);
            customer.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            customer.setOperatorUid(1L);
            customer.setUpdateTime(customer.getCreateTime());
            customer.setVersion(ebeiCustomer.getVersion());

            //给企业客户创建一个对应的企业账号
            Organization organization = insertOrganization(customer);
            customer.setOrganizationId(organization.getId());
            enterpriseCustomerProvider.createEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);

            insertOrUpdateOrganizationDetail(organization, customer);
            insertOrUpdateOrganizationCommunityRequest(communityId, organization);
            //项目要求不要把联系人同步为管理员 20180125
            insertOrUpdateOrganizationMembers(namespaceId, organization, customer.getContactName(), customer.getContactPhone());
            organizationSearcher.feedDoc(organization);
//            return null;
//        });

    }

    // 需要把同步过来的业务人员添加为我司系统对应组织的管理员
    private void insertOrUpdateOrganizationMembers(Integer namespaceId, Organization organization, String contact, String contactPhone) {
       // #31149
        if (namespaceId == 999983) {
            return;
        }
        if (StringUtils.isBlank(contact) || StringUtils.isBlank(contactPhone) || organization == null) {
            return ;
        }
        try {
            if(contactPhone.contains(",")) {
                contactPhone = contactPhone.split(",")[0];
            }
            User user = new User();
            user.setId(1L);
            UserContext.setCurrentUser(user);
            UserContext.setCurrentNamespaceId(namespaceId);

            CreateOrganizationAdminCommand cmd = new CreateOrganizationAdminCommand();
            cmd.setContactName(contact);
            cmd.setContactToken(contactPhone);
            cmd.setOrganizationId(organization.getId());
            rolePrivilegeService.createOrganizationAdmin(cmd, namespaceId);
        } catch (Exception e) {
            LOGGER.error("sync organization members error: organizationId="+organization.getId()+", contact="+contact+", contactPhone="+contactPhone, e);
        }
    }

    private Organization insertOrganization(EnterpriseCustomer customer) {
        Organization org = organizationProvider.findOrganizationByName(customer.getName(), customer.getNamespaceId());
        if(org != null && OrganizationStatus.ACTIVE.equals(OrganizationStatus.fromCode(org.getStatus()))) {
            return org;
        }
        Organization organization = new Organization();
        organization.setParentId(0L);
        organization.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
        organization.setName(customer.getName());
        organization.setAddressId(0L);
        organization.setPath("");
        organization.setLevel(1);
        organization.setStatus(OrganizationStatus.ACTIVE.getCode());
        organization.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
        organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organization.setUpdateTime(organization.getCreateTime());
        organization.setDirectlyEnterpriseId(0L);
        organization.setNamespaceId(customer.getNamespaceId());
        organization.setShowFlag((byte)1);
        organization.setNamespaceOrganizationType(NamespaceOrganizationType.EBEI.getCode());
        organization.setNamespaceOrganizationToken(customer.getNamespaceCustomerToken());
        organizationProvider.createOrganization(organization);
        assetService.linkCustomerToBill(AssetTargetType.ORGANIZATION.getCode(), organization.getId(), organization.getName());
        return organization;
    }

    private void insertOrUpdateOrganizationDetail(Organization organization, EnterpriseCustomer customer) {
        OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
        if (organizationDetail == null) {
            organizationDetail = new OrganizationDetail();
            organizationDetail.setOrganizationId(organization.getId());
            organizationDetail.setDescription(organization.getDescription());
            organizationDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organizationDetail.setDisplayName(organization.getName());
            organizationDetail.setAddress(customer.getContactAddress());
            organizationProvider.createOrganizationDetail(organizationDetail);
        }else {
            organizationDetail.setOrganizationId(organization.getId());
            organizationDetail.setDescription(organization.getDescription());
            organizationDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organizationDetail.setDisplayName(organization.getName());
            organizationDetail.setAddress(customer.getContactAddress());
            organizationProvider.updateOrganizationDetail(organizationDetail);
        }
        organizationSearcher.feedDoc(organization);
    }

    private void insertOrUpdateOrganizationCommunityRequest(Long communityId, Organization organization) {
        if(communityId != null) {
            OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.findOrganizationCommunityRequestByOrganizationId(communityId, organization.getId());
            if (organizationCommunityRequest == null) {
                organizationCommunityRequest = new OrganizationCommunityRequest();
                organizationCommunityRequest.setCommunityId(communityId);
                organizationCommunityRequest.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
                organizationCommunityRequest.setMemberId(organization.getId());
                organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
                organizationCommunityRequest.setCreatorUid(1L);
                organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                organizationCommunityRequest.setOperatorUid(1L);
                organizationCommunityRequest.setApproveTime(organizationCommunityRequest.getCreateTime());
                organizationCommunityRequest.setUpdateTime(organizationCommunityRequest.getCreateTime());
                organizationProvider.createOrganizationCommunityRequest(organizationCommunityRequest);
            }else {
                organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
                organizationCommunityRequest.setOperatorUid(1L);
                organizationCommunityRequest.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                organizationProvider.updateOrganizationCommunityRequest(organizationCommunityRequest);
            }
        }
    }

    private void deleteEnterpriseCustomer(EnterpriseCustomer customer) {
        LOGGER.debug("syncDataToDb deleteEnterpriseCustomer customer: {}",
                StringHelper.toJsonString(customer));
        if (CommonStatus.fromCode(customer.getStatus()) != CommonStatus.INACTIVE) {
            customer.setOperatorUid(1L);
            customer.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            customer.setStatus(CommonStatus.INACTIVE.getCode());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);
        }

        DeleteOrganizationIdCommand deleteOrganizationIdCommand = new DeleteOrganizationIdCommand();
        deleteOrganizationIdCommand.setId(customer.getOrganizationId());
        organizationService.deleteEnterpriseById(deleteOrganizationIdCommand,false);

    }

    private void updateEnterpriseCustomer(EnterpriseCustomer customer, Long communityId, EbeiCustomer ebeiCustomer) {
        LOGGER.debug("syncDataToDb updateEnterpriseCustomer customer: {}, ebeiCustomer: {}",
                StringHelper.toJsonString(customer), StringHelper.toJsonString(ebeiCustomer));
//        this.dbProvider.execute((TransactionStatus status) -> {
            customer.setCommunityId(communityId);
            customer.setNamespaceCustomerType(NamespaceCustomerType.EBEI.getCode());
            customer.setNamespaceCustomerToken(ebeiCustomer.getOwnerId());
            customer.setName(ebeiCustomer.getCompanyName());
            customer.setNickName(ebeiCustomer.getCompanyName());
            customer.setContactName(ebeiCustomer.getContactPerson());
            customer.setContactPhone(ebeiCustomer.getContactPhone());
            customer.setStatus(CommonStatus.ACTIVE.getCode());
            customer.setOperatorUid(1L);
            customer.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            customer.setVersion(ebeiCustomer.getVersion());
//            if(customer.getTrackingUid() == null) {
//                customer.setTrackingUid(-1L);
//            }
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);

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

}
