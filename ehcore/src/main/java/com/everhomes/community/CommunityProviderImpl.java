// @formatter:off
package com.everhomes.community;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import ch.hsr.geohash.GeoHash;

import com.everhomes.address.CommunityAdminStatus;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupMember;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAddresses;
import com.everhomes.server.schema.tables.daos.EhCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhCommunityGeopointsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhCommunityGeopoints;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.records.EhCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhGroupMembersRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.Tuple;

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
        long id = shardingProvider.allocShardableContentId(EhCommunities.class).second();
        
        community.setId(id);
        community.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        community.setCreatorUid(creatorId);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, id));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.insert(community);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCommunities.class, null);
    }

    @Caching(evict = { @CacheEvict(value="Community", key="#community.id") } )
    @Override
    public void updateCommunity(Community community) {
        assert(community.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, community.getId()));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.update(community);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunities.class, community.getId());
    }

    @Caching(evict = { @CacheEvict(value="Community", key="#community.id") } )
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

    @Cacheable(value="Community", key="#id")
    @Override
    public Community findCommunityById(long id) {
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
    
    @Cacheable(value="CommunityGeoList", key="#id")
    @Override
    public List<CommunityGeoPoint> listCommunityGeoPoints(long id) {
        List<CommunityGeoPoint> l = new ArrayList<>();
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, id));
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
    
    @Override
    public List<Community> findNearyByCommunityById(long communityId){
        long startTime = System.currentTimeMillis();
        CommunityProvider self = PlatformContext.getComponent(CommunityProvider.class);
        List<CommunityGeoPoint> pointList = self.listCommunityGeoPoints(communityId);
        List<Community> results = new ArrayList<Community>();
        List<CommunityGeoPoint> nearyByPointList = new ArrayList<CommunityGeoPoint>();
        
//        pointList.forEach((CommunityGeoPoint p) ->{
//            List<CommunityGeoPoint> l = self.findCommunityGeoPointByGeoHash(p.getLatitude(),p.getLongitude());
//            if(l != null && !l.isEmpty())
//                nearyByPointList.addAll(l);
//        });
        nearyByPointList = findCommunityGeoPointByGeoHash(pointList);
        
        List<Long> communityIds = new ArrayList<Long>();
        if(nearyByPointList == null || nearyByPointList.isEmpty()) {
            return results;
        }
        
        for(CommunityGeoPoint p : nearyByPointList){
            if(!communityIds.contains(p.getCommunityId().longValue()))
                communityIds.add(p.getCommunityId());
        }
        
        communityIds.forEach((Long id) ->{
            Community community = self.findCommunityById(id);
            if(community != null)
                results.add(community);
        });
        long endTime = System.currentTimeMillis();
        LOGGER.info("Find nearby community elapse=" + (endTime - startTime));
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
    public List<CommunityGeoPoint> findCommunityGeoPointByGeoHash(double latitude, double longitude) {
        List<CommunityGeoPoint> l = new ArrayList<>();
        String geoHashStr = GeoHashUtils.encode(latitude, longitude).substring(0, 6);
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
            
        final List<Community> results = new ArrayList<>();
        
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        if(offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                    (DSLContext context, Object reducingContext)-> {
                        
                    Long count = context.selectCount().from(Tables.EH_COMMUNITIES)
                            .where(Tables.EH_COMMUNITIES.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode()))
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
    public List<Community> listWaitingForApproveCommunities(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        
        final List<Community> communities = new ArrayList<Community>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
            
        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_COMMUNITIES.ID.gt(locator.getAnchor()));
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
}
