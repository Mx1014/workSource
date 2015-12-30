// @formatter:off
package com.everhomes.user;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.SignupCommand;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.rest.user.VerifyAndLogonCommand;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.RandomGenerator;
import com.everhomes.util.Version;
import com.everhomes.util.WebTokenGenerator;

public class UserServiceTest extends CoreServerTestCase {
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Ignore @Test
    public void testUserCRUD() {
        User user = new User();
        user.setAccountName("lucy");
        user.setStatus((byte)UserStatus.ACTIVE.ordinal());
        
        this.userProvider.createUser(user);
        System.out.println("user id : " + user.getId());
    }
    
    @Ignore @Test
    public void testUserIdentifierCRUD() {
/*        
        UserIdentifier identifier = new UserIdentifier();
        identifier.setOwnerUid(1L);
        identifier.setIdentifierType((byte)IdentifierType.MOBILE.ordinal());
        identifier.setIdentifierToken("14083076807");
        
        this.userProvider.createIdentifier(identifier);
        
        identifier = new UserIdentifier();
        identifier.setOwnerUid(1L);
        identifier.setIdentifierType((byte)IdentifierType.EMAIL.ordinal());
        identifier.setIdentifierToken("kelveny@gmail.com");
        
        this.userProvider.createIdentifier(identifier);
 */
        UserIdentifier identifier = new UserIdentifier();
        identifier.setOwnerUid(1L);
        identifier.setIdentifierType(IdentifierType.MOBILE.getCode());
        identifier.setIdentifierToken("1234");
        identifier.setClaimStatus(IdentifierClaimStatus.CLAIMING.getCode());
        
        this.userProvider.createIdentifier(identifier);

/*        
        UserIdentifier foundIdentifier = this.userProvider.findClaimingIdentifierByToken("1234");
        if(foundIdentifier != null) {
            System.out.println("identifer: " + foundIdentifier.toString());
        }
        
        foundIdentifier = this.userProvider.findClaimingIdentifierByToken("1234");
        if(foundIdentifier != null) {
            System.out.println("identifer: " + foundIdentifier.toString());
        }        
        
        identifier.setClaimStatus(IdentifierClaimStatus.VERIFYING.getCode());
        this.userProvider.updateIdentifier(identifier);

        foundIdentifier = this.userProvider.findClaimingIdentifierByToken("1234");
        if(foundIdentifier != null) {
            System.out.println("identifer: " + foundIdentifier.toString());
        }
*/        
        this.userProvider.deleteIdentifier(identifier);
    }
    
    @Ignore @Test
    public void testDeviceSearch() {
        UserIdentifier device = this.userProvider.findIdentifierById(1);
        System.out.println("device: " + device.getIdentifierToken());
        
        device = this.userProvider.findClaimedIdentifierByToken(Namespace.DEFAULT_NAMESPACE, "14083076807");
        System.out.println("device id : " + device.getId());
    }
    
    @Ignore @Test
    public void testLoginToken() {
        LoginToken token = new LoginToken(1, 1, 100);
        String tokenString = WebTokenGenerator.getInstance().toWebToken(token);
        
        LoginToken token2 = WebTokenGenerator.getInstance().fromWebToken(tokenString, LoginToken.class);
        Assert.assertTrue(token.getUserId() == token2.getUserId());
        Assert.assertTrue(token.getLoginId() == token2.getLoginId());
        Assert.assertTrue(token.getLoginInstanceNumber() == token2.getLoginInstanceNumber());
        
        Version ver = Version.fromVersionString("1.0.1-SNAPSHOT");
        Assert.assertTrue(ver.getMajor() == 1);
        Assert.assertTrue(ver.getMinor() == 0);
        Assert.assertTrue(ver.getRevision() == 1);
        Assert.assertTrue(ver.getTag().equals("SNAPSHOT"));
        
        String randomCode = RandomGenerator.getRandomDigitalString(4);
        System.out.println("random code: " + randomCode);
    }
    
    @Ignore @Test
    public void testSignupToken() {
        SignupToken token = new SignupToken(100, IdentifierType.MOBILE, "140812345567");
        
        String tokenString = WebTokenGenerator.getInstance().toWebToken(token);
        
        SignupToken token2 = WebTokenGenerator.getInstance().fromWebToken(tokenString, SignupToken.class);
        Assert.assertTrue(token.getUserId() == token2.getUserId());
        Assert.assertTrue(token.getIdentifierType() == token2.getIdentifierType());
        Assert.assertTrue(token.getIdentifierToken().equals(token2.getIdentifierToken()));
    }
    
    @Ignore @Test
    public void testSignup() {
        SignupCommand cmd = new SignupCommand();
        cmd.setType("mobile");
        cmd.setToken("14081234567");
        SignupToken token = this.userService.signup(cmd,null);
        System.out.println("Signup token: " + WebTokenGenerator.getInstance().toWebToken(token));
    }
    
    @Ignore @Test
    public void testVerifyAndLogon() {
        VerifyAndLogonCommand cmd = new VerifyAndLogonCommand();
        cmd.setSignupToken("eyJ1c2VySWQiOjEsImlkZW50aWZpZXJUeXBlIjoibW9iaWxlIiwiaWRlbnRpZmllclRva2VuIjoiMTQwODEyMzQ1NjcifQ");
        cmd.setVerificationCode("414846");
        cmd.setDeviceIdentifier("virtual device");
        UserLogin login = this.userService.verifyAndLogon(cmd);
        
        LoginToken loginToken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber());
        System.out.println("Login token: " + WebTokenGenerator.getInstance().toWebToken(loginToken));
    }
    
    @Ignore @Test
    public void testLoginByToken() {
        LoginToken token = WebTokenGenerator.getInstance().fromWebToken("eyJ1c2VySWQiOjEsImxvZ2luSWQiOjEsImxvZ2luSW5zdGFuY2VOdW1iZXIiOi0xMzY5Njg0ODEzfQ", LoginToken.class);
        this.userService.logonByToken(token);
    }
    
    @Test
    public void testPasswordHash() {
        String salt = EncryptionUtils.createRandomSalt();
        String hash = EncryptionUtils.hashPassword(String.format("%s%s","password",salt));
        System.out.println("Salt:" + salt + ", hash:" + hash);
    }
}

