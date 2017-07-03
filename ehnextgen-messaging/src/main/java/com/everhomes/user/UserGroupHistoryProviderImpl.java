package com.everhomes.user;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserGroupHistoriesDao;
import com.everhomes.server.schema.tables.pojos.EhUserGroupHistories;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhUserGroupHistoriesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class UserGroupHistoryProviderImpl implements UserGroupHistoryProvider {
    @Autowired
    private DbProvider dbProvider;
   
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Override
    public Long createUserGroupHistory(UserGroupHistory history) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserGroupHistories.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, history.getOwnerUid()));
        history.setId(id);
        history.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhUserGroupHistoriesDao dao = new EhUserGroupHistoriesDao(context.configuration());
        dao.insert(history);
        return id;
    }
    
    @Override 
    public void deleteUserGroupHistory(UserGroupHistory history) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, history.getOwnerUid()));
        EhUserGroupHistoriesDao dao = new EhUserGroupHistoriesDao(context.configuration());
        dao.deleteById(history.getId());
    }
    
    @Override
    public UserGroupHistory getHistoryById(Long id) {
//        UserGroupHistory[] result = new UserGroupHistory[1];
//        
//        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
//            (DSLContext context, Object reducingContext) -> {
//                result[0] = context.select().from(Tables.EH_USER_GROUP_HISTORIES)
//                    .where(Tables.EH_USER_GROUP_HISTORIES.ID.eq(id))
//                    .fetchAny().map((r) -> {
//                        return ConvertHelper.convert(r, UserGroupHistory.class);
//                    });
//
//                if (result[0] != null) {
//                    return false;
//                } else {
//                    return true;
//                }
//            });
//        
//        return result[0];
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUserGroupHistories.class, id));
    	EhUserGroupHistoriesDao dao = new EhUserGroupHistoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), UserGroupHistory.class);
    }
    
    @Override
    public List<UserGroupHistory> queryUserGroupHistoryByUserId(ListingLocator locator, Long userId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
 
        SelectQuery<EhUserGroupHistoriesRecord> query = context.selectQuery(Tables.EH_USER_GROUP_HISTORIES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
 
        query.addConditions(Tables.EH_USER_GROUP_HISTORIES.OWNER_UID.eq(userId));
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_USER_GROUP_HISTORIES.ID.gt(locator.getAnchor()));
            }
        
        query.addLimit(count+1);
        List<UserGroupHistory> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, UserGroupHistory.class);
        });
        
        if(objs == null) {
            objs = new ArrayList<UserGroupHistory>();
        }
        
        if(objs.size() > count) {
            objs.remove(objs.size()-1);
            locator.setAnchor(objs.get(objs.size()-1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }
    
    @Override
    public UserGroupHistory queryUserGroupHistoryByFamilyId(Long userId, Long familyId) {
        ListingLocator locator = new ListingLocator();
        List<UserGroupHistory> histories = this.queryUserGroupHistoryByUserId(locator, userId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USER_GROUP_HISTORIES.GROUP_ID.eq(familyId));
                query.addConditions(Tables.EH_USER_GROUP_HISTORIES.GROUP_DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()));
                return query;
            }
            
        });
        
        if(histories != null && histories.size() > 0) {
            return histories.get(0);
        }
        
        return null;
    }
    
    @Override
    public UserGroupHistory queryUserGroupHistoryByAddressId(Long userId, Long addressId) {
        ListingLocator locator = new ListingLocator();
        List<UserGroupHistory> histories = this.queryUserGroupHistoryByUserId(locator, userId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USER_GROUP_HISTORIES.ADDRESS_ID.eq(addressId));
                query.addConditions(Tables.EH_USER_GROUP_HISTORIES.GROUP_DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()));
                return query;
            }
            
        });
        
        if(histories != null && histories.size() > 0) {
            return histories.get(0);
        }
        
        return null;
    }
    
    @Override
    public List<UserGroupHistory> queryUserGroupHistoryByUserId(Long userId) {
        ListingLocator locator = new ListingLocator();
        List<UserGroupHistory> objs = this.queryUserGroupHistoryByUserId(locator, userId, 100, null);
        Collections.sort(objs, new Comparator<UserGroupHistory>() {

            @Override
            public int compare(UserGroupHistory o1, UserGroupHistory o2) {
                if(o1.getId() > o2.getId()) {
                    return -1;
                } else {
                    return 1;
                    }
            }
            
        });
        return objs;
    }

	@Override
	public List<UserGroupHistory> queryUserGroupHistoryByGroupIds(String userInfoKeyword, String communityKeyword,
                                                    List<Long> groupIds, CrossShardListingLocator locator, int pageSize) {
		 
		List<UserGroupHistory> objs = new ArrayList<UserGroupHistory>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), objs, (DSLContext context, Object reducingContext) -> {
//			DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
	        
	        SelectQuery<EhUserGroupHistoriesRecord> query = context.selectQuery(Tables.EH_USER_GROUP_HISTORIES);
	         
	        query.addConditions(Tables.EH_USER_GROUP_HISTORIES.GROUP_ID.in(groupIds));
            if (StringUtils.isNotBlank(userInfoKeyword)) {
                Field<String> keyword = DSL.concat("%", userInfoKeyword, "%");
                query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_USER_GROUP_HISTORIES.OWNER_UID.eq(Tables.EH_USERS.ID));
                query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));
                query.addConditions(Tables.EH_USERS.NICK_NAME.like(keyword).or(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(keyword)));
            }
            if (StringUtils.isNotBlank(communityKeyword)) {
                Field<String> keyword = DSL.concat("%", communityKeyword, "%");
                query.addJoin(Tables.EH_COMMUNITIES, JoinType.JOIN, Tables.EH_USER_GROUP_HISTORIES.COMMUNITY_ID.eq(Tables.EH_COMMUNITIES.ID));
                query.addConditions(Tables.EH_COMMUNITIES.NAME.like(keyword));
            }

            if(locator.getAnchor() != null) {
	            query.addConditions(Tables.EH_USER_GROUP_HISTORIES.ID.gt(locator.getAnchor()));
            }
	        
	        query.addLimit(pageSize+1);
	        query.addOrderBy(Tables.EH_USER_GROUP_HISTORIES.ID.desc());
	        query.fetch().map((r) -> {
	        	objs.add( ConvertHelper.convert(r, UserGroupHistory.class));
	        	return null;
	        });
	        return true;
		});
        
        if(objs.size() > pageSize) {
            objs.remove(objs.size()-1);
            locator.setAnchor(objs.get(objs.size()-1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
	}

}
