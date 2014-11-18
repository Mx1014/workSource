package com.everhomes.user;

import java.util.List;

public interface UserServiceProvider {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    User findUserById(long id);
    User findUserByAccountName(String accountName);
    
    List<UserIdentifier> listUserIdentifiersOfUser(long userId);

    void createIdentifier(UserIdentifier userIdentifier);
    void updateIdentifier(UserIdentifier userIdentifier);
    void deleteIdentifier(UserIdentifier userIdentifier);
    void deleteIdentifier(long id);
    UserIdentifier findIdentifierById(long id);
    UserIdentifier findClaimedIdentifierByToken(String identifierToken);
    
    SignupToken signup(IdentifierType identifierType, String identifierToken);
    UserIdentifier findIdentifierByToken(SignupToken signupToken);
    void resendVerficationCode(SignupToken signupToken);
    UserLogin verifyAndLogon(SignupToken signupToken, String verificationCode, String deviceIdentifier);
    
    UserLogin logon(String userIdentifierToken, String password, String deviceIdentifier);
    UserLogin logonByToken(LoginToken loginToken);
    UserLogin findLoginByToken(LoginToken loginToken);
    void logoff(UserLogin login);
    
    boolean isValidLoginToken(LoginToken loginToken);
}
