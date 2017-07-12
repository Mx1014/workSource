package com.everhomes.user;

import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;

import com.everhomes.aclink.AclinkUser;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.SearchUserImpersonationCommand;
import com.everhomes.rest.user.UserImperInfo;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhUserImpersonationsDao;
import com.everhomes.server.schema.tables.pojos.EhUserImpersonations;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhUserImpersonationsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.MapReduceCallback;

@Component
public class UserImpersonationProviderImpl implements UserImpersonationProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createUserImpersonation(UserImpersonation obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserImpersonations.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserImpersonations.class));

        obj.setId(id);
        prepareObj(obj);
        EhUserImpersonationsDao dao = new EhUserImpersonationsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateUserImpersonation(UserImpersonation obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserImpersonations.class));
        EhUserImpersonationsDao dao = new EhUserImpersonationsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteUserImpersonation(UserImpersonation obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserImpersonations.class));
        EhUserImpersonationsDao dao = new EhUserImpersonationsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public UserImpersonation getUserImpersonationById(Long id) {
        try {
        UserImpersonation[] result = new UserImpersonation[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserImpersonations.class));

        result[0] = context.select().from(Tables.EH_USER_IMPERSONATIONS)
            .where(Tables.EH_USER_IMPERSONATIONS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, UserImpersonation.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<UserImpersonation> queryUserImpersonations(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserImpersonations.class));

        SelectQuery<EhUserImpersonationsRecord> query = context.selectQuery(Tables.EH_USER_IMPERSONATIONS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_USER_IMPERSONATIONS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<UserImpersonation> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, UserImpersonation.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(UserImpersonation obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    /**
     * ownerId: the origin userId
     * targetId: the userId be replacement
     */
    @Override
    public UserImpersonation getUserImpersonationByOwnerId(Long userId) {
        ListingLocator locator = new ListingLocator();
        List<UserImpersonation> objs = this.queryUserImpersonations(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.STATUS.eq(UserStatus.ACTIVE.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.OWNER_ID.eq(userId));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.OWNER_TYPE.eq(EntityType.USER.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.TARGET_TYPE.eq(EntityType.USER.getCode()));
                return query;
            }
            
        });
        
        if(objs == null || objs.size() == 0) {
            return null;
        }
        
        return objs.get(0);
    }
    
    @Override
    public UserImpersonation getUserImpersonationByTargetId(Long userId) {
        ListingLocator locator = new ListingLocator();
        List<UserImpersonation> objs = this.queryUserImpersonations(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.STATUS.eq(UserStatus.ACTIVE.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.TARGET_ID.eq(userId));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.OWNER_TYPE.eq(EntityType.USER.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.TARGET_TYPE.eq(EntityType.USER.getCode()));
                return query;
            }
            
        });
        
        if(objs == null || objs.size() == 0) {
            return null;
        }
        
        return objs.get(0);
    }
    
    @Override
    public List<UserImpersonation> findTargetUserImpersonations(Long userId, ListingLocator locator, int count) {
        List<UserImpersonation> objs = this.queryUserImpersonations(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.STATUS.eq(UserStatus.ACTIVE.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.TARGET_ID.eq(userId));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.OWNER_TYPE.eq(EntityType.USER.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.TARGET_TYPE.eq(EntityType.USER.getCode()));
                return query;
            }
            
        });
        
        return objs;
    }
    
    @Override
    public List<UserImpersonation> findOwnerUserImpersonations(Long userId, ListingLocator locator, int count) {
        List<UserImpersonation> objs = this.queryUserImpersonations(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.STATUS.eq(UserStatus.ACTIVE.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.OWNER_ID.eq(userId));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.OWNER_TYPE.eq(EntityType.USER.getCode()));
                query.addConditions(Tables.EH_USER_IMPERSONATIONS.TARGET_TYPE.eq(EntityType.USER.getCode()));
                return query;
            }
            
        });
        
        return objs;
    }
    
    @Override
    public List<UserImperInfo> searchUserByPhone(Integer namespaceId, String keyword, Byte impOnly, CrossShardListingLocator locator, int pageSize) {
        List<UserImperInfo> list = new ArrayList<UserImperInfo>();
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            Condition cond = Tables.EH_USERS.ID.ne(0l);
            if(namespaceId != null) {
                cond = cond.and(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
            }
            if(keyword != null && !StringUtils.isEmpty(keyword)){
                 Condition cond1 = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(keyword + "%");
                 cond1 = cond1.or(Tables.EH_USERS.NICK_NAME.like(keyword+"%"));
                 cond = cond.and(cond1);
            }
            
            SelectOnConditionStep<Record> first = context.select().from(Tables.EH_USERS).leftOuterJoin(Tables.EH_USER_IDENTIFIERS)
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));
            SelectOnConditionStep<Record> second = null;
            if(impOnly != null && impOnly.byteValue() > 0) {
                second = first.join(Tables.EH_USER_IMPERSONATIONS)
                        .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IMPERSONATIONS.OWNER_ID).or(Tables.EH_USERS.ID.eq(Tables.EH_USER_IMPERSONATIONS.TARGET_ID)));
            } else {
                second = first.leftOuterJoin(Tables.EH_USER_IMPERSONATIONS)
                        .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IMPERSONATIONS.OWNER_ID).or(Tables.EH_USERS.ID.eq(Tables.EH_USER_IMPERSONATIONS.TARGET_ID)));
            }
             
            if(locator.getAnchor() != null ) {
                cond = cond.and(Tables.EH_USERS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
            }
            
            second.where(cond).orderBy(Tables.EH_USERS.CREATE_TIME.desc())
            .limit(pageSize)
            .fetch().map(r -> {
                UserImperInfo user = new UserImperInfo();
                user.setId(r.getValue(Tables.EH_USERS.ID));
                user.setNamespaceId(r.getValue(Tables.EH_USERS.NAMESPACE_ID));
                user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
                Long ownerId = r.getValue(Tables.EH_USER_IMPERSONATIONS.OWNER_ID);
                Long targetId = r.getValue(Tables.EH_USER_IMPERSONATIONS.TARGET_ID);
                user.setOwnerId(ownerId);
                user.setTargetId(targetId);
                user.setImperId(r.getValue(Tables.EH_USER_IMPERSONATIONS.ID));
                user.setPhone(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
                Timestamp ts = r.getValue(Tables.EH_USERS.CREATE_TIME);
                if(ts != null) {
                    user.setCreateTime(ts.getTime());  
                }
                
                user.setGender(r.getValue(Tables.EH_USERS.GENDER));
                list.add(user);
                return null;
            });
            return true;
        });
        
        if(list.size() == pageSize) {
            locator.setAnchor(list.get(pageSize-1).getCreateTime());
        } else {
            locator.setAnchor(null);
        }
        
        return list;
    }
}
