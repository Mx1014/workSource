package com.everhomes.enterprise;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.jooq.Record1;
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
import com.everhomes.server.schema.tables.records.EhGroupsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseContactsRecord;
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
        
        //TODO for forum
        enterprise.setDiscriminator(GroupDiscriminator.Enterprise.getCode());
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
    
    public List<Group> queryGroupsWithOk(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        final List<Group> groups = new ArrayList<>();
        
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);

            if(callback != null)
                callback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_GROUPS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_GROUPS.ID.asc());
            query.addLimit(count - groups.size() + 1);
            
            query.fetch().map((r) -> {
                groups.add(ConvertHelper.convert(r, Group.class));
                return null;
            });
            
            if(groups.size() > count) {
                return AfterAction.done;
            }
           
            return AfterAction.next;
        });
        
        if(groups.size() > count) {
            //Bigger than origin, so has more data
            groups.remove(groups.size() - 1);
            locator.setAnchor(groups.get(groups.size() - 1).getId());            
        } else {
            locator.setAnchor(null);
        }
        
        return groups;
    }
    
    @Override
    public List<Enterprise> queryEnterprises(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        List<Group> groups = this.queryGroupsWithOk(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(callback != null)
                    callback.buildCondition(locator, query);
                query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.Enterprise.getCode()));
                return query;
            }
            
        });
        
        List<Enterprise> ents = new ArrayList<Enterprise>();
        for(Group g : groups) {
            ents.add(ConvertHelper.convert(g, Enterprise.class));
        }
        return ents;
    }
    
    @Override
    public List<Enterprise> listEnterprises(CrossShardListingLocator locator, int count) {
        return this.queryEnterprises(locator, count, null);
    }
    
    @Override
    public void createEnterpriseCommunity(Long creatorId, EnterpriseCommunity ec) {
        //TODO for forum
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
        ec.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.insert(ec);
    }
    
    @Override
    public void updateEnterpriseCommunityMap(EnterpriseCommunityMap ec) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, ec.getCommunityId()));
        ec.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        ec.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
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
    public List<EnterpriseCommunityMap> queryEnterpriseCommunityMap(CrossShardListingLocator locator, int count, 
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
    
    //Use join
    @Override
    public List<Enterprise> queryEnterpriseByPhone(String phone) {
        final List<Enterprise> enterprises = new ArrayList<>();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);     
            SelectConditionStep<Record1<Long>> step2 = context.select(Tables.EH_ENTERPRISE_CONTACTS.ENTERPRISE_ID)
                    .from(Tables.EH_ENTERPRISE_CONTACTS).join(Tables.EH_ENTERPRISE_CONTACT_ENTRIES)
                    .on(Tables.EH_ENTERPRISE_CONTACTS.ID.eq(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.CONTACT_ID))
                    .where(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ENTRY_VALUE.eq(phone));
            query.addConditions(Tables.EH_GROUPS.ID.in(step2));
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_GROUPS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_GROUPS.ID.asc());
            
            query.fetch().map((r) -> {
                enterprises.add(ConvertHelper.convert(r, Enterprise.class));
                return null;
            });
           
            return AfterAction.next;
        });
        
        return enterprises;
    }
}
