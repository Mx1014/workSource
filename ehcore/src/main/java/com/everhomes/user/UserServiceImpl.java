// @formatter:off
package com.everhomes.user;

import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.naming.NameMapper;
import com.everhomes.user.UserLogin.Status;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PasswordHash;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

/**
 * 
 * Implement business logic for user management
 * 
 * @author Kelven Yang
 *
 */
@Component
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    private UserServiceProvider serviceProvider;

    @Override
    public SignupToken signup(final IdentifierType identifierType, final String identifierToken) {
        UserIdentifier identifier = this.dbProvider.execute(new TransactionCallback<UserIdentifier>() {

            @Override
            public UserIdentifier doInTransaction(TransactionStatus arg0) {
                User user = new User();
                user.setStatus((byte)UserStatus.active.ordinal());
                serviceProvider.createUser(user);
                
                UserIdentifier identifier = new UserIdentifier();
                identifier.setOwnerUid(user.getId());
                identifier.setIdentifierType((byte)identifierType.ordinal());
                identifier.setIdentifierToken(identifierToken);

                String verificationCode = RandomGenerator.getRandomDigitalString(6);
                identifier.setClaimStatus((byte)IdentifierClaimStatus.verifying.ordinal());
                identifier.setVerificationCode(verificationCode);
                identifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                serviceProvider.createIdentifier(identifier);
                
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
            this.serviceProvider.updateIdentifier(identifier);
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
                this.serviceProvider.updateIdentifier(identifier);
            }

            User user = this.serviceProvider.findUserById(identifier.getOwnerUid());
            UserLogin login = createLogin(Namespace.DEFAULT_NAMESPACE, user, deviceIdentifier);
            login.setStatus(Status.loggedIn);
            return login;
        }
        
        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
    }
    
    @Override
    public UserLogin logon(int namespaceId, String userIdentifierToken, String password, String deviceIdentifier) {
        User user = null;
        
        UserIdentifier userIdentifier = this.serviceProvider.findClaimedIdentifierByToken(userIdentifierToken);
        if(userIdentifier == null) {
            LOGGER.warn("Unable to find identifier record of " + userIdentifierToken);
            user = this.serviceProvider.findUserByAccountName(userIdentifierToken);
            if(user == null) {
                LOGGER.error("Unable to locate user with account name: " + userIdentifierToken);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNABLE_TO_LOCATE_USER, "Unable to locate user");
            }
        } else {
            user = this.serviceProvider.findUserById(userIdentifier.getOwnerUid());
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
  
        if(deviceIdentifier != null && deviceIdentifier.isEmpty())
            deviceIdentifier = null;
            
        UserLogin login = createLogin(namespaceId, user, deviceIdentifier);
        login.setStatus(Status.loggedIn);
        
        if(LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("User %s logged in. login info: %s", userIdentifierToken, StringHelper.toJsonString(login)));
        return login;
    }
    
    @Override
    public UserLogin logonByToken(LoginToken loginToken) {
        assert(loginToken != null);
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
        UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()));
        if(login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber()) {
            login.setStatus(Status.loggedIn);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            accessor.putMapValueObject(String.valueOf(loginToken.getLoginId()), login);
            return login;
        }
        
        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_LOGIN_TOKEN, 
                "Invalid token or token has expired");
    }
    
    private UserLogin createLogin(int namespaceId, User user, String deviceIdentifier) {
        String userKey = NameMapper.getCacheKey("user", user.getId(), null);
        
        // get "index" accessor
        String hkeyIndex = "0";
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
        Integer maxLoginId = accessor.getMapValueObject(hkeyIndex);
        UserLogin foundLogin = null;
        int nextLoginId = 1;
        
        if(maxLoginId != null) {
            for(int i = 1; i <= maxLoginId.intValue(); i++) {
                String hkeyLogin = String.valueOf(i);
                Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
                UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
                if(login != null) {
                    if(login.getNamespaceId() == namespaceId) {
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
                    }
                    
                    if(login.getLoginId() >= nextLoginId)
                        nextLoginId = login.getLoginId() + 1;
                }
            }
        }
        
        if(foundLogin == null) {
            foundLogin = new UserLogin(namespaceId, user.getId(), nextLoginId, deviceIdentifier);
            accessor.putMapValueObject(hkeyIndex, nextLoginId);
        }
        
        foundLogin.setStatus(Status.loggedIn);
        foundLogin.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            
        String hkeyLogin = String.valueOf(nextLoginId);
        Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        accessorLogin.putMapValueObject(hkeyLogin, foundLogin);
        
        return foundLogin;
    }
    
    public UserLogin registerLoginConnection(LoginToken loginToken, int borderId) {
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        
        String hkeyLogin = String.valueOf(loginToken.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        UserLogin login = accessor.getMapValueObject(hkeyLogin);
        if(login != null) {
            login.setLoginBorderId(borderId);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            accessor.putMapValueObject(hkeyLogin, login);
        } else {
            LOGGER.warn("Unable to find UserLogin in big map. LoginToken: " + StringHelper.toJsonString(loginToken));
        }
        
        return login;
    }
    
    public UserLogin unregisterLoginConnection(LoginToken loginToken, int borderId) {
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        String hkeyLogin = String.valueOf(loginToken.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        UserLogin login = accessor.getMapValueObject(hkeyLogin);
        if(login != null) {
            login.setLoginBorderId(null);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            accessor.putMapValueObject(hkeyLogin, login);
        } else {
            LOGGER.warn("Unable to find UserLogin in big map. LoginToken: " + StringHelper.toJsonString(loginToken));
        }
        
        return login;
    }
    
    @Override
    public List<UserLogin> listUserLogins(long uid) {
        List<UserLogin> logins = new ArrayList<>();
        String userKey = NameMapper.getCacheKey("user", uid, null);
        
        // get "index" accessor
        String hkeyIndex = "0";
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
        Integer maxLoginId = accessor.getMapValueObject(hkeyIndex);
        if(maxLoginId != null) {
            for(int i = 1; i <= maxLoginId.intValue(); i++) {
                String hkeyLogin = String.valueOf(i);
                Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
                UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
                if(login != null) {
                    logins.add(login);
                }
            }
        }
        return logins;
    }
    
    @Override
    public UserLogin findLoginByToken(LoginToken loginToken) {
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        String hkeyLogin = String.valueOf(loginToken.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        return accessor.getMapValueObject(hkeyLogin);
    }
    
    @Override
    public void logoff(UserLogin login) {
        String userKey = NameMapper.getCacheKey("user", login.getUserId(), null);
        String hkeyLogin = String.valueOf(login.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        login.setStatus(Status.loggedOff);
        accessor.putMapValueObject(hkeyLogin, login);
    }
    
    @Override
    public boolean isValidLoginToken(LoginToken loginToken) {
        assert(loginToken != null);
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
        UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()));
        if(login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber())
            return true;
        
        return false;
    }
    
    private static boolean isVerificationExpired(Timestamp ts) {
        // TODO hard-code expiration time to 10 minutes
        return DateHelper.currentGMTTime().getTime() - ts.getTime() > 10*60000;
    }
}
