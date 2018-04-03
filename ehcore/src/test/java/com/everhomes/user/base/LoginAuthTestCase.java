package com.everhomes.user.base;

import com.everhomes.acl.*;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.user.*;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LoginAuthTestCase extends CoreServerTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthTestCase.class);

    @Autowired
    protected DbProvider dbProvider;
    
    @Autowired
    protected UserProvider userProvider;
    
    @Autowired
    protected AclProvider aclProvider;

    @Autowired
    UserService userService;

    @Before
    public void setUp() throws Exception {
    	super.setUp();
    }
    
    /**
     * 登录
     * @param phone 手机号
     * @param password 密码
     */
    protected User logon(String phone, String password) {
    	UserLogin login = userService.logon(0, 86, phone, EncryptionUtils.hashPassword(password), null, null);
        Assert.assertNotNull(login);
        Assert.assertTrue(login.getLoginId() > 0);
        
        UserContext context = UserContext.current();
        context.setLogin(login);
        User user = this.userProvider.findUserById(login.getUserId());
        Assert.assertNotNull(user);
        context.setUser(user);
        
        return user;
    }
    
    /**
     * 退出登录
     */
    protected void logout() {
    	UserLogin login = UserContext.current().getLogin();
    	userService.logoff(login);
    }
    
    /**
     * 指定手机号和密码创建用户（其它值为空）
     * @param phone 手机号
     * @param password 密码
     */
    protected long createPhoneUser(String phone, String password) {
        password = EncryptionUtils.hashPassword(password);
    	User user = new User();
    	String salt=EncryptionUtils.createRandomSalt();
        user.setSalt(salt);
        user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", password, salt)));
    	byte userStatus = UserStatus.ACTIVE.getCode();
        user.setStatus(userStatus);
        
        return createPhoneUser(phone, user);
    }
    
    /**
     * 指定手机号和用户信息创建用户（用户信息里至少要含密码）
     * @param phone 手机号
     * @param user 用户其它信息（至少要含密码）
     */
    protected long createPhoneUser(String phone, User user) {
        byte identifierType = IdentifierType.MOBILE.getCode();
    	//UserIdentifier identifier = this.dbProvider.execute((TransactionStatus status) -> {
	    	userProvider.createUser(user);
	    	long userId = user.getId();
	        Assert.assertTrue("User id should be greater than 0, userId=" + userId, userId > 0);
	        User dbUser = userProvider.findUserById(userId);
	        Assert.assertNotNull("The user should be found in db, userId=" + userId, dbUser);
	        byte userStatus = UserStatus.ACTIVE.getCode();
	        Assert.assertEquals(Byte.valueOf(userStatus), dbUser.getStatus());
	        
	        UserIdentifier newIdentifier = new UserIdentifier();
	        newIdentifier.setOwnerUid(userId);
	        newIdentifier.setIdentifierType(identifierType);
	        newIdentifier.setIdentifierToken(phone);
	
	        String verificationCode = RandomGenerator.getRandomDigitalString(6);
	        newIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
	        newIdentifier.setVerificationCode(verificationCode);
	        newIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	        userProvider.createIdentifier(newIdentifier);
	        
//	        return newIdentifier;
//    	});
        long userIdentifierId = newIdentifier.getId();
        Assert.assertTrue("User identifier id should be greater than 0, userIdentifierId=" + userIdentifierId, userIdentifierId > 0);
        UserIdentifier dbIdentifier = userProvider.findIdentifierById(userIdentifierId);
        Assert.assertNotNull("The user identifier should be found in db, userIdentifierId=" + userIdentifierId, dbIdentifier);
        Assert.assertEquals(phone, dbIdentifier.getIdentifierToken());
        //Assert.assertEquals(Byte.valueOf(identifierType), dbIdentifier.getClaimStatus());
        
        return newIdentifier.getOwnerUid();
    }
    
    /**
     * 根据指定手机号删除用户
     * @param phone 手机号
     */
    protected List<Long> deletePhoneUser(String phone) {
    	List<Long> userIdList = new ArrayList<Long>();
    	
    	UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(phone);
    	long userId = 0L;
    	while(userIdentifier != null) {
    		userId = userIdentifier.getOwnerUid();
    		userIdList.add(userId);
    		userProvider.deleteUser(userId);
    		userProvider.deleteIdentifier(userIdentifier);
    		userIdentifier = userProvider.findClaimedIdentifierByToken(phone);
    	}
    	
    	return userIdList;
    }
    
    protected void prepareAclAndAssignments() {
        deleteAllAcls();
        deleteAllAssignments();
        
        createAcl();
    }
    
    protected void createAcl() {
        Acl acl = new Acl();
        acl.setOwnerType("system");
        acl.setPrivilegeId(Privilege.All);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.SystemAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType("extension");
        acl.setPrivilegeId(Privilege.Visible);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.SystemExtension);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
        
        // setup default GROUP resource ACL
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.Create);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.AuthenticatedUser);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.Write);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceCreator);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.Delete);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceCreator);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
        
        // everyone in the group can invite friends to join the group
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupInviteJoin);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceUser);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        // Creator/admin can update group member info
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupUpdateMember);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceCreator);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupUpdateMember);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        // Only admin can approve group member request
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupApproveMember);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        // Only admin can reject group member request
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupRejectMember);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        // Only admin can revoke group member
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupRevokeMember);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupListMember);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceUser);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupRequestAdminRole);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceUser);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupInviteAdminRole);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.GROUP.getCode());
        acl.setPrivilegeId(PrivilegeConstants.GroupAdminOps);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.aclProvider.createAcl(acl);
        
        // setup default FORUM resource ACL
        acl = new Acl();
        acl.setOwnerType(EntityType.FORUM.getCode());
        acl.setPrivilegeId(PrivilegeConstants.ForumNewTopic);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceUser);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);

        acl = new Acl();
        acl.setOwnerType(EntityType.FORUM.getCode());
        acl.setPrivilegeId(PrivilegeConstants.ForumDeleteTopic);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceCreator);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);

        acl = new Acl();
        acl.setOwnerType(EntityType.FORUM.getCode());
        acl.setPrivilegeId(PrivilegeConstants.ForumDeleteTopic);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.FORUM.getCode());
        acl.setPrivilegeId(PrivilegeConstants.ForumNewReply);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceUser);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
        
        acl = new Acl();
        acl.setOwnerType(EntityType.FORUM.getCode());
        acl.setPrivilegeId(PrivilegeConstants.ForumDeleteReply);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceCreator);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);

        acl = new Acl();
        acl.setOwnerType(EntityType.FORUM.getCode());
        acl.setPrivilegeId(PrivilegeConstants.ForumDeleteReply);
        acl.setGrantType((byte)1);
        acl.setCreatorUid(User.ROOT_UID);
        acl.setRoleId(Role.ResourceAdmin);
        acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        createAcl(acl);
    }
    
    protected void createAcl(Acl acl) {
    	try {
            this.aclProvider.createAcl(acl);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    protected void deleteAllAcls() {
        List<Acl> aclList = this.aclProvider.getAllAcls();
        for(Acl acl : aclList) {
            this.aclProvider.deleteAcl(acl);
        }
    }
    
    protected void deleteAllAssignments() {
        List<RoleAssignment> assignments = this.aclProvider.getAllRoleAssignments();
        for(RoleAssignment assignment : assignments) {
            this.aclProvider.deleteRoleAssignment(assignment);
        }
    }
}
