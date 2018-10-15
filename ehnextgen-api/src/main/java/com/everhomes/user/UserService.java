// @formatter:off
package com.everhomes.user;

import com.everhomes.community.Community;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.asset.PushUsersCommand;
import com.everhomes.rest.asset.PushUsersResponse;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.link.RichLinkDTO;
import com.everhomes.rest.openapi.FunctionCardDto;
import com.everhomes.rest.openapi.GetOrgCheckInDataCommand;
import com.everhomes.rest.openapi.UserCouponsCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.ui.organization.SetCurrentCommunityForSceneCommand;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.admin.*;

import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

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
    void resendVerficationCode(Integer namespaceId, SignupToken signupToken, Integer regionCode, HttpServletRequest request);
    UserLogin verifyAndLogon(VerifyAndLogonCommand cmd);
    UserLogin verifyAndLogonByIdentifier(VerifyAndLogonByIdentifierCommand cmd);

    UserLogin logonDryrun(Integer namespaceId, String userIdentifierToken, String password);
    UserLogin logon(int namespaceId, Integer regionCode, String userIdentifierToken, String password, String deviceIdentifier, String pusherIdentify);
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

    Long setDefaultCommunityForWx(Long userId, Integer namespaceId);

    void updateUserCurrentCommunityToProfile(Long userId, Long communityId, Integer namespaceId);

    List<UserIdentifierDTO> listUserIdentifiers();
    void deleteUserIdentifier(long identifierId);

    void resendVerficationCode(ResendVerificationCodeByIdentifierCommand cmd, HttpServletRequest request);

    void resendVerficationCodeByApp(ResendVerificationCodeByIdentifierCommand cmd, HttpServletRequest request);
    void sendCodeWithPictureValidate(SendCodeWithPictureValidateCommand cmd, HttpServletRequest request);

    void sendCodeWithPictureValidateByApp(SendCodeWithPictureValidateCommand cmd, HttpServletRequest request);

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
	List<SceneDTO> listUserRelatedScenes(ListUserRelatedScenesCommand cmd);
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

    SearchTypes getSearchTypes(Integer namespaceId, String searchContentType);

    ListSearchTypesBySceneReponse listSearchTypesByScene(ListSearchTypesBySceneCommand cmd);


    void deleteUserImpersonation(DeleteUserImpersonationCommand cmd);

    boolean isValid(LoginToken token);
    LoginToken getLoginToken(HttpServletRequest request);
    UserLogin logonBythirdPartUser(Integer namespaceId, String userType, String userToken, HttpServletRequest request, HttpServletResponse response);
    UserLogin logonBythirdPartAppUser(Integer namespaceId, String userType, String userToken, HttpServletRequest request, HttpServletResponse response);
    /**
     * 注册第三方用户
     * @param user
     * @param request
     * @return 如果创建了新用户则返回true，否则返回false
     */
    boolean signupByThirdparkUser(User user, HttpServletRequest request);
    User signupByThirdparkUserByApp(User user, HttpServletRequest request);
	Boolean validateUserPass(ValidatePassCommand passCmd);

    List<SceneDTO> listTouristRelatedScenes();

    /**
     * 判断是否登录
     * @return
     */
    boolean isLogon();

    /**
     * 检查游客是否能继续访问场景资源
     * @param sceneType
     */
    void checkUserScene(SceneType sceneType);

    /**
     * 查询命名空间下的用户
     * @param cmd
     * @return
     */
    ListRegisterUsersResponse searchUserByNamespace(SearchUserByNamespaceCommand cmd);
	UserLogin reSynThridUser(InitBizInfoCommand cmd);
	InitBizInfoDTO findInitBizInfo();

    /**
     * 设置会话消息免打扰
     * @param cmd
     * @return
     */
    UserNotificationSettingDTO updateUserNotificationSetting(UpdateUserNotificationSettingCommand cmd);

    /**
     * 获取会话消息免打扰设置
     * @param cmd
     * @return
     */
    UserNotificationSettingDTO getUserNotificationSetting(GetUserNotificationSettingCommand cmd);

    /**
     * 根据会话获取用户信息
     * @param cmd
     * @return
     */
    MessageSessionInfoDTO getMessageSessionInfo(GetMessageSessionInfoCommand cmd);

    SearchUsersResponse searchUsers(SearchUsersCommand cmd);

    /**
     * 检查短信黑名单
     * @param smsAction     标识是从哪里发出的短信，目前没什么用
     * @param identifierToken   手机号
     */
    void checkSmsBlackList(String smsAction, String identifierToken);

    /**
     * 用户修改手机号时发送短信验证码，两步短信验证码都是这个接口
     * @param cmd
     * @param request
     */
    void sendVerificationCodeByResetIdentifier(SendVerificationCodeByResetIdentifierCommand cmd, HttpServletRequest request);

    /**
     * 核实修改手机号的验证码
     * @param cmd
     */
    void verifyResetIdentifierCode(VerifyResetIdentifierCodeCommand cmd);

    /**
     * 申诉修改手机号
     * @param cmd
     * @return
     */
    UserAppealLogDTO createResetIdentifierAppeal(CreateResetIdentifierAppealCommand cmd);

    /**
     * 申诉列表
     * @param cmd
     * @return
     */
    ListUserAppealLogsResponse listUserAppealLogs(ListUserAppealLogsCommand cmd);

    /**
     * 修改申诉状态
     * @param cmd
     * @return
     */
    UserAppealLogDTO updateUserAppealLog(UpdateUserAppealLogCommand cmd);

    /**
     * 获取修改手机号的验证码
     * @param cmd
     * @return
     */
    UserIdentifierLogDTO listResetIdentifierCode(ListResetIdentifierCodeCommand cmd);

	//added by R 20170713, 通讯录2.4增加

    SceneContactV2DTO getRelevantContactInfo(GetRelevantContactInfoCommand cmd);

	//added by R 20170824, 人事1.4,  判断管理员
    CheckContactAdminResponse checkContactAdmin(CheckContactAdminCommand cmd);

    //added by R 20170803, 消息2.1增加
    SceneContactV2DTO getContactInfoByUserId(GetContactInfoByUserIdCommand cmd);

    ListAuthFormsResponse listAuthForms();

	GetFamilyButtonStatusResponse getFamilyButtonStatus();


    /**
     *
     */
    List<String[]> listBuildingAndApartmentById(Long uid);
    /**
     * created by wentian
     * 根据客户名和地址定位唯一用户
     */
    TargetDTO findTargetByNameAndAddress(String contractNum, String targetName , Long ownerId,String tel,String ownerType,String targetType,Integer namespaceId);

    Long getCommunityIdBySceneToken(SceneTokenDTO sceneTokenDTO);

    List<SceneDTO> listUserRelatedScenesByCurrentType(ListUserRelatedScenesByCurrentTypeCommand cmd);

    UserIdentifier getUserIdentifier(Long userId);

    VerificationCodeForBindPhoneResponse verificationCodeForBindPhone(VerificationCodeForBindPhoneCommand cmd);

    void verificationCodeForBindPhoneByApp(VerificationCodeForBindPhoneCommand cmd);

    UserLogin bindPhone(BindPhoneCommand cmd);

    UserLogin bindPhoneByApp(BindPhoneCommand cmd);

    void checkVerifyCodeAndResetPassword(CheckVerifyCodeAndResetPasswordCommand cmd);

    void checkVerifyCodeAndResetPasswordWithoutIdentifyToken(CheckVerifyCodeAndResetPasswordWithoutIdentifyTokenCommand cmd);

    UserTemporaryTokenDTO checkUserTemporaryToken(CheckUserTemporaryTokenCommand cmd);

    SceneDTO getProfileScene();

    List<SceneDTO> listUserRelateScenesByCommunityId(ListUserRelateScenesByCommunityId cmd);

    List<SceneDTO> listAllCommunityScenesIfGeoExist(ListAllCommunityScenesIfGeoExistCommand cmd);

    SceneDTO convertCommunityToScene(Integer namespaceId, Long userId, Community default_community);

    List<SceneDTO> listAllCommunityScenes();

    /**
     * 用于测试服务器状态，不要用于业务使用 by lqs 20171019
     */
    String checkServerStatus();

    /**
     * 客户端更新设备信息到服务器端
     * @param cmd
     * @param request
     * @param response
     * @return 返回服务器端的信息
     */
    SystemInfoResponse updateUserBySystemInfo(SystemInfoCommand cmd,
            HttpServletRequest request, HttpServletResponse response);

    void syncUsersFromAnBangWuYe(SyncUsersFromAnBangWuYeCommand cmd);

    PushUsersResponse createUsersForAnBang(PushUsersCommand cmd);


    //通过安邦提供的token调用他方接口进行验证，并拿到用户信息
    UserLogin verifyUserByTokenFromAnBang(String token);

    //通过安邦提供的token获取本地用户并登录
    void logonBuAnBangToken();

    void pushUserDemo();

    SearchUserByIdentifierResponse searchUserByIdentifier(SearchUserByIdentifierCommand cmd);

    QRCodeDTO querySubjectIdForScan();

    DeferredResult<RestResponse> waitScanForLogon(String subjectId, HttpServletRequest request, HttpServletResponse response);

    String getSercetKeyForScan(String args);

    void logonByScan(String subjectId, String message);

    List<FunctionCardDto> listUserRelatedCards();
    User getUserFromAnBangToken(String token);
    List<SceneDTO> listAnbangRelatedScenes(ListAnBangRelatedScenesCommand cmd);
    String makeAnbangRedirectUrl(Long userId, String location,
            Map<String, String[]> paramMap);
  
    Byte isUserAuth();

    UserDTO getUserFromPhone(FindUserByPhoneCommand cmd);

    void sendVerficationCode4Point(Integer namespaceId, UserDTO user, Integer regionCode, HttpServletRequest request) ;

    PointCheckVCDTO pointCheckVerificationCode(PointCheckVerificationCodeCommand cmd) ;

    void registerWXLoginConnection(HttpServletRequest request);

    ListAddressUsersResponse listAddressUsers(ListAddressUsersCommand cmd);
    GetUserConfigAfterStartupResponse getUserConfigAfterStartup(
            GetUserConfigAfterStartupCommand cmd);
    SmartCardVerifyResponse smartCardVerify(SmartCardVerifyCommand cmd);
    GenerateSmartCardCodeResponse generateSmartCardCode(
            GenerateSmartCardCodeCommand cmd);

    /**
     * 根据userId来查询用户信息的方法
     * @param cmd
     * @return
     */
    UserDTO findUserInfoByUserId(UserCouponsCommand cmd);


    /**
     * 查询超级管理员
     * @param cmd
     * @return
     */
    UserDTO getTopAdministrator( GetTopAdministratorCommand cmd);

    void updateUserVipLevel(Long userId, Integer vipLevel);
	SmartCardVerifyResponse smartCardBarcodeVerify(SmartCardVerifyCommand cmd);
}
