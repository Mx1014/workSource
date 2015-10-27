package com.everhomes.enterprise;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCommunityMapDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.records.EhEnterpriseCommunityMapRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class EnterpriseProviderImpl implements EnterpriseProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    //TODO enterprise field
    @Override
    public void createEnterprise(Enterprise enterprise) {
        enterprise.setDiscriminator(GroupDiscriminator.Enterprise.toString());
        this.groupProvider.createGroup(enterprise);
    }
    
    @Override
    public void updateEnterprise(Enterprise enterprise) {
        this.groupProvider.updateGroup(enterprise);
    }
    
    @Override
    public Enterprise getEnterpriseById(Long id) {
        Group g = this.groupProvider.findGroupById(id);
        return ConvertHelper.convert(g, Enterprise.class);
    }
    
    @Override
    public void deleteEnterpriseById(Long id) {
        this.groupProvider.deleteGroup(id);
    }
    
    @Override
    public List<Enterprise> queryEnterprise() {
        //TODO query enterprise
        //this.groupProvider.queryGroups(locator, count, callback);
        return null;
    }
    
    @Override
    public void createEnterpriseCommunity(Long creatorId, EnterpriseCommunity ec) {
        ec.setCommunityType(EnterpriseCommunityType.Enterprise.getCode());
        this.communityProvider.createCommunity(creatorId, ec);
    }
    
    @Override
    public EnterpriseCommunity getEnterpriseCommunityById(Long id) {
        Community c = this.communityProvider.findCommunityById(id);
        if(c.getCommunityType() == EnterpriseCommunityType.Enterprise.getCode()) {
            return ConvertHelper.convert(c, EnterpriseCommunity.class);    
        }
        return null;
    }
    
    @Override
    public void createEnterpriseCommunityMap(EnterpriseCommunityMap ec) {
        long id = this.shardingProvider.allocShardableContentId(EhCommunities.class).second();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, ec.getCommunityId()));
        ec.setId(id);
        ec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.insert(ec);
    }
    
    @Override
    public void updateEnterpriseCommunityMap(EnterpriseCommunityMap ec) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, ec.getCommunityId()));
        ec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.update(ec);        
    }
    
    @Override
    public void deleteEnterpriseCommunityMapById(EnterpriseCommunityMap ec) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, ec.getCommunityId()));
        ec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.delete(ec);        
    }
    
    @Override
    public EnterpriseCommunityMap getEnterpriseCommunityMapById(Long id) {
        EnterpriseCommunityMap[] result = new EnterpriseCommunityMap[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ENTERPRISE_COMMUNITY_MAP)
                    .where(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseCommunityMap.class);
                    });

                if (result[0] != null) {
                    return false;
                } else {
                    return true;
                }
            });
        
        return result[0];
    }
    
    @Override
    public List<EnterpriseCommunityMap> queryEnterpriseMapByCommunityId(ListingLocator locator, Long comunityId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, comunityId));
 
        SelectQuery<EhEnterpriseCommunityMapRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_COMMUNITY_MAP);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
 
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseCommunityMap.class);
        });
    }
    
    @Override
    public List<EnterpriseCommunityMap> queryContactGroupMembers(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<EnterpriseCommunityMap> contacts = new ArrayList<EnterpriseCommunityMap>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEnterpriseCommunityMapRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_COMMUNITY_MAP);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.asc());
            query.addLimit(count - contacts.size());
            
            query.fetch().map((r) -> {
                contacts.add(ConvertHelper.convert(r, EnterpriseCommunityMap.class));
                return null;
            });
           
            if(contacts.size() >= count) {
                locator.setAnchor(contacts.get(contacts.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
 
        });
        return contacts;
    }
}
