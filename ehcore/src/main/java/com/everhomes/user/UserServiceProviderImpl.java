package com.everhomes.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.constants.ErrorCodes;
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
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.UserLogin.Status;
import com.everhomes.util.ActionCallback;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.MapReduceCallback;
import com.everhomes.util.PasswordHash;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import static com.everhomes.server.schema.Tables.*;

/**
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
 *      cache User-identifiers
 *          uid -> list of identifiers
 *      cache UserIdentifiers
 *          identifierToken -> UserIdentifiers>
 * </pre>
 * 
 * @author Kelven Yang
 *
 */
@Component
public class UserServiceProviderImpl implements UserServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private CacheProvider cacheProvider;
    
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    private SmsProvider smsProvider;
    
    private Gson gson;
    
    public UserServiceProviderImpl() {
        gson = new GsonBuilder().create();   
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
    public void updateUser(User user) {
        assert(user.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, user.getId().longValue()));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.update(user);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName());
        String cacheKey = String.valueOf(user.getId());
        cacheAccessor.evict(cacheKey);
    }

    @Override
    public void deleteUser(long id) {
        // TODO delete all user references, due to database sharding, those foreign references will be fully taken
        // care of by database
        
        // delete the record itself
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.deleteById(id);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName());
        String cacheKey = String.valueOf(id);
        cacheAccessor.evict(cacheKey);
    }
    
    @Override
    public User findUserById(final long id) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName());
        String cacheKey = String.valueOf(id);
        User user = cacheAccessor.get(cacheKey, User.class, new ActionCallback<User>() {
            public User doAction() {
                DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
                EhUsersDao dao = new EhUsersDao(context.configuration());
                EhUsers user = dao.findById(id);
                return ConvertHelper.convert(user, User.class);
            }
        });
        return user;
    }
    
    @Override
    public User findUserByAccountName(final String accountName) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName());
        String cacheKey = NameMapper.getCacheKey("acct", accountName, null);
        
        User user = cacheAccessor.get(cacheKey, User.class, new ActionCallback<User>() {
            public User doAction() {
                final User result[] = new User[1];
                dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, new MapReduceCallback<DSLContext, Object>() {
                    @Override
                    public boolean map(DSLContext context, Object reducingContext) {
                        EhUsersRecord record = (EhUsersRecord)context.select()
                            .from(EH_USERS)
                            .where(EH_USERS.ACCOUNT_NAME.equal(accountName))
                            .fetchOne();
                        
                        if(record != null) {
                            result[0] = ConvertHelper.convert(record, User.class);
                            return false;
                        }
                        
                        return true;
                    }
                });
                
                return result[0];
            }
        });
        
        return user;
    }
    
    @Override
    public List<UserIdentifier> listUserIdentifiersOfUser(long userId) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-identifiers");
        String cacheKey = String.valueOf(userId);
        
        String jsonIdentifierList = cacheAccessor.get(cacheKey, String.class);
        if(jsonIdentifierList == null) {
            return gson.fromJson(jsonIdentifierList, new TypeToken<ArrayList<UserIdentifier>>() {}.getType());
        }
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        List<UserIdentifier> identifiers = context.select().from(EH_USER_IDENTIFIERS).where(EH_USER_IDENTIFIERS.OWNER_UID.eq(userId))
            .fetch().map(new RecordMapper<Record, UserIdentifier>() {
               public UserIdentifier map(Record record) {
                   return ConvertHelper.convert(record, UserIdentifier.class);
               }
            });
        cacheAccessor.put(cacheKey, gson.toJson(identifiers));
        return identifiers;
    }
    
    @Override
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
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-identifiers");
        String cacheKey = String.valueOf(userIdentifier.getOwnerUid());
        cacheAccessor.evict(cacheKey);
    }
    
    @Override
    public void updateIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getId() != null);
        assert(userIdentifier.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.update(userIdentifier);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(UserIdentifier.class.getSimpleName());
        cacheAccessor.evict(userIdentifier.getIdentifierToken());
        
        cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-identifiers");
        String cacheKey = String.valueOf(userIdentifier.getOwnerUid());
        cacheAccessor.evict(cacheKey);
    }

    @Override
    public void deleteIdentifier(UserIdentifier userIdentifier) {
        assert(userIdentifier.getId() != null);
        assert(userIdentifier.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userIdentifier.getOwnerUid().longValue()));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
        dao.delete(userIdentifier);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(UserIdentifier.class.getSimpleName());
        cacheAccessor.evict(userIdentifier.getIdentifierToken());
        
        cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-identifiers");
        String cacheKey = String.valueOf(userIdentifier.getOwnerUid());
        cacheAccessor.evict(cacheKey);
    }
    
    @Override
    public void deleteIdentifier(long id) {
        UserIdentifier userIdentifier = findIdentifierById(id);
        if(userIdentifier != null)
            deleteIdentifier(userIdentifier);
    }
    
    @Override
    public UserIdentifier findIdentifierById(final long id) {
        final UserIdentifier[] result = new UserIdentifier[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, new MapReduceCallback<DSLContext, Object>() {
            @Override
            public boolean map(DSLContext context, Object reducingContext) {
                EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
                EhUserIdentifiers userIdentifier = dao.findById(id);
                if(userIdentifier != null) {
                    result[0] = ConvertHelper.convert(userIdentifier, UserIdentifier.class);
                    return false;
                }
                return true;
            }
        });

        return result[0];
    }
    
    @Override
    public UserIdentifier findClaimedIdentifierByToken(final String identifierToken) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(UserIdentifier.class.getSimpleName());
        UserIdentifier userIdentifier = cacheAccessor.get(identifierToken, UserIdentifier.class, new ActionCallback<UserIdentifier>() {
            public UserIdentifier doAction() {
                final UserIdentifier[] result = new UserIdentifier[1];
                
                dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, new MapReduceCallback<DSLContext, Object>() {
                    @Override
                    public boolean map(DSLContext context, Object reducingContext) {
                        Record record = context.select().from(EH_USER_IDENTIFIERS)
                            .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(identifierToken))
                            .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq((byte)IdentifierClaimStatus.claimed.ordinal()))
                            .fetchOne();
                        
                        if(record != null) {
                            result[0] = ConvertHelper.convert(record, UserIdentifier.class);
                            return false;
                        }
                        return true;
                    }
                });
                return result[0];
            }
        });
        
        return userIdentifier;
    }

    @Override
    public SignupToken signup(final IdentifierType identifierType, final String identifierToken) {
        UserIdentifier identifier = this.dbProvider.execute(new TransactionCallback<UserIdentifier>() {

            @Override
            public UserIdentifier doInTransaction(TransactionStatus arg0) {
                User user = new User();
                user.setStatus((byte)UserStatus.active.ordinal());
                createUser(user);
                
                UserIdentifier identifier = new UserIdentifier();
                identifier.setOwnerUid(user.getId());
                identifier.setIdentifierType((byte)identifierType.ordinal());
                identifier.setIdentifierToken(identifierToken);

                String verificationCode = RandomGenerator.getRandomDigitalString(6);
                identifier.setClaimStatus((byte)IdentifierClaimStatus.verifying.ordinal());
                identifier.setVerificationCode(verificationCode);
                identifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                createIdentifier(identifier);
                
                // TODO
                LOGGER.debug("Send verfication code: " + verificationCode + " for new user: " + identifierToken);
                return identifier;
            }
        });
        
        SignupToken signupToken = new SignupToken(identifier.getOwnerUid(), identifierType, identifierToken);
        return signupToken;
    }
    
    @Override
    public UserIdentifier findIdentifierByToken(SignupToken signupToken) {
        assert(signupToken != null);
    
        // TODO: add cache support later
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(User.class, signupToken.getUserId()));
        Record record = context.select().from(EH_USER_IDENTIFIERS)
            .where(EH_USER_IDENTIFIERS.OWNER_UID.eq(signupToken.getUserId()))
            .and(EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq((byte)signupToken.getIdentifierType().ordinal()))
            .and(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(signupToken.getIdentifierToken()))
            .fetchOne();
        
        return ConvertHelper.convert(record, UserIdentifier.class);
    }
    
    @Override
    public void resendVerficationCode(SignupToken signupToken) {
        UserIdentifier identifier = this.findIdentifierByToken(signupToken);
        if(identifier == null)
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
        
        if(identifier.getClaimStatus() == IdentifierClaimStatus.claiming.ordinal() ||
            identifier.getClaimStatus() == IdentifierClaimStatus.verifying.ordinal()) {
            Timestamp ts = identifier.getNotifyTime();
            if(ts == null || isVerificationExpired(ts)) {
                String verificationCode = RandomGenerator.getRandomDigitalString(6);
                identifier.setVerificationCode(verificationCode);
                
                // TODO
                LOGGER.debug("Send notification code " + verificationCode + " to " + identifier.getIdentifierToken());
            } else {

                // TODO
                LOGGER.debug("Send notification code " + identifier.getVerificationCode() + " to " + identifier.getIdentifierToken());
            }
            
            identifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.updateIdentifier(identifier);
        } else {
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALD_TOKEN_STATUS, "Invalid token status");
        }
    }
    
    @Override
    public UserLogin verifyAndLogon(SignupToken signupToken, String verificationCode, String deviceIdentifier) {
        UserIdentifier identifier = this.findIdentifierByToken(signupToken);
        if(identifier == null)
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
    
        // make it idempotent in case client disconnects before it has received the successful return
        if((identifier.getClaimStatus() == IdentifierClaimStatus.verifying.ordinal() ||
               identifier.getClaimStatus() == IdentifierClaimStatus.claimed.ordinal())
            && identifier.getVerificationCode() != null 
            && identifier.getVerificationCode().equals(verificationCode)) {
            
            if(identifier.getClaimStatus() == IdentifierClaimStatus.verifying.ordinal()) {
                identifier.setClaimStatus((byte)IdentifierClaimStatus.claimed.ordinal());
                this.updateIdentifier(identifier);
            }

            User user = this.findUserById(identifier.getOwnerUid());
            UserLogin login = createLogin(user, deviceIdentifier);
            login.setStatus(Status.loggedIn);
            return login;
        }
        
        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
    }
    
    @Override
    public UserLogin logon(String userIdentifierToken, String password, String deviceIdentifier) {
        User user = null;
        
        UserIdentifier userIdentifier = findClaimedIdentifierByToken(userIdentifierToken);
        if(userIdentifier == null) {
            LOGGER.warn("Unable to find identifier record of " + userIdentifierToken);
            user = this.findUserByAccountName(userIdentifierToken);
            if(user == null) {
                LOGGER.error("Unable to locate user with account name: " + userIdentifierToken);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNABLE_TO_LOCATE_USER, "Unable to locate user");
            }
        } else {
            user = this.findUserById(userIdentifier.getOwnerUid());
            if(user == null) {
                LOGGER.error("Unable to find owner user of identifier record: " + userIdentifierToken);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User does not exist");
            }
        }
        
        try {
            if(!PasswordHash.validatePassword(password, user.getPasswordHash())) {
                LOGGER.error("Password does not match for " + userIdentifierToken);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Invalid password");
            }
        } catch (NoSuchAlgorithmException e) {
            assert(false);
            throw RuntimeErrorException.errorWith(e, ErrorCodes.SCOPE_GENERAL, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                  "Supported hashing algorithm");
        } catch (InvalidKeySpecException e) {
            assert(false);
            throw RuntimeErrorException.errorWith(e, ErrorCodes.SCOPE_GENERAL, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Invalid key spec");
        }
        
        UserLogin login = createLogin(user, deviceIdentifier);
        login.setStatus(Status.loggedIn);
        return login;
    }
    
    @Override
    public UserLogin logonByToken(LoginToken loginToken) {
        assert(loginToken != null);
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
        UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()), UserLogin.class);
        if(login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber()) {
            login.setStatus(Status.loggedIn);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            accessor.putMapValueObject(String.valueOf(loginToken.getLoginId()), login, UserLogin.class);
            return login;
        }
        
        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_LOGIN_TOKEN, 
                "Invalid token or token has expired");
    }
    
    private UserLogin createLogin(User user, String deviceIdentifier) {
        String userKey = NameMapper.getCacheKey("user", user.getId(), null);
        
        // get "index" accessor
        String hkeyIndex = "0";
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
        Integer maxLoginId = accessor.getMapValueObject(hkeyIndex, Integer.class);
        UserLogin foundLogin = null;
        int nextLoginId = 1;
        
        if(maxLoginId != null) {
            for(int i = 1; i < maxLoginId.intValue(); i++) {
                String hkeyLogin = String.valueOf(i);
                Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
                UserLogin login = accessorLogin.getMapValueObject(hkeyLogin, UserLogin.class);
                if(login != null) {
                    if(login.getDeviceIdentifier() == null && deviceIdentifier == null) {
                        // toggle status so that we can have a new loginIntanceNumber, this
                        // will cause the previous one to be kicked out
                        login.setStatus(Status.loggedOff);
                        foundLogin = login;
                        break;
                    } else if(login.getDeviceIdentifier() != null && deviceIdentifier != null) {
                        if(login.getDeviceIdentifier().equals(deviceIdentifier)) {
                            foundLogin = login;
                            break;
                        }
                    }
                    
                    if(login.getLoginId() >= nextLoginId)
                        nextLoginId = login.getLoginId() + 1;
                }
            }
        }
        
        if(foundLogin == null) {
            foundLogin = new UserLogin(user.getId(), nextLoginId, deviceIdentifier);
            accessor.putMapValueObject(hkeyIndex, nextLoginId, Integer.class);
        }
        
        foundLogin.setStatus(Status.loggedIn);
        foundLogin.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            
        String hkeyLogin = String.valueOf(nextLoginId);
        Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        accessorLogin.putMapValueObject(hkeyLogin, foundLogin, UserLogin.class);
        
        return foundLogin;
    }
    
    @Override
    public UserLogin findLoginByToken(LoginToken loginToken) {
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        String hkeyLogin = String.valueOf(loginToken.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        return accessor.getMapValueObject(hkeyLogin, UserLogin.class);
    }
    
    @Override
    public void logoff(UserLogin login) {
        String userKey = NameMapper.getCacheKey("user", login.getUserId(), null);
        String hkeyLogin = String.valueOf(login.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        login.setStatus(Status.loggedOff);
        accessor.putMapValueObject(hkeyLogin, login, UserLogin.class);
    }
    
    @Override
    public boolean isValidLoginToken(LoginToken loginToken) {
        assert(loginToken != null);
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
        UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()), UserLogin.class);
        if(login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber())
            return true;
        
        return false;
    }
    
    private static boolean isVerificationExpired(Timestamp ts) {
        // TODO hard-code expiration time to 10 minutes
        return DateHelper.currentGMTTime().getTime() - ts.getTime() > 10*60000;
    }
}
