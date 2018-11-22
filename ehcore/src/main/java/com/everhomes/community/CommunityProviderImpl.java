// @formatter:off
package com.everhomes.community;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.Organization;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.enterprise.listEnterpriseNoReleaseWithCommunityIdCommand;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.*;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
import org.jooq.impl.DSL;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultRecordMapper;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import ch.hsr.geohash.GeoHash;

import com.everhomes.address.Address;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.BuildingAdminStatus;
import com.everhomes.rest.community.ResourceCategoryStatus;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.enterprise.EnterpriseContactStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAddresses;

import com.everhomes.server.schema.tables.daos.EhBuildingAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhBuildingsDao;
import com.everhomes.server.schema.tables.daos.EhCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhCommunityGeopointsDao;
import com.everhomes.server.schema.tables.daos.EhResourceCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhResourceCategoryAssignmentsDao;
import com.everhomes.server.schema.tables.pojos.EhBuildingAttachments;
import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhCommunityGeopoints;
import com.everhomes.server.schema.tables.pojos.EhResourceCategories;
import com.everhomes.server.schema.tables.pojos.EhResourceCategoryAssignments;
import com.everhomes.server.schema.tables.pojos.EhUsers;

import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;

import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.Tuple;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.*;
import org.jooq.impl.DefaultRecordMapper;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import javax.naming.NamingEnumeration;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommunityProviderImpl implements CommunityProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequnceProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void createCommunity(Long creatorId, Community community) {
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        long id = this.dbProvider.allocPojoRecordId(EhCommunities.class);
        //long id = shardingProvider.allocShardableContentId(EhCommunities.class).second();

        community.setId(id);

        if(community.getCommunityNumber() == null){
            community.setCommunityNumber("L"  + id);
        }
        community.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        community.setCreatorUid(creatorId);
        community.setUuid(UUID.randomUUID().toString());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, id));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.insert(community);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCommunities.class, null);
    }

    @Caching(evict = { @CacheEvict(value="Community", key="#community.id"),
            @CacheEvict(value="Communities", key="#community.id")} )
    @Override
    public void updateCommunity(Community community) {
        assert(community.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, community.getId()));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.update(community);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunities.class, community.getId());
    }

    @Caching(evict = { @CacheEvict(value="Community", key="#community.id"),
            @CacheEvict(value="Communities", key="#community.id")} )
    @Override
    public void deleteCommunity(Community community) {
        assert(community.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, community.getId()));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.deleteById(community.getId());

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunities.class, community.getId());
    }

    @Override
    public void deleteCommunityById(long id) {
        CommunityProvider self = PlatformContext.getComponent(CommunityProvider.class);

        Community community = self.findCommunityById(id);
        if(community != null)
            self.deleteCommunity(community);
    }

    @Cacheable(value="Community", key="#id" , unless="#result == null")
    @Override
    public Community findCommunityById(Long id) {
        final Community[] result = new Community[1];
    	
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
            (DSLContext context, Object reducingContext) -> {
                EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
                result[0] = ConvertHelper.convert(dao.findById(id), Community.class);

                if(result[0] != null) {
                    return false;
                }

                return true;
            });

        return result[0];
    }

    @Cacheable(value="CommunityGeoList", key="#id" , unless="#result == null")
    @Override
    public List<CommunityGeoPoint> listCommunityGeoPoints(long id) {
        List<CommunityGeoPoint> l = new ArrayList<>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, id));
        context.select().from(Tables.EH_COMMUNITY_GEOPOINTS)
            .where(Tables.EH_COMMUNITY_GEOPOINTS.COMMUNITY_ID.equal(id))
            .fetch().map((r) -> {
                l.add(ConvertHelper.convert(r, CommunityGeoPoint.class));
               return null;
            });

        return l;
    }

    @Caching(evict = { @CacheEvict(value="CommunityGeoList", key="#geoPoint.communityId") } )
    @Override
    public void createCommunityGeoPoint(CommunityGeoPoint geoPoint) {
        assert(geoPoint.getCommunityId() != null);

        long id = this.sequnceProvider.getNextSequence(
            NameMapper.getSequenceDomainFromTablePojo(EhCommunityGeopoints.class));
        geoPoint.setId(id);

        DSLContext context = this.dbProvider.getDslContext(
            AccessSpec.readWriteWith(EhCommunities.class, geoPoint.getCommunityId()));
        EhCommunityGeopointsDao dao = new EhCommunityGeopointsDao(context.configuration());

        dao.insert(geoPoint);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCommunityGeopoints.class, null);
    }

    @Caching(evict = { @CacheEvict(value="CommunityGeoPoints", key="#geoPoint.id"),
            @CacheEvict(value="CommunityGeoList", key="#geoPoint.communityId") } )
    @Override
    public void updateCommunityGeoPoint(CommunityGeoPoint geoPoint) {
        assert(geoPoint.getCommunityId() != null);

        DSLContext context = this.dbProvider.getDslContext(
            AccessSpec.readWriteWith(EhCommunities.class, geoPoint.getCommunityId()));
        EhCommunityGeopointsDao dao = new EhCommunityGeopointsDao(context.configuration());

        dao.update(geoPoint);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityGeopoints.class, geoPoint.getId());
    }

    @Caching(evict = { @CacheEvict(value="CommunityGeoPoints", key="#geoPoint.id"),
            @CacheEvict(value="CommunityGeoList", key="#geoPoint.communityId") } )
    @Override
    public void deleteCommunityGeoPoint(CommunityGeoPoint geoPoint) {
        assert(geoPoint.getCommunityId() != null);

        DSLContext context = this.dbProvider.getDslContext(
            AccessSpec.readWriteWith(EhCommunities.class, geoPoint.getCommunityId()));
        EhCommunityGeopointsDao dao = new EhCommunityGeopointsDao(context.configuration());

        dao.deleteById(geoPoint.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityGeopoints.class, geoPoint.getId());
    }

    @Cacheable(value="CommunityGeoPoints", key="#id")
    @Override
    public CommunityGeoPoint findCommunityGeoPointById(long id) {
        final CommunityGeoPoint[] result = new CommunityGeoPoint[1];

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
            (DSLContext context, Object reducingContext) -> {
                EhCommunityGeopointsDao dao = new EhCommunityGeopointsDao(context.configuration());
                result[0] = ConvertHelper.convert(dao.findById(id), CommunityGeoPoint.class);

                if(result[0] != null) {
                    return false;
                }

                return true;
            });

        return result[0];
    }

    @Cacheable(value="nearbyCommunities", key="{#namespaceId, #communityId}" ,unless="#result.size() == 0")
    @Override
    public List<Community> findNearyByCommunityById(Integer namespaceId, long communityId){
        long startTime = System.currentTimeMillis();
//        CommunityProvider self = PlatformContext.getComponent(CommunityProvider.class);
//        List<CommunityGeoPoint> pointList = self.listCommunityGeoPoints(communityId);
//        List<Community> results = new ArrayList<Community>();
//        List<CommunityGeoPoint> nearyByPointList = new ArrayList<CommunityGeoPoint>();

//        pointList.forEach((CommunityGeoPoint p) ->{
//            List<CommunityGeoPoint> l = self.findCommunityGeoPointByGeoHash(p.getLatitude(),p.getLongitude());
//            if(l != null && !l.isEmpty())
//                nearyByPointList.addAll(l);
//        });
//        nearyByPointList = findCommunityGeoPointByGeoHash(pointList);
//        
//        List<Long> communityIds = new ArrayList<Long>();
//        if(nearyByPointList == null || nearyByPointList.isEmpty()) {
//            return results;
//        }
//        
//        for(CommunityGeoPoint p : nearyByPointList){
//            if(!communityIds.contains(p.getCommunityId().longValue()))
//                communityIds.add(p.getCommunityId());
//        }
//        
//        communityIds.forEach((Long id) ->{
//            Community community = self.findCommunityById(id);
//            if(community != null)
//                results.add(community);
//        });

        // 获取周边社区时分两种情况：1）若数据库有指定则优先使用数据库指定的；2）数据库没有时则进行计算取得；
        // 由于运营的要求，需要能够手工调整周边社区的范围 by lqs 20151022
        CommunityProvider self = PlatformContext.getComponent(CommunityProvider.class);
        List<Community> results = self.findFixNearbyCommunityById(namespaceId, communityId);
        if(results == null || results.size() == 0) {
            results = self.calculateNearbyCommunityByGeoPoints(communityId, namespaceId);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("Find nearby community elapse=" + (endTime - startTime));
        return results;
    }

    @Cacheable(value="nearbyCommunityMap", key="{#namespaceId, #communityId}" ,unless="#result.size() == 0")
    @Override
    public List<NearbyCommunityMap> findNearbyCommunityMap(Integer namespaceId, Long communityId) {
        List<NearbyCommunityMap> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, communityId));
        context.select().from(Tables.EH_NEARBY_COMMUNITY_MAP)
            .where(Tables.EH_NEARBY_COMMUNITY_MAP.COMMUNITY_ID.equal(communityId))
            .and(Tables.EH_NEARBY_COMMUNITY_MAP.NAMESPACE_ID.eq(namespaceId))
            .fetch().map((r) -> {
                list.add(ConvertHelper.convert(r, NearbyCommunityMap.class));
               return null;
            });

        return list;
    }

    @Cacheable(value="fixNearbyCommunities", key="{#namespaceId, #communityId}" ,unless="#result.size() == 0")
    @Override
    public List<Community> findFixNearbyCommunityById(Integer namespaceId, Long communityId) {
        long startTime = System.currentTimeMillis();
        List<Community> results = new ArrayList<Community>();

        CommunityProvider self = PlatformContext.getComponent(CommunityProvider.class);
        List<NearbyCommunityMap> list = self.findNearbyCommunityMap(namespaceId, communityId);

        List<Long> communityIds = new ArrayList<Long>();
        if(list != null) {
            for(NearbyCommunityMap m : list){
                if(!communityIds.contains(m.getNearbyCommunityId()))
                    communityIds.add(m.getNearbyCommunityId());
            }
        }

        communityIds.forEach((Long id) ->{
            Community community = self.findCommunityById(id);
            if(community != null)
                results.add(community);
        });

        if(LOGGER.isDebugEnabled()) {
            List<Long> ids = new ArrayList<Long>();
            for(Community community : results) {
                ids.add(community.getId());
            }
            LOGGER.debug("Find fix nearby communities, communityId=" + communityId + ", nearbyCommunities=" + ids);
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("Find fix nearby communities, communityId=" + communityId + ", elapse=" + (endTime - startTime));

        return results;
    }

    @Cacheable(value="calNearbyCommunities", key="#communityId" ,unless="#result.size() == 0")
    @Override
    public List<Community> calculateNearbyCommunityByGeoPoints(Long communityId, Integer namespaceId) {
        long startTime = System.currentTimeMillis();
        List<Community> results = new ArrayList<Community>();

        CommunityProvider self = PlatformContext.getComponent(CommunityProvider.class);
        List<CommunityGeoPoint> pointList = self.listCommunityGeoPoints(communityId);
        List<CommunityGeoPoint> nearyByPointList = findCommunityGeoPointByGeoHash(pointList);

        List<Long> communityIds = new ArrayList<Long>();
        if(nearyByPointList != null) {
            for(CommunityGeoPoint p : nearyByPointList){
                if(!communityIds.contains(p.getCommunityId().longValue()))
                    communityIds.add(p.getCommunityId());
            }
        }
        communityIds.forEach((Long id) ->{
            Community community = self.findCommunityById(id);
            if(community != null && community.getNamespaceId().equals(namespaceId))
                results.add(community);
        });

        if(LOGGER.isDebugEnabled()) {
            List<Long> ids = new ArrayList<Long>();
            for(Community community : results) {
                ids.add(community.getId());
            }
            LOGGER.debug("Calculate nearby communities 20160815, communityId=" + communityId + ", nearbyCommunities=" + ids);
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("Calculate nearby communities 20160815, communityId=" + communityId + ", elapse=" + (endTime - startTime));

        return results;
    }

    private List<CommunityGeoPoint> findCommunityGeoPointByGeoHash(List<CommunityGeoPoint> list) {

        if(list == null || list.isEmpty()) return null;

        Condition c = null;
        for(CommunityGeoPoint p : list){
            List<String> geoHashList = getGeoHashCodeList(p.getLatitude(), p.getLongitude());
            for(String geoHashStr : geoHashList){
                if(c == null) {
                    c = Tables.EH_COMMUNITY_GEOPOINTS.GEOHASH.like(geoHashStr + "%");
                } else {
                    c = c.or(Tables.EH_COMMUNITY_GEOPOINTS.GEOHASH.like(geoHashStr + "%"));
                }
            }
        }
        final Condition condition = c;

        List<CommunityGeoPoint> results = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), results, (DSLContext context, Object reducingContext) -> {
            SelectQuery<?> query = context.selectQuery(Tables.EH_COMMUNITY_GEOPOINTS);

            query.addConditions(condition);
            query.fetch().map((r) -> {
                results.add(ConvertHelper.convert(r, CommunityGeoPoint.class));
                return null;
            });
            return true;
        });
        return results;
    }

    private List<String> getGeoHashCodeList(double latitude, double longitude){

        GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
        GeoHash[] adjacents = geo.getAdjacent();
        List<String> geoHashCodes = new ArrayList<String>();
        geoHashCodes.add(geo.toBase32());
        for(GeoHash g : adjacents) {
            geoHashCodes.add(g.toBase32());
        }
        return geoHashCodes;
    }

    @Override
    public List<CommunityGeoPoint> findCommunityGeoPointByGeoHash(double latitude, double longitude, int geoHashLength) {
        List<CommunityGeoPoint> l = new ArrayList<>();
        String geoHashStr = GeoHashUtils.encode(latitude, longitude).substring(0, geoHashLength);
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext)-> {

                    String likeVal = geoHashStr + "%";
                    context.select()
                        .from(Tables.EH_COMMUNITY_GEOPOINTS)
                        .where(Tables.EH_COMMUNITY_GEOPOINTS.GEOHASH.like(likeVal))
                        .fetch().map((r) -> {
                            l.add(ConvertHelper.convert(r, CommunityGeoPoint.class));
                            return null;
                        });

                return true;
            });
        return l;
    }

    @Override
    public List<Community> listAllCommunities(long pageOffset, long pageSize) {

 //   	int namespaceId =UserContext.getCurrentNamespaceId(null);
        final List<Community> results = new ArrayList<>();

        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        if(offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
                    (DSLContext context, Object reducingContext)-> {

                    Long count = context.selectCount().from(Tables.EH_COMMUNITIES)
                            .where(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()))
//                            .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                            .fetchOne(0, Long.class);

                    countsInShards.add(count);
                    return true;
                });

            targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
        }
        if(targetShard.first() < 0)
            return results;

        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        final Tuple<Integer, Long> fallingShard = targetShard;

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), currentShard,
            (DSLContext context, Object reducingContext)-> {
                int[] current = (int[])reducingContext;
                if(current[0] < fallingShard.first()) {
                    current[0] += 1;
                    return true;
                }

                long off = 0;
                if(current[0] == fallingShard.first())
                    off = fallingShard.second();

                context.select().from(Tables.EH_COMMUNITIES)
                    .where(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()))
//                    .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                    .limit((int)pageSize).offset((int)off)
                    .fetch().map((r) -> {
                        Community community = ConvertHelper.convert(r, Community.class);
                        if(results.size() <= pageSize + 1)
                            results.add(community);
                        return null;
                });

            return true;
        });

        if(results.size() > pageSize) {
            results.remove(results.size() - 1);
            return results;
        }

        return results;
    }

    @Override
    public List<Community> listNamespaceCommunities(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);

        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));

        List<Community> communities = new ArrayList<>();
        query.fetch().map((r) -> {
            communities.add(ConvertHelper.convert(r, Community.class));
            return null;
        });

        return communities;
    }

    @Override
    public List<Community> listAllCommunitiesWithNamespaceToken() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);

        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TOKEN.isNotNull().or(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TOKEN.ne("")));

        List<Community> communities = new ArrayList<>();
        query.fetch().map((r) -> {
            communities.add(ConvertHelper.convert(r, Community.class));
            return null;
        });

        return communities;
    }

    @Override
    public List<Community> listCommunities(Integer namespaceId, ListingLocator locator, Integer pageSize,
                                                   ListingQueryBuilderCallback queryBuilderCallback) {
        pageSize = pageSize +1;
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        final List<Community> communities = new ArrayList<Community>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_COMMUNITIES.ID.lt(locator.getAnchor()));

        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_COMMUNITIES.ID.desc());
        query.addLimit(pageSize);

        query.fetch().map((r) -> {
            communities.add(ConvertHelper.convert(r, Community.class));
            return null;
        });

        locator.setAnchor(null);
        if(communities.size() == pageSize) {
            communities.remove(communities.size() - 1);
            locator.setAnchor(communities.get(communities.size() -1).getId());
        }
        return communities;
    }

    @Override
    public List<Community> listCommunitiesByStatus(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
    	int namespaceId =UserContext.getCurrentNamespaceId(null);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));

        final List<Community> communities = new ArrayList<Community>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor()));

        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_COMMUNITIES.ID.asc());
        query.addLimit(count);

        query.fetch().map((r) -> {
            communities.add(ConvertHelper.convert(r, Community.class));
            return null;
        });

        if(communities.size() > 0) {
            locator.setAnchor(communities.get(communities.size() -1).getId());
        }

        return communities;
    }

    @Override
    public Community findCommunityByAreaIdAndName(Long areaId, String name) {
    	int namespaceId =UserContext.getCurrentNamespaceId(null);
    	 final Community[] result = new Community[1];

         this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
             (DSLContext context, Object reducingContext) -> {
            	 context.select().from(Tables.EH_COMMUNITIES)
        		 .where(Tables.EH_COMMUNITIES.AREA_ID.eq(areaId))
        		 .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
        		.and(Tables.EH_COMMUNITIES.NAME.eq(name)).fetch().map(r ->{
        			return result[0] = ConvertHelper.convert(r,Community.class);
        		});
            	 return true;


//                 result[0] = ConvertHelper.convert(context.select().from(Tables.EH_COMMUNITIES)
//                		 .where(Tables.EH_COMMUNITIES.AREA_ID.eq(areaId))
//                 		.and(Tables.EH_COMMUNITIES.NAME.eq(name)).fetch(), Community.class);
//             
//                 if(result[0] != null) {
//                     return false;
//                 }
//                 
//                 return true;
             });

         return result[0];
    }

    @Override
    public List<Community> findCommunitiesByNameAndCityId(String name, long cityId, int namespaceId) {
//    	int namespaceId =UserContext.getCurrentNamespaceId(null);
        List<Community> result = new ArrayList<Community>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
                (DSLContext context, Object reducingContext) -> {
                    context.select().from(Tables.EH_COMMUNITIES)
                    .where(Tables.EH_COMMUNITIES.CITY_ID.eq(cityId))
                    .and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                   .and(Tables.EH_COMMUNITIES.NAME.like("%" + name + "%")).fetch().map(r ->{
                       result.add(ConvertHelper.convert(r,Community.class));
                       return null;
                   });
                    return true;
                });
        return result;
    }

    @Override
    public List<Community> findCommunitiesByNameCityIdAreaId(String name, Long cityId,Long areaId) {
    	int namespaceId =UserContext.getCurrentNamespaceId(null);
        List<Community> result = new ArrayList<Community>();
        LOGGER.info("findCommunitiesByNameCityIdAreaId-areaId="+areaId+",cityId="+cityId+",name="+name);

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
                (DSLContext context, Object reducingContext) -> {

                	Condition condition = Tables.EH_COMMUNITIES.NAME.eq(name);
                    if(cityId != null)
                    	condition = condition.and(Tables.EH_COMMUNITIES.CITY_ID.eq(cityId));
                    if(areaId != null)
                    	condition = condition.and(Tables.EH_COMMUNITIES.AREA_ID.eq(areaId));

                    condition = condition.and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
                    context.select().from(Tables.EH_COMMUNITIES)
                    .where(condition).fetch().map(r ->{
                    		result.add(ConvertHelper.convert(r,Community.class));
                    		return null;
                   });
                    return true;
                });
        return result;
    }


    @Override
    public List<Community> findCommunitiesByIds(List<Long> ids) {
        List<Community> result = new ArrayList<Community>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
                (DSLContext context, Object reducingContext) -> {
                    context.select().from(Tables.EH_COMMUNITIES)
                    .where(Tables.EH_COMMUNITIES.ID.in(ids))
                    .fetch().map(r ->{
                       result.add(ConvertHelper.convert(r,Community.class));
                       return null;
                   });
                    return true;
                });
        return result;
    }

    @Override
    public Community findCommunityByUuid(String uuid) {
        final Community[] result = new Community[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
                (DSLContext context, Object reducingContext) -> {
                    context.select().from(Tables.EH_COMMUNITIES)
                    .where(Tables.EH_COMMUNITIES.UUID.eq(uuid))
                    .fetch().map(r ->{
                       result[0] = ConvertHelper.convert(r,Community.class);
                       return null;
                   });
                    return true;
                });
        return result[0];
    }

	@Override
	public List<Community> listCommunitiesByKeyWord(ListingLocator locator,
			int count, String keyword, Integer namespaceId, Byte communityType) {
        if(namespaceId == null){
            namespaceId =UserContext.getCurrentNamespaceId(null);
        }
		final List<Community> communities = new ArrayList<Community>();
		Condition cond = Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId);
		cond = cond.and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
		if(null != locator.getAnchor()){
			cond = cond.and(Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor()));
		}
		if(communityType != null){
            cond = cond.and(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(communityType));
        }
		
		if(!StringUtils.isEmpty(keyword)){
			cond = cond.and(Tables.EH_COMMUNITIES.NAME.like('%'+keyword+'%').or(Tables.EH_COMMUNITIES.ALIAS_NAME.like('%'+keyword+'%')));
		}
		Condition condition = cond;
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
				(DSLContext context, Object reducingContext) -> {

					context.select().from(Tables.EH_COMMUNITIES)
					.where(condition)
					.limit(count)
					.fetch().map((r) -> {
						communities.add(ConvertHelper.convert(r, Community.class));
						return null;
					});

					return true;
				});

		return communities;
	}

	@Override
	public List<Building> ListBuildingsByCommunityId(ListingLocator locator, int count, Long communityId, Integer namespaceId, String keyword) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, locator.getEntityId()));
		List<Building> buildings = new ArrayList<Building>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_BUILDINGS.DEFAULT_ORDER.lt(locator.getAnchor()));
        }

        if (null != communityId) {
            query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
        }
        if (!StringUtils.isBlank(keyword)) {
            query.addConditions(Tables.EH_BUILDINGS.NAME.like("%" + keyword + "%"));
        }
        if (null != namespaceId) {
            query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        }
        query.addConditions(Tables.EH_BUILDINGS.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_BUILDINGS.DEFAULT_ORDER.desc());
        query.addLimit(count);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query buildings by count, sql=" + query.getSQL());
            LOGGER.debug("Query buildings by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhBuildingsRecord record) -> {
        	buildings.add(ConvertHelper.convert(record, Building.class));
        	return null;
        });

        if(buildings.size() > 0) {
            locator.setAnchor(buildings.get(buildings.size() -1).getDefaultOrder());
        }

		return buildings;
	}

    @Override
    public List<Building> ListBuildingsByCommunityId(ListingLocator locator, int count, Long communityId, Integer namespaceId, String keyword, Timestamp lastUpdateTime) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, locator.getEntityId()));
        List<Building> buildings = new ArrayList<Building>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_BUILDINGS.DEFAULT_ORDER.lt(locator.getAnchor()));
        }

        if (null != communityId) {
            query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
        }
        if (!StringUtils.isBlank(keyword)) {
            query.addConditions(Tables.EH_BUILDINGS.NAME.like("%" + keyword + "%"));
        }
        if (null != namespaceId) {
            query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        }

        if(lastUpdateTime != null) {
            query.addConditions(Tables.EH_BUILDINGS.CREATE_TIME.gt(lastUpdateTime)
                    .or(Tables.EH_BUILDINGS.OPERATE_TIME.gt(lastUpdateTime)));
        }

        query.addConditions(Tables.EH_BUILDINGS.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_BUILDINGS.DEFAULT_ORDER.desc());
        query.addLimit(count);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query buildings by count, sql=" + query.getSQL());
            LOGGER.debug("Query buildings by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhBuildingsRecord record) -> {
            buildings.add(ConvertHelper.convert(record, Building.class));
            return null;
        });

        if(buildings.size() > 0) {
            locator.setAnchor(buildings.get(buildings.size() -1).getDefaultOrder());
        }

        return buildings;
    }

    @Override
    public List<Building> ListBuildingsBykeywordAndNameSpace(Integer namespaceId, String keyword) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBuildings.class));
        List<Building> buildings = new ArrayList<Building>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);

        query.addConditions(Tables.EH_BUILDINGS.NAME.like("%" + keyword + "%"));
        query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));

        query.addConditions(Tables.EH_BUILDINGS.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_BUILDINGS.DEFAULT_ORDER.desc());

        query.fetch().map((EhBuildingsRecord record) -> {
            buildings.add(ConvertHelper.convert(record, Building.class));
            return null;
        });

        return buildings;
    }

	@Override
	public Building findBuildingById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBuildings.class, id));
        EhBuildingsDao dao = new EhBuildingsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Building.class);
	}

	@Override
	public Building findBuildingByCommunityIdAndName(long communityId, String buildingName) {
//		int namespaceId = UserContext.getCurrentNamespaceId(null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBuildings.class));
		
		//产品确认过的只用楼栋名称查，不用去查简称
		/*Condition cond = Tables.EH_BUILDINGS.NAME.eq(buildingName);
		cond = cond.or(Tables.EH_BUILDINGS.ALIAS_NAME.eq(buildingName));*/
		SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);
		query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
//		query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_BUILDINGS.NAME.eq(buildingName));
		//查询“未删除”状态的楼栋  by tangcen 2018年8月5日15:40:02
		query.addConditions(Tables.EH_BUILDINGS.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()));

        LOGGER.debug("findBuildingByCommunityIdAndName, sql=" + query.getSQL());
        LOGGER.debug("findBuildingByCommunityIdAndName, bindValues=" + query.getBindValues());
        return ConvertHelper.convert(query.fetchOne(), Building.class);
	}

    @Override
    public Building findBuildingByCommunityIdAndNumber(Long communityId, String buildingNumber) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBuildings.class));
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);
        query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_BUILDINGS.BUILDING_NUMBER.eq(buildingNumber));
        query.addConditions(Tables.EH_BUILDINGS.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()));

        List<Building> buildings = query.fetchInto(Building.class);
        if (buildings!=null && buildings.size()>0) {
			return buildings.get(0);
		}
        return null;
    }

    @Override
    public Community findCommunityByCommunityNumber(String communityNumber, Integer namespaceId) {
        return null;
    }

    @Override
	public void populateBuildingAttachments(final Building building) {
        if(building == null) {
            return;
        } else {
            List<Building> buildings = new ArrayList<Building>();
            buildings.add(building);

            populateBuildingAttachments(buildings);
        }
    }

	@Override
    public void populateBuildingAttachments(final List<Building> buildings) {
        if(buildings == null || buildings.size() == 0) {
            return;
        }

        final List<Long> buildingIds = new ArrayList<Long>();
        final Map<Long, Building> mapBuildings = new HashMap<Long, Building>();

        for(Building building: buildings) {
        	buildingIds.add(building.getId());
        	mapBuildings.put(building.getId(), building);
        }
        
        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhBuildings.class, buildingIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhBuildings.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhBuildings.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhBuildingAttachmentsRecord> query = context.selectQuery(Tables.EH_BUILDING_ATTACHMENTS);
            query.addConditions(Tables.EH_BUILDING_ATTACHMENTS.BUILDING_ID.in(buildingIds));
            query.fetch().map((EhBuildingAttachmentsRecord record) -> {
                Building building = mapBuildings.get(record.getBuildingId());
                assert(building != null);
                building.getAttachments().add(ConvertHelper.convert(record, BuildingAttachment.class));

                return null;
            });
            return true;
        });
    }

	@Override
	public Building createBuilding(Long creatorId, Building building) {

		long id = this.sequnceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBuildings.class));

		building.setId(id);
        building.setDefaultOrder(id);
		building.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		building.setCreatorUid(creatorId);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhBuildings.class, id));
        EhBuildingsDao dao = new EhBuildingsDao(context.configuration());
        dao.insert(building);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBuildings.class, null);

        return building;
	}

	@Override
	public void updateBuilding(Building building) {

		assert(building.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhBuildings.class, building.getId()));
        EhBuildingsDao dao = new EhBuildingsDao(context.configuration());

        dao.update(building);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBuildings.class, building.getId());
	}

	@Override
	public void deleteBuilding(Building building) {

		assert(building.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhBuildings.class, building.getId()));
        EhBuildingsDao dao = new EhBuildingsDao(context.configuration());
        dao.deleteById(building.getId());

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBuildings.class, building.getId());
	}

	@Override
	public void createBuildingAttachment(BuildingAttachment attachment) {

		assert(attachment.getBuildingId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhBuildings.class, attachment.getBuildingId()));
        long id = this.sequnceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBuildingAttachments.class));
        attachment.setId(id);

        EhBuildingAttachmentsDao dao = new EhBuildingAttachmentsDao(context.configuration());
        dao.insert(attachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBuildingAttachments.class, null);
	}

	@Override
	public Boolean verifyBuildingName(Long communityId, String buildingName) {
		int namespaceId = UserContext.getCurrentNamespaceId();

        final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhBuildings.class), null,
                (DSLContext context, Object reducingContext)-> {

                    SelectJoinStep<Record1<Integer>> query = context.selectCount()
                            .from(Tables.EH_BUILDINGS);

                    Condition condition = Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId);
                    condition = condition.and(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
                    condition = condition.and(Tables.EH_BUILDINGS.NAME.eq(buildingName));

                    count[0] = query.where(condition).fetchOneInto(Integer.class);
                    return true;
                });
        if(count[0] > 0) {
            return false;
        }
        return true;
	}

	@Override
	public List<Building> listBuildingsByStatus(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback) {

		int namespaceId = UserContext.getCurrentNamespaceId(null);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBuildings.class));

        final List<Building> buildings = new ArrayList<Building>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_BUILDINGS.ID.gt(locator.getAnchor()));
        query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_BUILDINGS.ID.asc());
        query.addLimit(count);

        query.fetch().map((r) -> {
        	buildings.add(ConvertHelper.convert(r, Building.class));
            return null;
        });

        if(buildings.size() > 0) {
            locator.setAnchor(buildings.get(buildings.size() -1).getId());
        }

        return buildings;
	}

	@Override
	public int countBuildingsBycommunityId(Long communityId) {
		int namespaceId = UserContext.getCurrentNamespaceId(null);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Building> buildings = new ArrayList<Building>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);


        query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_BUILDINGS.ID.desc());

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query buildings by count, sql=" + query.getSQL());
            LOGGER.debug("Query buildings by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhBuildingsRecord record) -> {
        	buildings.add(ConvertHelper.convert(record, Building.class));
        	return null;
        });



		return buildings.size();
	}

	@Override
	public List<Long> listBuildingIdByCommunityId(Long communityId) {
		int namespaceId = UserContext.getCurrentNamespaceId(null);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Long> buildingIds = new ArrayList<Long>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);

        query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
        query.addOrderBy(Tables.EH_BUILDINGS.ID.desc());

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query buildings by count, sql=" + query.getSQL());
            LOGGER.debug("Query buildings by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhBuildingsRecord record) -> {
        	buildingIds.add(record.getId());
        	return null;
        });



		return buildingIds;
	}


	@Override
	public List<CommunityUser> listUserCommunities(CommunityUser communityUser,int pageOffset,int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));

//		context.select().from(Tables.EH_USERS).


		Condition cond = Tables.EH_USER_COMMUNITIES.COMMUNITY_ID.eq(communityUser.getCommunityId());
		if(1 == communityUser.getIsAuth()){
			cond = cond.and(Tables.EH_ENTERPRISE_CONTACTS.STATUS.eq(EnterpriseContactStatus.ACTIVE.getCode()));
		}else if(2 == communityUser.getIsAuth()){
			Condition cond2 = Tables.EH_ENTERPRISE_CONTACTS.STATUS.notEqual(EnterpriseContactStatus.ACTIVE.getCode());
			cond2 = cond2.or(Tables.EH_ENTERPRISE_CONTACTS.STATUS.isNull());
			cond = cond.and(cond2);
		}

		if(!StringUtils.isEmpty(communityUser.getUserName())){
			Condition cond1 = Tables.EH_USERS.NICK_NAME.eq(communityUser.getUserName());
			cond1 = cond1.or(Tables.EH_ENTERPRISE_CONTACTS.NAME.eq(communityUser.getUserName()));
			cond1 = cond1.or(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(communityUser.getPhone()));
			cond = cond.and(cond1);
		}

		List<CommunityUser> communityUsers = new ArrayList<CommunityUser>();






		context.select().from(Tables.EH_USER_COMMUNITIES).leftOuterJoin(Tables.EH_ENTERPRISE_CONTACTS).
						on(Tables.EH_USER_COMMUNITIES.OWNER_UID.eq(Tables.EH_ENTERPRISE_CONTACTS.USER_ID)).
						leftOuterJoin(Tables.EH_USERS).on(Tables.EH_USER_COMMUNITIES.OWNER_UID.eq(Tables.EH_USERS.ID)).
						leftOuterJoin(Tables.EH_USER_IDENTIFIERS).on(Tables.EH_USER_COMMUNITIES.OWNER_UID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID)).
						leftOuterJoin(Tables.EH_GROUPS).on(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID.eq(Tables.EH_GROUPS.ID)).
						where(cond).limit(pageSize).offset(pageOffset).fetch().map(r ->{
							CommunityUser user = new CommunityUser();
							user.setId(r.getValue(Tables.EH_USER_COMMUNITIES.ID));
							user.setUserId(r.getValue(Tables.EH_USER_COMMUNITIES.OWNER_UID));
							user.setUserName(StringUtils.isEmpty(r.getValue(Tables.EH_ENTERPRISE_CONTACTS.NAME)) ? r.getValue(Tables.EH_USERS.NICK_NAME) : r.getValue(Tables.EH_ENTERPRISE_CONTACTS.NAME));
							user.setApplyTime(r.getValue(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME));
							user.setPhone(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
							user.setEnterpriseName(r.getValue(Tables.EH_GROUPS.NAME));
							user.setIsAuth(null != r.getValue(Tables.EH_ENTERPRISE_CONTACTS.STATUS) && r.getValue(Tables.EH_ENTERPRISE_CONTACTS.STATUS).equals(EnterpriseContactStatus.ACTIVE.getCode()) ? 1 : 2);
							Long enterpriseId = r.getValue(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID);

							if(null != enterpriseId){
								DSLContext cont = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
								Record record =  cont.select().from(Tables.EH_ENTERPRISE_ADDRESSES).leftOuterJoin(Tables.EH_ADDRESSES)
												.on(Tables.EH_ENTERPRISE_ADDRESSES.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID))
												.where(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID.eq(enterpriseId)).fetch().get(0);

								if(null != record){
									user.setBuildingName(record.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
									user.setAddressName(record.getValue(Tables.EH_ADDRESSES.ADDRESS));
									user.setAddressId(record.getValue(Tables.EH_ADDRESSES.ID));
								}
							}

							communityUsers.add(user);
							return null;
		                });
		return communityUsers;
	}


	@Override
	public void deleteBuildingAttachmentsByBuildingId(Long buildingId) {

		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhBuildingAttachmentsRecord> query = context.selectQuery(Tables.EH_BUILDING_ATTACHMENTS);
					query.addConditions(Tables.EH_BUILDING_ATTACHMENTS.BUILDING_ID.eq(buildingId));
		            query.fetch().map((EhBuildingAttachmentsRecord record) -> {
		            	deleteBuildingAttachmentsById(record.getId());
		            	return null;
					});

					return true;
				});

	}

	private void deleteBuildingAttachmentsById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class));
        EhBuildingAttachmentsDao dao = new EhBuildingAttachmentsDao(context.configuration());
        dao.deleteById(id);
    }

	@Override
	public List<Community> listCommunitiesByNamespaceId(Integer namespaceId) {
        if(namespaceId == null || namespaceId.equals(0) || namespaceId == 0){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "stupid namespaceId = 0");
        }
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        context.select().from(Tables.EH_COMMUNITIES);
        List<Community> list = context.select().from(Tables.EH_COMMUNITIES)
            .where(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
            .and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()))
            .fetch().map((r) -> {
                return ConvertHelper.convert(r, Community.class);
            });

        return list;
	}

	/**
	 * Added by xiongying 20160518
	 */
	@Override
	public List<CommunityDTO> listCommunitiesByType(int namespaceId, List<Long> communityIds, Byte communityType,
			ListingLocator locator, int pageSize) {
//		int namespaceId = UserContext.getCurrentNamespaceId();
		final List<CommunityDTO> results = new ArrayList<>();

		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
	            (DSLContext context, Object reducingContext)-> {

	            	//增加分页 by sfyan 20160524
	            	Condition cond = Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId);
                    if(null != communityIds && communityIds.size() > 0)
	        		    cond = cond.and(Tables.EH_COMMUNITIES.ID.in(communityIds));
	        		cond = cond.and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
	        		if(null != communityType){
	        			cond = cond.and(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(communityType));
	        		}

	        		if(null != locator.getAnchor()){
	        			cond = cond.and(Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor()));
	        		}
	        		SelectOffsetStep<Record> query = context.select().from(Tables.EH_COMMUNITIES)
	        		    .where(cond).limit(pageSize);
	        		if(LOGGER.isDebugEnabled()) {
	                    LOGGER.debug("Query communities by type, sql=" + query.getSQL());
	                    LOGGER.debug("Query communities by type, bindValues=" + query.getBindValues());
	                }

	                query.fetch().map((r) -> {
	                CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
	                results.add(community);
	                locator.setAnchor(null);
	                if(results.size() == pageSize){
	                	locator.setAnchor(community.getId());
	                }
	                return null;
	            });

	            return true;
	        });
		return results;
	}


	/**
	 * Added by Janson
	 */
    @Override
    public List<Community> findCommunitiesByCityId(ListingLocator locator, int count, int namespaceId, long cityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));

        final List<Community> communities = new ArrayList<Community>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);


        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor()));

        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_COMMUNITIES.CITY_ID.eq(cityId));
        query.addOrderBy(Tables.EH_COMMUNITIES.ID.asc());
        query.addLimit(count);

        query.fetch().map((r) -> {
            communities.add(ConvertHelper.convert(r, Community.class));
            return null;
        });

        if(communities.size() >= count) {
            locator.setAnchor(communities.get(communities.size() -1).getId());
        } else {
            locator.setAnchor(null);
        }

        return communities;
    }

	@Override
	public List<CommunityDTO> listCommunitiesByNamespaceId(Byte communityType, Integer namespaceId,ListingLocator locator, int pageSize) {
		final List<CommunityDTO> results = new ArrayList<>();

		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
	            (DSLContext context, Object reducingContext)-> {

	            	Condition cond = Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor());
	        		cond = cond.and(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
	        		cond = cond.and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
	        		if(null != communityType){
	        			cond = cond.and(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(communityType));
	        		}

	            context.select().from(Tables.EH_COMMUNITIES)
	                .where(cond)
	                .limit(pageSize)
	                .fetch().map((r) -> {
	                CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
	                results.add(community);

	                return null;
	            });

	            return true;
	        });
		return results;
	}

	@Override
	public void createResourceCategory(ResourceCategory resourceCategory) {
		long id = this.sequnceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhResourceCategories.class));
        resourceCategory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resourceCategory.setId(id);
		if(null != resourceCategory.getPath())
			resourceCategory.setPath(resourceCategory.getPath() + "/" + id);
		else
			resourceCategory.setPath("/" + id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhResourceCategoriesDao dao = new EhResourceCategoriesDao(context.configuration());
        dao.insert(resourceCategory);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhResourceCategories.class, null);
	}

	@Override
	public ResourceCategory findResourceCategoryById(Long id) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhResourceCategories.class));
        EhResourceCategoriesDao dao = new EhResourceCategoriesDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), ResourceCategory.class);
	}

	@Override
	public ResourceCategory findResourceCategoryByParentIdAndName(Long ownerId, String ownerType, Long parentId, String name, Byte type) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhResourceCategories.class));
        SelectJoinStep<Record> query = context.select().from(Tables.EH_RESOURCE_CATEGORIES);

        Condition condition = Tables.EH_RESOURCE_CATEGORIES.OWNER_ID.eq(ownerId);
        condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.STATUS.eq(ResourceCategoryStatus.ACTIVE.getCode()));
        condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.TYPE.eq(type));

        if(null != parentId)
            condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.PARENT_ID.eq(parentId));
        if(!StringUtils.isBlank(name))
        	condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.NAME.eq(name));
        return ConvertHelper.convert(query.where(condition).fetchOne(), ResourceCategory.class);
	}

	@Override
	public List<ResourceCategory> listResourceCategory(Long ownerId, String ownerType, Long parentId, String path, Byte type) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhResourceCategories.class));
        SelectJoinStep<Record> query = context.select().from(Tables.EH_RESOURCE_CATEGORIES);

        Condition condition = Tables.EH_RESOURCE_CATEGORIES.OWNER_ID.eq(ownerId);
        condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.STATUS.eq(ResourceCategoryStatus.ACTIVE.getCode()));
        condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.TYPE.eq(type));
        if(null != parentId)
            condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.PARENT_ID.eq(parentId));
        if(!StringUtils.isBlank(path))
            condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.PATH.like(path + "%"));

        return query.where(condition).fetch().stream().map(r -> ConvertHelper.convert(r, ResourceCategory.class)).
        		collect(Collectors.toList());
	}

    @Override
    public List<ResourceCategory> listResourceCategory(Long ownerId, String ownerType, List<Long> ids, Byte type) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhResourceCategories.class));
        SelectJoinStep<Record> query = context.select().from(Tables.EH_RESOURCE_CATEGORIES);

        Condition condition =  Tables.EH_RESOURCE_CATEGORIES.STATUS.eq(ResourceCategoryStatus.ACTIVE.getCode());
        condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.TYPE.eq(type));
        if(!StringUtils.isEmpty(ownerType)){
            condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.OWNER_TYPE.eq(ownerType));
        }
        if(null != ownerId){
            condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.OWNER_ID.eq(ownerId));
        }
        if(null != ids && 0 != ids.size())
            condition = condition.and(Tables.EH_RESOURCE_CATEGORIES.ID.in(ids));

        return query.where(condition).fetch().stream().map(r -> ConvertHelper.convert(r, ResourceCategory.class)).
                collect(Collectors.toList());
    }

	@Override
	public void updateResourceCategory(ResourceCategory resourceCategory) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhResourceCategoriesDao dao = new EhResourceCategoriesDao(context.configuration());
        dao.update(resourceCategory);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhResourceCategories.class, null);
	}

	@Override
	public void createResourceCategoryAssignment(ResourceCategoryAssignment resourceCategoryAssignment) {
		long id = this.sequnceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhResourceCategoryAssignments.class));
        resourceCategoryAssignment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		resourceCategoryAssignment.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhResourceCategoryAssignmentsDao dao = new EhResourceCategoryAssignmentsDao(context.configuration());
        dao.insert(resourceCategoryAssignment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhResourceCategoryAssignments.class, null);
	}

	@Override
	public void updateResourceCategoryAssignment(ResourceCategoryAssignment resourceCategoryAssignment) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhResourceCategoryAssignmentsDao dao = new EhResourceCategoryAssignmentsDao(context.configuration());
        dao.update(resourceCategoryAssignment);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhResourceCategoryAssignments.class, null);
	}

	@Override
	public ResourceCategoryAssignment findResourceCategoryAssignment(Long resourceId, String resourceType, Integer namespaceId) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhResourceCategoryAssignments.class));

        SelectQuery<EhResourceCategoryAssignmentsRecord> query = context.selectQuery(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS);
        query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_ID.eq(resourceId));
        query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_TYPE.eq(resourceType));
//        query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_CATEGRY_ID.eq(resourceCategoryId));
        query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.NAMESPACE_ID.eq(namespaceId));

        return ConvertHelper.convert(query.fetchOne(), ResourceCategoryAssignment.class);
	}

	@Override
	public List<ResourceCategoryAssignment> listResourceCategoryAssignment(Long categoryId, Integer namespaceId) {
        return listResourceCategoryAssignment(categoryId, namespaceId, null, null);
	}

    @Override
    public List<ResourceCategoryAssignment> listResourceCategoryAssignment(Long categoryId, Integer namespaceId, String resourceType, List<Long> resourceIds) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhResourceCategoryAssignments.class));

        SelectQuery<EhResourceCategoryAssignmentsRecord> query = context.selectQuery(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS);
        query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_CATEGRY_ID.eq(categoryId));
        query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.NAMESPACE_ID.eq(namespaceId));
        if(null != resourceType){
            query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_TYPE.eq(resourceType));
        }
        if(null != resourceIds && resourceIds.size() > 0){
            query.addConditions(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_ID.in(resourceIds));
        }
        return query.fetch().stream().map(r -> ConvertHelper.convert(r, ResourceCategoryAssignment.class))
                .collect(Collectors.toList());
    }

	@Override
	public void deleteResourceCategoryAssignmentById(Long id) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhResourceCategoryAssignmentsDao dao = new EhResourceCategoryAssignmentsDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhResourceCategoryAssignments.class, null);

	}

    @Override
    public void deleteResourceCategoryById(Long id) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhResourceCategoriesDao dao = new EhResourceCategoriesDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhResourceCategories.class, null);

    }

	@Override
	public List<Community> listCommunitiesByCategory(Integer namespaceId, Long cityId, Long areaId, Long categoryId, String keyword, Long pageAnchor,
			Integer pageSize) {
//		int namespaceId =UserContext.getCurrentNamespaceId(null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class));

        SelectJoinStep<Record> query = context.select(Tables.EH_COMMUNITIES.fields()).from(Tables.EH_COMMUNITIES);
		Condition cond = Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId(namespaceId));
		cond = cond.and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
		if(null != pageAnchor && pageAnchor != 0){
			cond = cond.and(Tables.EH_COMMUNITIES.ID.gt(pageAnchor));
		}
		if(null != cityId){
			cond = cond.and(Tables.EH_COMMUNITIES.CITY_ID.eq(cityId));
		}
		if(null != areaId){
			cond = cond.and(Tables.EH_COMMUNITIES.AREA_ID.eq(areaId));
		}
		if(null != categoryId){
			query.join(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS).on(
					Tables.EH_COMMUNITIES.ID.eq(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_ID));
			cond = cond.and(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.RESOURCE_CATEGRY_ID.eq(categoryId));
		}
		if(!StringUtils.isEmpty(keyword)){
			cond = cond.and(Tables.EH_COMMUNITIES.NAME.like('%'+keyword+'%').or(Tables.EH_COMMUNITIES.ALIAS_NAME.like('%'+keyword+'%'))
            .or(Tables.EH_COMMUNITIES.ADDRESS.like('%'+keyword+'%')));
		}
		query.orderBy(Tables.EH_COMMUNITIES.ID.asc());
		if(null != pageSize)
			query.limit(pageSize);

		List<Community> communities = query.where(cond).fetch().
				map(new DefaultRecordMapper(Tables.EH_COMMUNITIES.recordType(), Community.class));

		return communities;
	}

    @Override
    public List<Community> listCommunitiesByCityIdAndAreaId(Integer namespaceId, Long cityId, Long areaId, String keyword,List<Long> communityIds, Long pageAnchor,
                                                     Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class));

        SelectJoinStep<Record> query = context.select(Tables.EH_COMMUNITIES.fields()).from(Tables.EH_COMMUNITIES);
        Condition cond = Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId);
        cond = cond.and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        if(null != pageAnchor && pageAnchor != 0){
            cond = cond.and(Tables.EH_COMMUNITIES.ID.gt(pageAnchor));
        }
        if(null != cityId){
            cond = cond.and(Tables.EH_COMMUNITIES.CITY_ID.eq(cityId));
        }
        if(null != areaId){
            cond = cond.and(Tables.EH_COMMUNITIES.AREA_ID.eq(areaId));
        }
        if (null != communityIds){
            cond = cond.and(Tables.EH_COMMUNITIES.ID.in(communityIds));
        }

        if(!StringUtils.isEmpty(keyword)){
            cond = cond.and(Tables.EH_COMMUNITIES.NAME.like('%'+keyword+'%').or(Tables.EH_COMMUNITIES.ALIAS_NAME.like('%'+keyword+'%'))
                    .or(Tables.EH_COMMUNITIES.ADDRESS.like('%'+keyword+'%')));
        }
        query.orderBy(Tables.EH_COMMUNITIES.ID.asc());
        if(null != pageSize) {
            query.limit(pageSize);
        }

        List<Community> communities = query.where(cond).fetch().
                map(new DefaultRecordMapper(Tables.EH_COMMUNITIES.recordType(), Community.class));

        return communities;
    }

    @Override
    public String getCommunityToken(String tokenType, Long communityId) {
        return this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class))
                .select(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TOKEN)
                .from(Tables.EH_COMMUNITIES)
                .where(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TYPE.eq(tokenType))
                .and(Tables.EH_COMMUNITIES.ID.eq(communityId))
                .fetchOne(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TOKEN);
    }

    @Override
    public List<Community> listCommunitiesByFeedbackForumId(Long feedbackForumId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        final List<Community> communities = new ArrayList<Community>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
        query.addConditions(Tables.EH_COMMUNITIES.FEEDBACK_FORUM_ID.eq(feedbackForumId));
        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.fetch().map(r ->{
            communities.add(ConvertHelper.convert(r, Community.class));
           return null;
        });
        return communities;
    }

    @Override
    public Map<Long, Community> listCommunitiesByIds(List<Long> ids) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        final Map<Long, Community> communities = new HashMap<>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
        query.addConditions(Tables.EH_COMMUNITIES.ID.in(ids));
        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.fetch().map(r ->{
            communities.put(r.getId(), ConvertHelper.convert(r, Community.class));
            return null;
        });
        return communities;
    }

	@Override
	public List<Community> listCommunityByNamespaceIdAndName(Integer namespaceId, String communityName) {
    	 List<Community> result = new ArrayList<Community>();

         this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
             (DSLContext context, Object reducingContext) -> {
            	 context.select().from(Tables.EH_COMMUNITIES)
        		 .where(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
        		.and(Tables.EH_COMMUNITIES.NAME.eq(communityName)).fetch().map(r ->{
        			return result.add(ConvertHelper.convert(r,Community.class));
        		});
            	 return true;
             });

         return result;

	}

    @Override
    public List<Community> listCommunityByNamespaceType(Integer namespaceId, String namespaceType) {
        List<Community> result = new ArrayList<Community>();

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result,
                (DSLContext context, Object reducingContext) -> {
                    context.select().from(Tables.EH_COMMUNITIES)
                            .where(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                            .and(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TYPE.eq(namespaceType)).fetch().map(r ->{
                        return result.add(ConvertHelper.convert(r,Community.class));
                    });
                    return true;
                });

        return result;
    }

    @Override
    public Map<String, Long> listCommunityIdByNamespaceType(Integer namespaceId, String namespaceType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        final Map<String, Long> communities = new HashMap<>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TYPE.eq(namespaceType));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.fetch().map(r ->{
            communities.put(r.getNamespaceCommunityToken(), r.getId());
            return null;
        });
        return communities;
    }

    @Override
    public Community findCommunityByNamespaceToken(String namespaceType, String namespaceToken) {
        List<Community> result = new ArrayList<Community>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TYPE.eq(namespaceType));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TOKEN.eq(namespaceToken));
        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.fetch().map(r ->{
            result.add(ConvertHelper.convert(r, Community.class));
            return null;
        });
        if(result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<Long> listCommunityByNamespaceToken(String namespaceType, List<String> namespaceToken) {
        List<Long> result = new ArrayList<Long>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TYPE.eq(namespaceType));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_COMMUNITY_TOKEN.in(namespaceToken));
        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.fetch().map(r ->{
            result.add(r.getId());
            return null;
        });
        if(result.size() == 0) {
            return null;
        }
        return result;
    }

    @Override
    public CommunityGeoPoint findCommunityGeoPointByCommunityId(long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunityGeopoints.class));
        List<CommunityGeoPoint> communities = new ArrayList<>();
        SelectQuery<EhCommunityGeopointsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_GEOPOINTS);
        query.addConditions(Tables.EH_COMMUNITY_GEOPOINTS.COMMUNITY_ID.eq(communityId));
        query.fetch().map(r -> {
            communities.add(ConvertHelper.convert(r, CommunityGeoPoint.class));
            return null;
        });
        if (communities == null || communities.size() == 0) {
            return null;
        }
        return communities.get(0);
    }
    @Override
    public Community findFirstCommunityByNameSpaceIdAndType(Integer namespaceId, Byte type) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        if (null != type) {
            query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(type));
        }
        query.addOrderBy(Tables.EH_COMMUNITIES.ID.asc());
        query.addLimit(1);

        Record record = query.fetchAny();
        if (record != null) {
            return ConvertHelper.convert(record, Community.class);
        }
        return null;
    }

    @Override
    public Long findDefaultCommunityByCommunityId(Integer namespaceId, Long originId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunityDefault.class));
        SelectQuery<EhCommunityDefaultRecord> query = context.selectQuery(Tables.EH_COMMUNITY_DEFAULT);
        query.addConditions(Tables.EH_COMMUNITY_DEFAULT.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_COMMUNITY_DEFAULT.ORIGIN_COMMUNITY_ID.eq(originId));
        query.addOrderBy(Tables.EH_COMMUNITY_DEFAULT.ID.desc());
        query.addLimit(1);
        LOGGER.debug("findDefaultCommunityByCommunityId sql :" + query.getSQL());
        EhCommunityDefaultRecord record = query.fetchAny();
        if (record != null) {
            return record.getTargetCommunityId();
        }
        return null;
    }
	
	@Override
    public List<Community> listCommunitiesByOrgId(ListingLocator locator, int count, Long orgId, String keyword) {

        int namespaceId =UserContext.getCurrentNamespaceId(null);
        final List<Community> communities = new ArrayList<Community>();
        Condition cond = Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId);
        cond = cond.and(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        if(null != locator.getAnchor()){
            cond = cond.and(Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor()));
        }

        if(!StringUtils.isEmpty(keyword)){
            cond = cond.and(Tables.EH_COMMUNITIES.NAME.like('%'+keyword+'%').or(Tables.EH_COMMUNITIES.ALIAS_NAME.like('%'+keyword+'%')));
        }

        cond = cond.and(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId));
        Condition condition = cond;
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
                (DSLContext context, Object reducingContext) -> {

                    context.select(Tables.EH_COMMUNITIES.fields()).from(Tables.EH_COMMUNITIES)
                            .join(Tables.EH_ORGANIZATION_COMMUNITIES)
                            .on(Tables.EH_COMMUNITIES.ID.eq(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID))
                            .where(condition)
                            .limit(count)
                            .fetch().map((r) -> {
                        communities.add(RecordHelper.convert(r, Community.class));
                        return null;
                    });

                    return true;
                });

        return communities;
    }


    @Override
    public List<Community> listCommunities(Integer namespaceId, Byte communityType, Long orgId, String keyword, Byte status, Byte ownerFlag, ListingLocator locator, int count) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.select(Tables.EH_COMMUNITIES.fields()).from(Tables.EH_COMMUNITIES).getQuery();
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_COMMUNITIES.STATUS.ne(CommunityAdminStatus.INACTIVE.getCode()));
        if(communityType != null){
            query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(communityType));
        }
        if(status != null){
            query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(status));
        }

        if(null != locator.getAnchor()){
            query.addConditions(Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor()));
        }

        if(!StringUtils.isEmpty(keyword)){

            query.addConditions(Tables.EH_COMMUNITIES.NAME.like('%'+keyword+'%').or(Tables.EH_COMMUNITIES.COMMUNITY_NUMBER.like('%'+keyword+'%')));
        }

        if(orgId != null){
            query.addJoin(Tables.EH_ORGANIZATION_COMMUNITIES, JoinType.JOIN, Tables.EH_COMMUNITIES.ID.eq(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID));
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId));
        }

        //查询无归属的园区
        if(ownerFlag != null && ownerFlag.byteValue() == 0){
            SelectQuery<EhOrganizationCommunitiesRecord> notExistsquery = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
            notExistsquery.addConditions(Tables.EH_COMMUNITIES.ID.eq(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID));
            query.addConditions(DSL.notExists(notExistsquery));
        }

        query.addLimit(count);

        List<Community> map = query.fetch().stream().map(r -> RecordHelper.convert(r, Community.class)).collect(Collectors.toList());

        return map;
    }

	@Override
    public List<CommunityGeoPoint> listCommunityGeoPointByGeoHashInCommunities(double latitude, double longitude, int geoHashLength, List<Long> communityIds) {
        List<CommunityGeoPoint> l = new ArrayList<>();
        String geoHashStr = GeoHashUtils.encode(latitude, longitude).substring(0, geoHashLength);
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null,
                (DSLContext context, Object reducingContext)-> {

                    String likeVal = geoHashStr + "%";
                    context.select()
                            .from(Tables.EH_COMMUNITY_GEOPOINTS)
                            .where(Tables.EH_COMMUNITY_GEOPOINTS.GEOHASH.like(likeVal)).and(Tables.EH_COMMUNITY_GEOPOINTS.COMMUNITY_ID.in(communityIds))
                            .fetch().map((r) -> {
                        l.add(ConvertHelper.convert(r, CommunityGeoPoint.class));
                        return null;
                    });

                    return true;
                });
        return l;
    }

	@Override
	public Community findCommunityByNamespaceIdAndName(Integer namespaceId, String name) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
		Condition cond = Tables.EH_COMMUNITIES.NAME.eq(name);
		cond = cond.or(Tables.EH_COMMUNITIES.ALIAS_NAME.eq(name));
		SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
		query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(cond);

        LOGGER.debug("findCommunityByNamespaceIdAndName, sql = {}" , query.getSQL());
        LOGGER.debug("findCommunityByNamespaceIdAndName, bindValues = {}" , query.getBindValues());
        return ConvertHelper.convert(query.fetchOne(), Community.class);
	}

	@Override
	public Community findCommunityByNumber(String communityNumber, Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
		SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
		query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_NUMBER.eq(communityNumber));
		query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));

        LOGGER.debug("findCommunityByNumber, sql={}", query.getSQL());
        LOGGER.debug("findCommunityByNumber, bindValues={}", query.getBindValues());
        return ConvertHelper.convert(query.fetchOne(), Community.class);
	}

	    @Override
    public Community findDefaultCommunity(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);

        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_COMMUNITIES.DEFAULT_COMMUNITY_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()));
        return query.fetchAnyInto(Community.class);
    }

    @Override
    public Community findAnyCommunity(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);

        query.addConditions(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId));
        return query.fetchAnyInto(Community.class);
    }

    /**
     * 查询该域空间下不在该项目中的所有企业
     * @param communityId
     * @param namespaceId
     * @return
     */
    @Override
    public List<Long> findOrganizationIdsByNamespaceId(Long communityId , Integer namespaceId){
        //获取上下文
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        List<Long> communityIdList = context.select(Tables.EH_COMMUNITIES.ID).from(Tables.EH_COMMUNITIES).where(
                Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_COMMUNITIES.ID.ne(communityId))
                .fetchInto(Long.class);
        return communityIdList;

    }


    /**
     * 根据项目名称和域空间Id来查询项目
     * @param name
     * @param namespaceId
     * @return
     */
    public Community findCommunityByNameAndNamespaceId(String name,Integer namespaceId){
        //获取上下文
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        Community community = context.select().from(Tables.EH_COMMUNITIES)
                .where(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_COMMUNITIES.NAME.eq(name))
                .fetchAnyInto(Community.class);
        return community;
    }


	@Override
	public Integer countActiveBuildingsByCommunityId(Long communityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectCount()
				.from(Tables.EH_BUILDINGS)
				.where(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId))
				.and(Tables.EH_BUILDINGS.STATUS.eq((BuildingAdminStatus.ACTIVE.getCode())))
				.fetchOneInto(Integer.class);
	}

	@Override
	public Integer countActiveApartmentsByCommunityId(Long communityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectCount()
				.from(Tables.EH_ADDRESSES)
				.where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
				.and(Tables.EH_ADDRESSES.STATUS.eq((byte)2))
				.and(Tables.EH_ADDRESSES.IS_FUTURE_APARTMENT.eq((byte)0))
				.fetchOneInto(Integer.class);
	}

	@Override
	public List<Building> listBuildingsByKeywords(Integer namespaceId, Long communityId, Long buildingId,
			String keyWords, CrossShardListingLocator locator, int pageSize) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, locator.getEntityId()));
		List<Building> buildings = new ArrayList<Building>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);

        if (null != namespaceId) {
            query.addConditions(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        }
        if (null != communityId) {
            query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
        }
        if (null != buildingId) {
            query.addConditions(Tables.EH_BUILDINGS.ID.eq(buildingId));
        }
        if (!StringUtils.isBlank(keyWords)) {
            query.addConditions(Tables.EH_BUILDINGS.NAME.like("%" + keyWords + "%")
            					.or(Tables.EH_BUILDINGS.MANAGER_NAME.like("%" + keyWords + "%"))
            					.or(Tables.EH_BUILDINGS.CONTACT.like("%" + keyWords + "%")));
        }
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_BUILDINGS.DEFAULT_ORDER.lt(locator.getAnchor()));
        }
        query.addConditions(Tables.EH_BUILDINGS.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_BUILDINGS.DEFAULT_ORDER.desc());
        query.addLimit(pageSize+1);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listBuildingsByKeywords, sql=" + query.getSQL());
            LOGGER.debug("listBuildingsByKeywords, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhBuildingsRecord record) -> {
        	buildings.add(ConvertHelper.convert(record, Building.class));
        	return null;
        });

        if(buildings.size() > 0) {
            locator.setAnchor(buildings.get(buildings.size() -1).getDefaultOrder());
        }

		return buildings;
	}

	@Override
	public Integer countRelatedContractNumberInCommunity(Long communityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectCount()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId))
				.and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
				.fetchOneInto(Integer.class);
	}

	@Override
	public List<Community> findCommunitiesByNamespaceId(Integer nameSpaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
		return context.select()
					.from(Tables.EH_COMMUNITIES)
					.where(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(nameSpaceId))
					.fetchInto(Community.class);
	}

	@Override
	public List<Building> findBuildingsByCommunityId(Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
		return context.select()
					.from(Tables.EH_BUILDINGS)
					.where(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId))
					.and(Tables.EH_BUILDINGS.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()))
					.fetchInto(Building.class);
	}

	@Override
	public List<Building> findBuildingsByNamespaceId(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
		return context.select()
					.from(Tables.EH_BUILDINGS)
					.where(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId))
					.and(Tables.EH_BUILDINGS.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()))
					.fetchInto(Building.class);
	}

	@Override
	public Map<Long, Building> mapBuildingIdAndBuilding(List<Long> buildingIds) {
		Map<Long, Building> result = new HashMap<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
		context.select()
				.from(Tables.EH_BUILDINGS)
				.where(Tables.EH_BUILDINGS.ID.in(buildingIds))
				.and(Tables.EH_BUILDINGS.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()))
				.fetchInto(Building.class)
				.stream()
				.forEach(r->{
					result.put(r.getId(), r);
				});
		return result;
	}
	
	@Override
	public Long getOrganizationIdByCommunityId(Long communityId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID)
					  .from(Tables.EH_ORGANIZATION_COMMUNITIES)
					  .where(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId))
					  .fetchAnyInto(Long.class);
	}

	@Override
	public String findCommunityCategoryByCommunityId(Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
		com.everhomes.server.schema.tables.EhResourceCategoryAssignments ehResourceCategoryAssignments = Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS;
		com.everhomes.server.schema.tables.EhResourceCategories ehResourceCategories = Tables.EH_RESOURCE_CATEGORIES;
		
		return context.select(ehResourceCategories.NAME)
					.from(ehResourceCategoryAssignments)
					.leftOuterJoin(ehResourceCategories).on(ehResourceCategoryAssignments.RESOURCE_CATEGRY_ID.eq(ehResourceCategories.ID))
					.where(ehResourceCategoryAssignments.RESOURCE_ID.eq(communityId))
					.fetchAnyInto(String.class);
	}

	@Override
	public List<Building> findBuildingsByIds(List<Long> buildingIds) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Building> buildings = context.select()
							        		.from(Tables.EH_BUILDINGS)
							        		.where(Tables.EH_BUILDINGS.ID.in(buildingIds))
							        		.and(Tables.EH_BUILDINGS.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()))
							        		.fetchInto(Building.class);
		return buildings;
	}

	@Override
	public List<Long> findCommunityIdsByOrgId(Long organizationId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID)
						.from(Tables.EH_ORGANIZATION_COMMUNITIES)
						.where(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId))
						.fetchInto(Long.class);
	}

}
