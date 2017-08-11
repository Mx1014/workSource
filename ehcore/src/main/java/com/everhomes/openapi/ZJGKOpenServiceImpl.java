package com.everhomes.openapi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.ebei.EbeiJsonEntity;
import com.everhomes.pmtask.ebei.EbeiResult;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.address.*;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.openapi.shenzhou.*;
import com.everhomes.rest.organization.NamespaceOrganizationType;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.techpark.expansion.LeasePromotionStatus;
import com.everhomes.rest.techpark.expansion.LeasePromotionType;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.server.schema.tables.pojos.EhZjSyncdataBackup;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryProvider;
import com.everhomes.techpark.expansion.LeasePromotion;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ying.xiong on 2017/8/7.
 */
public class ZJGKOpenServiceImpl {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZJGKOpenServiceImpl.class);
    private CloseableHttpClient httpclient = null;

    private ExecutorService queueThreadPool = Executors.newFixedThreadPool(1);

    private String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";
    private String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";

    private static final String PAGE_SIZE = "20";
    private static final String SUCCESS_CODE = "200";
    private static final Integer NAMESPACE_ID = 999971;

    private static final String SYNC_COMMUNITIES = "/openapi/syncCommunities";
    private static final String SYNC_BUILDINGS = "/openapi/syncBuildings";
    private static final String SYNC_APARTMENTS = "/openapi/syncApartments";
    private static final String SYNC_ENTERPRISES = "/openapi/syncEnterprises";
    private static final String SYNC_INDIVIDUALS = "/openapi/syncIndividualCustomer";

    @Autowired
    private ConfigurationProvider configurationProvider;

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

  @Scheduled(cron="0 0 1 * * ?")
    public void syncData() {
//        Map<String, String> params = generateParams();
//        String communities = postToShenzhou(params, SYNC_COMMUNITIES, null);
//        String buildings = postToShenzhou(params, SYNC_BUILDINGS, null);
//        String apartments = postToShenzhou(params, SYNC_APARTMENTS, null);
//        String enterprises = postToShenzhou(params, SYNC_ENTERPRISES, null);
//        String individuals = postToShenzhou(params, SYNC_INDIVIDUALS, null);
        syncCommunities("0");
        syncBuildings("0");
        syncApartments("0");
        syncEnterprises("0");
    }

    private void syncCommunities(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String communities = postToShenzhou(params, SYNC_COMMUNITIES, null);

        ShenzhouJsonEntity<List<CommunityDTO>> entity = JSONObject.parseObject(communities, new TypeReference<ShenzhouJsonEntity<List<CommunityDTO>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<CommunityDTO> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.COMMUNITY.getCode());

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncCommunities(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.COMMUNITY.getCode());
            }
        }
    }

    private void syncBuildings(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String buildings = postToShenzhou(params, SYNC_BUILDINGS, null);

        ShenzhouJsonEntity<List<BuildingDTO>> entity = JSONObject.parseObject(buildings, new TypeReference<ShenzhouJsonEntity<List<BuildingDTO>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<BuildingDTO> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.BUILDING.getCode());

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncBuildings(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.BUILDING.getCode());
            }
        }
    }

    private void syncApartments(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String buildings = postToShenzhou(params, SYNC_APARTMENTS, null);

        ShenzhouJsonEntity<List<ApartmentDTO>> entity = JSONObject.parseObject(buildings, new TypeReference<ShenzhouJsonEntity<List<ApartmentDTO>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<ApartmentDTO> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.APARTMENT.getCode());

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncApartments(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.APARTMENT.getCode());
            }
        }
    }

    private void syncEnterprises(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String buildings = postToShenzhou(params, SYNC_ENTERPRISES, null);

        ShenzhouJsonEntity<List<EnterpriseCustomerDTO>> entity = JSONObject.parseObject(buildings, new TypeReference<ShenzhouJsonEntity<List<EnterpriseCustomerDTO>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<EnterpriseCustomerDTO> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.ENTERPRISE.getCode());

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncEnterprises(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.ENTERPRISE.getCode());
            }
        }
    }


    private Map<String, String> generateParams(String pageOffset) {
        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);
        params.put("pageOffset", pageOffset);
        params.put("pageSize", PAGE_SIZE);

        return params;
    }

    private void syncData(ShenzhouJsonEntity entity, Byte dataType) {
        ZjSyncdataBackup backup = new ZjSyncdataBackup();
        backup.setNamespaceId(NAMESPACE_ID);
        backup.setDataType(dataType);
        backup.setNextPageOffset(entity.getNextPageOffset());
        backup.setData(entity.getData().toString());
        backup.setStatus(CommonStatus.ACTIVE.getCode());
        backup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        backup.setCreatorUid(1L);
        backup.setUpdateTime(backup.getCreateTime());

        zjSyncdataBackupProvider.createZjSyncdataBackup(backup);

    }

    private void syncDataToDb(Byte dataType) {
        queueThreadPool.execute(()->{
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("dataType {} enter into thread=================", dataType);
            }

            List<ZjSyncdataBackup> backupList = zjSyncdataBackupProvider.listZjSyncdataBackupByParam(NAMESPACE_ID, dataType);
            if (backupList == null || backupList.isEmpty()) {
                return ;
            }
            try {
                updateAllDate(dataType, NAMESPACE_ID , backupList);
            } finally {
                zjSyncdataBackupProvider.updateZjSyncdataBackupInactive(backupList);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("dataType {} get out thread=================", dataType);
            }
        });
    }

    private String postToShenzhou(Map<String, String> params, String method, Map<String, String> headers) {

        String shenzhouUrl = configurationProvider.getValue(NAMESPACE_ID, "shenzhou.host.url", "");
        HttpPost httpPost = new HttpPost(shenzhouUrl + method);
        CloseableHttpResponse response = null;

        String json = null;
        try {
            StringEntity stringEntity = new StringEntity(params.toString(), "utf8");
            httpPost.setEntity(stringEntity);

            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }

        } catch (Exception e) {
            LOGGER.error("sync from shenzhou request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "sync from shenzhou request error.");
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("sync from shenzhou close instream, response error, param={}", params, e);
                }
            }
        }

        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Data from shenzhou, param={}, json={}", params, json);

        return json;
    }

    //基本思路：来自神州数码的数据：在我们数据中不在同步数据里的，删除；两边都在的，更新；不在我们数据库中，在同步数据里的，插入；
    private void updateAllDate(Byte dataType, Integer namespaceId,
                               List<ZjSyncdataBackup> backupList) {
        DataType zjDataType = DataType.fromCode(dataType);
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
//                syncAllEnterprises(namespaceId, backupList);
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
        Long feedbackForumId = 1L;
        Long organizationId = 1L;
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

    private void syncAllApartments(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        //楼栋的同步按照从门牌中提取来同步
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        List<Address> myApartmentList = addressProvider.listAddressByNamespaceType(namespaceId, NamespaceAddressType.SHENZHOU.getCode());
        List<ZJApartment> mergeApartmentList = mergeBackupList(backupList, ZJApartment.class);
        List<ZJApartment> theirApartmentList = new ArrayList<ZJApartment>();
        if(mergeApartmentList != null && mergeApartmentList.size() > 0) {
            Map<String, Long> communities = communityProvider.listCommunityIdByNamespaceType(namespaceId, NamespaceBuildingType.SHENZHOU.getCode());
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
        // 如果两边都有，更新；如果我们有，他们没有，删除；
        for (Address myApartment : myApartmentList) {
            ZJApartment customerApartment = findFromTheirApartmentList(myApartment, theirApartmentList);
            if (customerApartment != null) {
                updateAddress(myApartment, customerApartment);
            }else {
                deleteAddress(myApartment);
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
                    insertAddress(namespaceId, customerApartment);
                }else {
                    updateAddress(address, customerApartment);
                }
            }
        }

    }

    private void insertAddress(Integer namespaceId, ZJApartment customerApartment) {
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
                leasePromotion.setSubject(apartmentName);
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
                leasePromotion.setRentPosition(buildingName);
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

    private void deleteAddress(Address address) {
        if (CommonStatus.fromCode(address.getStatus()) != CommonStatus.INACTIVE) {
            address.setOperatorUid(1L);
            address.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            address.setStatus(CommonStatus.INACTIVE.getCode());
            addressProvider.updateAddress(address);
        }
    }

    private void updateAddress(Address address, ZJApartment customerApartment) {
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

//    private void syncAllEnterprises(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
//        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
//        List<EnterpriseCustomer> myOrganizationList = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceType(namespaceId, NamespaceCustomerType.SHENZHOU.getCode());
//        List<ZJEnterprise> theirEnterpriseList = mergeBackupList(backupList, ZJEnterprise.class);
//        dbProvider.execute(s->{
//            syncAllEnterprises(namespaceId, myOrganizationList, theirEnterpriseList);
//            return true;
//        });
//    }
//
//    private void syncAllEnterprises(Integer namespaceId, List<EnterpriseCustomer> myOrganizationList, List<ZJEnterprise> theirEnterpriseList) {
//        // 如果两边都有，更新；如果我们有，他们没有，删除；
//        for (EnterpriseCustomer myOrganization : myOrganizationList) {
//            ZJEnterprise zjEnterprise = findFromTheirEnterpriseList(myOrganization, theirEnterpriseList);
//            if (zjCommunity != null) {
//                updateCommunity(myCommunity, zjCommunity);
//            }else {
//                deleteCommunity(myCommunity);
//            }
//        }
//        // 如果他们有，我们没有，插入
//        // 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
//        if (theirCommunityList != null) {
//            for (ZJCommunity zjCommunity : theirCommunityList) {
//                if ((zjCommunity.getDealed() != null && zjCommunity.getDealed().booleanValue() == true)) {
//                    continue;
//                }
//                // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
//                List<Community> community = communityProvider.listCommunityByNamespaceIdAndName(NAMESPACE_ID, zjCommunity.getCommunityName());
//                if (community == null) {
//                    insertCommunity(NAMESPACE_ID, zjCommunity, defaultForumId, feedbackForumId, organizationId);
//                }else {
//                    updateCommunity(community.get(0), zjCommunity);
//                }
//            }
//        }
//    }
//
//    private ZJEnterprise findFromTheirEnterpriseList(EnterpriseCustomer myOrganization, List<ZJEnterprise> theirEnterpriseList) {
//        if (theirEnterpriseList != null) {
//            for (ZJEnterprise zjEnterprise : theirEnterpriseList) {
//                if (NamespaceCustomerType.SHENZHOU.getCode().equals(myCommunity.getNamespaceCommunityType())
//                        && myCommunity.getNamespaceCommunityToken().equals(zjCommunity.getCommunityIdentifier())) {
//                    zjCommunity.setDealed(true);
//                    return zjCommunity;
//                }
//            }
//        }
//        return null;
//    }
//
//    private void insertCommunity(Integer namespaceId, ZJCommunity zjcommunity, Long defaultForumId, Long feedbackForumId, Long organizationId) {
//        if (StringUtils.isBlank(zjcommunity.getCommunityIdentifier())) {
//            return;
//        }
//        Community community = new Community();
//        community.setName(zjcommunity.getCommunityName());
//        community.setCommunityType(zjcommunity.getCommunityType());
//        community.setNamespaceId(namespaceId);
//        community.setAddress(zjcommunity.getAddress());
//        community.setZipcode(zjcommunity.getZipcode());
//        community.setDescription(zjcommunity.getDescription());
//        community.setAptCount(zjcommunity.getAptCount());
//        community.setNamespaceCommunityToken(zjcommunity.getCommunityIdentifier());
//        community.setNamespaceCommunityType(NamespaceCommunityType.SHENZHOU.getCode());
//        community.setAreaSize(zjcommunity.getAreaSize());
//        community.setChargeArea(zjcommunity.getChargeArea());
//        community.setSharedArea(zjcommunity.getSharedArea());
//        community.setRentArea(zjcommunity.getRentArea());
//        community.setBuildArea(zjcommunity.getBuildArea());
//
//        Region city = regionProvider.findRegionByName(namespaceId, zjcommunity.getCityName());
//        community.setCityId(city.getId());
//        community.setCityName(city.getName());
//        Region area = regionProvider.findRegionByName(namespaceId, zjcommunity.getAreaName());
//        community.setAreaId(area.getId());
//        community.setAreaName(area.getName());
//
//        community.setDefaultForumId(defaultForumId);
//        community.setFeedbackForumId(feedbackForumId);
//        community.setStatus(CommonStatus.ACTIVE.getCode());
//        communityProvider.createCommunity(1L, community);
//        communitySearcher.feedDoc(community);
//
//        CommunityGeoPoint geoPoint = new CommunityGeoPoint();
//        geoPoint.setCommunityId(community.getId());
//        geoPoint.setLatitude(zjcommunity.getLatitude());
//        geoPoint.setLongitude(zjcommunity.getLongitude());
//        String geohash= GeoHashUtils.encode(zjcommunity.getLatitude(), zjcommunity.getLongitude());
//        geoPoint.setGeohash(geohash);
//        communityProvider.createCommunityGeoPoint(geoPoint);
//
//        OrganizationCommunity departmentCommunity = new OrganizationCommunity();
//        departmentCommunity.setCommunityId(community.getId());
//        departmentCommunity.setOrganizationId(organizationId);
//        organizationProvider.createOrganizationCommunity(departmentCommunity);
//
//        NamespaceResource resource = new NamespaceResource();
//        resource.setNamespaceId(namespaceId);
//        resource.setResourceType(NamespaceResourceType.COMMUNITY.getCode());
//        resource.setResourceId(community.getId());
//        namespaceResourceProvider.createNamespaceResource(resource);
//    }
//
//    private void deleteCommunity(Community community) {
//        if (CommonStatus.fromCode(community.getStatus()) != CommonStatus.INACTIVE) {
//            community.setOperatorUid(1L);
//            community.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//            community.setStatus(CommonStatus.INACTIVE.getCode());
//            communityProvider.updateCommunity(community);
//        }
//    }
//
//    private void updateCommunity(Community community, ZJCommunity zjcommunity) {
//        community.setOperatorUid(1L);
//        community.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        community.setName(zjcommunity.getCommunityName());
//        community.setCommunityType(zjcommunity.getCommunityType());
//        community.setAddress(zjcommunity.getAddress());
//        community.setZipcode(zjcommunity.getZipcode());
//        community.setDescription(zjcommunity.getDescription());
//        community.setAptCount(zjcommunity.getAptCount());
//        community.setNamespaceCommunityToken(zjcommunity.getCommunityIdentifier());
//        community.setAreaSize(zjcommunity.getAreaSize());
//        community.setChargeArea(zjcommunity.getChargeArea());
//        community.setSharedArea(zjcommunity.getSharedArea());
//        community.setRentArea(zjcommunity.getRentArea());
//        community.setBuildArea(zjcommunity.getBuildArea());
//
//        Region city = regionProvider.findRegionByName(NAMESPACE_ID, zjcommunity.getCityName());
//        community.setCityId(city.getId());
//        community.setCityName(city.getName());
//        Region area = regionProvider.findRegionByName(NAMESPACE_ID, zjcommunity.getAreaName());
//        community.setAreaId(area.getId());
//        community.setAreaName(area.getName());
//
//        community.setStatus(CommonStatus.ACTIVE.getCode());
//        communityProvider.updateCommunity(community);
//        communitySearcher.feedDoc(community);
//
//        CommunityGeoPoint geoPoint = communityProvider.findCommunityGeoPointByCommunityId(community.getId());
//        if(geoPoint != null) {
//            geoPoint.setLatitude(zjcommunity.getLatitude());
//            geoPoint.setLongitude(zjcommunity.getLongitude());
//            String geohash= GeoHashUtils.encode(zjcommunity.getLatitude(), zjcommunity.getLongitude());
//            geoPoint.setGeohash(geohash);
//            communityProvider.updateCommunityGeoPoint(geoPoint);
//        } else {
//            geoPoint = new CommunityGeoPoint();
//            geoPoint.setCommunityId(community.getId());
//            geoPoint.setLatitude(zjcommunity.getLatitude());
//            geoPoint.setLongitude(zjcommunity.getLongitude());
//            String geohash= GeoHashUtils.encode(zjcommunity.getLatitude(), zjcommunity.getLongitude());
//            geoPoint.setGeohash(geohash);
//            communityProvider.createCommunityGeoPoint(geoPoint);
//        }
//
//    }
}
