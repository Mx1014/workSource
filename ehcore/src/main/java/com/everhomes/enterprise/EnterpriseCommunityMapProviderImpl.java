package com.everhomes.enterprise;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.Group;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCommunityMapDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.records.EhEnterpriseCommunityMapRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class EnterpriseCommunityMapProviderImpl {
    @Autowired
    private DbProvider dbProvider;
   
    @Autowired
    private ShardingProvider shardingProvider;
    
    // TODO member of eh_communities partition
    public void createEnterpriseCommunityMap(EnterpriseCommunityMap m) {
        long id = this.shardingProvider.allocShardableContentId(EhCommunities.class).second();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, m.getCommunityId()));
        m.setId(id);
        //Default approving state
        //m.setStatus(EnterpriseContactStatus.Approving.getCode());
        m.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.insert(m);
    }
    
    public void updateEnterpriseCommunityMap(EnterpriseCommunityMap m) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, m.getCommunityId()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.update(m);
    }
    
    public void deleteEnterpriseCommunityMapById(EnterpriseCommunityMap m) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, m.getCommunityId()));
        EhEnterpriseCommunityMapDao dao = new EhEnterpriseCommunityMapDao(context.configuration());
        dao.deleteById(m.getId());        
    }
    
    //TODO for cache
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
    
    public List<EnterpriseContact> queryContactByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, enterpriseId));
 
        SelectQuery<EhEnterpriseCommunityMapRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_COMMUNITY_MAP);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
 
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnterpriseContact.class);
        });
    }
    
    public List<EnterpriseContact> queryContacts(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhCommunities.class);
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
                contacts.add(ConvertHelper.convert(r, EnterpriseContact.class));
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
