package com.everhomes.openapi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.AppProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.customer.IndividualCustomerProvider;
import com.everhomes.customer.SyncDataTaskProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.http.HttpUtils;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.organization.pm.OrganizationOwnerAddress;
import com.everhomes.organization.pm.OrganizationOwnerType;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.address.AddressLivingStatus;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.address.NamespaceBuildingType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.openapi.shenzhou.ApartmentStatusDTO;
import com.everhomes.rest.openapi.shenzhou.CommunityAddressDTO;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.ShenzhouJsonEntity;
import com.everhomes.rest.openapi.shenzhou.SyncFlag;
import com.everhomes.rest.openapi.shenzhou.ZJApartment;
import com.everhomes.rest.openapi.shenzhou.ZJBuilding;
import com.everhomes.rest.openapi.shenzhou.ZJCommunity;
import com.everhomes.rest.openapi.shenzhou.ZJEnterprise;
import com.everhomes.rest.openapi.shenzhou.ZJIndividuals;
import com.everhomes.rest.organization.DeleteOrganizationIdCommand;
import com.everhomes.rest.organization.NamespaceOrganizationType;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressAuthType;
import com.everhomes.rest.organization.pm.OrganizationOwnerStatus;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.techpark.expansion.LeasePromotionStatus;
import com.everhomes.rest.techpark.expansion.LeasePromotionType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.varField.ModuleName;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.PMOwnerSearcher;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryProvider;
import com.everhomes.techpark.expansion.LeasePromotion;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/7.
 */
@Component
public class ZJGKOpenServiceImpl {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZJGKOpenServiceImpl.class);
    private CloseableHttpClient httpclient = null;

    private ExecutorService queueThreadPool = Executors.newFixedThreadPool(1);

    private static ThreadLocal<SimpleDateFormat> simpleDateSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    private static final String PAGE_SIZE = "20";
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer NAMESPACE_ID = 999971;

    private static final String SYNC_COMMUNITIES = "/openapi/asset/syncCommunities";
    private static final String SYNC_BUILDINGS = "/openapi/asset/syncBuildings";
    private static final String SYNC_APARTMENTS = "/openapi/asset/syncApartments";
    private static final String SYNC_ENTERPRISES = "/openapi/customer/syncEnterpriseCustomers";
    private static final String SYNC_INDIVIDUALS = "/openapi/customer/syncUserCustomers";
    private static final String SYNC_USER_APARTMENTS_LIVING_STATUS = "/openapi/asset/syncUserApartmentLivingStatus";
    private static final String SYNC_ENTERPRISE_APARTMENTS_LIVING_STATUS = "/openapi/asset/syncEnterpriseApartmentLivingStatus";

    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private SyncDataTaskProvider syncDataTaskProvider;

    @Autowired
    private AppProvider appProvider;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private RegionProvider regionProvider;

    @Autowired
    private BuildingProvider buildingProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private CommunitySearcher communitySearcher;

    @Autowired
    private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;

    @Autowired
    private NamespaceResourceProvider namespaceResourceProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ThirdpartConfigurationProvider thirdpartConfigurationProvider;

    @Autowired
    private IndividualCustomerProvider individualCustomerProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private PMOwnerSearcher pmOwnerSearcher;

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private AssetService assetService;

    public void syncData() {
//        Map<String, String> params = generateParams();
//        String communities = postToShenzhou(params, SYNC_COMMUNITIES, null);
//        String buildings = postToShenzhou(params, SYNC_BUILDINGS, null);
//        String apartments = postToShenzhou(params, SYNC_APARTMENTS, null);
//        String enterprises = postToShenzhou(params, SYNC_ENTERPRISES, null);
//        String individuals = postToShenzhou(params, SYNC_INDIVIDUALS, null);
//        syncCommunities("0");
//        syncBuildings("0");
//        syncApartments("0");
//        syncEnterprises("0");
    }

    public void syncCommunities(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String communities = postToShenzhou(params, SYNC_COMMUNITIES, null);

        ShenzhouJsonEntity<List<ZJCommunity>> entity = JSONObject.parseObject(communities, new TypeReference<ShenzhouJsonEntity<List<ZJCommunity>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ZJCommunity> dtos = entity.getResponse();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.COMMUNITY.getCode(), null);

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncCommunities(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.COMMUNITY.getCode(), SyncFlag.ALL.getCode(), 0L);
            }
        }
    }

    public void syncBuildings(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String buildings = postToShenzhou(params, SYNC_BUILDINGS, null);

        ShenzhouJsonEntity<List<ZJBuilding>> entity = JSONObject.parseObject(buildings, new TypeReference<ShenzhouJsonEntity<List<ZJBuilding>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ZJBuilding> dtos = entity.getResponse();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.BUILDING.getCode(), null);

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncBuildings(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.BUILDING.getCode(), SyncFlag.ALL.getCode(), 0L);
            }
        }
    }

    public void syncApartments(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String apartments = postToShenzhou(params, SYNC_APARTMENTS, null);

        ShenzhouJsonEntity<List<ZJApartment>> entity = JSONObject.parseObject(apartments, new TypeReference<ShenzhouJsonEntity<List<ZJApartment>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ZJApartment> dtos = entity.getResponse();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.APARTMENT.getCode(), null);

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncApartments(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.APARTMENT.getCode(), SyncFlag.ALL.getCode(), 0L);
            }
        }
    }

    //每天凌晨一点更新
    @Scheduled(cron = "0 0 1 * * ?")
    public void syncApartmentsLivingStatus() {
//        syncEnterpriseApartmentsLivingStatus("0");
        syncUserApartmentsLivingStatus("0");

    }

    private void syncEnterpriseApartmentsLivingStatus(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        Long begin = DateHelper.currentGMTTime().getTime();
        String apartments = postToShenzhou(params, SYNC_ENTERPRISE_APARTMENTS_LIVING_STATUS, null);
        Long end = DateHelper.currentGMTTime().getTime();
        Long els = end-begin;
        ShenzhouJsonEntity<List<ApartmentStatusDTO>> entity = JSONObject.parseObject(apartments, new TypeReference<ShenzhouJsonEntity<List<ApartmentStatusDTO>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ApartmentStatusDTO> dtos = entity.getResponse();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.APARTMENT_LIVING_STATUS.getCode(), null);

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncEnterpriseApartmentsLivingStatus(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.APARTMENT_LIVING_STATUS.getCode(), SyncFlag.ALL.getCode(), 0L);
            }
        }
    }

    private void syncUserApartmentsLivingStatus(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        Long begin = DateHelper.currentGMTTime().getTime();
        String apartments = postToShenzhou(params, SYNC_USER_APARTMENTS_LIVING_STATUS, null);
        Long end = DateHelper.currentGMTTime().getTime();
        Long els = end-begin;
        ShenzhouJsonEntity<List<ApartmentStatusDTO>> entity = JSONObject.parseObject(apartments, new TypeReference<ShenzhouJsonEntity<List<ApartmentStatusDTO>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ApartmentStatusDTO> dtos = entity.getResponse();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.APARTMENT_LIVING_STATUS.getCode(), null);

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncUserApartmentsLivingStatus(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.APARTMENT_LIVING_STATUS.getCode(), SyncFlag.ALL.getCode(), 0L);
            }
        }
    }

    public void syncEnterprises(String pageOffset, String communityIdentifier, Long taskId) {
        String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
        String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");

        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        params.put("crypto", "sssss");
        if(communityIdentifier == null) {
            communityIdentifier = "";
        }
        params.put("communityIdentifier", communityIdentifier);
        if(pageOffset == null || "".equals(pageOffset)) {
            pageOffset = "0";
        }
        params.put("pageOffset", pageOffset);
        params.put("pageSize", PAGE_SIZE);
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);
        String enterprises = postToShenzhou(params, SYNC_ENTERPRISES, null);

        ShenzhouJsonEntity<List<ZJEnterprise>> entity = JSONObject.parseObject(enterprises, new TypeReference<ShenzhouJsonEntity<List<ZJEnterprise>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ZJEnterprise> dtos = entity.getResponse();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.ENTERPRISE.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncEnterprises(entity.getNextPageOffset().toString(), communityIdentifier, taskId);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                if(communityIdentifier == null || "".equals(communityIdentifier)) {
                    syncDataToDb(DataType.ENTERPRISE.getCode(), SyncFlag.ALL.getCode(), taskId);
                } else {
                    syncDataToDb(DataType.ENTERPRISE.getCode(), SyncFlag.PART.getCode(), taskId);
                }
            }
        }
    }

    public void syncIndividuals(String pageOffset, String communityIdentifier, Long taskId) {
        String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
        String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");

        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        params.put("crypto", "sssss");
        if(communityIdentifier == null) {
            communityIdentifier = "";
        }
        params.put("communityIdentifier", communityIdentifier);
//        params.put("pageOffset", "0");
        params.put("pageOffset", pageOffset);
        params.put("pageSize", PAGE_SIZE);
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);
        String individuals = postToShenzhou(params, SYNC_INDIVIDUALS, null);

        ShenzhouJsonEntity<List<ZJIndividuals>> entity = JSONObject.parseObject(individuals, new TypeReference<ShenzhouJsonEntity<List<ZJIndividuals>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ZJIndividuals> dtos = entity.getResponse();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.INDIVIDUAL.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncIndividuals(entity.getNextPageOffset().toString(), communityIdentifier, taskId);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                if(communityIdentifier == null || "".equals(communityIdentifier)) {
                    syncDataToDb(DataType.INDIVIDUAL.getCode(), SyncFlag.ALL.getCode(), taskId);
                } else {
                    syncDataToDb(DataType.INDIVIDUAL.getCode(), SyncFlag.PART.getCode(), taskId);
                }
            }
        }
    }


    private Map<String, String> generateParams(String pageOffset) {
        String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
        String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        params.put("crypto", "sssss");
        params.put("pageOffset", pageOffset);
        params.put("pageSize", PAGE_SIZE);
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);

        return params;
    }

    private void syncData(ShenzhouJsonEntity entity, Byte dataType, String communityIdentifier) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("zj syncData: dataType: {}, communityIdentifier: {}", dataType, communityIdentifier);
        }

        ZjSyncdataBackup backup = new ZjSyncdataBackup();
        backup.setNamespaceId(NAMESPACE_ID);
        backup.setDataType(dataType);
        backup.setNextPageOffset(entity.getNextPageOffset());
        backup.setData(StringHelper.toJsonString(entity.getResponse()));
        backup.setStatus(CommonStatus.ACTIVE.getCode());
        backup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        backup.setCreatorUid(1L);
        backup.setUpdateTime(backup.getCreateTime());

        if(communityIdentifier == null) {
            backup.setAllFlag(SyncFlag.ALL.getCode());
        } else {
            backup.setAllFlag(SyncFlag.PART.getCode());
            backup.setUpdateCommunity(communityIdentifier);
        }

        zjSyncdataBackupProvider.createZjSyncdataBackup(backup);

    }

    private void syncDataToDb(Byte dataType, Byte allFlag, Long taskId) {
//        queueThreadPool.execute(()->{
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("dataType {} enter into thread=================", dataType);
            }

            List<ZjSyncdataBackup> backupList = zjSyncdataBackupProvider.listZjSyncdataBackupByParam(NAMESPACE_ID, dataType);

            if (backupList == null || backupList.isEmpty()) {
                LOGGER.debug("syncDataToDb backupList is empty, NAMESPACE_ID: {}, dataType: {}", NAMESPACE_ID, dataType);
                return ;
            }
            try {
                LOGGER.debug("syncDataToDb backupList size：{}", backupList.size());
                updateAllDate(dataType, allFlag, NAMESPACE_ID , backupList);
            } finally {
                zjSyncdataBackupProvider.updateZjSyncdataBackupInactive(backupList);

                //万一同步时间太长transaction断掉 在这里也要更新下
//                if(SyncFlag.PART.equals(SyncFlag.fromCode(allFlag))) {
//                    String communityIdentifier = backupList.get(0).getUpdateCommunity();
//                    Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.SHENZHOU.getCode(), communityIdentifier);
//                    if(community != null) {
//                        SyncDataTask task = syncDataTaskProvider.findExecutingSyncDataTask(community.getId(), SyncDataTaskType.fromName(dataType).getCode());
//
//                        if(task != null) {
//                            task.setStatus(SyncDataTaskStatus.FINISH.getCode());
//                            task.setResult("同步成功");
//                            task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                            syncDataTaskProvider.updateSyncDataTask(task);
//                        }
//                    }
//                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("dataType {} get out thread=================", dataType);
            }
//        });
    }

    private String postToShenzhou(Map<String, String> params, String method, Map<String, String> headers) {
        String shenzhouUrl = configurationProvider.getValue(NAMESPACE_ID, "shenzhou.host.url", "");
        String json = null;

        try {
            Long beforeRequest = System.currentTimeMillis();
            json = HttpUtils.postJson(shenzhouUrl + method, StringHelper.toJsonString(params), 30, HTTP.UTF_8);
            Long afterRequest = System.currentTimeMillis();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("request shenzhou url: {}, total elapse: {}", shenzhouUrl + method, afterRequest-beforeRequest);
            }
        } catch (Exception e) {
            LOGGER.error("sync from shenzhou request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "sync from shenzhou request error.");
        }
        return json;
    }

    //基本思路：来自神州数码的数据：在我们数据中不在同步数据里的，删除；两边都在的，更新；不在我们数据库中，在同步数据里的，插入；
    private void updateAllDate(Byte dataType, Byte allFlag, Integer namespaceId,
                               List<ZjSyncdataBackup> backupList) {
        DataType zjDataType = DataType.fromCode(dataType);
        LOGGER.debug("zjDataType : {}", zjDataType);
        switch (zjDataType) {
            case COMMUNITY:
                syncAllCommunities(namespaceId, backupList);
                break;
            case BUILDING:
			    syncAllBuildings(namespaceId, backupList);
                break;
            case APARTMENT:
                syncAllApartments(namespaceId, backupList);
                break;
            case ENTERPRISE:
                LOGGER.debug("syncDataToDb SYNC ENTERPRISE");
                syncAllEnterprises(namespaceId, backupList, allFlag);
                break;
            case APARTMENT_LIVING_STATUS:
                syncApartmentLivingStatus(namespaceId, backupList);
                break;
            case INDIVIDUAL:
                LOGGER.debug("syncDataToDb SYNC INDIVIDUAL");
                syncAllIndividuals(namespaceId, backupList, allFlag);
                break;

            default:
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "error data type");
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

    private void syncAllCommunities(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        List<Community> myCommunityList = communityProvider.listCommunityByNamespaceType(namespaceId, NamespaceCommunityType.SHENZHOU.getCode());
        List<ZJCommunity> theirCommunityList = mergeBackupList(backupList, ZJCommunity.class);
        dbProvider.execute(s->{
            syncAllCommunities(namespaceId, myCommunityList, theirCommunityList);
            return true;
        });
    }

    private void syncAllCommunities(Integer namespaceId, List<Community> myCommunityList, List<ZJCommunity> theirCommunityList) {
        Long defaultForumId = 1L;
        ThirdpartConfiguration defaultForum = thirdpartConfigurationProvider.findByName("default.forum.id", namespaceId);
        if(defaultForum != null) {
            defaultForumId = Long.valueOf(defaultForum.getValue());
        }
        Long feedbackForumId = 2L;
        ThirdpartConfiguration feedbackForum = thirdpartConfigurationProvider.findByName("feedback.forum.id", namespaceId);
        if(feedbackForum != null) {
            feedbackForumId = Long.valueOf(feedbackForum.getValue());
        }
        Long organizationId = 0L;
        ThirdpartConfiguration organization = thirdpartConfigurationProvider.findByName("organization", namespaceId);
        if(organization != null) {
            organizationId = Long.valueOf(organization.getValue());
        }
        // 如果两边都有，更新；如果我们有，他们没有，删除；
        for (Community myCommunity : myCommunityList) {
            ZJCommunity zjCommunity = findFromTheirCommunityList(myCommunity, theirCommunityList);
            if (zjCommunity != null) {
                updateCommunity(myCommunity, zjCommunity);
            }else {
                deleteCommunity(myCommunity);
            }
        }
        // 如果他们有，我们没有，插入
        // 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
        if (theirCommunityList != null) {
            for (ZJCommunity zjCommunity : theirCommunityList) {
                if ((zjCommunity.getDealed() != null && zjCommunity.getDealed().booleanValue() == true)) {
                    continue;
                }
                // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                List<Community> community = communityProvider.listCommunityByNamespaceIdAndName(NAMESPACE_ID, zjCommunity.getCommunityName());
                if (community == null) {
                    insertCommunity(NAMESPACE_ID, zjCommunity, defaultForumId, feedbackForumId, organizationId);
                }else {
                    updateCommunity(community.get(0), zjCommunity);
                }
            }
        }
    }

    private ZJCommunity findFromTheirCommunityList(Community myCommunity, List<ZJCommunity> theirCommunityList) {
        if (theirCommunityList != null) {
            for (ZJCommunity zjCommunity : theirCommunityList) {
                if (NamespaceCommunityType.SHENZHOU.getCode().equals(myCommunity.getNamespaceCommunityType())
                        && myCommunity.getNamespaceCommunityToken().equals(zjCommunity.getCommunityIdentifier())) {
                    zjCommunity.setDealed(true);
                    return zjCommunity;
                }
            }
        }
        return null;
    }

    private void insertCommunity(Integer namespaceId, ZJCommunity zjcommunity, Long defaultForumId, Long feedbackForumId, Long organizationId) {
        if (StringUtils.isBlank(zjcommunity.getCommunityIdentifier())) {
            return;
        }
        Community community = new Community();
        community.setName(zjcommunity.getCommunityName());
        community.setCommunityType(zjcommunity.getCommunityType());
        community.setNamespaceId(namespaceId);
        community.setAddress(zjcommunity.getAddress());
        community.setZipcode(zjcommunity.getZipcode());
        community.setDescription(zjcommunity.getDescription());
        community.setAptCount(zjcommunity.getAptCount());
        community.setNamespaceCommunityToken(zjcommunity.getCommunityIdentifier());
        community.setNamespaceCommunityType(NamespaceCommunityType.SHENZHOU.getCode());
        community.setAreaSize(zjcommunity.getAreaSize());
        community.setChargeArea(zjcommunity.getChargeArea());
        community.setSharedArea(zjcommunity.getSharedArea());
        community.setRentArea(zjcommunity.getRentArea());
        community.setBuildArea(zjcommunity.getBuildArea());

        Region city = regionProvider.findRegionByName(namespaceId, zjcommunity.getCityName());
        community.setCityId(city.getId());
        community.setCityName(city.getName());
        Region area = regionProvider.findRegionByName(namespaceId, zjcommunity.getAreaName());
        community.setAreaId(area.getId());
        community.setAreaName(area.getName());

        community.setDefaultForumId(defaultForumId);
        community.setFeedbackForumId(feedbackForumId);
        community.setStatus(CommonStatus.ACTIVE.getCode());
        communityProvider.createCommunity(1L, community);
        communitySearcher.feedDoc(community);

        CommunityGeoPoint geoPoint = new CommunityGeoPoint();
        geoPoint.setCommunityId(community.getId());
        geoPoint.setLatitude(zjcommunity.getLatitude());
        geoPoint.setLongitude(zjcommunity.getLongitude());
        String geohash= GeoHashUtils.encode(zjcommunity.getLatitude(), zjcommunity.getLongitude());
        geoPoint.setGeohash(geohash);
        communityProvider.createCommunityGeoPoint(geoPoint);

        OrganizationCommunity departmentCommunity = new OrganizationCommunity();
        departmentCommunity.setCommunityId(community.getId());
        departmentCommunity.setOrganizationId(organizationId);
        organizationProvider.createOrganizationCommunity(departmentCommunity);

        NamespaceResource resource = new NamespaceResource();
        resource.setNamespaceId(namespaceId);
        resource.setResourceType(NamespaceResourceType.COMMUNITY.getCode());
        resource.setResourceId(community.getId());
        namespaceResourceProvider.createNamespaceResource(resource);
    }

    private void deleteCommunity(Community community) {
        if (CommonStatus.fromCode(community.getStatus()) != CommonStatus.INACTIVE) {
            community.setOperatorUid(1L);
            community.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            community.setStatus(CommonStatus.INACTIVE.getCode());
            communityProvider.updateCommunity(community);
            communitySearcher.feedDoc(community);
        }

        List<OrganizationCommunity> communityOrgs = organizationProvider.listOrganizationByCommunityId(community.getId());
        if(communityOrgs != null && communityOrgs.size() > 0) {
            for(OrganizationCommunity orgCommunity : communityOrgs) {
                organizationProvider.deleteOrganizationCommunity(orgCommunity);
            }
        }

        List<NamespaceResource> namespaceResources = namespaceResourceProvider.listResourceByNamespace(NAMESPACE_ID, NamespaceResourceType.COMMUNITY, community.getId());
        if(namespaceResources != null && namespaceResources.size() > 0) {
            for(NamespaceResource resource : namespaceResources) {
                namespaceResourceProvider.deleteNamespaceResource(resource);
            }
        }

    }

    private void updateCommunity(Community community, ZJCommunity zjcommunity) {
        community.setOperatorUid(1L);
        community.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        community.setName(zjcommunity.getCommunityName());
        community.setCommunityType(zjcommunity.getCommunityType());
        community.setAddress(zjcommunity.getAddress());
        community.setZipcode(zjcommunity.getZipcode());
        community.setDescription(zjcommunity.getDescription());
        community.setAptCount(zjcommunity.getAptCount());
        community.setNamespaceCommunityToken(zjcommunity.getCommunityIdentifier());
        community.setAreaSize(zjcommunity.getAreaSize());
        community.setChargeArea(zjcommunity.getChargeArea());
        community.setSharedArea(zjcommunity.getSharedArea());
        community.setRentArea(zjcommunity.getRentArea());
        community.setBuildArea(zjcommunity.getBuildArea());

        Region city = regionProvider.findRegionByName(NAMESPACE_ID, zjcommunity.getCityName());
        community.setCityId(city.getId());
        community.setCityName(city.getName());
        Region area = regionProvider.findRegionByName(NAMESPACE_ID, zjcommunity.getAreaName());
        community.setAreaId(area.getId());
        community.setAreaName(area.getName());

        community.setStatus(CommonStatus.ACTIVE.getCode());
        communityProvider.updateCommunity(community);
        communitySearcher.feedDoc(community);

        CommunityGeoPoint geoPoint = communityProvider.findCommunityGeoPointByCommunityId(community.getId());
        if(geoPoint != null) {
            geoPoint.setLatitude(zjcommunity.getLatitude());
            geoPoint.setLongitude(zjcommunity.getLongitude());
            String geohash= GeoHashUtils.encode(zjcommunity.getLatitude(), zjcommunity.getLongitude());
            geoPoint.setGeohash(geohash);
            communityProvider.updateCommunityGeoPoint(geoPoint);
        } else {
            geoPoint = new CommunityGeoPoint();
            geoPoint.setCommunityId(community.getId());
            geoPoint.setLatitude(zjcommunity.getLatitude());
            geoPoint.setLongitude(zjcommunity.getLongitude());
            String geohash= GeoHashUtils.encode(zjcommunity.getLatitude(), zjcommunity.getLongitude());
            geoPoint.setGeohash(geohash);
            communityProvider.createCommunityGeoPoint(geoPoint);
        }

    }

    private void syncAllBuildings(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        dbProvider.execute(s->{
            //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
            List<Building> myBuildingList = buildingProvider.listBuildingByNamespaceType(namespaceId, NamespaceBuildingType.SHENZHOU.getCode());
            List<ZJBuilding> mergeBuildingList = mergeBackupList(backupList, ZJBuilding.class);
            List<ZJBuilding> theirBuildingList = new ArrayList<ZJBuilding>();
            if(mergeBuildingList != null && mergeBuildingList.size() > 0) {
                Map<String, Long> communities = communityProvider.listCommunityIdByNamespaceType(namespaceId, NamespaceBuildingType.SHENZHOU.getCode());
                mergeBuildingList.forEach(building -> {
                    Long communityId = communities.get(building.getCommunityIdentifier());
                    if(communityId != null) {
                        building.setCommunityId(communityId);
                    }
                    theirBuildingList.add(building);
                });
            }
            syncAllBuildings(namespaceId, myBuildingList, theirBuildingList);
            return true;
        });

    }

    private void syncAllBuildings(Integer namespaceId, List<Building> myBuildingList, List<ZJBuilding> theirBuildingList) {
        // 如果两边都有，更新；如果我们有，他们没有，删除；
        for (Building myBuilding : myBuildingList) {
            ZJBuilding customerBuilding = findFromTheirBuildingList(myBuilding, theirBuildingList);
            if (customerBuilding != null) {
                updateBuilding(myBuilding, customerBuilding);
            }else {
                deleteBuilding(myBuilding);
            }
        }
        // 如果他们有，我们没有，插入
        // 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
        if (theirBuildingList != null) {
            for (ZJBuilding customerBuilding : theirBuildingList) {
                if ((customerBuilding.getDealed() != null && customerBuilding.getDealed().booleanValue() == true) || StringUtils.isBlank(customerBuilding.getBuildingName())) {
                    continue;
                }
                // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                Building building = buildingProvider.findBuildingByName(namespaceId, customerBuilding.getCommunityId(), customerBuilding.getBuildingName());
                if (building == null) {
                    insertBuilding(namespaceId, customerBuilding);
                }else {
                    updateBuilding(building, customerBuilding);
                }
            }
        }
    }

    private void insertBuilding(Integer namespaceId, ZJBuilding customerBuilding) {
        if (StringUtils.isBlank(customerBuilding.getBuildingName())) {
            return;
        }
        Building building = new Building();

        building.setCommunityId(customerBuilding.getCommunityId());
        building.setName(customerBuilding.getBuildingName());
        building.setAliasName(customerBuilding.getBuildingName());
        building.setStatus(CommonStatus.ACTIVE.getCode());
        building.setCreatorUid(1L);
        building.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        building.setOperatorUid(1L);
        building.setOperateTime(building.getCreateTime());
        building.setNamespaceId(namespaceId);
        building.setContact(customerBuilding.getContact());
        building.setAddress(customerBuilding.getAddress());
        building.setConstructionCompany(customerBuilding.getConstructionCompany());
        building.setDescription(customerBuilding.getDescription());
        building.setAreaSize(customerBuilding.getAreaSize());
        building.setSharedArea(customerBuilding.getSharedArea());
        building.setChargeArea(customerBuilding.getChargeArea());
        building.setBuildArea(customerBuilding.getBuildArea());
        building.setRentArea(customerBuilding.getRentArea());
        if(customerBuilding.getFloorCount() != null) {
            building.setFloorCount(String.valueOf(customerBuilding.getFloorCount()));
        }
        building.setNamespaceBuildingType(NamespaceBuildingType.SHENZHOU.getCode());
        building.setNamespaceBuildingToken(customerBuilding.getBuildingIdentifier());
        buildingProvider.createBuilding(building);
    }

    private void deleteBuilding(Building building) {
        if (CommonStatus.fromCode(building.getStatus()) != CommonStatus.INACTIVE) {
            building.setOperatorUid(1L);
            building.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            building.setStatus(CommonStatus.INACTIVE.getCode());
            buildingProvider.updateBuilding(building);
        }
    }

    private void updateBuilding(Building building, ZJBuilding customerBuilding) {
        building.setName(customerBuilding.getBuildingName());
        building.setAliasName(customerBuilding.getBuildingName());
        building.setContact(customerBuilding.getContact());
        building.setAddress(customerBuilding.getAddress());
        building.setConstructionCompany(customerBuilding.getConstructionCompany());
        building.setDescription(customerBuilding.getDescription());
        building.setAreaSize(customerBuilding.getAreaSize());
        building.setSharedArea(customerBuilding.getSharedArea());
        building.setChargeArea(customerBuilding.getChargeArea());
        building.setBuildArea(customerBuilding.getBuildArea());
        building.setRentArea(customerBuilding.getRentArea());
        if(customerBuilding.getFloorCount() != null) {
            building.setFloorCount(String.valueOf(customerBuilding.getFloorCount()));
        }

        building.setOperatorUid(1L);
        building.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        building.setNamespaceBuildingToken(customerBuilding.getBuildingIdentifier());
        building.setStatus(CommonStatus.ACTIVE.getCode());
        buildingProvider.updateBuilding(building);

    }

    private ZJBuilding findFromTheirBuildingList(Building myBuilding, List<ZJBuilding> theirBuildingList) {
        if (theirBuildingList != null) {
            for (ZJBuilding customerBuilding : theirBuildingList) {
                if (NamespaceBuildingType.SHENZHOU.getCode().equals(myBuilding.getNamespaceBuildingType())
                && myBuilding.getNamespaceBuildingToken().equals(customerBuilding.getBuildingIdentifier())) {
                    customerBuilding.setDealed(true);
                    return customerBuilding;
                }
            }
        }
        return null;
    }
    private void syncApartmentLivingStatus(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        //只更新我们系统中有的门牌的状态，如果在我们系统中找不到则不管
        List<ApartmentStatusDTO> theirApartmentList = mergeBackupList(backupList, ApartmentStatusDTO.class);
        if(theirApartmentList != null && theirApartmentList.size() > 0) {
            ThirdpartConfiguration organization = thirdpartConfigurationProvider.findByName("organization", namespaceId);
            if(organization == null) {
                organization = new ThirdpartConfiguration();
                organization.setValue("0");
            }
            Long organizationId = Long.valueOf(organization.getValue());
            theirApartmentList.forEach(apartment -> {
                Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceAddressType.SHENZHOU.getCode(), apartment.getApartmentIdentifier());
                if(address != null) {
                    //更新地址表
                    address.setLivingStatus(apartment.getLivingStatus());
                    addressProvider.updateAddress(address);
                    //更新物业公司-地址映射表
                    CommunityAddressMapping organizationAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
                    if (organizationAddressMapping != null) {
                        updateOrganizationAddressMapping(organizationAddressMapping, address);
                    }
                    //新增或更新招租信息
                    insertOrUpdateLeasePromotion(address.getLivingStatus(), address.getNamespaceId(), address.getCommunityId(), address.getRentArea(), address.getBuildingName(), address.getApartmentName());
                }
            });

        }
    }

    private void syncAllApartments(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        //楼栋的同步按照从门牌中提取来同步
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        List<Address> myApartmentList = addressProvider.listAddressByNamespaceType(namespaceId, NamespaceAddressType.SHENZHOU.getCode());
        List<ZJApartment> mergeApartmentList = mergeBackupList(backupList, ZJApartment.class);
        List<ZJApartment> theirApartmentList = new ArrayList<ZJApartment>();
        if(mergeApartmentList != null && mergeApartmentList.size() > 0) {
            Map<String, Long> communities = communityProvider.listCommunityIdByNamespaceType(namespaceId, NamespaceCommunityType.SHENZHOU.getCode());
            mergeApartmentList.forEach(apartment -> {
                Long communityId = communities.get(apartment.getCommunityIdentifier());
                if(communityId != null) {
                    apartment.setCommunityId(communityId);
                }
                theirApartmentList.add(apartment);
            });
        }
        dbProvider.execute(s->{
            syncAllApartments(namespaceId, myApartmentList, theirApartmentList);
            // 同步完地址后更新community表中的门牌总数
            List<Community> communities = communityProvider.listCommunityByNamespaceType(namespaceId, NamespaceBuildingType.SHENZHOU.getCode());
            if(communities != null && communities.size() > 0) {
                communities.forEach(community -> {
                    Integer count = addressProvider.countApartment(community.getId());
                    if (community.getAptCount().intValue() != count.intValue()) {
                        community.setAptCount(count);
                        communityProvider.updateCommunity(community);
                    }
                });
            }
            return true;
        });
    }

    private void syncAllApartments(Integer namespaceId, List<Address> myApartmentList, List<ZJApartment> theirApartmentList) {
        Long organizationId = 0L;
        ThirdpartConfiguration organization = thirdpartConfigurationProvider.findByName("organization", namespaceId);
        if(organization != null) {
            organizationId = Long.valueOf(organization.getValue());
        }
        // 如果两边都有，更新；如果我们有，他们没有，删除；
        for (Address myApartment : myApartmentList) {
            ZJApartment customerApartment = findFromTheirApartmentList(myApartment, theirApartmentList);
            if (customerApartment != null) {
                updateAddress(myApartment, organizationId, customerApartment);
            }else {
                deleteAddress(organizationId, myApartment);
            }
        }
        // 如果他们有，我们没有，插入
        // 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
        if (theirApartmentList != null) {
            for (ZJApartment customerApartment : theirApartmentList) {
                if ((customerApartment.getDealed() != null && customerApartment.getDealed().booleanValue() == true) || StringUtils.isBlank(customerApartment.getBuildingName()) || StringUtils.isBlank(customerApartment.getApartmentName())) {
                    continue;
                }
                // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                Address address = addressProvider.findAddressByBuildingApartmentName(namespaceId, customerApartment.getCommunityId(), customerApartment.getBuildingName(), customerApartment.getApartmentName());
                if (address == null) {
                    insertAddress(namespaceId, organizationId, customerApartment);
                }else {
                    updateAddress(address, organizationId, customerApartment);
                }
            }
        }

    }

    private void insertAddress(Integer namespaceId, Long organizationId, ZJApartment customerApartment) {
        if (StringUtils.isBlank(customerApartment.getBuildingName()) || StringUtils.isBlank(customerApartment.getApartmentName())) {
            return;
        }
        Community community = communityProvider.findCommunityById(customerApartment.getCommunityId());
        if (community == null) {
            community = new Community();
            community.setId(customerApartment.getCommunityId());
        }
        Address address = new Address();
        address.setUuid(UUID.randomUUID().toString());
        address.setCommunityId(community.getId());
        address.setCityId(community.getCityId());
        address.setCityName(community.getCityName());
        address.setAreaId(community.getAreaId());
        address.setAreaName(community.getAreaName());
        address.setZipcode(community.getZipcode());
        address.setAddress(getAddress(customerApartment.getBuildingName(), customerApartment.getApartmentName()));
        address.setAddressAlias(address.getAddress());
        address.setBuildingName(customerApartment.getBuildingName());
        address.setBuildingAliasName(customerApartment.getBuildingName());
        address.setApartmentName(customerApartment.getApartmentName());
        if(customerApartment.getApartmentFloor() != null) {
            address.setApartmentFloor(String.valueOf(customerApartment.getApartmentFloor()));
        }

        address.setStatus(CommonStatus.ACTIVE.getCode());
        address.setCreatorUid(1L);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setOperatorUid(1L);
        address.setOperateTime(address.getCreateTime());
        address.setAreaSize(customerApartment.getAreaSize()==null?customerApartment.getRentArea():customerApartment.getAreaSize());
        address.setNamespaceId(namespaceId);
        address.setRentArea(customerApartment.getRentArea());
        address.setBuildArea(customerApartment.getBuildArea());
        address.setChargeArea(customerApartment.getChargeArea());
        address.setSharedArea(customerApartment.getSharedArea());
        address.setLayout(customerApartment.getLayout());
        address.setLivingStatus(customerApartment.getLivingStatus());
        address.setNamespaceAddressType(NamespaceAddressType.SHENZHOU.getCode());
        address.setNamespaceAddressToken(customerApartment.getApartmentIdentifier());

        //添加电商用到的楼栋和门牌
        address.setBusinessBuildingName(customerApartment.getBuildingName());
        address.setBusinessApartmentName(customerApartment.getApartmentName());

        addressProvider.createAddress(address);

        insertOrganizationAddressMapping(organizationId, address);
        insertOrUpdateLeasePromotion(customerApartment.getLivingStatus(), address.getNamespaceId(), address.getCommunityId(), address.getRentArea(), address.getBuildingName(), address.getApartmentName());

    }

    // 插入或更新招租管理
    private void insertOrUpdateLeasePromotion(Byte theirLivingStatus, Integer namespaceId, Long communityId, Double rentArea, String buildingName, String apartmentName){
        String namespaceType = "shenzhou";
        String namespaceToken = generateLeasePromotionToken(buildingName, apartmentName);
        if (AddressMappingStatus.FREE.equals(AddressMappingStatus.fromCode(theirLivingStatus))) {
            // 待租状态时，在招租管理里面插入一条数据，状态为已下线（因为需要园区编辑图文信息之后再发布到APP端），招租标题就是楼栋门牌，发布时间和入驻时间=插入数据时间，面积=传过来的面积，负责人和电话都为空
            // 如果有数据则不变
            LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionByToken(namespaceId, communityId, namespaceType, namespaceToken);
            if (leasePromotion == null) {
                leasePromotion = new LeasePromotion();
                leasePromotion.setNamespaceId(namespaceId);
                leasePromotion.setCommunityId(communityId);
                leasePromotion.setRentType(LeasePromotionType.ORDINARY.getCode());
//                leasePromotion.setSubject(apartmentName);
                leasePromotion.setRentAreas(String.valueOf(rentArea));
                leasePromotion.setCreateUid(1L);
                leasePromotion.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                leasePromotion.setUpdateTime(leasePromotion.getCreateTime());
                leasePromotion.setStatus(LeasePromotionStatus.OFFLINE.getCode());
                Building building = buildingProvider.findBuildingByName(namespaceId, communityId, buildingName);
                if (building != null) {
                    leasePromotion.setBuildingId(building.getId());
                }else {
                    leasePromotion.setBuildingId(0L);
                }
//                leasePromotion.setRentPosition(buildingName);
                leasePromotion.setEnterTime(leasePromotion.getCreateTime());
                leasePromotion.setNamespaceType(namespaceType);
                leasePromotion.setNamespaceToken(namespaceToken);
                enterpriseApplyEntryProvider.createLeasePromotion(leasePromotion);
            }
        }else if (AddressMappingStatus.RENT.equals(AddressMappingStatus.fromCode(theirLivingStatus))) {
            // 新租和已租时，将招租管理对应的门牌的招租状态改为“已出租”
            LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionByToken(namespaceId, communityId, namespaceType, namespaceToken);
            if (leasePromotion != null && LeasePromotionStatus.fromType(leasePromotion.getStatus()) != LeasePromotionStatus.RENTAL) {
                leasePromotion.setStatus(LeasePromotionStatus.RENTAL.getCode());
                enterpriseApplyEntryProvider.updateLeasePromotion(leasePromotion);
            }
        }
    }

    private String generateLeasePromotionToken(String buildingName, String apartmentName) {
        return buildingName+"-"+apartmentName;
    };

    private String getAddress(String buildingName, String apartmentName){
        if (apartmentName.contains(buildingName)) {
            return apartmentName;
        }
        return buildingName+"-"+apartmentName;
    }

    private void deleteAddress(Long organizationId, Address address) {
        if (CommonStatus.fromCode(address.getStatus()) != CommonStatus.INACTIVE) {
            address.setOperatorUid(1L);
            address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            address.setStatus(CommonStatus.INACTIVE.getCode());
            addressProvider.updateAddress(address);
        }

        CommunityAddressMapping organizationAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
        if (organizationAddressMapping != null) {
            organizationProvider.deleteOrganizationAddressMapping(organizationAddressMapping);
        }
    }

    private void updateAddress(Address address, Long organizationId, ZJApartment customerApartment) {
        Community community = communityProvider.findCommunityById(customerApartment.getCommunityId());
        if (community == null) {
            community = new Community();
            community.setId(customerApartment.getCommunityId());
        }
        address.setCommunityId(community.getId());
        address.setCityId(community.getCityId());
        address.setCityName(community.getCityName());
        address.setAreaId(community.getAreaId());
        address.setAreaName(community.getAreaName());
        address.setZipcode(community.getZipcode());
        address.setAddress(getAddress(customerApartment.getBuildingName(), customerApartment.getApartmentName()));
        address.setAddressAlias(address.getAddress());
        address.setBuildingName(customerApartment.getBuildingName());
        address.setBuildingAliasName(customerApartment.getBuildingName());
        address.setApartmentName(customerApartment.getApartmentName());
        if(customerApartment.getApartmentFloor() != null) {
            address.setApartmentFloor(String.valueOf(customerApartment.getApartmentFloor()));
        }

        address.setAreaSize(customerApartment.getAreaSize()==null?customerApartment.getRentArea():customerApartment.getAreaSize());
        address.setRentArea(customerApartment.getRentArea());
        address.setBuildArea(customerApartment.getBuildArea());
        address.setChargeArea(customerApartment.getChargeArea());
        address.setSharedArea(customerApartment.getSharedArea());
        address.setLayout(customerApartment.getLayout());
        address.setLivingStatus(customerApartment.getLivingStatus());
        address.setOperatorUid(1L);
        address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setStatus(CommonStatus.ACTIVE.getCode());

        //添加电商用到的楼栋和门牌
        address.setBusinessBuildingName(customerApartment.getBuildingName());
        address.setBusinessApartmentName(customerApartment.getApartmentName());

        addressProvider.updateAddress(address);
        insertOrUpdateLeasePromotion(customerApartment.getLivingStatus(), address.getNamespaceId(), address.getCommunityId(), address.getRentArea(), address.getBuildingName(), address.getApartmentName());
        CommunityAddressMapping organizationAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
        if (organizationAddressMapping == null) {
            insertOrganizationAddressMapping(organizationId, address);
        }else {
            updateOrganizationAddressMapping(organizationAddressMapping, address);
        }
    }

    private void updateOrganizationAddressMapping(CommunityAddressMapping organizationAddressMapping, Address address) {
        organizationAddressMapping.setOrganizationAddress(address.getAddress());
        organizationAddressMapping.setLivingStatus(address.getLivingStatus());
        organizationProvider.updateOrganizationAddressMapping(organizationAddressMapping);
    }

    private void insertOrganizationAddressMapping(Long organizationId, Address address) {
        CommunityAddressMapping organizationAddressMapping = new CommunityAddressMapping();
        organizationAddressMapping.setOrganizationId(organizationId);
        organizationAddressMapping.setCommunityId(address.getCommunityId());
        organizationAddressMapping.setAddressId(address.getId());
        organizationAddressMapping.setOrganizationAddress(address.getAddress());
        organizationAddressMapping.setLivingStatus(address.getLivingStatus());
        organizationAddressMapping.setNamespaceType(NamespaceAddressType.SHENZHOU.getCode());
        organizationProvider.createOrganizationAddressMapping(organizationAddressMapping);
    }

    private ZJApartment findFromTheirApartmentList(Address myApartment, List<ZJApartment> theirApartmentList) {
        if (theirApartmentList != null) {
            for (ZJApartment customerApartment : theirApartmentList) {
                if (NamespaceAddressType.SHENZHOU.getCode().equals(myApartment.getNamespaceAddressType())
                && myApartment.getNamespaceAddressToken().equals(customerApartment.getApartmentIdentifier())) {
                    customerApartment.setDealed(true);
                    return customerApartment;
                }
            }
        }
        return null;
    }

    private void syncAllEnterprises(Integer namespaceId, List<ZjSyncdataBackup> backupList, Byte allFlag) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        Long specialCommunityId = null;
        if(SyncFlag.PART.equals(SyncFlag.fromCode(allFlag))) {
            String communityIdentifier = backupList.get(0).getUpdateCommunity();
            Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.SHENZHOU.getCode(), communityIdentifier);
            if(community != null) {
                specialCommunityId = community.getId();
            }
        }

        List<EnterpriseCustomer> myEnterpriseCustomerList = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceType(namespaceId, NamespaceCustomerType.SHENZHOU.getCode(), specialCommunityId);

        List<ZJEnterprise> mergeEnterpriseList = mergeBackupList(backupList, ZJEnterprise.class);
        List<ZJEnterprise> theirEnterpriseList = new ArrayList<ZJEnterprise>();
        if(mergeEnterpriseList != null && mergeEnterpriseList.size() > 0) {
            LOGGER.debug("syncDataToDb mergeEnterpriseList size: {}", mergeEnterpriseList.size());
            if(SyncFlag.PART.equals(SyncFlag.fromCode(allFlag))) {
                LOGGER.debug("syncDataToDb part");
                String communityIdentifier = backupList.get(0).getUpdateCommunity();
                Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.SHENZHOU.getCode(), communityIdentifier);
                if(community != null) {
                    mergeEnterpriseList.forEach(enterprise -> {
                        enterprise.setCommunityId(community.getId());
                        theirEnterpriseList.add(enterprise);
                    });
                }

            } else {
                LOGGER.debug("syncDataToDb all");
                Map<String, Long> communities = communityProvider.listCommunityIdByNamespaceType(namespaceId, NamespaceCommunityType.SHENZHOU.getCode());
                for(ZJEnterprise enterprise : mergeEnterpriseList) {
                    Long communityId = communities.get(enterprise.getCommunityIdentifier());
                    if(communityId != null) {
                        enterprise.setCommunityId(communityId);
                    }
                    theirEnterpriseList.add(enterprise);
                }
//                mergeEnterpriseList.forEach(enterprise -> {
//                    Long communityId = communities.get(enterprise.getCommunityIdentifier());
//                    if(communityId != null) {
//                        enterprise.setCommunityId(communityId);
//                    }
//                    theirEnterpriseList.add(enterprise);
//                });
            }

        }
        LOGGER.debug("syncDataToDb namespaceId: {}, myEnterpriseCustomerList size: {}, theirEnterpriseList size: {}",
                namespaceId, myEnterpriseCustomerList.size(), theirEnterpriseList.size());
        dbProvider.execute(s->{
            syncAllEnterprises(namespaceId, myEnterpriseCustomerList, theirEnterpriseList);
            return true;
        });
    }

    private void syncAllEnterprises(Integer namespaceId, List<EnterpriseCustomer> myEnterpriseCustomerList, List<ZJEnterprise> theirEnterpriseList) {
        // 如果两边都有，更新；如果我们有，他们没有，删除；
        for (EnterpriseCustomer myEnterpriseCustomer : myEnterpriseCustomerList) {
            ZJEnterprise zjEnterprise = findFromTheirEnterpriseList(myEnterpriseCustomer, theirEnterpriseList);
            if (zjEnterprise != null) {
                updateEnterpriseCustomer(myEnterpriseCustomer, zjEnterprise);
            }else {
                deleteEnterpriseCustomer(myEnterpriseCustomer);
            }
        }
        // 如果他们有，我们没有，插入
        // 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
        if (theirEnterpriseList != null) {
            for (ZJEnterprise zjEnterprise : theirEnterpriseList) {
                LOGGER.debug("zjEnterprise deal: {}", zjEnterprise.getDealed());
                if ((zjEnterprise.getDealed() != null && zjEnterprise.getDealed().booleanValue() == true)) {
                    continue;
                }
                // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId,  zjEnterprise.getCommunityId(), zjEnterprise.getName());
                if (customers == null || customers.size() == 0) {
                    insertEnterpriseCustomer(NAMESPACE_ID, zjEnterprise);
                }else {
                    updateEnterpriseCustomer(customers.get(0), zjEnterprise);
                }
            }
        }
    }

    private ZJEnterprise findFromTheirEnterpriseList(EnterpriseCustomer myOrganization, List<ZJEnterprise> theirEnterpriseList) {
        if (theirEnterpriseList != null) {
            for (ZJEnterprise zjEnterprise : theirEnterpriseList) {
                if (NamespaceCustomerType.SHENZHOU.getCode().equals(myOrganization.getNamespaceCustomerType())
                        && myOrganization.getNamespaceCustomerToken().equals(zjEnterprise.getEnterpriseIdentifier())) {
                    zjEnterprise.setDealed(true);
                    return zjEnterprise;
                }
            }
        }
        return null;
    }

    private Long enterpriseScopeFieldItem(Integer namespaceId, Long communityId, String displayName) {
        if(!StringUtils.isBlank(displayName)) {
            ScopeFieldItem scopeCategoryFieldItem = fieldService.findScopeFieldItemByDisplayName(namespaceId,1012516L, communityId, ModuleName.ENTERPRISE_CUSTOMER.getName(), displayName);
            if(scopeCategoryFieldItem != null) {
                return scopeCategoryFieldItem.getItemId();
            }
        }
        return null;
    }
    private void insertEnterpriseCustomer(Integer namespaceId, ZJEnterprise zjEnterprise) {
        LOGGER.debug("syncDataToDb insertEnterpriseCustomer namespaceId: {}, zjEnterprise: {}",
                namespaceId, StringHelper.toJsonString(zjEnterprise));
        this.dbProvider.execute((TransactionStatus status) -> {
            EnterpriseCustomer customer = new EnterpriseCustomer();
            Long communityId = zjEnterprise.getCommunityId() == null ? 0L : zjEnterprise.getCommunityId();
            customer.setCommunityId(communityId);
            customer.setNamespaceId(namespaceId);
            // 暂时使用固定的管理公司id
            customer.setOwnerId(1012516L);
            customer.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            customer.setNamespaceCustomerType(NamespaceCustomerType.SHENZHOU.getCode());
            customer.setNamespaceCustomerToken(zjEnterprise.getEnterpriseIdentifier());
            customer.setName(zjEnterprise.getName());
            customer.setNickName(zjEnterprise.getName());
            customer.setCustomerNumber(zjEnterprise.getCustomerNumber());
            customer.setCategoryItemName(zjEnterprise.getCustomerCategory());
            Long categoryItemId = enterpriseScopeFieldItem(namespaceId, communityId, zjEnterprise.getCustomerCategory());
            if(categoryItemId != null) {
                customer.setCategoryItemId(categoryItemId);
            }
            customer.setLevelItemName(zjEnterprise.getCustomerLevel());
            Long levelItemId = enterpriseScopeFieldItem(namespaceId, communityId, zjEnterprise.getCustomerLevel());
            if(levelItemId != null) {
                customer.setLevelItemId(levelItemId);
            }
            customer.setContactName(zjEnterprise.getContactName());
            customer.setContactGenderItemName(zjEnterprise.getContactGender());
            Long genderItemId = enterpriseScopeFieldItem(namespaceId, communityId, zjEnterprise.getContactGender());
            if(genderItemId != null) {
                customer.setContactGenderItemId(genderItemId);
            }
            customer.setContactMobile(zjEnterprise.getContactMobile());
            customer.setContactAddress(zjEnterprise.getContactAddress());
            customer.setContactEmail(zjEnterprise.getContactEmail());
            customer.setContactPhone(zjEnterprise.getContactPhone());
            customer.setContactPosition(zjEnterprise.getContactPosition());
            customer.setContactOffficePhone(zjEnterprise.getContactOffficePhone());
            customer.setContactFamilyPhone(zjEnterprise.getContactFamilyPhone());
            customer.setContactFax(zjEnterprise.getContactFax());
            customer.setCorpEmail(zjEnterprise.getCorpEmail());
            customer.setCorpWebsite(zjEnterprise.getCorpWebsite());
            customer.setCorpNatureItemName(zjEnterprise.getCorpNature());
            Long natureItemId = enterpriseScopeFieldItem(namespaceId, communityId, zjEnterprise.getCorpNature());
            if(natureItemId != null) {
                customer.setCorpNatureItemId(natureItemId);
            }
            customer.setCorpIndustryItemName(zjEnterprise.getCorpIndustry());
            Long industryItemId = enterpriseScopeFieldItem(namespaceId, communityId, zjEnterprise.getCorpIndustry());
            if(industryItemId != null) {
                customer.setCorpIndustryItemId(industryItemId);
            }
            customer.setCorpDescription(zjEnterprise.getDescription());
            if(zjEnterprise.getCorpEntryDate() != null) {
                customer.setCorpEntryDate(dateStrToTimestamp(zjEnterprise.getCorpEntryDate()));
            }
            customer.setSourceItemName(zjEnterprise.getSource());
            Long sourceItemId = enterpriseScopeFieldItem(namespaceId, communityId, zjEnterprise.getSource());
            if(sourceItemId != null) {
                customer.setContactGenderItemId(sourceItemId);
            }
            customer.setStatus(CommonStatus.ACTIVE.getCode());
            customer.setTrackingUid(-1L);
            customer.setCreatorUid(1L);
            customer.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            customer.setOperatorUid(1L);
            customer.setUpdateTime(customer.getCreateTime());

            //给企业客户创建一个对应的企业账号
            Organization organization = insertOrganization(customer);
            customer.setOrganizationId(organization.getId());
            enterpriseCustomerProvider.createEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);

            insertOrUpdateOrganizationDetail(organization, customer);
            insertOrUpdateOrganizationCommunityRequest(zjEnterprise.getCommunityId(), organization);
            insertOrUpdateOrganizationAddresses(zjEnterprise.getAddressList(), customer);
            insertOrUpdateOrganizationMembers(namespaceId, organization, customer.getContactName(), customer.getContactMobile());
            organizationSearcher.feedDoc(organization);
            return null;
        });

    }

    // 需要把同步过来的业务人员添加为我司系统对应组织的管理员
    private void insertOrUpdateOrganizationMembers(Integer namespaceId, Organization organization, String contact, String contactPhone) {
        if (StringUtils.isBlank(contact) || StringUtils.isBlank(contactPhone) || organization == null) {
            return ;
        }
        try {
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

    private void insertOrUpdateOrganizationAddresses(List<CommunityAddressDTO> apartmentIdentifier, EnterpriseCustomer customer){
        List<OrganizationAddress> myOrganizationAddressList = organizationProvider.listOrganizationAddressByOrganizationId(customer.getOrganizationId());
        List<CommunityAddressDTO> apartments = apartmentIdentifier;
        for (OrganizationAddress organizationAddress : myOrganizationAddressList) {
            Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
            if (address != null && address.getNamespaceAddressType() != null && address.getNamespaceAddressToken() != null) {
                if (address.getNamespaceAddressType().equals(NamespaceAddressType.SHENZHOU.getCode())) {
                    for(CommunityAddressDTO identifier : apartmentIdentifier) {
                        if(address.getNamespaceAddressToken().equals(identifier.getApartmentIdentifier())) {
                            apartments.remove(identifier);
                        }
                    }
                }
            } else {
                deleteOrganizationAddress(organizationAddress);
            }
        }

        if(apartments != null && apartments.size() > 0) {
            apartments.forEach(apartment -> {
                insertOrganizationAddress(apartment, customer);
            });
        }

    }

    private void insertOrganizationAddress(CommunityAddressDTO apartmentIdentifier, EnterpriseCustomer customer) {
        if (apartmentIdentifier == null) {
            return;
        }
        Long before = System.currentTimeMillis();
        Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceAddressType.SHENZHOU.getCode(), apartmentIdentifier.getApartmentIdentifier());
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
    }

    private void deleteOrganizationAddress(OrganizationAddress organizationAddress) {
        if (OrganizationAddressStatus.fromCode(organizationAddress.getStatus()) != OrganizationAddressStatus.INACTIVE) {
            organizationAddress.setStatus(OrganizationAddressStatus.INACTIVE.getCode());
            organizationAddress.setOperatorUid(1L);
            organizationAddress.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            organizationProvider.updateOrganizationAddress(organizationAddress);
        }
    }

    private Organization insertOrganization(EnterpriseCustomer customer) {
        Organization org = organizationProvider.findOrganizationByName(customer.getName(), customer.getNamespaceId());
        if(org != null && OrganizationStatus.ACTIVE.equals(OrganizationStatus.fromCode(org.getStatus()))) {
            //已存在则更新 地址、官网地址、企业logo
            org.setWebsite(customer.getCorpWebsite());
            organizationProvider.updateOrganization(org);
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
        organization.setNamespaceOrganizationType(NamespaceOrganizationType.SHENZHOU.getCode());
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
            organizationDetail.setContact("");
            organizationProvider.updateOrganizationDetail(organizationDetail);
        }
        organizationSearcher.feedDoc(organization);
    }

    private void insertOrUpdateOrganizationCommunityRequest(Long communityId, Organization organization) {
        if(communityId != null) {
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            //删除不在本园区的
            List<OrganizationCommunityRequest> requests = organizationProvider.listOrganizationCommunityRequestsByOrganizationId(organization.getId());
            if(requests != null && requests.size() > 0) {
                for(OrganizationCommunityRequest request : requests) {
                    if(!request.getCommunityId().equals(communityId)) {
                        request.setMemberStatus(OrganizationCommunityRequestStatus.INACTIVE.getCode());
                        request.setOperatorUid(1L);
                        request.setUpdateTime(now);
                        organizationProvider.updateOrganizationCommunityRequest(request);
                    }
                }
            }

            OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.findOrganizationCommunityRequestByOrganizationId(communityId, organization.getId());
            if (organizationCommunityRequest == null) {
                organizationCommunityRequest = new OrganizationCommunityRequest();
                organizationCommunityRequest.setCommunityId(communityId);
                organizationCommunityRequest.setMemberType(OrganizationCommunityRequestType.Organization.getCode());
                organizationCommunityRequest.setMemberId(organization.getId());
                organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
                organizationCommunityRequest.setCreatorUid(1L);
                organizationCommunityRequest.setCreateTime(now);
                organizationCommunityRequest.setOperatorUid(1L);
                organizationCommunityRequest.setApproveTime(organizationCommunityRequest.getCreateTime());
                organizationCommunityRequest.setUpdateTime(organizationCommunityRequest.getCreateTime());
                organizationProvider.createOrganizationCommunityRequest(organizationCommunityRequest);
            }else {
                organizationCommunityRequest.setMemberStatus(OrganizationCommunityRequestStatus.ACTIVE.getCode());
                organizationCommunityRequest.setOperatorUid(1L);
                organizationCommunityRequest.setUpdateTime(now);
                organizationProvider.updateOrganizationCommunityRequest(organizationCommunityRequest);
            }
        }
    }

    private Timestamp dateStrToTimestamp(String str) {
        LocalDate localDate = LocalDate.parse(str,dateSF);
        Timestamp ts = new Timestamp(Date.valueOf(localDate).getTime());
        return ts;
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
        organizationService.deleteEnterpriseById(deleteOrganizationIdCommand, false);
//        List<OrganizationAddress> myOrganizationAddressList = organizationProvider.listOrganizationAddressByOrganizationId(customer.getOrganizationId());
//        if(myOrganizationAddressList != null && myOrganizationAddressList.size() > 0) {
//            myOrganizationAddressList.forEach(organizationAddress -> {
//                deleteOrganizationAddress(organizationAddress);
//            });
//        }

    }

    private void updateEnterpriseCustomer(EnterpriseCustomer customer, ZJEnterprise zjEnterprise) {
        LOGGER.debug("syncDataToDb updateEnterpriseCustomer customer: {}, zjEnterprise: {}",
                StringHelper.toJsonString(customer), StringHelper.toJsonString(zjEnterprise));
        this.dbProvider.execute((TransactionStatus status) -> {
            Long communityId = zjEnterprise.getCommunityId() == null ? 0L : zjEnterprise.getCommunityId();
            customer.setCommunityId(communityId);
            customer.setNamespaceCustomerType(NamespaceCustomerType.SHENZHOU.getCode());
            customer.setNamespaceCustomerToken(zjEnterprise.getEnterpriseIdentifier());
            customer.setName(zjEnterprise.getName());
            customer.setNickName(zjEnterprise.getName());
            customer.setCustomerNumber(zjEnterprise.getCustomerNumber());
            customer.setCategoryItemName(zjEnterprise.getCustomerCategory());
            Long categoryItemId = enterpriseScopeFieldItem(customer.getNamespaceId(), communityId, zjEnterprise.getCustomerCategory());
            if(categoryItemId != null) {
                customer.setCategoryItemId(categoryItemId);
            }
            customer.setLevelItemName(zjEnterprise.getCustomerLevel());
            Long levelItemId = enterpriseScopeFieldItem(customer.getNamespaceId(), communityId, zjEnterprise.getCustomerLevel());
            if(levelItemId != null) {
                customer.setLevelItemId(levelItemId);
            }
            customer.setContactName(zjEnterprise.getContactName());
            customer.setContactGenderItemName(zjEnterprise.getContactGender());
            Long genderItemId = enterpriseScopeFieldItem(customer.getNamespaceId(), communityId, zjEnterprise.getContactGender());
            if(genderItemId != null) {
                customer.setContactGenderItemId(genderItemId);
            }
            customer.setContactMobile(zjEnterprise.getContactMobile());
            customer.setContactAddress(zjEnterprise.getContactAddress());
            customer.setContactEmail(zjEnterprise.getContactEmail());
            customer.setContactPhone(zjEnterprise.getContactPhone());
            customer.setContactOffficePhone(zjEnterprise.getContactOffficePhone());
            customer.setContactFamilyPhone(zjEnterprise.getContactFamilyPhone());
            customer.setContactFax(zjEnterprise.getContactFax());
            customer.setCorpEmail(zjEnterprise.getCorpEmail());
            customer.setCorpWebsite(zjEnterprise.getCorpWebsite());
            customer.setCorpNatureItemName(zjEnterprise.getCorpNature());
            Long natureItemId = enterpriseScopeFieldItem(customer.getNamespaceId(), communityId, zjEnterprise.getCorpNature());
            if(natureItemId != null) {
                customer.setCorpNatureItemId(natureItemId);
            }
            customer.setCorpIndustryItemName(zjEnterprise.getCorpIndustry());
            Long industryItemId = enterpriseScopeFieldItem(customer.getNamespaceId(), communityId, zjEnterprise.getCorpIndustry());
            if(industryItemId != null) {
                customer.setCorpIndustryItemId(industryItemId);
            }
            customer.setCorpDescription(zjEnterprise.getDescription());
            if (zjEnterprise.getCorpEntryDate() != null) {
                customer.setCorpEntryDate(dateStrToTimestamp(zjEnterprise.getCorpEntryDate()));
            }
            customer.setSourceItemName(zjEnterprise.getSource());
            Long sourceItemId = enterpriseScopeFieldItem(customer.getNamespaceId(), communityId, zjEnterprise.getSource());
            if(sourceItemId != null) {
                customer.setSourceItemId(sourceItemId);
            }
            customer.setStatus(CommonStatus.ACTIVE.getCode());
            customer.setOperatorUid(1L);
            customer.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            if(customer.getTrackingUid() == null) {
                customer.setTrackingUid(-1L);
            }
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
            insertOrUpdateOrganizationCommunityRequest(zjEnterprise.getCommunityId(), organization);
            insertOrUpdateOrganizationAddresses(zjEnterprise.getAddressList(), customer);
            insertOrUpdateOrganizationMembers(customer.getNamespaceId(), organization, customer.getContactName(), customer.getContactMobile());
            organizationSearcher.feedDoc(organization);
            return null;
        });
    }

    private void syncAllIndividuals(Integer namespaceId, List<ZjSyncdataBackup> backupList, Byte allFlag) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        Long specialCommunityId = null;
        if(SyncFlag.PART.equals(SyncFlag.fromCode(allFlag))) {
            String communityIdentifier = backupList.get(0).getUpdateCommunity();
            Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.SHENZHOU.getCode(), communityIdentifier);
            if(community != null) {
                specialCommunityId = community.getId();
            }
        }

        List<OrganizationOwner> myIndividualCustomerList = individualCustomerProvider.listOrganizationOwnerByNamespaceType(namespaceId, NamespaceCustomerType.SHENZHOU.getCode(), specialCommunityId);

        List<ZJIndividuals> mergeIndividualList = mergeBackupList(backupList, ZJIndividuals.class);
        List<ZJIndividuals> theirIndividualList = new ArrayList<ZJIndividuals>();
        if(mergeIndividualList != null && mergeIndividualList.size() > 0) {
            if(SyncFlag.PART.equals(SyncFlag.fromCode(allFlag))) {
                String communityIdentifier = backupList.get(0).getUpdateCommunity();
                Community community = communityProvider.findCommunityByNamespaceToken(NamespaceCommunityType.SHENZHOU.getCode(), communityIdentifier);
                if(community != null) {
                    mergeIndividualList.forEach(individual -> {
                        individual.setCommunityId(community.getId());
                        theirIndividualList.add(individual);
                    });
                }

            } else {
                Map<String, Long> communities = communityProvider.listCommunityIdByNamespaceType(namespaceId, NamespaceCommunityType.SHENZHOU.getCode());
                mergeIndividualList.forEach(individual -> {
                    Long communityId = communities.get(individual.getCommunityIdentifier());
                    if(communityId != null) {
                        individual.setCommunityId(communityId);
                    }
                    theirIndividualList.add(individual);
                });
            }

        }

        dbProvider.execute(s->{
            LOGGER.info("syncDataToDb namespaceId: {}, myIndividualCustomerList: {}, theirIndividualList: {}",
                    namespaceId, myIndividualCustomerList, theirIndividualList);
            syncAllIndividuals(namespaceId, myIndividualCustomerList, theirIndividualList);
            return true;
        });
    }

    private void syncAllIndividuals(Integer namespaceId, List<OrganizationOwner> myIndividualCustomerList, List<ZJIndividuals> theirIndividualList) {
        // 如果两边都有，更新；如果我们有，他们没有，删除；
        for (OrganizationOwner myIndividualCustomer : myIndividualCustomerList) {
            ZJIndividuals zjIndividual = findFromTheirIndividualList(myIndividualCustomer, theirIndividualList);
            if (zjIndividual != null) {
                updateIndividualCustomer(myIndividualCustomer, zjIndividual);
            }else {
                deleteIndividualCustomer(myIndividualCustomer);
            }
        }
        // 如果他们有，我们没有，插入
        // 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
        if (theirIndividualList != null) {
            for (ZJIndividuals zjIndividual : theirIndividualList) {
                LOGGER.info("syncDataToDb zjIndividual DEAL: {}", zjIndividual.getDealed());
                if ((zjIndividual.getDealed() != null && zjIndividual.getDealed().booleanValue() == true)) {
                    continue;
                }
                // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
                List<OrganizationOwner> customers = individualCustomerProvider.listOrganizationOwnerByNamespaceIdAndName(namespaceId, zjIndividual.getName());
                if (customers == null || customers.size() == 0) {
                    insertIndividualCustomer(NAMESPACE_ID, zjIndividual);
                }else {
                    updateIndividualCustomer(customers.get(0), zjIndividual);
                }
            }
        }
    }

    private ZJIndividuals findFromTheirIndividualList(OrganizationOwner myIndividualCustomer, List<ZJIndividuals> theirIndividualList) {
        if (theirIndividualList != null) {
            for (ZJIndividuals zjIndividual : theirIndividualList) {
                if (NamespaceCustomerType.SHENZHOU.getCode().equals(myIndividualCustomer.getNamespaceCustomerType())
                        && myIndividualCustomer.getNamespaceCustomerToken().equals(zjIndividual.getUserIdentifier())) {
                    zjIndividual.setDealed(true);
                    return zjIndividual;
                }
            }
        }
        return null;
    }


    private void insertIndividualCustomer(Integer namespaceId, ZJIndividuals zjIndividual) {
        LOGGER.info("syncDataToDb insertIndividualCustomer namespaceId: {}, zjIndividual: {}", namespaceId, zjIndividual);
        this.dbProvider.execute((TransactionStatus status) -> {
            OrganizationOwner customer = new OrganizationOwner();
            customer.setContactName(zjIndividual.getName());
            customer.setContactToken(zjIndividual.getMobile());
            customer.setContactType(ContactType.MOBILE.getCode());
            customer.setCompany(zjIndividual.getCompany());
            customer.setJob(zjIndividual.getJob());
            customer.setMaritalStatus(zjIndividual.getMaritalStatus());
            customer.setAddress(zjIndividual.getContactAddress());
            customer.setRegisteredResidence(zjIndividual.getRegisteredResidence());
            customer.setIdCardNumber(zjIndividual.getCardNumber());
            customer.setGender(UserGender.fromText(zjIndividual.getGender()) == null ? UserGender.UNDISCLOSURED.getCode() : UserGender.fromText(zjIndividual.getGender()).getCode());
            Timestamp ts = strToTimestamp(zjIndividual.getBirthday());
            if(ts != null) {
                customer.setBirthday(new Date(ts.getTime()));
            }
            customer.setNamespaceId(namespaceId);
            customer.setNamespaceCustomerType(NamespaceCustomerType.SHENZHOU.getCode());
            customer.setNamespaceCustomerToken(zjIndividual.getUserIdentifier());
            customer.setStatus(OrganizationOwnerStatus.NORMAL.getCode());
            customer.setCreatorUid(UserContext.currentUserId());
            customer.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

            if (zjIndividual.getAddressList() != null) {
                List<String> communityIdentifiers = zjIndividual.getAddressList().stream().map(communityAddress -> {
                    return communityAddress.getCommunityIdentifier();
                }).collect(Collectors.toList());
                List<Long> communityIds = communityProvider.listCommunityByNamespaceToken(NamespaceCommunityType.SHENZHOU.getCode(), communityIdentifiers);

                if (communityIds != null && communityIds.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Long communityId : communityIds) {
                        if (sb.length() == 0) {
                            sb.append(communityId);
                        } else {
                            sb.append(",");
                            sb.append(communityId);
                        }
                    }
                    if (sb.length() > 0) {
                        customer.setCommunityId(sb.toString());
                    }
                }
            }
            OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeByDisplayName(zjIndividual.getCustomerCategory());
            if(ownerType != null) {
                customer.setOrgOwnerTypeId(ownerType.getId());
            }
            customer.setOrganizationId(1012516L);
            organizationProvider.createOrganizationOwner(customer);
            CommunityPmOwner communityPmOwner = ConvertHelper.convert(customer, CommunityPmOwner.class);
            pmOwnerSearcher.feedDoc(communityPmOwner);
            //给个人客户添加地址
            insertOrganizationOwnerAddresses(customer, zjIndividual.getAddressList());
            return null;
        });

    }

    private Timestamp strToTimestamp(String str) {

        Timestamp ts = null;
        try {
            ts = new Timestamp(simpleDateSF.get().parse(str).getTime());
        } catch (ParseException e) {
            LOGGER.error("validityPeriod data format is not yyyymmdd. data = {}", str);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "validityPeriod data format is not yyyymmdd.");
        }

        return ts;
    }

    private void updateIndividualCustomer(OrganizationOwner customer, ZJIndividuals zjIndividual) {
        LOGGER.info("syncDataToDb updateIndividualCustomer customer: {}, zjIndividual: {}",
                customer, zjIndividual);
        this.dbProvider.execute((TransactionStatus status) -> {
            customer.setContactName(zjIndividual.getName());
            customer.setContactToken(zjIndividual.getMobile());
            customer.setContactType(ContactType.MOBILE.getCode());
            customer.setCompany(zjIndividual.getCompany());
            customer.setJob(zjIndividual.getJob());
            customer.setMaritalStatus(zjIndividual.getMaritalStatus());
            customer.setAddress(zjIndividual.getContactAddress());
            customer.setRegisteredResidence(zjIndividual.getRegisteredResidence());
            customer.setIdCardNumber(zjIndividual.getCardNumber());
            customer.setGender(UserGender.fromText(zjIndividual.getGender()) == null ? UserGender.UNDISCLOSURED.getCode() : UserGender.fromText(zjIndividual.getGender()).getCode());
            Timestamp ts = strToTimestamp(zjIndividual.getBirthday());
            if (ts != null) {
                customer.setBirthday(new Date(ts.getTime()));
            }
            OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeByDisplayName(zjIndividual.getCustomerCategory());
            if (ownerType != null) {
                customer.setOrgOwnerTypeId(ownerType.getId());
            }
            if (zjIndividual.getAddressList() != null) {
                List<String> communityIdentifiers = zjIndividual.getAddressList().stream().map(communityAddress -> {
                    return communityAddress.getCommunityIdentifier();
                }).collect(Collectors.toList());
                List<Long> communityIds = communityProvider.listCommunityByNamespaceToken(NamespaceCommunityType.SHENZHOU.getCode(), communityIdentifiers);

                if (communityIds != null && communityIds.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Long communityId : communityIds) {
                        if (sb.length() == 0) {
                            sb.append(communityId);
                        } else {
                            sb.append(",");
                            sb.append(communityId);
                        }
                    }
                    if (sb.length() > 0) {
                        customer.setCommunityId(sb.toString());
                    }
                }
            }
            customer.setNamespaceCustomerType(NamespaceCustomerType.SHENZHOU.getCode());
            customer.setStatus(OrganizationOwnerStatus.NORMAL.getCode());
            organizationProvider.updateOrganizationOwner(customer);

            CommunityPmOwner communityPmOwner = ConvertHelper.convert(customer, CommunityPmOwner.class);
            pmOwnerSearcher.feedDoc(communityPmOwner);
            return null;

        });
        insertOrUpdateOrganizationOwnerAddresses(zjIndividual.getCommunityId(), zjIndividual.getAddressList(), customer);
    }

    private void deleteIndividualCustomer(OrganizationOwner customer) {
        if (OrganizationOwnerStatus.fromCode(customer.getStatus()) != OrganizationOwnerStatus.DELETE) {
            customer.setStatus(OrganizationOwnerStatus.DELETE.getCode());
            organizationProvider.updateOrganizationOwner(customer);
            CommunityPmOwner communityPmOwner = ConvertHelper.convert(customer, CommunityPmOwner.class);
            pmOwnerSearcher.feedDoc(communityPmOwner);
        }

        List<OrganizationOwnerAddress> myOrganizationOwnerAddressList = individualCustomerProvider.listOrganizationOwnerAddressByOwnerId(customer.getId());
        if(myOrganizationOwnerAddressList != null && myOrganizationOwnerAddressList.size() > 0) {
            myOrganizationOwnerAddressList.forEach(organizationOwnerAddress -> {
                individualCustomerProvider.deleteOrganizationOwnerAddress(organizationOwnerAddress);
            });
        }
    }

    private void insertOrUpdateOrganizationOwnerAddresses(Long communityId, List<CommunityAddressDTO> addressList, OrganizationOwner customer){
        List<OrganizationOwnerAddress> myOrganizationOwnerAddressList = individualCustomerProvider.listOrganizationOwnerAddressByOwnerId(customer.getId());
        List<CommunityAddressDTO> apartments = addressList;
        for (OrganizationOwnerAddress organizationOwnerAddress : myOrganizationOwnerAddressList) {
            Address address = addressProvider.findAddressById(organizationOwnerAddress.getAddressId());
            if (address != null && address.getNamespaceAddressType() != null && address.getNamespaceAddressToken() != null) {
                if (address.getNamespaceAddressType().equals(NamespaceAddressType.SHENZHOU.getCode())) {
                    for(CommunityAddressDTO communityAddress : addressList) {
                        if(address.getNamespaceAddressToken().equals(communityAddress.getApartmentIdentifier())) {
                            apartments.remove(communityAddress);
                        }
                    }
                }
            } else {
                individualCustomerProvider.deleteOrganizationOwnerAddress(organizationOwnerAddress);
            }
        }
        if(apartments != null && apartments.size() > 0) {
            insertOrganizationOwnerAddresses(customer, apartments);
        }

    }

    private void insertOrganizationOwnerAddresses(OrganizationOwner customer, List<CommunityAddressDTO> addressList) {
        if(addressList != null && addressList.size() > 0) {
            addressList.forEach(ownerAddress -> {
                Address address = addressProvider.findAddressByNamespaceTypeAndName(NamespaceAddressType.SHENZHOU.getCode(), ownerAddress.getApartmentIdentifier());
                if (address == null) {
                    return;
                }
                OrganizationOwnerAddress organizationOwnerAddress = new OrganizationOwnerAddress();
                organizationOwnerAddress.setNamespaceId(customer.getNamespaceId());
                organizationOwnerAddress.setOrganizationOwnerId(customer.getId());
                organizationOwnerAddress.setAddressId(address.getId());
                organizationOwnerAddress.setLivingStatus(AddressLivingStatus.ACTIVE.getCode());
                organizationOwnerAddress.setAuthType(OrganizationOwnerAddressAuthType.ACTIVE.getCode());
                individualCustomerProvider.createOrganizationOwnerAddress(organizationOwnerAddress);
            });
        }
    }
}
