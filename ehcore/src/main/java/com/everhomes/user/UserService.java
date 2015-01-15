package com.everhomes.user;

import java.util.List;

/**
 * 
 * Define business logic interface for user management
 * 
 * @author Kelven Yang
 *
 */
public interface UserService {
    SignupToken signup(IdentifierType identifierType, String identifierToken);
    UserIdentifier findIdentifierByToken(SignupToken signupToken);
    void resendVerficationCode(SignupToken signupToken);
    UserLogin verifyAndLogon(SignupToken signupToken, String verificationCode, String deviceIdentifier);
    
    UserLogin logon(int namespaceId, String userIdentifierToken, String password, String deviceIdentifier);
    UserLogin logonByToken(LoginToken loginToken);
    UserLogin findLoginByToken(LoginToken loginToken);
    void logoff(UserLogin login);
    boolean isValidLoginToken(LoginToken loginToken);
    
    UserLogin registerLoginConnection(LoginToken loginToken, int borderId);
    UserLogin unregisterLoginConnection(LoginToken loginToken, int borderId);
    List<UserLogin> listUserLogins(long uid);
}
