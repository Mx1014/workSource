// @formatter:off
package com.everhomes.user;

import com.everhomes.aclink.AclinkUser;
import com.everhomes.asset.AssetPaymentStrings;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cache.CacheProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.rest.aclink.DoorAuthStatus;
import com.everhomes.rest.aclink.DoorAuthType;
import com.everhomes.rest.aclink.ListAclinkUserCommand;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.user.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhUserIdentifiersRecord;
import com.everhomes.server.schema.tables.records.EhUserLikesRecord;
import com.everhomes.server.schema.tables.records.EhUsersRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.sdk.SdkUserService;
import com.everhomes.util.*;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.everhomes.server.schema.Tables.*;

/**
 * 
 * Provide data services (cache-enabled) for user management service
 * 
 * <b>Multiple login per user support</b>
 * <p>
 *  It is based on deviceIdentifier that is given, login will be tied to deviceIdentifier(uniquely identifies the physical device),
 *  however for browser based login, its deviceIdentifier will be null, we will only accept maximum of one login, which means, if
 *  you log on into another browser, it will kick off a previous one
 *
 * <p>
 * <b>Schemas</b>
 *
 * <pre>
 * Redis schema
 *  seq (HashMap)
 *      "usr" -> next user account name
 * 
 * </pre>
 * 
 * <p>
 * 
 * @author Kelven Yang
 *
 */
@Component
public class UserProviderImpl implements UserProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProviderImpl.class);
   
    private static final int IDENTIFIER_CLAIMING_TIMEOUT_MINUTES = 10;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private CacheProvider cacheProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private EnterpriseContactProvider ecProvider;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SdkUserService sdkUserService;
    
    public UserProviderImpl() {
    }

    @Override
    public void createUser(User user) {
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        long id = sdkUserService.getSequence(Tables.EH_USERS.getName(), 1L);
        //long id = this.shardingProvider.allocShardableContentId(EhUsers.class).second();
        user.setId(id);
        if(user.getAccountName() == null) {
            long accountSeq = sdkUserService.getSequence("usr-account", 1L);
            user.setAccountName(String.valueOf(accountSeq));
        }
        if(user.getUuid() == null)
            user.setUuid(UUID.randomUUID().toString());
        user.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        user.setUpdateTime(user.getCreateTime());
        user.incrementVersion();
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.insert(user);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUsers.class, null);
        kafkaTemplate.send("user-create-core-event", String.valueOf(System.nanoTime()), StringHelper.toJsonString(user));
    }

    @Override
    public void createUserFromUnite(User user) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, user.getId()));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.insert(user);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUsers.class, null);
    }

    @Caching(evict={@CacheEvict(value="UserIdentifier-List", key="#userIdentifier.ownerUid")})
    @Override
    public void createIdentifierFromUnite(UserIdentifier userIdentifier) {
        assert(userIdentifier.getOwnerUid() != null);

        // identifier record will be saved in the same shard as its owner users
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid()));

        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.insert(userIdentifier);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserIdentifiers.class, null);
    }

    @Caching(evict={@CacheEvict(value="User-Id", key="#user.id"),
            @CacheEvict(value="User-Acount", key="#user.accountName")})
    @Override
    public void updateUser(User user) {
        assert(user.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, user.getId().longValue()));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        user.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        user.incrementVersion();

        dao.update(user);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUsers.class, user.getId());
        kafkaTemplate.send("user-update-core-event", String.valueOf(System.nanoTime()), StringHelper.toJsonString(user));

    }

    @Caching(evict={@CacheEvict(value="User-Id", key="#user.id"),  @CacheEvict(value="User-Acount", key="#user.accountName")})
    @Override
    public void updateUserFromUnite(User user) {
        assert(user.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, user.getId()));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        user.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(user);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUsers.class, user.getId());
    }

    @Caching(evict={@CacheEvict(value="UserIdentifier-Id", key="#userIdentifier.id"),
            @CacheEvict(value="UserIdentifier-Claiming", key="#userIdentifier.identifierToken", condition = "#userIdentifier.identifierToken != null"),
            @CacheEvict(value="UserIdentifier-List", key="#userIdentifier.ownerUid"),
            @CacheEvict(value="UserIdentifier-OwnerAndType", key="{#userIdentifier.ownerUid, #userIdentifier.identifierType}"),
            @CacheEvict(value="UserIdentifier-NamespaceAndToken", key="{#userIdentifier.namespaceId, #userIdentifier.identifierToken}" )})
    @Override
    public void updateIdentifierFromUnite(UserIdentifier userIdentifier) {
        assert(userIdentifier.getId() != null);
        assert(userIdentifier.getOwnerUid() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid()));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.update(userIdentifier);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserIdentifiers.class, userIdentifier.getId());
    }

    @Caching(evict={@CacheEvict(value="User-Id", key="#user.id"),
            @CacheEvict(value="User-Acount", key="#user.accountName"),
            @CacheEvict(value="UserIdentifier-List", key="#user.id")})
    @Override
    public void deleteUser(User user) {
        // TODO delete all user references
        
        // delete the record itself
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, user.getId()));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.deleteById(user.getId());
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUsers.class, user.getId());
    }
    
    @Override
    public void deleteUser(long id) {
        UserProvider self = PlatformContext.getComponent(UserProvider.class);
        User user = self.findUserById(id);
        
        if(user != null)
            self.deleteUser(user);
    }
    
    @Cacheable(value = "User-Id", key="#id", unless="#result == null")
    @Override
    public User findUserById(final long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        EhUsers user = dao.findById(id);
        return ConvertHelper.convert(user, User.class);
    }
    
    @Override
    @Cacheable(value = "User-Acount", key="#accountName", unless="#result == null")
    public User findUserByAccountName(final String accountName) {
        final User result[] = new User[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (DSLContext context, Object reducingContext) -> {
            EhUsersRecord record = (EhUsersRecord)context.select()
                .from(EH_USERS)
                .where(EH_USERS.ACCOUNT_NAME.equal(accountName))
                .fetchOne();
            
            if(record != null) {
                result[0] = ConvertHelper.convert(record, User.class);
                return false;
            }
            
            return true;
        });
        return result[0];
    }
    
    @Override
    public List<User> queryUsers(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        
        List<User> userList = new ArrayList<User>();
        
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUsers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhUsersRecord> query = context.selectQuery(Tables.EH_USERS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_USERS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_USERS.ID.asc());
            query.addLimit(count - userList.size());
            
            query.fetch().map((r) -> {
                userList.add(ConvertHelper.convert(r, User.class));
                return null;
            });
           
            if(userList.size() >= count) {
                locator.setAnchor(userList.get(userList.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if(userList.size() > 0) {
            locator.setAnchor(userList.get(userList.size() - 1).getId());
        }
        
        return userList;
    }

    @Override
    public List<User> listUsers(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        List<User> userList = new ArrayList<User>();

        Integer size = count + 1;
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<Record> query = context.selectQuery();
                    query.addFrom(Tables.EH_USERS);
                    query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));
                    query.addJoin(Tables.EH_ORGANIZATION_MEMBERS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID));


                    if (null != queryBuilderCallback) {
                        queryBuilderCallback.buildCondition(locator, query);
                    }
                    if (null != locator && null != locator.getAnchor())
                        query.addConditions(Tables.EH_USERS.ID.lt(locator.getAnchor()));

                    query.addOrderBy(Tables.EH_USERS.ID.desc());
                    query.addLimit(size);
                    LOGGER.debug("query sql:{}", query.getSQL());
                    LOGGER.debug("query param:{}", query.getBindValues());
                    query.fetch().map((r) -> {
                        User user = new User();
                        user.setId(r.getValue(Tables.EH_USERS.ID));
                        user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
                        user.setGender(r.getValue(Tables.EH_USERS.GENDER));
                        user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
                        user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
                        user.setNamespaceUserType(r.getValue(Tables.EH_USERS.NAMESPACE_USER_TYPE));
                        userList.add(user);
                        return null;
                    });
                    return true;
                });

        locator.setAnchor(null);
        if (userList.size() > count) {
            userList.remove(userList.size() - 1);
            locator.setAnchor(userList.get(userList.size() - 1).getId());
        }
        return userList;
    }

    @Cacheable(value = "UserIdentifier-List", key="#userId", unless="#result.size() == 0")
    @Override
    public List<UserIdentifier> listUserIdentifiersOfUser(long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        List<UserIdentifier> identifiers = context.select().from(EH_USER_IDENTIFIERS).where(EH_USER_IDENTIFIERS.OWNER_UID.eq(userId))
            .fetch().map((Record record) -> {
                   return ConvertHelper.convert(record, UserIdentifier.class);
            });
        return identifiers;
    }

    @Override
    public UserIdentifier findUserIdentifiersOfUser(long userId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        List<UserIdentifier> identifiers = context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.OWNER_UID.eq(userId))
                .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                .fetch().map((Record record) -> {
                    return ConvertHelper.convert(record, UserIdentifier.class);
                });
        if(identifiers == null || identifiers.size() == 0) {
            return null;
        }
        return identifiers.get(0);
    }

    @Caching(evict={@CacheEvict(value="UserIdentifier-List", key="#userIdentifier.ownerUid")})
    @Override
    public void createIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getOwnerUid() != null);
        
        // identifier record will be saved in the same shard as its owner users
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        long id = sdkUserService.getSequence(Tables.EH_USER_IDENTIFIERS.getName(), 1L);
        
        userIdentifier.setId(id);
        Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
        userIdentifier.setCreateTime(ts);
        //去除手机号首尾空格 add by yanlong.liang 20180929
        if (!StringUtils.isEmpty(userIdentifier.getIdentifierToken())) {
            userIdentifier.setIdentifierToken(userIdentifier.getIdentifierToken().trim());
        }

        userIdentifier.incrementVersion();

        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.insert(userIdentifier);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserIdentifiers.class, null);
        kafkaTemplate.send("userIdentifier-create-core-event", String.valueOf(System.nanoTime()), StringHelper.toJsonString(userIdentifier));

    }

    @Caching(evict={@CacheEvict(value="UserIdentifier-Id", key="#userIdentifier.id"),
            @CacheEvict(value="UserIdentifier-Claiming", key="#userIdentifier.identifierToken", condition = "#userIdentifier.identifierToken != null"),
            @CacheEvict(value="UserIdentifier-List", key="#userIdentifier.ownerUid"),
            @CacheEvict(value="UserIdentifier-OwnerAndType", key="{#userIdentifier.ownerUid, #userIdentifier.identifierType}"),
            @CacheEvict(value="UserIdentifier-NamespaceAndToken", key="{#userIdentifier.namespaceId, #userIdentifier.identifierToken}" )})
    @Override
    public void updateIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getId() != null);
        assert(userIdentifier.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        userIdentifier.incrementVersion();

        dao.update(userIdentifier);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserIdentifiers.class, userIdentifier.getId());
        kafkaTemplate.send("userIdentifier-update-core-event", String.valueOf(System.nanoTime()), StringHelper.toJsonString(userIdentifier));

    }

    @Caching(evict={@CacheEvict(value="UserIdentifier-Id", key="#userIdentifier.id"),
            @CacheEvict(value="UserIdentifier-Claiming", key="#userIdentifier.identifierToken", condition = "#userIdentifier.identifierToken != null"),
            @CacheEvict(value="UserIdentifier-List", key="#userIdentifier.ownerUid"),
            @CacheEvict(value="UserIdentifier-OwnerAndType", key="{#userIdentifier.ownerUid, #userIdentifier.identifierType}"),
            @CacheEvict(value="UserIdentifier-NamespaceAndToken", key="{#userIdentifier.namespaceId, #userIdentifier.identifierToken}" )})
    @Override
    public void deleteIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getId() != null);
        assert(userIdentifier.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.delete(userIdentifier);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserIdentifiers.class, userIdentifier.getId());
        kafkaTemplate.send("userIdentifier-delete-core-event", String.valueOf(System.nanoTime()), StringHelper.toJsonString(userIdentifier));

    }

    @Override
    public void deleteIdentifier(long id) {
        UserProvider self = PlatformContext.getComponent(UserProvider.class);
        
        UserIdentifier userIdentifier = self.findIdentifierById(id);
        if(userIdentifier != null) {
            self.deleteIdentifier(userIdentifier);
        }
    }
    
    @Cacheable(value = "UserIdentifier-Id", key="#id", unless="#result == null")
    @Override
    public UserIdentifier findIdentifierById(final long id) {
        final UserIdentifier[] result = new UserIdentifier[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
            EhUserIdentifiers userIdentifier = dao.findById(id);
            if(userIdentifier != null) {
                result[0] = ConvertHelper.convert(userIdentifier, UserIdentifier.class);
                return false;
            }
            return true;
        });

        return result[0];
    }
       
    @Cacheable(value = "UserIdentifier-Claiming", key="#identifierToken", unless="#result.size() == 0")
    @Override
    public List<UserIdentifier> findClaimingIdentifierByToken(String identifierToken) {
        final List<UserIdentifier> result = new ArrayList<>();
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMING.getCode())
                    .or(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.VERIFYING.getCode())))
                .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, UserIdentifier.class));
                return null;
            });
            
            return true;
        });
            
        return result;
    }
    
    @Override
    public List<UserIdentifier> findClaimedIdentifiersByToken(String identifierToken) {
        final List<UserIdentifier> result = new ArrayList<>();
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                .fetch().map((r) -> {
                    result.add(ConvertHelper.convert(r, UserIdentifier.class));
                    return null;
                });
            
            return true;
        });
        
        
        
        return result;
    }

    @Override
    public List<UserIdentifier> listClaimedIdentifiersByTokens(Integer namespaceId, List<String> identifiers) {
        final List<UserIdentifier> result = new ArrayList<>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                    .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.in(identifiers))
                    .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                    .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                    .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, UserIdentifier.class));
                return null;
            });

            return true;
        });

        return result;
    }

    @Override
    public UserIdentifier findClaimedIdentifierByToken(String identifierToken) {
        final List<UserIdentifier> result = new ArrayList<>();
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                .fetch().map((r) -> {
                    result.add(ConvertHelper.convert(r, UserIdentifier.class));
                    return null;
                });
            
            return true;
        });
        
        if(result.size() == 1)
            return result.get(0);
        else if(result.size() > 1) {
            LOGGER.warn(String.format("%s has been claimed by multiple users", identifierToken));
            return result.get(0);
        }
        
        return null;
    }


    /**
     *根据手机号和域空间来查询eh_user_identifiers表中的数据
     * @param identifierToken
     * @param namespaceId
     * @return
     */
    @Override
    public UserIdentifier findClaimedIdentifierByTokenAndNamespaceId(String identifierToken,Integer namespaceId) {
        final List<UserIdentifier> result = new ArrayList<>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                    .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                    .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                    .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                    .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, UserIdentifier.class));
                return null;
            });

            return true;
        });

        if(result.size() == 1)
            return result.get(0);
        else if(result.size() > 1) {
            LOGGER.warn(String.format("%s has been claimed by multiple users", identifierToken));
            return result.get(0);
        }

        return null;
    }

    @Override
    // 增加统计支付宝用户
    // public Integer countAppAndWeiXinUserByCreateTime(Integer namespaceId, LocalDateTime start, LocalDateTime end, List<Long> excludeUIDs) {
    public Integer countAppAndWeiXinAndAlipayUserByCreateTime(Integer namespaceId, LocalDateTime start, LocalDateTime end, List<Long> excludeUIDs) {
        com.everhomes.server.schema.tables.EhUsers t = Tables.EH_USERS;
        final Integer[] count = {0};
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null,
                (DSLContext context, Object reducingContext)-> {
                    Condition appUser = t.NAMESPACE_USER_TYPE.isNull().and(t.NAMESPACE_USER_TOKEN.eq(""));
                    Condition weiXinUser = t.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode());
                    // 增加统计支付宝用户
                    Condition alipayUser = t.NAMESPACE_USER_TYPE.eq(NamespaceUserType.ALIPAY.getCode());

                    SelectQuery<Record1<Integer>> query = context
                            .selectCount().from(t)
                            .where(t.NAMESPACE_ID.eq(namespaceId))
                            .and(t.STATUS.eq(UserStatus.ACTIVE.getCode()))
                            .and(appUser.or(weiXinUser).or(alipayUser))//增加支付宝用户
                            .getQuery();

                    if (excludeUIDs != null && excludeUIDs.size() > 0) {
                        query.addConditions(t.ID.notIn(excludeUIDs));
                    }
                    if (start != null) {
                        query.addConditions(t.CREATE_TIME.ge(Timestamp.valueOf(start)));
                    }
                    if (end != null) {
                        query.addConditions(t.CREATE_TIME.lt(Timestamp.valueOf(end)));
                    }
                    count[0] += query.fetchOneInto(Integer.class);
                    return true;
                });
        return count[0];
    }

    @Override
    // 增加统计支付宝用户
    // public List<User> listAppAndWeiXinUserByCreateTime(Integer namespaceId, LocalDateTime start, LocalDateTime end, List<Long> excludeUIDs) {
    public List<User> listAppAndWeiXinAndAlipayUserByCreateTime(Integer namespaceId, LocalDateTime start, LocalDateTime end, List<Long> excludeUIDs) {
        com.everhomes.server.schema.tables.EhUsers t = Tables.EH_USERS;
        final List<User> users = new ArrayList<>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null,
                (DSLContext context, Object reducingContext)-> {
                    Condition appUser = t.NAMESPACE_USER_TYPE.isNull().and(t.NAMESPACE_USER_TOKEN.eq(""));
                    Condition weiXinUser = t.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode());
                    // 增加统计支付宝用户
                    Condition alipayUser = t.NAMESPACE_USER_TYPE.eq(NamespaceUserType.ALIPAY.getCode());

                    SelectQuery<EhUsersRecord> query = context
                            .selectFrom(t)
                            .where(t.NAMESPACE_ID.eq(namespaceId))
                            .and(t.STATUS.eq(UserStatus.ACTIVE.getCode()))
                            .and(appUser.or(weiXinUser).or(alipayUser))//增加支付宝用户
                            .getQuery();

                    if (excludeUIDs != null && excludeUIDs.size() > 0) {
                        query.addConditions(t.ID.notIn(excludeUIDs));
                    }
                    if (start != null) {
                        query.addConditions(t.CREATE_TIME.ge(Timestamp.valueOf(start)));
                    }
                    if (end != null) {
                        query.addConditions(t.CREATE_TIME.lt(Timestamp.valueOf(end)));
                    }
                    users.addAll(query.fetchInto(User.class));
                    return true;
                });
        return users;
    }

    @Override
    public String getNickNameByUid(Long creatorUid) {
        return this.dbProvider.getDslContext(AccessSpec.readOnly()).select(Tables.EH_USERS.NICK_NAME)
                .from(Tables.EH_USERS).where(Tables.EH_USERS.ID.eq(creatorUid))
                .fetchOne(Tables.EH_USERS.NICK_NAME);
    }


    /**
     * 根据域空间id和注册的手机号来查询对应的注册信息
     * @param namespaceId
     * @param identifierToken
     * @return
     */
    @Cacheable(value = "UserIdentifier-NamespaceAndToken", key="{#namespaceId, #identifierToken}", unless="#result == null")
    @Override
    public UserIdentifier findClaimedIdentifierByToken(Integer namespaceId, String identifierToken) {
        final List<UserIdentifier> result = new ArrayList<>();
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                .fetch().map((r) -> {
                    result.add(ConvertHelper.convert(r, UserIdentifier.class));
                    return null;
                });
            
            return true;
        });
        
        if(result.size() == 1)
            return result.get(0);
        else if(result.size() > 1) {
            LOGGER.warn(String.format("%s has been claimed by multiple users", identifierToken));
            return result.get(0);
        }
        
        return null;
    }

    @Override
    public UserIdentifier findClaimedIdentifierByTokenAndNotUserId(Integer namespaceId, String identifierToken, Long userId) {
        final List<UserIdentifier> result = new ArrayList<>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                    .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                    .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                    .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                    .and(EH_USER_IDENTIFIERS.OWNER_UID.ne(userId))
                    .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, UserIdentifier.class));
                return null;
            });

            return true;
        });
        if(result.size() > 0){
            return result.get(0);
        }
        return null;
    }

    @Override
    public int countUserByNamespaceIdAndNamespaceUserType(Integer namespaceId, String namespaceUserType){
        final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null,
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_USERS)
                            .where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
                            .and(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(namespaceUserType))
                            .fetchOneInto(Integer.class);
                    return true;
                });

        return count[0];
    }
    @Cacheable(value = "UserIdentifier-OwnerAndType", key="{#ownerUid, #identifierType}", unless="#result == null")
    @Override
    public UserIdentifier findClaimedIdentifierByOwnerAndType(long ownerUid, byte identifierType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, ownerUid));
        Record record = context.select().from(EH_USER_IDENTIFIERS)
            .where(EH_USER_IDENTIFIERS.OWNER_UID.eq(ownerUid))
            .and(EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(identifierType))
            .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
            .fetchAny();
        return ConvertHelper.convert(record, UserIdentifier.class);
    }

    @Override
    public UserIdentifier findClaimingIdentifierByToken(Integer namespaceId, String identifierToken) {
        final List<UserIdentifier> result = new ArrayList<>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(EH_USER_IDENTIFIERS)
                    .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                    .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                    .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode())
                            .or(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.VERIFYING.getCode())))
                    .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, UserIdentifier.class));
                return null;
            });

            return true;
        });

        if(result.size() == 1)
            return result.get(0);
        else if(result.size() > 1) {
            LOGGER.warn(String.format("%s has been claimed by multiple users", identifierToken));
            return result.get(0);
        }

        return null;
    }

    @Override
    public UserIdentifier findIdentifierByOwnerAndTypeAndClaimStatus(long ownerUid, byte identifierType, byte claimStatus) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, ownerUid));
        Record record = context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.OWNER_UID.eq(ownerUid))
                .and(EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(identifierType))
                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(claimStatus))
                .fetchAny();
        return ConvertHelper.convert(record, UserIdentifier.class);
    }
    
	@Caching(evict = { @CacheEvict(value = "UserGroup-Listing", key = "{#userGroup.ownerUid, #userGroup.groupDiscriminator}"),
			@CacheEvict(value = "UserGroupByOwnerAndGroup", key = "{#userGroup.ownerUid, #userGroup.groupId}") })
    public void createUserGroup(UserGroup userGroup) {
        assert(userGroup.getOwnerUid() != null);
        assert(userGroup.getGroupId() != null);

        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserGroups.class));
        userGroup.setId(id);
        userGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userGroup.getOwnerUid()));
        EhUserGroupsDao dao = new EhUserGroupsDao(context.configuration());
        dao.insert(userGroup);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserGroups.class, null);
    }
    
	@Caching(evict = { @CacheEvict(value = "UserGroup-Listing", key = "{#userGroup.ownerUid, #userGroup.groupDiscriminator}"),
			@CacheEvict(value = "UserGroupByOwnerAndGroup", key = "{#userGroup.ownerUid, #userGroup.groupId}") })
    public void updateUserGroup(UserGroup userGroup) {
        assert(userGroup.getId() != null);
        assert(userGroup.getOwnerUid() != null);
        assert(userGroup.getGroupId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userGroup.getOwnerUid()));
        EhUserGroupsDao dao = new EhUserGroupsDao(context.configuration());
        dao.update(userGroup);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserGroups.class, userGroup.getId());
    }
    
	@Caching(evict = { @CacheEvict(value = "UserGroup-Listing", key = "{#userGroup.ownerUid, #userGroup.groupDiscriminator}"),
			@CacheEvict(value = "UserGroupByOwnerAndGroup", key = "{#userGroup.ownerUid, #userGroup.groupId}")})
    public void deleteUserGroup(UserGroup userGroup) {
        assert(userGroup.getId() != null);
        assert(userGroup.getOwnerUid() != null);
        assert(userGroup.getGroupId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userGroup.getOwnerUid()));
        EhUserGroupsDao dao = new EhUserGroupsDao(context.configuration());
        dao.delete(userGroup);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserGroups.class, userGroup.getId());
    }

	public void deleteUserGroup(long ownerId, long groupId) {
		UserProvider self = PlatformContext.getComponent(UserProvider.class);
		UserGroup userGroup = self.findUserGroupByOwnerAndGroup(ownerId, groupId);
		if(userGroup != null) {
			self.deleteUserGroup(userGroup);
		}
	}

	@Cacheable(value = "UserGroupByOwnerAndGroup", key = "{#ownerId, #groupId}", unless = "#result == null")
	public UserGroup findUserGroupByOwnerAndGroup(long ownerId, long groupId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, ownerId));
		
		Record r = context.select().from(Tables.EH_USER_GROUPS)
				.where(Tables.EH_USER_GROUPS.OWNER_UID.eq(ownerId))
				.and(Tables.EH_USER_GROUPS.GROUP_ID.eq(groupId)).fetchOne();
		
		return ConvertHelper.convert(r, UserGroup.class);
	}
    @Cacheable(value="UserGroup-Listing", key = "{#uid, #groupDiscriminator}")
    public List<UserGroup> listUserGroups(long uid, String groupDiscriminator) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        
        if(groupDiscriminator != null) {
            return context.select().from(Tables.EH_USER_GROUPS)
                .where(Tables.EH_USER_GROUPS.OWNER_UID.eq(uid))
                .and(Tables.EH_USER_GROUPS.GROUP_DISCRIMINATOR.eq(groupDiscriminator))
                .fetch().map((r)-> {
                   return ConvertHelper.convert(r, UserGroup.class); 
                });
        } else {
            return context.select().from(Tables.EH_USER_GROUPS)
            .where(Tables.EH_USER_GROUPS.OWNER_UID.eq(uid))
            .fetch().map((r)-> {
               return ConvertHelper.convert(r, UserGroup.class); 
            });
        }
    }

    @Override
    @Caching(evict = { 
            @CacheEvict(value="UserLike", key="{#userLike.ownerUid, #userLike.targetType, #userLike.targetId}"),
            @CacheEvict(value="UserLike-Listing", key="{#userLike.ownerUid, #userLike.targetType}")})
    public void createUserLike(UserLike userLike) {
        assert(userLike.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userLike.getOwnerUid()));
        
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserLikes.class));
        userLike.setId(id);
        userLike.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        EhUserLikesDao dao = new EhUserLikesDao(context.configuration());
        dao.insert(userLike);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserLikes.class, null);
    }

    @Caching(evict = { @CacheEvict(value="UserLikeById", key="#userLike.id"), 
            @CacheEvict(value="UserLike", key="{#userLike.ownerUid, #userLike.targetType, #userLike.targetId}"),
            @CacheEvict(value="UserLike-Listing", key="{#userLike.ownerUid, #userLike.targetType}")})
    @Override
    public void updateUserLike(UserLike userLike) {
        assert(userLike.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userLike.getOwnerUid()));
        EhUserLikesDao dao = new EhUserLikesDao(context.configuration());
        dao.update(userLike);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserLikes.class, userLike.getId());
    }

    @Caching(evict = { @CacheEvict(value="UserLikeById", key="#userLike.id"), 
            @CacheEvict(value="UserLike", key="{#userLike.ownerUid, #userLike.targetType, #userLike.targetId}"),
            @CacheEvict(value="UserLike-Listing", key="{#userLike.ownerUid, #userLike.targetType}")})
    @Override
    public void deleteUserLike(UserLike userLike) {
        assert(userLike.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userLike.getOwnerUid()));
        EhUserLikesDao dao = new EhUserLikesDao(context.configuration());
        dao.delete(userLike);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserLikes.class, userLike.getId());
    }

    @Override
    public void deleteUserLikeById(long id) {
        UserProvider self = PlatformContext.getComponent(UserProvider.class);
        UserLike userLike = self.findUserLikeById(id);
        if(userLike != null)
            self.deleteUserLike(userLike);
    }

    @Cacheable(value = "UserLikeById", key="#id")
    @Override
    public UserLike findUserLikeById(long id) {
        final UserLike[] result = new UserLike[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            EhUserLikesDao dao = new EhUserLikesDao(context.configuration());
            EhUserLikes userLike = dao.findById(id);
            if(userLike != null) {
                result[0] = ConvertHelper.convert(userLike, UserLike.class);
                return false;
            }
            return true;
        });

        return result[0];
    }

    @Cacheable(value = "UserLike", key="{#uid,  #targetType, #targetId}")
    @Override
    public UserLike findUserLike(long uid, String targetType, long targetId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        
        return ConvertHelper.convert(context.select().from(Tables.EH_USER_LIKES)
            .where(Tables.EH_USER_LIKES.OWNER_UID.eq(uid))
            .and(Tables.EH_USER_LIKES.TARGET_TYPE.eq(targetType))
            .and(Tables.EH_USER_LIKES.TARGET_ID.eq(targetId))
            .fetchOne(), UserLike.class);
    }

    @Cacheable(value = "UserLike-Listing", key="{#uid,  #targetType}")
    @Override
    public List<UserLike> listUserLikes(long uid, String targetType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));

        SelectQuery<EhUserLikesRecord> query = context.selectQuery(Tables.EH_USER_LIKES);
        query.addConditions(Tables.EH_USER_LIKES.OWNER_UID.eq(uid));
        if(targetType != null)
            query.addConditions(Tables.EH_USER_LIKES.TARGET_TYPE.eq(targetType));
        
        return query.fetch().map((r)-> { return ConvertHelper.convert(r, UserLike.class); } );
    }

    @Override
    public UserIdentifier findIdentifierByVerifyCode(String code,String identifyToken) {
        final UserIdentifier[] result = new UserIdentifier[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            Record record = context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.VERIFICATION_CODE.eq(code))
                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
                .and(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifyToken))
                .fetchOne();
            
            if(record != null) {
                result[0] = ConvertHelper.convert(record, UserIdentifier.class);
                return false;
            }
            return true;
        });
        return result[0];
    }


    @Override
    public UserInvitationsDTO findUserInvitationByCode(String invitationCode) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class));
        UserInvitationsDTO[] dto=new UserInvitationsDTO[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            EhUserInvitationsDao dao=new EhUserInvitationsDao(cxt.configuration());
            List<EhUserInvitations> invitation = dao.fetchByInviteCode(invitationCode);
            if(!CollectionUtils.isEmpty(invitation)){
                dto[0]=ConvertHelper.convert(invitation.get(0), UserInvitationsDTO.class);
                return false;
            }
            return true;
        });
      return dto[0];
    }

    @Override
    public void createInvitation(UserInvitation invitations) {
        Long id=sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserInvitations.class));
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,invitations.getOwnerUid()));
        invitations.setId(id);
        EhUserInvitationsDao dao=new EhUserInvitationsDao(cxt.configuration());
        dao.insert(invitations);
    }

    @Override
    public void createUserInvitationRoster(UserInvitationRoster roster,Long uid) {
        Long id=sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserInvitationRoster.class));
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,uid));
        roster.setId(id);
        EhUserInvitationRosterDao dao=new EhUserInvitationRosterDao(cxt.configuration());
        dao.insert(roster);
    }

    @Override
    public void updateInvitation(UserInvitation invitations) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,invitations.getOwnerUid()));
        EhUserInvitationsDao dao=new EhUserInvitationsDao(cxt.configuration());
        dao.update(invitations);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserInvitations.class, invitations.getId());
    }

    private long getIdentifierClaimingTimeoutMs() {
        return this.configurationProvider.getIntValue("user.claim.timeout", IDENTIFIER_CLAIMING_TIMEOUT_MINUTES)*60000;
    }
    
    @Override
    @Scheduled(initialDelay = 1000, fixedRate = 60000)
    public void cleanupZombies() {

        // TODO we probably need to reset cache as well
        Timestamp cutoffTime = new Timestamp(DateHelper.currentGMTTime().getTime() - getIdentifierClaimingTimeoutMs());
        dbProvider.mapReduce(AccessSpec.readWriteWith(EhUsers.class), null, (DSLContext context, Object reducingContext) -> {
            context.delete(Tables.EH_USER_IDENTIFIERS)
                    .where(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMING.getCode())
                            .or(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.VERIFYING.getCode())))
                    .and(EH_USER_IDENTIFIERS.CREATE_TIME.lt(cutoffTime)).execute();

            context.delete(Tables.EH_USERS)
                    .where(EH_USERS.STATUS.eq(UserStatus.INACTIVE.getCode()))
                    .and(EH_USERS.CREATE_TIME.lt(cutoffTime)).execute();
            return true;
        });
    }

    @Override
    public User findUserByUuid(String uuid) {
        User[] result = new User[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            EhUsersRecord record = (EhUsersRecord)context.select()
                    .from(EH_USERS)
                    .where(EH_USERS.UUID.equal(uuid))
                    .fetchOne();
                
                if(record != null) {
                    result[0] = ConvertHelper.convert(record, User.class);
                    return false;
                }
                
                return true;
        });
        return result[0];
    }

	@Override
	public List<InvitationRoster> listInvitationRostor(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        
        List<InvitationRoster> result = new ArrayList<InvitationRoster>();
        
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUsers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhUsersRecord> query = context.selectQuery(Tables.EH_USERS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            query.addConditions(Tables.EH_USERS.INVITOR_UID.isNotNull());
            query.addConditions(Tables.EH_USERS.INVITOR_UID.ne((long) 0));
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_USERS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_USERS.ID.asc());
            query.addLimit(count - result.size());
            
            query.fetch().map((r) -> {
            	User u = ConvertHelper.convert(r, User.class);
            	InvitationRoster roster = new InvitationRoster();
            	roster.setUid(u.getId());
            	roster.setUserNickName(u.getNickName());
            	roster.setRegTime(u.getCreateTime());
            	roster.setInviteType(u.getInviteType());
            	roster.setInviterId(u.getInvitorUid());
                result.add(roster);
                return null;
            });
           
            if(result.size() >= count) {
                locator.setAnchor(result.get(result.size() - 1).getUid());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if(result.size() > 0) {
            locator.setAnchor(result.get(result.size() - 1).getUid());
        }
        
        return result;
	}
	
	public List<User> listUserByKeyword(String keyword) {
		List<User> list = new ArrayList<User>();
		if(keyword == null)
			return null;
		
		String str = "%"+keyword+"%";
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            context.select().from(Tables.EH_USERS)
            .where(Tables.EH_USERS.NICK_NAME.like(str))
            .fetch().map(r -> {
            	list.add(ConvertHelper.convert(r,User.class));
            	return null;
            });
			return true;
        });
		return list;
	}
	
	@Override
	public List<User> listUserByKeyword(String keyword, Integer namespaceId,
			CrossShardListingLocator locator, int pageSize) {
		return listUserByKeyword(null, null, null, keyword, null, namespaceId, locator, pageSize);
	}
	
	@Override
	public List<User> listUserByKeyword(Integer isAuth, Byte gender, Long organizationId, String keyword, Byte executiveFlag,
			Integer namespaceId, CrossShardListingLocator locator, int pageSize) {

		List<User> list = new ArrayList<User>();
		
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
			Condition cond = Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId);
			if(!StringUtils.isEmpty(keyword)){
				 Condition cond1 = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(keyword);
				 cond1 = cond1.or(Tables.EH_USERS.NICK_NAME.like("%"+keyword+"%"));
				 cond = cond.and(cond1);
			}
			if(executiveFlag != null){ 
				cond = cond.and(Tables.EH_USERS.EXECUTIVE_TAG.eq(executiveFlag));
			}

            if(gender != null){
                cond = cond.and(Tables.EH_USERS.GENDER.eq(gender));
            }
			 
			if(locator.getAnchor() != null ) {
				cond = cond.and(Tables.EH_USERS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
		    }

            SelectOnConditionStep query = context.select().from(Tables.EH_USERS).leftOuterJoin(Tables.EH_USER_IDENTIFIERS)
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));

            SelectQuery orgQuery = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
            byte orgQueryFlag = 0;

            //查询认证用户，用左连接
            if (null != isAuth && 0 != isAuth) {
                if (1 == isAuth) {
                    orgQuery.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                }else if (2 == isAuth){
                    orgQuery.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode())
                            .or(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.WAITING_FOR_ACCEPTANCE.getCode())));

                }
                orgQueryFlag = 1;
            }

            //公司，用自然连接
            if (null != organizationId) {

                orgQuery.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId));
                orgQuery.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
                orgQuery.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
                orgQueryFlag = 2;

		    }

            if (1 == orgQueryFlag) {
                query.leftOuterJoin(orgQuery.asTable(Tables.EH_ORGANIZATION_MEMBERS.getName())).on(Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID));
            }
		    if (2 == orgQueryFlag) {
                query.join(orgQuery.asTable(Tables.EH_ORGANIZATION_MEMBERS.getName())).on(Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID));

            }

            query.where(cond).groupBy(Tables.EH_USERS.ID).orderBy(Tables.EH_USERS.CREATE_TIME.desc())
            .limit(pageSize)
            .fetch().map(r -> {
            	User user = ConvertHelper.convert(r,User.class);
            	user.setNamespaceId(r.getValue(Tables.EH_USERS.NAMESPACE_ID));
            	user.setId(r.getValue(Tables.EH_USERS.ID));
            	user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
            	user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
            	user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
            	user.setStatus(r.getValue(Tables.EH_USERS.STATUS));
            	user.setGender(r.getValue(Tables.EH_USERS.GENDER));
            	user.setExecutiveTag(r.getValue(Tables.EH_USERS.EXECUTIVE_TAG));
            	user.setPositionTag(r.getValue(Tables.EH_USERS.POSITION_TAG));
            	user.setIdentityNumberTag(r.getValue(Tables.EH_USERS.IDENTITY_NUMBER_TAG));
            	list.add(user);
            	return null;
            });
			return true;
        });
		
		if(list.size() == pageSize) {
		    locator.setAnchor(list.get(pageSize-1).getCreateTime().getTime());
		} else {
		    locator.setAnchor(null);
		}
		
		return list;
	}
	
	
	   @Override
	    public List<User> listUserByNamespace(String keyword, Integer namespaceId,
	            CrossShardListingLocator locator, int pageSize) {
	        
	        List<User> list = new ArrayList<User>();
	        
	        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
	            Condition cond = Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId);
	            if(!StringUtils.isEmpty(keyword)){
	                 Condition cond1 = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(keyword);
                    cond1 = cond1.or(Tables.EH_USERS.NICK_NAME.like(keyword + "%"));
	                 cond = cond.and(cond1);
	            }
	             
	            if(locator.getAnchor() != null ) {
	                cond = cond.and(Tables.EH_USERS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
	            }
	            context.select().from(Tables.EH_USERS).leftOuterJoin(Tables.EH_USER_IDENTIFIERS)
	            .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID))
	            .where(cond).orderBy(Tables.EH_USERS.CREATE_TIME.desc())
	            .limit(pageSize)
	            .fetch().map(r -> {
	                User user = ConvertHelper.convert(r,User.class);
	                user.setNamespaceId(r.getValue(Tables.EH_USERS.NAMESPACE_ID));
	                user.setId(r.getValue(Tables.EH_USERS.ID));
	                user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
	                user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
	                user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
	                user.setStatus(r.getValue(Tables.EH_USERS.STATUS));
	                user.setGender(r.getValue(Tables.EH_USERS.GENDER));
	                list.add(user);
	                return null;
	            });
	            return true;
	        });
	        
	        if(list.size() == pageSize) {
	            locator.setAnchor(list.get(pageSize-1).getCreateTime().getTime());
	        } else {
	            locator.setAnchor(null);
	        }
	        
	        return list;
	    }

    @Override
    public List<User> searchUserByIdentifier(String identifier, Integer namespaceId, int pageSize) {

        List<User> list = new ArrayList<User>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            Condition cond = Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId);
            cond = cond.and(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(identifier + "%"));
            cond = cond.and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()));
            context.select().from(Tables.EH_USERS).join(Tables.EH_USER_IDENTIFIERS, JoinType.JOIN)
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID))
                    .where(cond).orderBy(Tables.EH_USERS.CREATE_TIME.desc())
                    .limit(pageSize)
                    .fetch().map(r -> {
                User user = ConvertHelper.convert(r,User.class);
                user.setNamespaceId(r.getValue(Tables.EH_USERS.NAMESPACE_ID));
                user.setId(r.getValue(Tables.EH_USERS.ID));
                user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
                user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
                user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
                user.setStatus(r.getValue(Tables.EH_USERS.STATUS));
                user.setGender(r.getValue(Tables.EH_USERS.GENDER));
                list.add(user);
                return null;
            });
            return true;
        });

        return list;
    }
	
	/**
	 * added by Janson
	 */
	@Override
    public List<AclinkUser> searchDoorUsers(ListAclinkUserCommand cmd, CrossShardListingLocator locator, int pageSize) {
	    Integer namespaceId = cmd.getNamespaceId();
	    Long organizationId = cmd.getOrganizationId();
//	    Long buildingId = cmd.getBuildingId();
	    String buildingName =  cmd.getBuildingName();
	    Byte isAuth = cmd.getIsAuth();
	    String keyword = cmd.getKeyword();
        
        List<AclinkUser> list = new ArrayList<AclinkUser>();
        Map<Long, Long> isExists = new HashMap<Long, Long>();
        List<Long> ids = new ArrayList<Long>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, new MapReduceCallback<DSLContext, Object> () {

            @Override
            public boolean map(DSLContext context, Object obj) {
                SelectOnConditionStep<Record> onQuery = context.select().from(Tables.EH_USERS)
                        .leftOuterJoin(Tables.EH_USER_IDENTIFIERS).on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID)
                                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode())));
                
                SelectConditionStep<Record> select = null;
                boolean useAddress = false;
                boolean useMembers = false;
                
                Condition cond = Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId);
                if(!StringUtils.isEmpty(keyword)){
                     Condition cond1 = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(keyword);
                     cond1 = cond1.or(Tables.EH_USERS.NICK_NAME.like(keyword + "%"));
                     cond = cond.and(cond1);
                     }
                
                if(cmd.getIsOpenAuth() != null && cmd.getIsOpenAuth() > 0) {
                        onQuery = onQuery.join(Tables.EH_DOOR_AUTH).on(Tables.EH_DOOR_AUTH.USER_ID.eq(Tables.EH_USERS.ID));
                        cond = cond.and(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode())
                                .and(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode()))
                                .and(Tables.EH_DOOR_AUTH.DOOR_ID.eq(cmd.getDoorId()))
                                .and(Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte)1))
                                );
                    }
                
                if(cmd.getIsOpenAuth() != null && cmd.getIsOpenAuth() <= 0) {
                    SelectQuery<Record1<Long>> subQuery = context.select(Tables.EH_USERS.ID).from(Tables.EH_DOOR_AUTH).join(Tables.EH_USERS).on(Tables.EH_DOOR_AUTH.USER_ID.eq(Tables.EH_USERS.ID))
                    .where(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode())
                            .and(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode()))
                            .and(Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte)1))
                            ).getQuery();
                    
                    cond = cond.and(Tables.EH_USERS.ID.notIn(subQuery));
                }
                
                if(isAuth != null) {
                    useMembers = true;
                    if(isAuth > 0) {
                        cond = cond.and(EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
                    } else {
                        cond = cond.and(EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.ACTIVE.getCode()));
                        }
                    }
                
                if(organizationId != null) {
                    useMembers = true;
                    cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId));
                    }
                
//                if(buildingId != null) {
//                    useAddress = true;
//                    useMembers = true;
//                    cond = cond.and(Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_ID.eq(buildingId));
//                    }
                
                if(buildingName != null && !buildingName.isEmpty()) {
                    useAddress = true;
                    useMembers = true;
                    cond = cond.and(Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_NAME.like(buildingName + "%"));
                    }
                
                if(useMembers) {
                    onQuery = onQuery.join(Tables.EH_ORGANIZATION_MEMBERS).on(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.ID)
                            .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode())));
//                    onQuery = onQuery.leftOuterJoin(Tables.EH_ORGANIZATION_MEMBERS).on(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.ID)
//                            .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode())));
                }
                
                if(useAddress) {
                    onQuery = onQuery.leftOuterJoin(Tables.EH_ORGANIZATION_ADDRESSES)
                            .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID));
                }
                 
                if(locator.getAnchor() != null ) {
                    cond = cond.and(Tables.EH_USERS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
                    }
                
//                SelectOnConditionStep<Record> onQuery = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).join(Tables.EH_ORGANIZATION_ADDRESSES)
//                .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID))
//                .rightOuterJoin(Tables.EH_USERS).on(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.ID)
//                        .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode())))
//                        .leftOuterJoin(Tables.EH_USER_IDENTIFIERS).on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID)
//                                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode())));
                
                select = onQuery.where(cond);
                SelectOffsetStep<Record> query = select.orderBy(Tables.EH_USERS.CREATE_TIME.desc()).limit(pageSize * 2);
                final boolean useAddress2 = useAddress;
                final boolean useMembers2 = useMembers;
                query.fetch().map(r -> {
                    
//                    if(LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("Query users sql=" + query.getSQL());
//                        LOGGER.debug("Query users bindValues=" + query.getBindValues());
//                    }
                    
                    if(list.size() >= pageSize) {
                        return null;
                    }
                    
                    AclinkUser user = ConvertHelper.convert(r, AclinkUser.class);
                    user.setId(r.getValue(Tables.EH_USERS.ID));
                    
                    ids.add(user.getId());
                    
                    if(isExists.containsKey(user.getId())) {
                        return null;
                    }
                    isExists.put(user.getId(), 1l);
                    
                    user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
                    user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
                    user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
                    user.setStatus(r.getValue(Tables.EH_USERS.STATUS));
                    user.setGender(r.getValue(Tables.EH_USERS.GENDER));
                    if(useMembers2) {
                        user.setCompanyId(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID));    
                    }
                    
                    if(useAddress2) {
                        user.setAddressId(r.getValue(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID));
                        user.setBuildingId(r.getValue(Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_ID));
                        user.setBuildingName(r.getValue(Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_NAME));    
                    }
                    
                    list.add(user);
                    return null;
                });
                return true;
            }
            
        });
        
        if(list.size() >= pageSize || ids.size() >= pageSize) {
            locator.setAnchor(list.get(list.size()-1).getCreateTime().getTime());
        } else {
            locator.setAnchor(null);
        }
        
        return list;
    }

	@Override
	public User findUserByNamespace(Integer namespaceId, String namespaceUserToken) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return ConvertHelper.convert(context.select().from(Tables.EH_USERS)
				.where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId).and(Tables.EH_USERS.NAMESPACE_USER_TOKEN.eq(namespaceUserToken))).limit(1).fetchOne(),
				User.class);
	}
	
	@Override
    public void createUserCommunity(UserCommunity userCommunity) {
        assert(userCommunity.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userCommunity.getOwnerUid()));
        
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserCommunities.class));
        userCommunity.setId(id);
        userCommunity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        EhUserCommunitiesDao dao = new EhUserCommunitiesDao(context.configuration());
        dao.insert(userCommunity);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserCommunities.class, null);
    }
	
    @Cacheable(value = "UserCommunity-OwnerAndCommunity", key="{#ownerUid, #communityId}", unless="#result == null")
    @Override
    public UserCommunity findUserCommunityByOwnerAndCommunity(long ownerUid, long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, ownerUid));
        Record record = context.select().from(EH_USER_COMMUNITIES)
            .where(EH_USER_COMMUNITIES.OWNER_UID.eq(ownerUid))
            .and(EH_USER_COMMUNITIES.COMMUNITY_ID.eq(communityId))
            .fetchAny();
        return ConvertHelper.convert(record, UserCommunity.class);
    }
    
    @Override
    public List<User> findUserByNamespaceId(Integer namespaceId, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));
//      List<User> users = context.select().from(Tables.EH_USERS)
//              .where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
//              .and(Tables.EH_USERS.ID.gt(locator.getAnchor()))
//              .limit(pageSize).fetch().map(r -> {
//                  return ConvertHelper.convert(r,User.class);
//              });
        
        // 把排序方式改为按时间倒序排列 by lqs 20160118
        SelectQuery<EhUsersRecord> query = context.selectQuery(Tables.EH_USERS);
        query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
        if(locator.getAnchor() != null && locator.getAnchor().longValue() > 0) {
            query.addConditions(Tables.EH_USERS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
        }
        query.addOrderBy(Tables.EH_USERS.CREATE_TIME.desc());
        query.addLimit(pageSize);
        
        List<User> users = query.fetch().map((EhUsersRecord record) -> {
            return ConvertHelper.convert(record, User.class);
        });
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query user by namespace, sql=" + query.getSQL());
            LOGGER.debug("Query user by namespace, bindValues=" + query.getBindValues());
        }
        
        if(null == users)
            users = new ArrayList<User>();
        
        locator.setAnchor(null);
        
        if(users.size() >= pageSize) {
//          locator.setAnchor(users.get(users.size() - 1).getId());
            locator.setAnchor(users.get(users.size() - 1).getCreateTime().getTime());
        }
        
        return users;
    }
    
    @Override
    public List<User> listUserByKeywords(String keyword) {
		List<User> list = new ArrayList<User>();
		if(keyword == null)
			return null;
		
		String str = "%"+keyword+"%";
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            context.select().from(Tables.EH_USERS)
            .leftOuterJoin(Tables.EH_USER_IDENTIFIERS)
            .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID))
            .where(Tables.EH_USERS.NICK_NAME.like(str))
            .or(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(keyword))
            .fetch().map(r -> {
            	User user = ConvertHelper.convert(r,User.class);
            	user.setId(r.getValue(Tables.EH_USERS.ID));
            	user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
            	list.add(user);
            	return null;
            });
			return true;
        });
		return list;
	}

//    @Override
//    public int countUserByNamespaceIdAndNamespaceUserType(Integer namespaceId, String namespaceUserType){
//        final Integer[] count = new Integer[1];
//        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null,
//                (DSLContext context, Object reducingContext)-> {
//                    count[0] = context.selectCount().from(Tables.EH_USERS)
//                            .where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
//                            .and(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(namespaceUserType))
//                            .fetchOneInto(Integer.class);
//                    return true;
//        });
//
//        return count[0];
//    }

    @Override
    public int countUserByNamespaceIdAndGender(Integer namespaceId, Byte gender){
        final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null,
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_USERS)
                            .where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
                            .and(Tables.EH_USERS.GENDER.eq(gender))
                            .fetchOneInto(Integer.class);
                    return true;
                });

        return count[0];
    }


    @Override
	public int countUserByNamespaceId(Integer namespaceId, Boolean isAuth) {
		
		final Integer[] count = new Integer[1];
		if(isAuth == null) {
			this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, 
	                (DSLContext context, Object reducingContext)-> {
	                    count[0] = context.selectCount().from(Tables.EH_USERS)
	                    		.where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
	                            .fetchOneInto(Integer.class);
	                    return true;
	                });
		} else {
			if(isAuth == true) {
				List<Long> userIds = ecProvider.queryUserIds();
				
				this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, 
		                (DSLContext context, Object reducingContext)-> {
		                    count[0] = context.selectCount().from(Tables.EH_USERS)
		                    		.where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
		                    		.and(Tables.EH_USERS.ID.in(userIds))
		                            .fetchOneInto(Integer.class);
		                    return true;
		                });
			} else {
				count[0] = countUserByNamespaceId(namespaceId, null) - countUserByNamespaceId(namespaceId, true);
			}
		}
		
		return count[0];
	}
	
	@Override
	public List<User> listUserByIds(Integer namespaceId, List<Long> userIds){
		List<User> list = new ArrayList<User>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
			context.select().from(Tables.EH_USERS)
    		.where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
    		.and(Tables.EH_USERS.ID.in(userIds))
            .fetch().map(r -> {
            		User user = ConvertHelper.convert(r,User.class);
            		user.setId(r.getValue(Tables.EH_USERS.ID));
            		user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
            		list.add(user);
            	return null;
            });
			return true;
        });
		
		return list;
	}
	

	@Override
	public List<User> listUserByNickNameOrIdentifier(String keyword) {
		List<User> list = new ArrayList<User>();
		if(keyword == null)
			return null;
		
		String str = "%"+keyword+"%";
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            context.select().from(Tables.EH_USERS)
            .leftOuterJoin(Tables.EH_USER_IDENTIFIERS)
            .on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID))
            .where(Tables.EH_USERS.NICK_NAME.like(str))
            .or(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(keyword))
            .fetch().map(r -> {
            	User user = ConvertHelper.convert(r,User.class);
            	user.setId(r.getValue(Tables.EH_USERS.ID));
            	user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
            	user.setAccountName(r.getValue(Tables.EH_USERS.ACCOUNT_NAME));
            	user.setAddress(r.getValue(Tables.EH_USERS.ADDRESS));
            	user.setAddressId(r.getValue(Tables.EH_USERS.ADDRESS_ID));
            	user.setAvatar(r.getValue(Tables.EH_USERS.AVATAR));
            	user.setBirthday(r.getValue(Tables.EH_USERS.BIRTHDAY));
            	user.setCommunityId(r.getValue(Tables.EH_USERS.COMMUNITY_ID));
            	user.setCompany(r.getValue(Tables.EH_USERS.COMPANY));
            	user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
            	user.setDeleteTime(r.getValue(Tables.EH_USERS.DELETE_TIME));
            	user.setGender(r.getValue(Tables.EH_USERS.GENDER));
            	user.setHomeTown(r.getValue(Tables.EH_USERS.HOME_TOWN));
            	user.setHomeTownPath(r.getValue(Tables.EH_USERS.HOME_TOWN_PATH));
            	user.setInviteType(r.getValue(Tables.EH_USERS.INVITE_TYPE));
            	user.setInvitorUid(r.getValue(Tables.EH_USERS.INVITOR_UID));
            	user.setLastLoginIp(r.getValue(Tables.EH_USERS.LAST_LOGIN_IP));
            	user.setLastLoginTime(r.getValue(Tables.EH_USERS.LAST_LOGIN_TIME));
            	user.setLevel(r.getValue(Tables.EH_USERS.LEVEL));
            	user.setLocale(r.getValue(Tables.EH_USERS.LOCALE));
            	user.setNamespaceId(r.getValue(Tables.EH_USERS.NAMESPACE_ID));
            	user.setNamespaceUserToken(r.getValue(Tables.EH_USERS.NAMESPACE_USER_TOKEN));
            	user.setOccupation(r.getValue(Tables.EH_USERS.OCCUPATION));
            	user.setOriginalAvatar(r.getValue(Tables.EH_USERS.ORIGINAL_AVATAR));
            	user.setPoints(r.getValue(Tables.EH_USERS.POINTS));
            	user.setRegChannelId(r.getValue(Tables.EH_USERS.REG_CHANNEL_ID));
            	user.setRegCityId(r.getValue(Tables.EH_USERS.REG_CITY_ID));
            	user.setRegIp(r.getValue(Tables.EH_USERS.REG_IP));
            	user.setSchool(r.getValue(Tables.EH_USERS.SCHOOL));
            	user.setStatus(r.getValue(Tables.EH_USERS.STATUS));
            	user.setStatusLine(r.getValue(Tables.EH_USERS.STATUS_LINE));
            	user.setUuid(r.getValue(Tables.EH_USERS.UUID));
            	list.add(user);
            	return null;
            });
			return true;
        });
		return list;
	}

	@Override
	public List<UserIdentifier> listUserIdentifierByIdentifier(String identifier) {
		List<UserIdentifier> identifiers = new ArrayList<UserIdentifier>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
			 context.select().from(Tables.EH_USER_IDENTIFIERS)
	            .where(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifier))
	            .fetch().map(r -> {
	            	identifiers.add(ConvertHelper.convert(r, UserIdentifier.class));
	            	return null;
	            });
			return true;
		});
		return identifiers;
	}

    @Override
    public List<User> findThirdparkUserByTokenAndType(Integer namespaceId, String userType, String userToken) {
        List<User> list = new ArrayList<User>();
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj)->{
            SelectQuery<EhUsersRecord> query = context.selectQuery(Tables.EH_USERS);
            query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
            // namespace_user_type字段是后来才加的，数据库中不一定有该字段的值，
            // 实际上是两种形式：1) namespace_id+namespace_user_token
            // 2) namespace_id+namespace_user_type+namespace_user_token(此种情况namespace_id可能为0）
            if(userType != null && userType.trim().length() > 0) {
                query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(userType));
            }
            query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TOKEN.eq(userToken));
            
            query.fetch().map(r -> {
                User user = ConvertHelper.convert(r,User.class);
                list.add(user);
                return null;
            });
            
            return true;
        });
        
        return list;
    }

    /**
     * 金地取数据使用
     */
	@Override
	public List<User> listUserByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, Integer pageSize) {
		//暂不考虑分库分表的问题，因为是按更新时间来取，比较麻烦
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));
		Result<Record> result = context.select().from(Tables.EH_USERS)
			.where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_USERS.ID.gt(pageAnchor))
			.and(Tables.EH_USERS.UPDATE_TIME.eq(new Timestamp(timestamp)))
			.orderBy(Tables.EH_USERS.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, User.class));
		}
		return new ArrayList<User>();
	}

    /**
     * 金地取数据使用
     */
	@Override
	public List<User> listUserByUpdateTime(Integer namespaceId, Long timestamp, Integer pageSize) {
		//暂不考虑分库分表的问题，因为是按更新时间来取，比较麻烦
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));
		Result<Record> result = context.select().from(Tables.EH_USERS)
			.where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_USERS.UPDATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_USERS.UPDATE_TIME.asc(), Tables.EH_USERS.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, User.class));
		}
		return new ArrayList<User>();
	}

    /**
     * 金地取数据使用
     * 随便找一个用户所在的组织
     */
	@Override
	public Organization findAnyUserRelatedOrganization(Long userId, Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select(Tables.EH_ORGANIZATIONS.fields()).from(Tables.EH_ORGANIZATIONS)
			.join(Tables.EH_ORGANIZATION_MEMBERS)
			.on(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID))
			.where(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
			.and(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
			.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId))
			.and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()))
			.and(Tables.EH_ORGANIZATIONS.LEVEL.eq(1))
			.fetchAny();
		
		if (record != null) {
			return RecordHelper.convert(record, Organization.class);
		}
		
		return null;
	}

    @Override
    public List<User> listUserByNickName(String keyword) {
        long startTime = System.currentTimeMillis();
        List<User> list = new ArrayList<User>();
        if(keyword == null)
            return null;

        String str = "%"+keyword+"%";
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context,obj)->{
            context.select().from(Tables.EH_USERS)
                    .where(Tables.EH_USERS.NICK_NAME.like(str))
                    .fetch().map(r -> {
                User user = ConvertHelper.convert(r,User.class);
                user.setId(r.getValue(Tables.EH_USERS.ID));
                user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
                list.add(user);
                return null;
            });
            return true;
        });
        long endTime = System.currentTimeMillis();
        LOGGER.debug("listUserByNickName size = " + list.size() + ", elapse=" + (endTime - startTime));
        return list;
    }

    @Override
    public List<UserGroup> listUserActiveGroups(long uid, String groupDiscriminator) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));

        if(groupDiscriminator != null) {
            return context.select().from(Tables.EH_USER_GROUPS)
                    .where(Tables.EH_USER_GROUPS.OWNER_UID.eq(uid))
                    .and(Tables.EH_USER_GROUPS.GROUP_DISCRIMINATOR.eq(groupDiscriminator))
                    .and(Tables.EH_USER_GROUPS.MEMBER_STATUS.eq((GroupMemberStatus.ACTIVE.getCode())))
                    .fetch().map((r)-> {
                        return ConvertHelper.convert(r, UserGroup.class);
                    });
        } else {
            return context.select().from(Tables.EH_USER_GROUPS)
                    .where(Tables.EH_USER_GROUPS.OWNER_UID.eq(uid))
                    .and(Tables.EH_USER_GROUPS.MEMBER_STATUS.eq((GroupMemberStatus.ACTIVE.getCode())))
                    .fetch().map((r)-> {
                        return ConvertHelper.convert(r, UserGroup.class);
                    });
        }

    }

    @Override
    public UserNotificationSetting findUserNotificationSetting(String ownerType, Long ownerId, String targetType, Long targetId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_USER_NOTIFICATION_SETTINGS)
                .where(Tables.EH_USER_NOTIFICATION_SETTINGS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_USER_NOTIFICATION_SETTINGS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_USER_NOTIFICATION_SETTINGS.TARGET_TYPE.eq(targetType))
                .and(Tables.EH_USER_NOTIFICATION_SETTINGS.TARGET_ID.eq(targetId))
                .fetchAnyInto(UserNotificationSetting.class);
    }

    @Override
    public void updateUserNotificationSetting(UserNotificationSetting setting) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserNotificationSettingsDao dao = new EhUserNotificationSettingsDao(context.configuration());
        dao.update(setting);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserNotificationSettings.class, setting.getId());
    }

    @Override
    public long createUserNotificationSetting(UserNotificationSetting setting) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserNotificationSettings.class));
        setting.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserNotificationSettingsDao dao = new EhUserNotificationSettingsDao(context.configuration());
        dao.insert(setting);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserNotificationSettings.class, id);
        return id;
    }

    @Override
    public List<TargetDTO> findUesrIdByNameAndAddressId(String targetName, List<Long> ids, String tel) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<TargetDTO> list = new ArrayList<>();
        com.everhomes.server.schema.tables.EhUsers r = Tables.EH_USERS.as("r");
        com.everhomes.server.schema.tables.EhUserIdentifiers o = Tables.EH_USER_IDENTIFIERS.as("o");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(r.ID);
        query.addSelect(r.NICK_NAME);
        query.addSelect(o.IDENTIFIER_TOKEN);
        query.addFrom(r);
        query.addFrom(o);
        query.addConditions(r.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        if(tel!=null){
            query.addConditions(o.IDENTIFIER_TOKEN.eq(tel));
        }
        if(targetName!=null){
            query.addConditions(r.NICK_NAME.eq(targetName));
        }
        query.addConditions(r.ID.eq(o.OWNER_UID));
        if(ids.size() == 1){
            query.addConditions(r.ADDRESS_ID.eq(ids.get(0)));
        }
        if(ids.size() > 1){
            query.addConditions(r.ADDRESS_ID.in(ids));
        }
        query.fetch()
                .map(f -> {
                    TargetDTO dto = new TargetDTO();
                    dto.setTargetId(f.getValue(r.ID));
                    dto.setTargetName(f.getValue(r.NICK_NAME));
                    dto.setTargetType("eh_user");
                    dto.setUserIdentifier(f.getValue(o.IDENTIFIER_TOKEN));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public TargetDTO findUserByToken(String tel,Integer namespaceId) {
        TargetDTO dto = new TargetDTO();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhUserIdentifiers t = Tables.EH_USER_IDENTIFIERS.as("t");
        com.everhomes.server.schema.tables.EhUsers t1 = Tables.EH_USERS.as("t1");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.OWNER_UID);
        query.addFrom(t);
        query.addConditions(t.IDENTIFIER_TOKEN.eq(tel));
        query.addConditions(t.CLAIM_STATUS.eq((byte)3));
        query.addConditions(t.IDENTIFIER_TYPE.eq((byte)0));
        if(namespaceId!=null) query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        Long userId = query.fetchOne(0,Long.class);
        if(userId==null){
            return null;
        }
        SelectQuery<Record> queryUser = context.selectQuery();
        queryUser.addSelect(t1.NICK_NAME);
        queryUser.addFrom(t1);
        queryUser.addConditions(t1.ID.eq(userId));
        String nameFound = queryUser.fetchOne(0, String.class);
        if(nameFound != null){
            dto.setUserIdentifier(tel);
            dto.setTargetId(userId);
            dto.setTargetType(AssetPaymentStrings.EH_USER);
            dto.setTargetName(nameFound);
            return dto;
        }
        return null;
    }
    
    /**
     * 用于测试缓存使用是否正常，不要用于业务使用 by lqs 20171019
     */
    @Cacheable(value = "checkCacheStatus", key="'cache.heartbeat'", unless="#result == null")
    @Override
    public String checkCacheStatus() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    /**
     * 用于测试缓存使用是否正常，不要用于业务使用 by lqs 20171019
     */
    @Caching(evict={@CacheEvict(value="checkCacheStatus", key="'cache.heartbeat'")})
    @Override
    public void updateCacheStatus() {
        // 只需要去掉缓存，使可缓存可测
    }

    @Override
    public String findMobileByUid(Long contactId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        String s = context.select(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN).from(Tables.EH_USER_IDENTIFIERS)
                .where(Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(contactId))
                .and(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq((byte) 0))
                .fetchOne(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN);
        return s;
    }
    
    @Override
    public void deleteUserAndUserIdentifiers(Integer namespaceId, List<String> namespaceUserTokens, String namespaceUserType) {
        List<Long> userIds = this.listUsersByNamespaceUserInfo(namespaceId, namespaceUserTokens, namespaceUserType);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        // delete users
        DeleteQuery<EhUsersRecord> query_user = context.deleteQuery(Tables.EH_USERS);
        query_user.addConditions(Tables.EH_USERS.ID.in(userIds));
        query_user.execute();

        // delete userIdentifiers
        DeleteQuery<EhUserIdentifiersRecord> query_userIdentifiers = context.deleteQuery(Tables.EH_USER_IDENTIFIERS);
        query_userIdentifiers.addConditions(Tables.EH_USER_IDENTIFIERS.OWNER_UID.in(userIds));
        query_userIdentifiers.execute();
    }

    @Override
    public List<Long> listUsersByNamespaceUserInfo(Integer namespaceId, List<String> namespaceUserTokens, String namespaceUserType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhUsersRecord> query = context.selectQuery(Tables.EH_USERS);
        if(namespaceUserTokens  != null && namespaceUserTokens.size() > 0)
            query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TOKEN.in(namespaceUserTokens));
        if(!StringUtils.isEmpty(namespaceUserType))
            query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(namespaceUserType));

        List<Long> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(r.getId());
            return null;
        });
        return result;
    }
    
    @Override
    public void updateIdentifierByUid(UserIdentifier userIdentifier) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));
        UpdateQuery<EhUserIdentifiersRecord> query = context.updateQuery(Tables.EH_USER_IDENTIFIERS);
        query.addValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN, userIdentifier.getIdentifierToken());
        query.addValue(Tables.EH_USER_IDENTIFIERS.NAMESPACE_ID, userIdentifier.getNamespaceId());
        query.addValue(Tables.EH_USER_IDENTIFIERS.VERIFICATION_CODE, userIdentifier.getVerificationCode());
        query.addValue(Tables.EH_USER_IDENTIFIERS.REGION_CODE, userIdentifier.getRegionCode());
        query.addValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TYPE, userIdentifier.getIdentifierType());
        query.addValue(Tables.EH_USER_IDENTIFIERS.NOTIFY_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
        query.addConditions(Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(userIdentifier.getOwnerUid()));
        query.execute();
    }
    
    @Override
    public void deleteIdentifier(Integer namespaceId, List<Long> uIds) {

    }
    
    @Override
    public TargetDTO findUserByTokenAndName(String tel, String targetName) {
        TargetDTO dto = new TargetDTO();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhUserIdentifiers t = Tables.EH_USER_IDENTIFIERS.as("t");
        com.everhomes.server.schema.tables.EhUsers t1 = Tables.EH_USERS.as("t1");
        Long userId;
        if (tel != null) {
            userId = context.select(t.OWNER_UID)
                    .from(t)
                    .where(t.IDENTIFIER_TOKEN.eq(tel))
                    .fetchOne(0, Long.class);
            if (userId == null) {
                return null;
            }
            String nameFound = context.select(t1.NICK_NAME)
                    .from(t1)
                    .where(t1.ID.eq(userId))
                    .fetchOne(0, String.class);
            if (targetName != null) {
                if (!nameFound.equals(targetName)) {
                    return null;
                }
            } else {
                dto.setUserIdentifier(tel);
                dto.setTargetId(userId);
                dto.setTargetType("eh_user");
                dto.setTargetName(nameFound);
            }
        } else if (targetName != null) {
            List<TargetDTO> dtos = new ArrayList<>();
            context.select(t.IDENTIFIER_TOKEN, t1.ID, t1.NICK_NAME)
                    .from(t1, t)
                    .where(t1.ID.eq(t.OWNER_UID))
                    .and(t1.NICK_NAME.eq(targetName))
                    .fetch()
                    .map(r -> {
                        TargetDTO d = new TargetDTO();
                        d.setTargetName(r.getValue(t1.NICK_NAME));
                        d.setTargetType("eh_user");
                        d.setTargetId(r.getValue(t1.ID));
                        d.setUserIdentifier(r.getValue(t.IDENTIFIER_TOKEN));
                        dtos.add(d);
                        return null;
                    });
            if (dtos.size() != 1) {
                return null;
            } else {
                return dtos.get(0);
            }
        }
        return null;
    }


    @Override
    public User findUserByNamespaceUserTokenAndType(String token, String type) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));
        User user = context.select().from(EH_USERS).where(EH_USERS.NAMESPACE_USER_TOKEN.eq(token))
                .and(EH_USERS.NAMESPACE_USER_TYPE.eq(type))
                .fetchAnyInto(User.class);
        return user;
    }


    /**
     * 根据UserId来查询用户信息
     * @param userId
     * @return
     */
    @Override
    public UserDTO findUserInfoByUserId(Long userId){
        UserDTO user = new UserDTO();
        //获取上下文
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        //表eh_users和表eh_user_identifiers进行联查
        SelectQuery<Record> query = context.select().from(Tables.EH_USERS).getQuery();
        query.addJoin(Tables.EH_USER_IDENTIFIERS,JoinType.JOIN,Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(Tables.EH_USERS.ID));
        //添加查询条件
        query.addConditions(Tables.EH_USERS.ID.eq(userId));
        query.addConditions(Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(userId));
        query.fetch().map( r ->{
            user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
            user.setAccountName(r.getValue(Tables.EH_USERS.ACCOUNT_NAME));
            return null;
        });
        return user;
    }

    @Override
    public List<UserDTO> listUserInfoByIdentifierToken(Integer namespaceId, List<String> identifierTokens) {
        List<UserDTO> userDTOList = new ArrayList<>();
        //获取上下文
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        //表eh_users和表eh_user_identifiers进行联查
        SelectQuery<Record> query = context.select().from(Tables.EH_USERS).getQuery();
        query.addJoin(Tables.EH_USER_IDENTIFIERS,JoinType.JOIN,Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(Tables.EH_USERS.ID));
        //添加查询条件
        query.addConditions(Tables.EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.in(identifierTokens));
        query.fetch().map( r ->{
            UserDTO user = new UserDTO();
            user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
            user.setAccountName(r.getValue(Tables.EH_USERS.ACCOUNT_NAME));
            user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
            user.setId(r.getValue(Tables.EH_USERS.ID));
            userDTOList.add(user);
            return null;
        });
        return userDTOList;
    }

    /**
     * 查询该手机号是否已经进行注册
     * @param contactToken
     * @return
     */
    @Override
    public UserIdentifier getUserByToken(String contactToken,Integer namespaceId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select()
                .from(Tables.EH_USER_IDENTIFIERS)
                .where(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(contactToken))
                .and(Tables.EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                .fetchAnyInto(UserIdentifier.class);
    }

    /**
     * 根据ownerUid和namespaceId来查询手机号
     * @param ownerUid
     * @param namespaceId
     * @return
     */
    @Override
    public String findContactTokenByOwnerUidAndNamespaceId(Long ownerUid , Integer namespaceId){
        //获取上下文
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        //2.查询Eh_user_identifiers表
        String contactToken = context.select(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN).from(Tables.EH_USER_IDENTIFIERS)
                .where(Tables.EH_USER_IDENTIFIERS.OWNER_UID.eq(ownerUid))
                .and(Tables.EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId)).fetchOneInto(String.class);
        return contactToken;
    }

    @Override
    public String findUserTokenOfUser(Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        try{
            return context.select(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN).from(EH_USER_IDENTIFIERS)
                    .where(EH_USER_IDENTIFIERS.OWNER_UID.eq(userId))
                    .fetchOne(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN);
        }catch (Exception e){
            LOGGER.error("there is more than one token for a fixed uid, uid = {}",userId, e);
            return null;
        }
    }

	@Override
	/*
	 * 根据用户id获得昵称和手机号，手机号有可能为空
	 * */
	public TargetDTO findUserTargetById(Long userId) {

		if (null == userId || userId < 0) {
			return null;
		}

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhUserIdentifiers token = Tables.EH_USER_IDENTIFIERS.as("token");
		com.everhomes.server.schema.tables.EhUsers user = Tables.EH_USERS.as("user");

		Record3<String, Long, String> ret =
				context.select(token.IDENTIFIER_TOKEN, user.ID, user.NICK_NAME).from(user)
				.leftOuterJoin(token).on(token.OWNER_UID.eq(user.ID).and(token.NAMESPACE_ID.eq(user.NAMESPACE_ID)))
				.where(user.ID.eq(userId))
				.fetchOne();

		if (null == ret) {
			return null;
		}

		TargetDTO targetDTO = new TargetDTO();
		targetDTO.setTargetName(ret.getValue(user.NICK_NAME));
		targetDTO.setTargetType("eh_user");
		targetDTO.setTargetId(ret.getValue(user.ID));
		targetDTO.setUserIdentifier(ret.getValue(token.IDENTIFIER_TOKEN));

		return targetDTO;
	}

}
