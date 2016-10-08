// @formatter:off
package com.everhomes.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.link.RichLinkDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.scene.SceneTypeInfoDTO;
import com.everhomes.rest.ui.organization.SetCurrentCommunityForSceneCommand;
import com.everhomes.rest.ui.user.GetUserRelatedAddressCommand;
import com.everhomes.rest.ui.user.GetUserRelatedAddressResponse;
import com.everhomes.rest.ui.user.ListSearchTypesBySceneCommand;
import com.everhomes.rest.ui.user.ListSearchTypesBySceneReponse;
import com.everhomes.rest.ui.user.ListScentTypeByOwnerCommand;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;
import com.everhomes.rest.user.AssumePortalRoleCommand;
import com.everhomes.rest.user.BorderListResponse;
import com.everhomes.rest.user.CreateInvitationCommand;
import com.everhomes.rest.user.CreateUserImpersonationCommand;
import com.everhomes.rest.user.DeleteUserImpersonationCommand;
import com.everhomes.rest.user.GetBizSignatureCommand;
import com.everhomes.rest.user.GetSignatureCommandResponse;
import com.everhomes.rest.user.GetUserInfoByIdCommand;
import com.everhomes.rest.user.ListLoginByPhoneCommand;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.SearchUserImpersonationCommand;
import com.everhomes.rest.user.SearchUserImpersonationResponse;
import com.everhomes.rest.user.SendMessageTestCommand;
import com.everhomes.rest.user.SetUserAccountInfoCommand;
import com.everhomes.rest.user.SetUserInfoCommand;
import com.everhomes.rest.user.SignupCommand;
import com.everhomes.rest.user.SynThridUserCommand;
import com.everhomes.rest.user.UserCurrentEntity;
import com.everhomes.rest.user.UserIdentifierDTO;
import com.everhomes.rest.user.UserImpersonationDTO;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserInvitationsDTO;
import com.everhomes.rest.user.UserLoginResponse;
import com.everhomes.rest.user.ValidatePassCommand;
import com.everhomes.rest.user.VerifyAndLogonByIdentifierCommand;
import com.everhomes.rest.user.VerifyAndLogonCommand;
import com.everhomes.rest.user.admin.ListInvitatedUserCommand;
import com.everhomes.rest.user.admin.ListInvitatedUserResponse;
import com.everhomes.rest.user.admin.ListUsersWithAddrCommand;
import com.everhomes.rest.user.admin.ListUsersWithAddrResponse;
import com.everhomes.rest.user.admin.SearchInvitatedUserCommand;
import com.everhomes.rest.user.admin.SearchUsersWithAddrCommand;
import com.everhomes.rest.user.admin.SendUserTestMailCommand;
import com.everhomes.rest.user.admin.SendUserTestRichLinkMessageCommand;
import com.everhomes.rest.user.admin.SendUserTestSmsCommand;
import com.everhomes.rest.user.admin.UsersWithAddrResponse;

/**
 * 
 * Define business logic interface for user management
 * 
 * @author Kelven Yang
 *
 */
public interface UserService {
    SignupToken signup(SignupCommand cmd, HttpServletRequest request);
    UserIdentifier findIdentifierByToken(Integer namespaceId, SignupToken signupToken);
    void resendVerficationCode(Integer namespaceId, SignupToken signupToken);
    UserLogin verifyAndLogon(VerifyAndLogonCommand cmd);
    UserLogin verifyAndLogonByIdentifier(VerifyAndLogonByIdentifierCommand cmd);
    
    User logonDryrun(String userIdentifierToken, String password);
    UserLogin logon(int namespaceId, String userIdentifierToken, String password, String deviceIdentifier, String pusherIdentify);
    UserLogin logonByToken(LoginToken loginToken);
    UserLogin findLoginByToken(LoginToken loginToken);
    void logoff(UserLogin login);
    boolean isValidLoginToken(LoginToken loginToken);
    
    UserLogin registerLoginConnection(LoginToken loginToken, int borderId, String borderSessionId);
    UserLogin unregisterLoginConnection(LoginToken loginToken, int borderId, String borderSessionId);
    void saveLogin(UserLogin login);
    List<UserLogin> listUserLogins(long uid);
    
    UserInfo getUserInfo();
    UserInfo getUserInfo(Long uid);
    UserInfo getUserSnapshotInfo(Long uid);
    UserInfo getUserSnapshotInfoWithPhone(Long uid);
    User findUserByIndentifier(Integer namespaceId, String indentifierToken);
    void setUserInfo(SetUserInfoCommand cmd); 
    void setUserAccountInfo(SetUserAccountInfoCommand cmd); 
    CommunityDTO setUserCurrentCommunity(long communityId);
    Long setDefaultCommunity(Long userId, Integer namespaceId);
    void updateUserCurrentCommunityToProfile(Long userId, Long communityId, Integer namespaceId);
    
    List<UserIdentifierDTO> listUserIdentifiers();
    void deleteUserIdentifier(long identifierId);
    
    void resendVerficationCode(UserIdentifier userIdentifier);
    
    UserInvitationsDTO createInvatation(CreateInvitationCommand cmd);
    
    void assumePortalRole(AssumePortalRoleCommand cmd);
    
    long getNextStoreSequence(UserLogin login, int namespaceId, long appId);
    
    List<ListUsersWithAddrResponse> listUsersWithAddr(ListUsersWithAddrCommand cmd);
    UsersWithAddrResponse searchUsersWithAddr(SearchUsersWithAddrCommand cmd);
    ListInvitatedUserResponse listInvitatedUser(ListInvitatedUserCommand cmd);
    ListInvitatedUserResponse searchInvitatedUser(SearchInvitatedUserCommand cmd);
    UserInfo getUserBasicByUuid(String uuid);
	GetSignatureCommandResponse getSignature();
	List<UserCurrentEntity> listUserCurrentEntity(Long userId);
	UserLogin synThridUser(SynThridUserCommand cmd);
	GetSignatureCommandResponse getThirdSignature(GetBizSignatureCommand cmd);
	UserInfo getUserInfoById(GetUserInfoByIdCommand cmd);
	String getUserAvatarUriByGender(Long userId, Integer namespaceId, Byte gener);
	void sendUserTestSms(SendUserTestSmsCommand cmd);
	void sendUserTestMail(SendUserTestMailCommand cmd);
	RichLinkDTO sendUserRichLinkMessage(SendUserTestRichLinkMessageCommand cmd);
	UserLogin innerLogin(Integer namespaceId, Long userId,String deviceIdentifier, String pusherIdentify);
	List<UserInfo> listUserByKeyword(String keyword);
	List<User> listUserByIdentifier(String identifier);
	List<UserInfo> listUserInfoByIdentifier(String identifier);
	

	List<SceneDTO> listUserRelatedScenes();
	void toFamilySceneDTO(Integer namespaceId, Long userId, List<SceneDTO> sceneList, List<FamilyDTO> familyDtoList);
	SceneDTO toFamilySceneDTO(Integer namespaceId, Long userId, FamilyDTO familyDto);
	SceneTokenDTO toSceneTokenDTO(Integer namespaceId, Long userId, FamilyDTO familyDto, SceneType sceneType);
	void toOrganizationSceneDTO(Integer namespaceId, Long userId, List<SceneDTO> sceneList, 
	    List<OrganizationDTO> organizationDtoList, SceneType sceneType);
	SceneDTO toOrganizationSceneDTO(Integer namespaceId, Long userId, OrganizationDTO organizationDto, SceneType sceneType);
	SceneTokenDTO toSceneTokenDTO(Integer namespaceId, Long userId, OrganizationDTO organizationDto, SceneType sceneType);
	GetUserRelatedAddressResponse getUserRelatedAddresses(GetUserRelatedAddressCommand cmd);
	SceneTokenDTO checkSceneToken(Long userId, String sceneToken);
	List<SceneDTO> setCurrentCommunityForScene(SetCurrentCommunityForSceneCommand cmd);
	SceneDTO toCommunitySceneDTO(Integer namespaceId, Long userId, CommunityDTO community, SceneType sceneType);
	SceneTokenDTO toSceneTokenDTO(Integer namespaceId, Long userId, CommunityDTO community, SceneType sceneType);
    UserLoginResponse listLoginsByPhone(ListLoginByPhoneCommand cmd);
    String sendMessageTest(SendMessageTestCommand cmd);
    String pushMessageTest(SendMessageTestCommand cmd);
    BorderListResponse listBorders();

    UserImpersonationDTO createUserImpersonation(CreateUserImpersonationCommand cmd);
    SearchUserImpersonationResponse listUserImpersons(SearchUserImpersonationCommand cmd);

    
    SearchContentsBySceneReponse searchContentsByScene(SearchContentsBySceneCommand cmd);
    ListSearchTypesBySceneReponse listSearchTypesByScene(ListSearchTypesBySceneCommand cmd);


    void deleteUserImpersonation(DeleteUserImpersonationCommand cmd);

    boolean isValid(LoginToken token);
    LoginToken getLoginToken(HttpServletRequest request);
    UserLogin logonBythirdPartUser(Integer namespaceId, String userType, String userToken, HttpServletRequest request, HttpServletResponse response);
    /**
     * 注册第三方用户
     * @param user
     * @param request
     * @return 如果创建了新用户则返回true，否则返回false
     */
    boolean signupByThirdparkUser(User user, HttpServletRequest request);
	Boolean validateUserPass(ValidatePassCommand passCmd);
}
