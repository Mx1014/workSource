// @formatter:off
package com.everhomes.user;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhUserIdentifiersDao;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhUsersRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import static com.everhomes.server.schema.Tables.*;

/**
 * 
 * Provide data services (cache-enabled) for user management service
 * 
 * <b>Multiple login per user support</b>
 * <p>
 *  It is based on deviceIdentifier that is given, login will be tied to deviceIdentifier(uniqely identifies the physical device),
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
 * <pre>
 * Cache schema
 * 
 *      cache User
 *          uid -> User
 *          acct:{account name} -> User
 *      cache UserIdentifiers
 *          uid -> list of identifiers
 *          identifierToken -> UserIdentifiers>
 *          'id'-#id -> identifier 
 *          
 * </pre>
 * 
 * @author Kelven Yang
 *
 */
@Component
public class UserServiceProviderImpl implements UserServiceProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private CacheProvider cacheProvider;
    
    public UserServiceProviderImpl() {
    }
    
    @Override
    public void createUser(User user) {
        long id = this.shardingProvider.allocShardableContentId(EhUsers.class).second();
        user.setId(id);
        if(user.getAccountName() == null) {
            long accountSeq = this.sequenceProvider.getNextSequence("usr");
            accountSeq += 1000000;
            user.setAccountName("U" + String.valueOf(accountSeq));
        }
        user.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.insert(user);
    }
    
    @Override
    @CacheEvict(value = "User", key="#user.id")
    public void updateUser(User user) {
        assert(user.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, user.getId().longValue()));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.update(user);
    }

    @Override
    @CacheEvict(value = "User", key="#id")
    public void deleteUser(long id) {
        // TODO delete all user references, due to database sharding, those foreign references will be fully taken
        // care of by database
        
        // delete the record itself
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.deleteById(id);
    }
    
    @Override
    @Cacheable(value = "User", key="#id")
    public User findUserById(final long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        EhUsers user = dao.findById(id);
        return ConvertHelper.convert(user, User.class);
    }
    
    @Override
    @Cacheable(value = "User", key="#accountName")
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
    @Cacheable(value = "UserIdentifier", key="#userId")
    public List<UserIdentifier> listUserIdentifiersOfUser(long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        List<UserIdentifier> identifiers = context.select().from(EH_USER_IDENTIFIERS).where(EH_USER_IDENTIFIERS.OWNER_UID.eq(userId))
            .fetch().map((Record record) -> {
                   return ConvertHelper.convert(record, UserIdentifier.class);
            });
        return identifiers;
    }
    
    @Override
    @Cacheable(value = "UserIdentifier", key="#userIdentifier.ownerUid")
    public void createIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getOwnerUid() != null);
        
        // identifier record will be saved in the same shard as its owner users
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserIdentifiers.class));
        
        userIdentifier.setId(id);
        Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
        userIdentifier.setCreateTime(ts);
        
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.insert(userIdentifier);
    }
    
    @Override
    @Cacheable(value = "UserIdentifier", key="#userIdentifier.ownerUid")
    public void updateIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getId() != null);
        assert(userIdentifier.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.update(userIdentifier);
    }

    @Override
    public void deleteIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getId() != null);
        assert(userIdentifier.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.delete(userIdentifier);
        
        CacheAccessor accessor = this.cacheProvider.getCacheAccessor("UserIdentifier");
        
        // clear id cache
        Method method = ReflectionUtils.findMethod(UserServiceProviderImpl.class, "findIdentifierById");
        accessor.evict(this, method, "id" + userIdentifier.getId());
        
        // clear token cache
        method = ReflectionUtils.findMethod(UserServiceProviderImpl.class, "findClaimedIdentifierByToken");
        accessor.evict(this, method, userIdentifier.getIdentifierToken());
     
        // clear owner user's identifier list cache
        method = ReflectionUtils.findMethod(UserServiceProviderImpl.class, "listUserIdentifiersOfUser");
        accessor.evict(this, method, userIdentifier.getOwnerUid());
    }
    
    @Override
    public void deleteIdentifier(long id) {
        UserIdentifier userIdentifier = findIdentifierById(id);
        if(userIdentifier != null) {
            deleteIdentifier(userIdentifier);
        }
    }
    
    @Override
    @Cacheable(value = "UserIdentifier", key="'id-' + #id")
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
    
    @Override
    @Cacheable(value = "UserIdentifier", key="#identifierToken")
    public UserIdentifier findClaimedIdentifierByToken(final String identifierToken) {
        final UserIdentifier[] result = new UserIdentifier[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            Record record = context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq((byte)IdentifierClaimStatus.claimed.ordinal()))
                .fetchOne();
            
            if(record != null) {
                result[0] = ConvertHelper.convert(record, UserIdentifier.class);
                return false;
            }
            return true;
        });
        return result[0];
    }
}
