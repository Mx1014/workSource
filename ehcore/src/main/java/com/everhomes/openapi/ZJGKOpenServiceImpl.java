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
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.ebei.EbeiJsonEntity;
import com.everhomes.pmtask.ebei.EbeiResult;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.address.NamespaceBuildingType;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.ShenzhouJsonEntity;
import com.everhomes.rest.openapi.shenzhou.ZJCommunity;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.server.schema.tables.pojos.EhZjSyncdataBackup;
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
        syncIndividuals("0");
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

//        ShenzhouJsonEntity<List<EnterpriseDTO>> entity = JSONObject.parseObject(buildings, new TypeReference<ShenzhouJsonEntity<List<EnterpriseDTO>>>(){});
//
//        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
//            List<EnterpriseDTO> dtos = entity.getData();
//            if(dtos != null && dtos.size() > 0) {
//                syncData(entity, DataType.ENTERPRISE.getCode());
//
//                //数据有下一页则继续请求
//                if(entity.getNextPageOffset() != null) {
//                    syncEnterprises(entity.getNextPageOffset().toString());
//                }
//            }
//
//            //如果到最后一页了，则开始更新到我们数据库中
//            if(entity.getNextPageOffset() == null) {
//                syncDataToDb(DataType.ENTERPRISE.getCode());
//            }
//        }
    }

    private void syncIndividuals(String pageOffset) {
        Map<String, String> params = generateParams(pageOffset);
        String buildings = postToShenzhou(params, SYNC_INDIVIDUALS, null);

        ShenzhouJsonEntity<List<OrganizationOwnerDTO>> entity = JSONObject.parseObject(buildings, new TypeReference<ShenzhouJsonEntity<List<OrganizationOwnerDTO>>>(){});

        if(SUCCESS_CODE.equals(entity.getErrorCode())) {
            List<OrganizationOwnerDTO> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.INDIVIDUAL.getCode());

                //数据有下一页则继续请求
                if(entity.getNextPageOffset() != null) {
                    syncIndividuals(entity.getNextPageOffset().toString());
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getNextPageOffset() == null) {
                syncDataToDb(DataType.INDIVIDUAL.getCode());
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
//            case BUILDING:
//			    syncAllBuildings(namespaceId, backupList);
//                break;
//            case APARTMENT:
//                syncAllApartments(namespaceId, backupList);
//                break;
//            case ENTERPRISE:
//                syncAllEnterprises(namespaceId, backupList);
//                break;
//            case INDIVIDUAL:
//                syncAllIndividuals(namespaceId, backupList);
//                break;

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
                    insertCommunity(NAMESPACE_ID, zjCommunity, defaultForumId, feedbackForumId);
                }else {
                    updateCommunity(community.get(0), zjCommunity);
                }
            }
        }
    }

    private ZJCommunity findFromTheirCommunityList(Community myCommunity, List<ZJCommunity> theirCommunityList) {
        if (theirCommunityList != null) {
            for (ZJCommunity zjCommunity : theirCommunityList) {
                if (myCommunity.getNamespaceCommunityToken().equals(zjCommunity.getCommunityIdentifier())) {
                    zjCommunity.setDealed(true);
                    return zjCommunity;
                }
            }
        }
        return null;
    }

    private void insertCommunity(Integer namespaceId, ZJCommunity zjcommunity, Long defaultForumId, Long feedbackForumId) {
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

        CommunityGeoPoint geoPoint = new CommunityGeoPoint();
        geoPoint.setCommunityId(community.getId());
        geoPoint.setLatitude(zjcommunity.getLatitude());
        geoPoint.setLongitude(zjcommunity.getLongitude());
        String geohash= GeoHashUtils.encode(zjcommunity.getLatitude(), zjcommunity.getLongitude());
        geoPoint.setGeohash(geohash);
        communityProvider.updateCommunityGeoPoint(geoPoint);

    }

//    private void syncAllApartments(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
//        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
//        List<Address> myApartmentList = addressProvider.listAddressByNamespaceType(namespaceId, appNamespaceMapping.getCommunityId(), NamespaceAddressType.SHENZHOU.getCode());
//        List<CustomerApartment> theirApartmentList = mergeBackupList(backupList, CustomerApartment.class);
//        formatCustomerBuildingName(theirApartmentList);
//        List<CustomerBuilding> theirBuildingList = fetchBuildingsFromApartments(theirApartmentList);
//        List<Building> myBuildingList = buildingProvider.listBuildingByNamespaceType(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), NamespaceBuildingType.JINDIE.getCode());
//        dbProvider.execute(s->{
//            syncAllApartments(namespaceId, myApartmentList, theirApartmentList);
//            return true;
//        });
//    }
//
//    private void syncAllApartments(AppNamespaceMapping appNamespaceMapping, List<Address> myApartmentList, List<CustomerApartment> theirApartmentList) {
//        // 如果两边都有，更新；如果我们有，他们没有，删除；
//        for (Address myApartment : myApartmentList) {
//            CustomerApartment customerApartment = findFromTheirApartmentList(myApartment, theirApartmentList);
//            if (customerApartment != null) {
//                updateAddress(myApartment, customerApartment);
//            }else {
//                deleteAddress(myApartment);
//            }
//        }
//        // 如果他们有，我们没有，插入
//        // 因为上面两边都有的都处理过了，所以剩下的就都是他们有我们没有的数据了
//        if (theirApartmentList != null) {
//            for (CustomerApartment customerApartment : theirApartmentList) {
//                if ((customerApartment.getDealed() != null && customerApartment.getDealed().booleanValue() == true) || StringUtils.isBlank(customerApartment.getBuildingName()) || StringUtils.isBlank(customerApartment.getApartmentName())) {
//                    continue;
//                }
//                // 这里要注意一下，不一定就是我们系统没有，有可能是我们系统本来就有，但不是他们同步过来的，这部分也是按更新处理
//                Address address = addressProvider.findAddressByBuildingApartmentName(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerApartment.getBuildingName(), customerApartment.getApartmentName());
//                if (address == null) {
//                    insertAddress(appNamespaceMapping.getNamespaceId(), appNamespaceMapping.getCommunityId(), customerApartment);
//                }else {
//                    updateAddress(address, customerApartment);
//                }
//            }
//        }
//
//        // 同步完地址后更新community表中的门牌总数
//        Community community = communityProvider.findCommunityById(appNamespaceMapping.getCommunityId());
//        Integer count = addressProvider.countApartment(appNamespaceMapping.getCommunityId());
//        if (community.getAptCount().intValue() != count.intValue()) {
//            community.setAptCount(count);
//            communityProvider.updateCommunity(community);
//        }
//    }

}
