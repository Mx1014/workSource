// @formatter:off
package com.everhomes.user;

import com.everhomes.activity.ActivityService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.border.Border;
import com.everhomes.border.BorderProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.WebRequestInterceptor;
import com.everhomes.device.DeviceProvider;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.oauth2.OAuth2Service;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.messaging.BlockingEventCommand;
import com.everhomes.rest.messaging.GetSercetKeyForScanCommand;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.oauth2.AuthorizationCommand;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.rest.openapi.UserCouponsCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.scene.SceneTypeInfoDTO;
import com.everhomes.rest.ui.user.BindPhoneCommand;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.ui.user.VerificationCodeForBindPhoneCommand;
import com.everhomes.rest.ui.user.VerificationCodeForBindPhoneResponse;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.sdk.LogonInfoCommand;
import com.everhomes.rest.user.sdk.UserLogonInfo;
import com.everhomes.scene.SceneService;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User management API controller
 *
 * @author Kelven Yang
 *<p>注册流程：</p>
 *<ol>
 *<li>调用{@link #signup(SignupCommand) signup}</li>
 *<li>调用{@link #verifyAndLogon(VerifyAndLogonCommand, HttpServletResponse) verifyAndLogon} 或者 {@link #verifyAndLogonByIdentifier(VerifyAndLogonByIdentifierCommand, HttpServletResponse) verifyAndLogonByIdentifier}</li>
 *<li>注册流程完成</li>
 *</ol>
 *<p>修改密码:</p>
 *<ol>
 *<li>登录{@link #logon(LogonCommand, HttpServletResponse) logon}</li>
 *<li>调用{@link #setpassword(SetPasswordCommand) setPassword}</li>
 *</ol>
 *<p>忘记密码:</p>
 *<ol>
 *<li>调用{@link #resendByVerifyCodeByIdentifier(ResendVerificationCodeByIdentifierCommand) resendByVerifyCodeByIdentifier}</li>
 *<li>调用{@link #verfiyAndResetPassword(VerifyAndResetPasswordCommand) verfiyAndResetPassword}</li>
 *</ol>
 *
 * <ul>
 * <li>初始化流程
 * <ul>
 * <li>登录 /user/login
 * <ul>
 * <li>返回borderList，随机选择一个border</li>
 * </ul></li>
 * <li>注册回调 registerMessageHandler</li>
 * <li>websocket 连接 border server
 * <ul>
 * <li>连接成功之后，在onOpen事件产生之后，通过register帧注册loginToken</li>
 * <li>注册成功之后返回registedOk，得到registedOk帧之后，可通过AppIdStatusCommand帧查appId列表，确认是否有新消息</li>
 * </ul></li>
 * </ul></li>
 * <li>消息发送流程
 * <ul>
 * <li>/user/sendMessage 参考 {@link com.everhomes.user.UserController} </li>
 * <li>指定 MessageChannel 发送给用户或组。参考 {@link com.everhomes.rest.messaging.MessageChannel} </li>
 * <li>消息最终会给客户端一个 @ StoredMessageIndicationPdu 消息</li>
 * <li>得到消息之后再通过 fetchPastToRecentMessages 参考 {@link com.everhomes.user.UserController} </li>
 * </ul></li>
 * <li>/user/fetchPastToRecentMessages
 * <ul>
 * <li>获取消息</li>
 * <li>将已获取的消息位置传给服务器，设置removeOld标识，服务器会自动把旧的消息删除</li>
 * </ul></li>
 * <li>推送的消息
 * <ul>
 * <li>设置消息为推送类型，消息会自动推送到用户登录的设备</li>
 * <li>推送的消息会有辅助的消息传给应用程序，并唤醒用户程序</li>
 * </ul></li>
 * <li>U2U消息流程
 * <ul>
 * <li>上下文信息设置为空</li>
 * <li>客户端基于发送者自己与接收者确定一个Session，并把Session保存于客户端本地</li>
 * <li>Channels设置接收方Token，发送方Token</li>
 * <li>接收方根据Channels，senderUid生成新的一个唯一的Session</li>
 * <li>总体说，此Session由发送方，接收方信息确定，从而保证客户端本地能标识成同一个Session</li>
 * </ul></li>
 * <li>组内消息流程
 * <ul>
 * <li>上下文信息设置为空</li>
 * <li>客户端根据组信息生成一个Session，保存于客户端</li>
 * <li>Channels设置组的Token</li>
 * <li>接收方根据Channels的组信息唯一标识一个Session</li>
 * </ul></li>
 * <li>U2G消息流程
 * <ul>
 * <li>当一个用户A发给一个家庭B信息的时候，用到U2G的情况</li>
 * <li>当A发送信息的时候，上下文信息设置为A</li>
 * <li>当家庭成员回复消息时，上下文信息设置为B家庭的信息</li>
 * <li>Channels设置为组Token，以及A用户的Token</li>
 * <li>当A发起会话时，要创建一个U2G的Session，使用A信息与家庭B信息唯一标识一个Session</li>
 * <li>当家庭B里面的用户收到消息时，同样根据A信息与B家庭信息唯一标识一个Session</li>
 * </ul></li>
 * <li>G2G消息流程
 * <ul>
 * <li>当两家庭对话时，使用此情况。与U2G情况类似</li>
 * <li>A家庭发消息，上下文信息设置为A；反之B发消息，设置为B</li>
 * <li>Channels设置为组A，组B</li>
 * <li>当B收到这样的消息时，要创建一个由A与B信息组成的Session，并将Session保存于本地。</li>
 * </ul></li>
 * <li>聊天模块化设计参考
 * <ul>
 * <li>为了降低上层用户使用消息接口的难度，大致抽象为两个层次。尽供参考。</li>
 * <li>Interface层
 * <ul>
 * <li>ClientMessage 对MessageDTO的封装。用户只需要关心是发给哪个用户或哪个组，不需要关心Message内部的Channels等信息</li>
 * <li>SessionProvider 会话管理。管理通信过程中产生的会话。</li>
 * </ul></li>
 * <li>Implement层
 * <ul>
 * <li>MessageStore 消息存储管理</li>
 * <li>Client 用于http post/get 请求</li>
 * <li>SessionProviderImpl 管理会话
 * <ul>
 * <li>用户可以创建U2U U2G G2G等类型的会话</li>
 * <li>用户可以从接收到的消息里创建出相应的会话</li>
 * <li>通过ClientMessage发消息</li>
 * </ul></li>
 * </ul></li>
 * </ul></li>
 * <li>聊天消息格式约定 v0.1
 * <ul>
 * <li>metaAppId用于标识meta数据，可以认为是meta的类型标识</li>
 * <li>聊天消息确定的metaAppId为 META_CHAT （反正是一个宏）</li>
 * <li>messageId由客户端产生，可由senderUid+logonId+generatorId组成一个此Session唯一的ID</li>
 * <li>meta 数组
 * <ul>
 * <li>messageId: senderUid+logonId+generatorId 的字符串</li>
 * <li>contentType: text image/gif image/jpg audio/mp3 audio/m4a vidoe/wmv 等字符串</li>
 * <li>title: 标识会话产生的原因，比如“来自读书圈的消息”</li>
 * <li>objectId: 如果是图片等 资源数据，此处存uri信息。如果是文件聊天，可无</li>
 * <li>more : TODO</li>
 * </ul></li>
 * </ul></li>
 * </ul>
 * @author Kelven Yang
 */
@RestDoc(value="User controller", site="messaging")
@RestController
@RequestMapping("/user")
public class UserController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private static final String BIZ_BACK_URL = "biz.back.url";

	@Autowired
	private UserService userService;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private BorderProvider borderProvider;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private OAuth2Service oAuth2Service;

	@Autowired
	private AppProvider appProvider;

	//Just for test
	@Autowired
	private DeviceProvider deviceProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private SceneService sceneService;
	
	@Autowired
    private ActivityService activityService;

	public UserController() {
	}
	/**
	 * <b>URL: /user/signup</b>
	 * <p>注册</p>
	 * @return 注册临时token
	 */
	@RequestMapping("signup")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse signup(@Valid SignupCommand cmd, HttpServletRequest request) {
		SignupToken token = userService.signup(cmd, request);
		return new RestResponse(WebTokenGenerator.getInstance().toWebToken(token));
	}
	
    /**
     * <b>URL: /user/signupByAppKey</b>
     * <p>注册，由于注册接口配有@RequireAuthentication(false)，也就是不需要登录即可以调用，这导致会有人攻击服务器而不停消耗短信。
     *        为了防止被攻击，假定只有APP才能够注册发验证码（若WEB需要发验证码，需要添加额外的图片验证码来防止机器攻击。由于老版本
     *        仍然在使用，故老版本还是使用老接口而不能直接改老接口，从而新增加一个接口去掉@RequireAuthentication(false)。客户端对所有
     *        接口都加上签名，而把需要发短信的两个接口都换成新的接口。 by lqs 20170626</p>
     * @return 注册临时token
     */
    @RequestMapping("signupByAppKey")
    @RestReturn(String.class)
    public RestResponse signupByAppKey(@Valid SignupCommandByAppKey cmd, HttpServletRequest request) {
        // 手机号或者邮箱，SignupCommandByAppKey拷贝自com.everhomes.rest.user.SignupCommand， 由于原来使用token字段来填手机号，
        // 但token属于特殊字段，会导致Webtoken解释异常，故在新接口把字段名称修改一下，但在service仍然用回原来的command
        // by lqs 20170714
        SignupCommand newCmd = ConvertHelper.convert(cmd, SignupCommand.class);
        newCmd.setToken(cmd.getUserIdentifier());
        
        SignupToken token = userService.signup(newCmd, request);
        return new RestResponse(WebTokenGenerator.getInstance().toWebToken(token));
    }

	@RequestMapping("signupForCodeRequest")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse signupForCodeRequest(@Valid SignupCommandByAppKey cmd, HttpServletRequest request) {
		// 手机号或者邮箱，SignupCommandByAppKey拷贝自com.everhomes.rest.user.SignupCommand， 由于原来使用token字段来填手机号，
		// 但token属于特殊字段，会导致Webtoken解释异常，故在新接口把字段名称修改一下，但在service仍然用回原来的command
		// by lqs 20170714


		// 敢哥说开一个口给创业场用
		if(cmd.getNamespaceId().intValue() != 999964){
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					100000000, "Forbidden");
		}
		SignupCommand newCmd = ConvertHelper.convert(cmd, SignupCommand.class);
		newCmd.setToken(cmd.getUserIdentifier());

		SignupToken token = userService.signup(newCmd, request);
		return new RestResponse(WebTokenGenerator.getInstance().toWebToken(token));
	}

	/**
	 * <b>URL: /user/resendVerificationCode</b>
	 * <p>重新发送验证码</p>
	 * @return 如果正常则返回OK，错误则返回错误信息
	 */
	@RequestMapping("resendVerificationCode")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse resendVerificationCode(@Valid ResendVerificationCodeCommand cmd, HttpServletRequest request) {
		SignupToken token = WebTokenGenerator.getInstance().fromWebToken(cmd.getSignupToken(), SignupToken.class);
		if(token == null) {
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
		}

        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		this.userService.resendVerficationCode(namespaceId, token, cmd.getRegionCode(), request);
		return new RestResponse("OK");
	}

    /**
     * <b>URL: /user/resendVerificationCodeByAppKey</b>
     * <p>重新发送验证码，由于注册接口配有@RequireAuthentication(false)，也就是不需要登录即可以调用，这导致会有人攻击服务器而不停消耗短信。
     *        为了防止被攻击，假定只有APP才能够注册发验证码（若WEB需要发验证码，需要添加额外的图片验证码来防止机器攻击。由于老版本
     *        仍然在使用，故老版本还是使用老接口而不能直接改老接口，从而新增加一个接口去掉@RequireAuthentication(false)。客户端对所有
     *        接口都加上签名，而把需要发短信的两个接口都换成新的接口。 by lqs 20170626</p>
     * @return 如果正常则返回OK，错误则返回错误信息
     */
    @RequestMapping("resendVerificationCodeByAppKey")
    @RequireAuthentication(false)
    @RestReturn(String.class)
    public RestResponse resendVerificationCodeByAppKey(@Valid ResendVerificationCodeCommand cmd, HttpServletRequest request) {
        SignupToken token = WebTokenGenerator.getInstance().fromWebToken(cmd.getSignupToken(), SignupToken.class);
        if(token == null) {
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
        }

        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        this.userService.resendVerficationCode(namespaceId, token, cmd.getRegionCode(), request);
        return new RestResponse("OK");
    }

	/**
	 * <b>URL: /user/verifyAndLogon</b>
	 * <p>验证并登录</p>
	 * @return {@link LogonCommandResponse}
	 */
	@RequestMapping("verifyAndLogon")
	@RequireAuthentication(false)
	@RestReturn(LogonCommandResponse.class)
	public RestResponse verifyAndLogon(@Valid VerifyAndLogonCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		UserLogin login = this.userService.verifyAndLogon(cmd);
		LoginToken loginToken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(loginToken);
		setCookieInResponse("token", tokenString, request, response);
		
		// 当从园区版登录时，有指定的namespaceId，需要对这些用户进行特殊处理
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        if(namespaceId != null) {
            setCookieInResponse("namespace_id", String.valueOf(namespaceId), request, response);
            userService.setDefaultCommunity(login.getUserId(), namespaceId);
        }
        
		LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
		cmdResponse.setAccessPoints(listAllBorderAccessPoints());
		cmdResponse.setContentServer(contentServerService.getContentServer());

		return new RestResponse(cmdResponse);
	}

	/**
	 * <b>URL: /user/setUserDefaultCommunity</b>
	 * <p>设置用户默认园区</p>
	 */
	@RequestMapping("setUserDefaultCommunity")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse setUserDefaultCommunity(@Valid SetUserDefaultCommunityCommand cmd) {

        userService.setDefaultCommunity(cmd.getUserId(), cmd.getNamespaceId());

		return new RestResponse();
	}

    /**
     * <b>URL: /user/setUserDefaultCommunity</b>
     * <p>设置微信用户默认园区</p>
     */
    @RequestMapping("setUserDefaultCommunityForWx")
    @RequireAuthentication(false)
    @RestReturn(String.class)
    public RestResponse setUserDefaultCommunityForWx(@Valid SetUserDefaultCommunityCommand cmd) {

        userService.setDefaultCommunityForWx(cmd.getUserId(), cmd.getNamespaceId());

        return new RestResponse();
    }

    /**
     * <b>URL: /user/updateUserCurrentCommunityToProfile</b>
     * <p>设置微信用户默认园区</p>
     */
    @RequestMapping("updateUserCurrentCommunityToProfile")
    @RequireAuthentication(false)
    @RestReturn(String.class)
    public RestResponse updateUserCurrentCommunityToProfile(@Valid SetUserCurrentCommunityCommand cmd) {

        userService.updateUserCurrentCommunityToProfile(cmd.getUserId(), cmd.getCommunityId(), cmd.getNamespaceId());

        return new RestResponse();
    }
	/**
	 * <b>URL: /user/verifyAndLogonByIdentifier</b>
	 * <p>通过用户标识验证并登录</p>
	 * @return {@link LogonCommandResponse}
	 */
	@RequestMapping("verifyAndLogonByIdentifier")
	@RequireAuthentication(false)
	@RestReturn(LogonCommandResponse.class)
	public RestResponse verifyAndLogonByIdentifier(@Valid VerifyAndLogonByIdentifierCommand cmd, HttpServletRequest request, 
			HttpServletResponse response) {
		UserLogin login = this.userService.verifyAndLogonByIdentifier(cmd);

		LoginToken loginToken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(loginToken);
		setCookieInResponse("token", tokenString, request, response);
        
        // 当从园区版登录时，有指定的namespaceId，需要对这些用户进行特殊处理
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        if(namespaceId != null) {
            setCookieInResponse("namespace_id", String.valueOf(namespaceId), request, response);
            userService.setDefaultCommunity(login.getUserId(), namespaceId);
        }
		
		LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
		cmdResponse.setAccessPoints(listAllBorderAccessPoints());
		cmdResponse.setContentServer(contentServerService.getContentServer());

		return new RestResponse(cmdResponse);
	}

	/**
	 * <b>URL: /user/logon</b>
	 * <p>登录</p>
	 * @return {@link LogonCommandResponse}
	 */
	@RequestMapping("logon")
	@RequireAuthentication(false)
	@RestReturn(LogonCommandResponse.class)
	public RestResponse logon(@Valid LogonCommand cmd, HttpServletRequest request, HttpServletResponse response) {
	    long startTime = System.currentTimeMillis();
	    long loginStartTime = System.currentTimeMillis();

        int regionCode = cmd.getRegionCode() != null ? cmd.getRegionCode() : 86;
        UserLogin login = this.userService.logon(cmd.getNamespaceId() == null ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId(),
                regionCode, cmd.getUserIdentifier(), cmd.getPassword(), cmd.getDeviceIdentifier(), cmd.getPusherIdentify());
		long loginEndTime = System.currentTimeMillis();
		
		LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

		LOGGER.debug(String.format("Return login info. token: %s, login info: %s", tokenString, StringHelper.toJsonString(login)));
		setCookieInResponse("token", tokenString, request, response);
        
        // 当从园区版登录时，有指定的namespaceId，需要对这些用户进行特殊处理
        long partnerStartTime = System.currentTimeMillis();
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        if(namespaceId != null) {
            setCookieInResponse("namespace_id", String.valueOf(namespaceId), request, response);
            userService.setDefaultCommunity(login.getUserId(), namespaceId);
        }
        long partnerEndTime = System.currentTimeMillis();

		LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
		cmdResponse.setAccessPoints(listAllBorderAccessPoints());
		cmdResponse.setContentServer(contentServerService.getContentServer());
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Logon success, elapse=" + (endTime - startTime) + ", loginElapse=" + (loginEndTime - loginStartTime)
                + ", partnerElapse=" + (partnerEndTime - partnerStartTime) + ", token=" + token);
        }
        
		return new RestResponse(cmdResponse);
	}

	/**
	 * <b>URL: /user/logonByToken</b>
	 * <p>通过logoinToken登录</p>
	 * @return {@link LogonCommandResponse}
	 */
	@RequestMapping("logonByToken")
	@RequireAuthentication(false)
	@RestReturn(LogonCommandResponse.class)
	public RestResponse logonByToken(@Valid LogonByTokenCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		LoginToken loginToken = WebTokenGenerator.getInstance().fromWebToken(cmd.getLoginToken(), LoginToken.class);
		if(loginToken == null)
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_LOGIN_TOKEN, "Invalid login token");

		if(cmd.getNamespaceId() != null) {
		    UserContext.current().setNamespaceId(cmd.getNamespaceId());    
		}
		
		UserLogin login = this.userService.logonByToken(loginToken);
		LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(token);
		setCookieInResponse("token", tokenString, request, response);

		LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
		cmdResponse.setAccessPoints(listAllBorderAccessPoints());
		cmdResponse.setContentServer(contentServerService.getContentServer());
		return new RestResponse(cmdResponse);
	}
	/**
	 * <b>URL: /user/setPassword</b>
	 * <p>设置密码</p>
	 * @return 如果成功返回OK
	 * @throws Exception
	 */
	@RequestMapping("setPassword")
	@RestReturn(String.class)
	public RestResponse setPassword(@Valid SetPasswordCommand cmd) throws Exception {
		User user = UserContext.current().getUser();

		boolean allowChange = false;
		String salt="";
		if(cmd.getOldPassword() == null && user.getPasswordHash() == null) {
			allowChange = true;
			salt = EncryptionUtils.createRandomSalt();
		} else if(cmd.getOldPassword() != null && user.getPasswordHash() != null) {
			allowChange = EncryptionUtils.validateHashPassword(cmd.getOldPassword(), user.getSalt(), user.getPasswordHash());
			salt = user.getSalt();
		}

		if(allowChange) {
			user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getNewPassword(),salt)));
			user.setSalt(salt);
			this.userProvider.updateUser(user);
			return new RestResponse("OK");
		}

		throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_OLD_PASSWORD_NOT_MATCH, "Old password does not match");
	}

	/**
	 * <b>URL: /user/listUserIdentifiers</b>
	 * <p>查询所有用户登录标识</p>
	 * @return {@link UserIdentifierDTO}
	 */
	@RequestMapping("listUserIdentifiers")
	@RestReturn(value=UserIdentifierDTO.class, collection = true)
	public RestResponse listUserIdentifiers() throws Exception {
		List<UserIdentifierDTO> identifiers = this.userService.listUserIdentifiers();

		return new RestResponse(identifiers);
	}

	/**
	 * <b>URL: /user/deleteUserIdentifier</b>
	 * <p>删除用户标识</p>
	 * @param cmd 用户标识ID<手机号或者邮箱>
	 * @return
	 */
	@RequestMapping("deleteUserIdentifier")
	@RestReturn(value=String.class)
	public RestResponse deleteUserIdentifier(@Valid DeleteUserIdentifierCommand cmd) {

		this.userService.deleteUserIdentifier(cmd.getUserIdentifierId());
		return new RestResponse("OK");
	}

	/**
	 * <b>URL: /user/getUserInfo</b>
	 * <p>查询用户信息</p>
	 * @return {@link UserInfo}
	 */
	@RequestMapping("getUserInfo")
	@RestReturn(UserInfo.class)
	public RestResponse getUserInfo(GetUserSnapshotInfoCommand cmd) {
        UserInfo info;
        if (cmd.getUid() != null) {
            info = this.userService.getUserSnapshotInfoWithPhone(cmd.getUid());
        } else {
            info = this.userService.getUserInfo();
        }
		return new RestResponse(info);
	}

	/**
	 * <b>URL: /user/validateToken</b>
	 * <p>校验用户的登录 token</p>
	 */
	@RequestMapping("validateToken")
	@RestReturn(UserInfo.class)
	public RestResponse validateToken(ValidateUserTokenCommand cmd, HttpServletRequest request) {
        LoginToken loginToken = userService.getLoginToken(request);
        if (userService.isValid(loginToken)) {
            return new RestResponse(userService.getUserSnapshotInfoWithPhone(loginToken.getUserId()));
        }
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.ERROR_ACCESS_DENIED);
        response.setErrorDescription("invalid token");
        return response;
	}

	/**
	 * <b>URL: /user/setUserInfo</b>
	 * <p>设置用户信息</p>
	 * @return OK
	 */
	@RequestMapping("setUserInfo")
	@RestReturn(String.class)
	public RestResponse setUserInfo(@Valid SetUserInfoCommand cmd) throws Exception {
		this.userService.setUserInfo(cmd);

		return new RestResponse("OK");
	}

	/**
	 * <b>URL: /user/setUserAccountInfo</b>
	 * <p>更新用户信息</p>
	 * @return OK
	 */
	@RequestMapping("setUserAccountInfo")
	@RestReturn(String.class)
	public RestResponse setUserAccountInfo(@Valid SetUserAccountInfoCommand cmd) throws Exception {
		this.userService.setUserAccountInfo(cmd);

		return new RestResponse("OK");
	}

	/**
	 * <b>URL: /user/setCurrentCommunity</b>
	 * <p>设置当前小区</p>
	 * @param cmd 小区ID
	 * @return OK
	 */
	@RequestMapping("setCurrentCommunity")
	@RestReturn(CommunityDTO.class)
	public RestResponse setCurrentCommunity(@Valid SetCurrentCommunityCommand cmd) throws Exception {
	    CommunityDTO community = this.userService.setUserCurrentCommunity(cmd.getCommunityId());

	    return new RestResponse(community);
	}

    /**
     * <b>URL: /user/updateUserNotificationSetting</b>
     * <p>设置会话推送免打扰</p>
     */
    @RequestMapping("updateUserNotificationSetting")
    @RestReturn(UserNotificationSettingDTO.class)
    public RestResponse updateUserNotificationSetting(@Valid UpdateUserNotificationSettingCommand cmd) {
        UserNotificationSettingDTO dto = this.userService.updateUserNotificationSetting(cmd);
        return new RestResponse(dto);
    }

    /**
     * <b>URL: /user/getUserNotificationSetting</b>
     * <p>获取会话推送免打扰设置</p>
     */
    @RequestMapping("getUserNotificationSetting")
    @RestReturn(UserNotificationSettingDTO.class)
    public RestResponse getUserNotificationSetting(@Valid GetUserNotificationSettingCommand cmd) {
        UserNotificationSettingDTO dto = this.userService.getUserNotificationSetting(cmd);
        return new RestResponse(dto);
    }

    /**
     * <b>URL: /user/getMessageSessionInfo</b>
     * <p>获取消息会话所需的信息</p>
     */
    @RequestMapping("getMessageSessionInfo")
    @RestReturn(MessageSessionInfoDTO.class)
    public RestResponse getMessageSessionInfo(@Valid GetMessageSessionInfoCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        MessageSessionInfoDTO dto = this.userService.getMessageSessionInfo(cmd);
        String eTag = DigestUtils.md5Hex(dto.toString().getBytes(Charset.forName("UTF-8")));
        if(!EtagHelper.checkHeaderEtagOnly(30, eTag, request, response)) {
            return null;
        }
        return new RestResponse(dto);
    }

	/**
	 * <b>URL: /user/sendMessage</b>
	 * <p>发送消息</p>
	 */
	@RequestMapping("sendMessage")
	@RestReturn(Long.class)
	public RestResponse sendMessage(@Valid SendMessageCommand cmd) {
		if(cmd.getChannels() == null || cmd.getChannels().size() == 0) {
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_MISSING_MESSAGE_CHANNEL, "Missing message target channel");
		}

		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserBlacklistAuthority(cmd.getSenderUid(), "", 0L, PrivilegeConstants.BLACKLIST_SEND_MESSAGE);

		MessageChannel mainChannel = cmd.getChannels().get(0);
		MessageDTO message = new MessageDTO();
		message.setAppId(cmd.getAppId() != null ? cmd.getAppId() : AppConstants.APPID_MESSAGING);
		message.setSenderUid(cmd.getSenderUid());
		message.setChannels(mainChannel);
		message.setContextType(cmd.getContextType());
		message.setContextToken(cmd.getContextToken());
		message.setMeta(cmd.getMeta());
		message.setBody(cmd.getBody());
		message.setSenderTag(cmd.getSenderTag());

		if(null != cmd.getMeta()) {
			String metaAppId = cmd.getMeta().get("metaAppId");
			if(metaAppId != null) {
				message.setMetaAppId(NumberUtils.toLong(metaAppId, 0l));
			} else {
				message.setMetaAppId(0l);  
			}    
		}

		message.setBodyType(cmd.getBodyType());
		//        if(null != cmd.getBodyType()) {
		//            message.getMeta().put("bodyType", cmd.getBodyType());    
		//            }

		long senderBoxSequence = this.userService.getNextStoreSequence(UserContext.current().getLogin(),
				UserContext.current().getLogin().getNamespaceId(), message.getAppId());

		// cmd.getChannels().forEach((channel) -> {
			messagingService.routeMessage(UserContext.current().getLogin(),
					cmd.getAppId() != null ? cmd.getAppId() : App.APPID_MESSAGING,
							mainChannel.getChannelType(), mainChannel.getChannelToken(), message,
							cmd.getDeliveryOption() != null ? cmd.getDeliveryOption() : 0);
		// });

		return new RestResponse(senderBoxSequence);
	}

	/**
	 * <b>URL: /user/fetchPastToRecentMessages</b>
	 * <p>获取过去到现在的消息</p>
	 */
	@RequestMapping("fetchPastToRecentMessages")
	@RestReturn(FetchMessageCommandResponse.class)
	public RestResponse fetchPastToRecentMessages(@Valid FetchPastToRecentMessageCommand cmd) {
	    RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
	    try {
	        long startTime = System.currentTimeMillis();
	        FetchMessageCommandResponse cmdResponse = this.messagingService.fetchPastToRecentMessages(cmd);
	        long endTime = System.currentTimeMillis();
	        LOGGER.info("fetchPastToRecentMessages took=" + (endTime - startTime) + " milliseconds");
	        response.setResponseObject(cmdResponse);
	        return response;
	    } catch(Exception ex) {
	        LOGGER.error("fetchPastToRecentMessages error:", ex);
	    }
	    return response;
	}

	/**
	 * <b>URL: /user/fetchRecentToPastMessages</b>
	 * <p>获取现在到过去的消息</p>
	 */
	@RequestMapping("fetchRecentToPastMessages")
	@RestReturn(FetchMessageCommandResponse.class)
	public RestResponse fetchRecentToPastMessages(@Valid FetchRecentToPastMessageCommand cmd) {
		FetchMessageCommandResponse cmdResponse = this.messagingService.fetchRecentToPastMessages(cmd);

		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /user/logoff</b>
	 * <p>登出</p>
	 * @return OK
	 */
	@RequestMapping("logoff")
	@RestReturn(String.class)
	public RestResponse logoff(HttpServletRequest request, HttpServletResponse response) {
		UserLogin login = UserContext.current().getLogin();
		this.userService.logoff(login);

		setCookieInResponse("token", "", request, response);
		return new RestResponse("OK");
	}

	/**
	 * <b>URL: /user/assumePortalRole</b>
	 * <p>设置角色权限</p>
	 * @return OK
	 */
	@RequestMapping("assumePortalRole")
	@RestReturn(String.class)
	public RestResponse assumePortalRole(@Valid AssumePortalRoleCommand cmd) {

		this.userService.assumePortalRole(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /user/listBorderAndContent</b>
     * <p>获取borderServer地址</p>
     * @return OK
     */
    @RequestMapping("listBorderAndContent")
    @RestReturn(ListBorderAndContentResponse.class)
    public RestResponse listBorderAndContent() {

        List<String> strings = this.listAllBorderAccessPoints();
        ListBorderAndContentResponse res = new ListBorderAndContentResponse();
        res.setBorders(strings);
        res.setContentUrl(contentServerService.getContentServer());
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	private List<String> listAllBorderAccessPoints() {
		List<Border> borders = this.borderProvider.listAllBorders();
		return borders.stream().map((Border border) -> {
			return String.format("%s:%d", border.getPublicAddress(), border.getPublicPort());
		}).collect(Collectors.toList());
	}

	// 转移到ContentServerServiceImpl，使得其它模块也可以调用 by lqs 20160923
//	private String getContentServer(){
//		try {
//			ContentServer server = contentServerService.selectContentServer();
//			return String.format("%s:%d",server.getPublicAddress(),server.getPublicPort());
//		} catch (Exception e) {
//			LOGGER.error("cannot find content server",e);
//			return null;
//		}
//	}

	/**
	 * <b>URL: /user/resendVerificationCodeByIdentifier</b>
	 * <p>忘记密码</p>
	 * @return OK
	 */
	@RequestMapping("resendVerificationCodeByIdentifier")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse resendByVerifyCodeByIdentifier(@Valid ResendVerificationCodeByIdentifierCommand cmd, HttpServletRequest request){
		assert StringUtils.isNotEmpty(cmd.getIdentifier());

		userService.resendVerficationCode(cmd, request);
		return new RestResponse("OK");
	}

    /**
     * <b>URL: /user/resendVerificationCodeByIdentifierAndAppKey</b>
     * <p>忘记密码，由于注册接口配有@RequireAuthentication(false)，也就是不需要登录即可以调用，这导致会有人攻击服务器而不停消耗短信。
     *        为了防止被攻击，假定只有APP才能够注册发验证码（若WEB需要发验证码，需要添加额外的图片验证码来防止机器攻击。由于老版本
     *        仍然在使用，故老版本还是使用老接口而不能直接改老接口，从而新增加一个接口去掉@RequireAuthentication(false)。客户端对所有
     *        接口都加上签名，而把需要发短信的两个接口都换成新的接口。 by lqs 20170626</p>
     * @return OK
     */
    @RequestMapping("resendVerificationCodeByIdentifierAndAppKey")
    @RestReturn(String.class)
    public RestResponse resendVerificationCodeByIdentifierAndAppKey(@Valid ResendVerificationCodeByIdentifierCommand cmd, HttpServletRequest request){
        assert StringUtils.isNotEmpty(cmd.getIdentifier());

        userService.resendVerficationCode(cmd, request);
        return new RestResponse("OK");
    }


	/**
	 * <b>URL: /user/resendVerificationCodeByIdentifierAndAppKey</b>
	 * <p></p>
	 * @return OK
	 */
	@RequestMapping("sendCodeWithPictureValidate")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse sendCodeWithPictureValidate(SendCodeWithPictureValidateCommand cmd, HttpServletRequest request){
		assert StringUtils.isNotEmpty(cmd.getIdentifier());

		userService.sendCodeWithPictureValidate(cmd, request);
		return new RestResponse("OK");
	}

	/**
	 * <b>URL: /user/sendCodeWithPictureValidateByApp</b>
	 * <p></p>
	 * @return OK
	 */
	@RequestMapping("sendCodeWithPictureValidateByApp")
	@RestReturn(String.class)
	public RestResponse sendCodeWithPictureValidateByApp(SendCodeWithPictureValidateCommand cmd, HttpServletRequest request){
		assert StringUtils.isNotEmpty(cmd.getIdentifier());

		userService.sendCodeWithPictureValidateByApp(cmd, request);
		return new RestResponse("OK");
	}



	/**
	 * <b>URL: /user/sendVerificationCodeByResetIdentifier</b>
	 * <p>发送修改手机号的短信验证码</p>
	 */
	@RequestMapping("sendVerificationCodeByResetIdentifier")
	@RestReturn(String.class)
	public RestResponse sendVerificationCodeByResetIdentifier(@Valid SendVerificationCodeByResetIdentifierCommand cmd, HttpServletRequest request){
		userService.sendVerificationCodeByResetIdentifier(cmd, request);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

	/**
	 * <b>URL: /user/verifyResetIdentifierCode</b>
	 * <p>核实修改手机号的短信验证码</p>
	 */
	@RequestMapping("verifyResetIdentifierCode")
	@RestReturn(String.class)
	public RestResponse verifyResetIdentifierCode(@Valid VerifyResetIdentifierCodeCommand cmd){
		userService.verifyResetIdentifierCode(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

	/**
	 * <b>URL: /user/listResetIdentifierCode</b>
	 * <p>获取修改手机号的短信验证码</p>
	 */
	@RequestMapping("listResetIdentifierCode")
	@RestReturn(value = UserIdentifierLogDTO.class)
	public RestResponse listResetIdentifierCode(@Valid ListResetIdentifierCodeCommand cmd){
		UserIdentifierLogDTO log = userService.listResetIdentifierCode(cmd);
        RestResponse response = new RestResponse(log);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

	/**
	 * <b>URL: /user/createResetIdentifierAppeal</b>
	 * <p>申诉修改手机号</p>
	 */
	@RequestMapping("createResetIdentifierAppeal")
	@RestReturn(UserAppealLogDTO.class)
	public RestResponse createResetIdentifierAppeal(@Valid CreateResetIdentifierAppealCommand cmd){
        UserAppealLogDTO dto = userService.createResetIdentifierAppeal(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /user/verfiyAndReset</b>
	 * <p>忘记密码，重新设置</p>
	 * @return  OK
	 */
	@RequestMapping(value = "verfiyAndResetPassword")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse verfiyAndResetPassword(@Valid VerifyAndResetPasswordCommand cmd) {
		assert StringUtils.isNotEmpty(cmd.getVerifyCode());
		assert StringUtils.isNotEmpty(cmd.getNewPassword());
		assert StringUtils.isNotEmpty(cmd.getIdentifierToken());
		UserIdentifier identifier = userProvider.findIdentifierByVerifyCode(cmd.getVerifyCode(),
				cmd.getIdentifierToken());
		if (identifier == null) {
			LOGGER.error("invalid operation,can not find verify information");

			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE,
					"invalid params");

		}
		// check the expire time
		if (DateHelper.currentGMTTime().getTime() - identifier.getNotifyTime().getTime() > 10 * 60000) {
			LOGGER.error("the verifycode is invalid with timeout");
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_INVALD_TOKEN_STATUS, "Invalid token status");
		}
		// find user by uid
		User user = userProvider.findUserById(identifier.getOwnerUid());
		user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getNewPassword(), user.getSalt())));
		userProvider.updateUser(user);
		return new RestResponse("OK");
	}

	@RequestMapping(value = "appIdStatus")
	@RestReturn(AppIdStatusResponse.class)
	public RestResponse getAppIdStatus(@Valid AppIdStatusCommand cmd) {
		AppIdStatusResponse cmdResponse = new AppIdStatusResponse();
		
		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		if(UserContext.current().getLogin() == null) {
		    //check user first
		    return response;
		}

		try {
			for(Long appId : cmd.getAppIds()) {
				FetchPastToRecentMessageCommand messageCmd = new FetchPastToRecentMessageCommand();
				messageCmd.setAppId(appId);
				messageCmd.setAnchor(0l);
				messageCmd.setCount(1);
				messageCmd.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
				messageCmd.setRemoveOld((byte)0);
				FetchMessageCommandResponse resp = this.messagingService.fetchPastToRecentMessages(messageCmd);

				if(resp.getMessages().size() > 0) {
					cmdResponse.getAppIds().add(appId);
				}
			}			
		} catch(Exception ex) {
			LOGGER.error("getAppIds error", ex);
		}

		return response;
	}

	/**
	 * 查询家庭成员个人信息
	 * @return {@link UserInfo}
	 */
	@RequestMapping("getFamilyMemberInfo")
	@RestReturn(UserInfo.class)
	public RestResponse getFamilyMemberInfo(GetFamilyMemberInfoCommand cmd){
		return new RestResponse(userService.getUserInfo(cmd.getUid()));
	}

	/**
	 * 查询个人简介
	 * 返回值有：
	 * <ol>
	 * <li>个人头像</li>
	 * <li>个人帐号</li>
	 * <li>个人手机号局部</li>
	 * <li>个人邮箱</li>
	 * <li>个人Id</li>
	 * <li>职业</li>
	 * <li>昵称</li>
	 * </ol>
	 * @return {@link UserInfo}
	 */
	@RequestMapping("getUserSnapshotInfo")
	@RestReturn(UserInfo.class)
	public RestResponse getUserSnapshotInfo(GetUserSnapshotInfoCommand cmd,HttpServletRequest request, HttpServletResponse response){
		UserInfo userInfo = userService.getUserSnapshotInfo(cmd.getUid());
		if(userInfo==null){
			return new RestResponse(userInfo);
		}
		if(EtagHelper.checkHeaderEtagOnly(30, userInfo.hashCode()+"", request, response)){
			return new RestResponse(userInfo);
		}
		return new RestResponse("OK");
	}

	@RequestMapping("oauth2Authorize")
	@RestReturn(String.class)
	public RestResponse oauth2Authorize(@Valid AuthorizationCommand cmd) throws URISyntaxException {
		User user = UserContext.current().getUser();

		if(cmd.getclient_id() == null) {
            throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
                    OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "OAuth2 client id is null");		    
		}
		
		// double check in confirmation call to protect against tampering in confirmation callback
		App app = this.appProvider.findAppByKey(cmd.getclient_id());
		if(app == null) {
			throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
					OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid OAuth2 client id");
		}

		String redirectUri = null;
		try {
			redirectUri = URLDecoder.decode(cmd.getredirect_uri(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			redirectUri = cmd.getredirect_uri();
		}
		if(redirectUri == null || redirectUri.isEmpty()) {
			redirectUri = this.oAuth2Service.getDefaultRedirectUri(app.getId());
		}
		//Fix the redirectUri
		cmd.setredirect_uri(redirectUri);

		if(!this.oAuth2Service.validateRedirectUri(app.getId(), redirectUri)) {
			throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
					OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid OAuth2 client redirect URI");
		}

		LOGGER.info("Confirm OAuth2 authorization: {}", cmd);

		URI uri;
		try {
			uri = oAuth2Service.confirmAuthorization(user, cmd);
		} catch (RuntimeErrorException e) {
			LOGGER.error("Unexpected exception", e);

			switch(e.getErrorCode()) {
			case OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST:
				throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE, OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, 
						"Invalid OAuth2 request");

			default : {
				uri = new URI(String.format("%s#error=%s&state=%s", redirectUri,
						this.oAuth2Service.getOAuth2AuthorizeError(e.getErrorCode()), cmd.getState()));
				break;
			}
			}
		} catch(Exception e) {
			LOGGER.error("Unexpected exception", e);

			uri = new URI(String.format("%s#error=%s&state=%s", redirectUri,
					this.oAuth2Service.getOAuth2AuthorizeError(OAuth2ServiceErrorCode.ERROR_SERVER_ERROR), cmd.getState()));
		}

		return new RestResponse(uri.toString());
	}

	private static Cookie findCookieInRequest(String name, HttpServletRequest request) {
		List<Cookie> matchedCookies = new ArrayList<>();

		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					LOGGER.debug("Found matched cookie with name {} at value: {}, path: {}, version: {}", name,
							cookie.getValue(), cookie.getPath(), cookie.getVersion());
					matchedCookies.add(cookie);
				}
			}
		}

		if(matchedCookies.size() > 0)
			return matchedCookies.get(matchedCookies.size() - 1);
		return null;
	}

	private static void setCookieInResponse(String name, String value, HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = findCookieInRequest(name, request);
		if(cookie == null)
			cookie = new Cookie(name, value);
		else
			cookie.setValue(value);
		cookie.setPath("/");
		if(value == null || value.isEmpty())
			cookie.setMaxAge(0);

		response.addCookie(cookie);
	}

	/**
	 * <b>URL: /user/appAgreements</b>
	 */
	@RequestMapping("appAgreements")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse getAppAgreements(GetAppAgreementCommand cmd){
	    // 之前版本该接口没有参数，为了兼容需要判断cmd是否存在 by lqs 20151210
	    Integer namespaceId = (cmd == null || cmd.getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
		String result = configurationProvider.getValue(namespaceId, ConfigConstants.APP_AGREEMENTS_URL, "");
		if(result == null) {
		    result = configurationProvider.getValue(Namespace.DEFAULT_NAMESPACE, ConfigConstants.APP_AGREEMENTS_URL, "");
		}

		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /user/adminLogon</b>
	 * <p>登录</p>
	 * @return {@link LogonCommandResponse}
	 */
	@RequestMapping("adminLogon")
	@RequireAuthentication(false)
	@RestReturn(LogonCommandResponse.class)
	public RestResponse adminLogon(@Valid LogonCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        int regionCode = cmd.getRegionCode() != null ? cmd.getRegionCode() : 86;
        int namespaceId = cmd.getNamespaceId() == null ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
        UserLogin login = this.userService.logon(namespaceId, regionCode, cmd.getUserIdentifier(),
                cmd.getPassword(), cmd.getDeviceIdentifier(), cmd.getPusherIdentify());

		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(login.getUserId(), 0);

		LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

		LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, StringHelper.toJsonString(login)));
		setCookieInResponse("token", tokenString, request, response);

		LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
		cmdResponse.setAccessPoints(listAllBorderAccessPoints());
		cmdResponse.setContentServer(contentServerService.getContentServer());
		return new RestResponse(cmdResponse);
	}

	/**
	 * <b>URL: /openapi/appServiceAccess</b>
	 * <p>跳进服务市场的简化版本。</p>
	 * <p>传入appKey以及链接等信息，从服务器拿取用户uuid，然后跳转到服务市场对应的链接，并给第三方网站uuid</p>
	 * <p>安全性：远没有OAuth2安全性好</p>
	 * <p>TODO: 通过appKey去校验跳转链接的域名，或者由appKey去拿到app对应的域名</p>
	 */
	@RequestMapping("appServiceAccess")
	@RestReturn(String.class)
	public RestResponse appServiceAccess(@Valid AppServiceAccessCommand cmd) {
		App app = this.appProvider.findAppByKey(cmd.getAppKey());
		if(app == null) {
			throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
					OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid client id");
		}

		//TODO for more security type
		if(!cmd.getType().equals("simple")) {
			throw RuntimeErrorException.errorWith(OAuth2ServiceErrorCode.SCOPE,
					OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST, "Invalid access type");           
		}

		//TODO use uri parser to do better hear.
		String uri = cmd.getUri();
		int i =  uri.indexOf("#");
		if(i > 0) {
			uri = uri.substring(0, i);
		}

		if(uri.indexOf("?") >= 0) {
			return new RestResponse(uri + "&user=" + UserContext.current().getUser().getUuid());    
		} else {
			return new RestResponse(uri + "?user=" + UserContext.current().getUser().getUuid());
		}

	}

	@RequestMapping("findTokenByUserId")
	@RestReturn(String.class)
	public RestResponse findTokenByUserId(@Valid FindTokenByUserIdCommand cmd) {
		if(cmd.getUserId()==null){
			LOGGER.error("userId is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"userId is null.");
		}
		User user = userProvider.findUserById(cmd.getUserId());
		if(user==null){
			LOGGER.error("user not found.userId="+cmd.getUserId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"user not found");
		}
		
		final String bizBackUrl = configurationProvider.getValue(BIZ_BACK_URL, "http://biz.zuolin.com/Zl-MallMgt/bizmgt/main/tokenlogin.ihtml");
		final String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "https://core.zuolin.com");
		final String appKey = configurationProvider.getValue(ConfigConstants.BIZ_APPKEY, "d80e06ca-3766-11e5-b18f-b083fe4e159f");
		App app = appProvider.findAppByKey(appKey);
		if(app==null){
			LOGGER.error("app not found.appKey="+appKey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"app not found");
		}
		
		String uuid=user.getUuid();
		Integer randomNum = (int) (Math.random()*1000);
		Long timestamp = System.currentTimeMillis();
		Map<String,String> params = new HashMap<String, String>();
		params.put("uuid", uuid);
		params.put("appKey", appKey);
		params.put("randomNum", String.valueOf(randomNum));
		params.put("timestamp", String.valueOf(timestamp));
		String signature=null;
		try {
			signature = URLEncoder.encode(SignatureHelper.computeSignature(params, app.getSecretKey()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = String.format("%s?sourceUrl=%s&uuid=%s&appKey=%s&randomNum=%s&timestamp=%s&signature=%s", bizBackUrl,homeUrl,user.getUuid(),appKey,randomNum,timestamp,signature);
	
		RestResponse response =  new RestResponse(url);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	@RequestMapping("getBizSignature")
	@RestReturn(GetSignatureCommandResponse.class)
	public RestResponse getBizSignature(){
		long s = System.currentTimeMillis();
		GetSignatureCommandResponse result = userService.getSignature();
		RestResponse response =  new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		long e = System.currentTimeMillis();
		LOGGER.debug("getBizSignature-elapse="+(e-s));
		return response;
	}
	
    @RequestMapping("listBorders")
    @RestReturn(BorderListResponse.class)
    public RestResponse listBorders(){
        RestResponse response = new RestResponse(userService.listBorders());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * <b>URL: /user/listSceneTypeByOwner</b>
     * <p>根据归属类型获取场景列表</p>
     */
    @RequestMapping("listSceneTypeByOwner")
	@RestReturn(value = SceneTypeInfoDTO.class, collection = true)
	public RestResponse listSceneTypeByOwner(@Valid ListScentTypeByOwnerCommand cmd) {
    	List<SceneTypeInfoDTO> list = sceneService.listSceneTypeByOwner(cmd);
    	RestResponse resp = new RestResponse(list);
    	resp.setErrorCode(ErrorCodes.SUCCESS);
    	resp.setErrorDescription("OK");
	    return resp;
	}
    
    /**
     * <b>URL: /user/requestVideoPermission</b>
     * <p>请求手机号权限</p>
     */
    @RequestMapping("requestVideoPermission")
    @RestReturn(value = UserVideoPermissionDTO.class)
    public RestResponse requestVideoPermission(@Valid RequestVideoPermissionCommand cmd) {
        RestResponse resp = new RestResponse(activityService.requestVideoPermission(cmd));
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /user/getVideoPermission</b>
     * <p>获取用户是否有视频权限</p>
     */
    @RequestMapping("getVideoPermission")
    @RestReturn(value = UserVideoPermissionDTO.class)
    public RestResponse getVideoPermisionByUserId() {
        GetVideoPermissionInfoCommand cmd = new GetVideoPermissionInfoCommand(); 
        RestResponse resp = new RestResponse(activityService.GetVideoPermisionInfo(cmd));
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /user/findInitBizInfo</b>
     * <p>获取初始化电商信息</p>
     */
    @RequestMapping("findInitBizInfo")
    @RestReturn(value = InitBizInfoDTO.class)
    public RestResponse findInitBizInfo() {
    	InitBizInfoDTO response = userService.findInitBizInfo();
        RestResponse resp = new RestResponse(response);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

	/**
	 * <b>URL: /user/checkVerifyCode</b>
	 * <p>忘记密码，核实验证码</p>
	 * @return  OK
	 */
	@RequestMapping(value = "checkVerifyCode")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse checkVerifyCode(@Valid CheckVerifyCodeCommand cmd) {
		assert StringUtils.isNotEmpty(cmd.getVerifyCode());
		assert StringUtils.isNotEmpty(cmd.getIdentifierToken());
		UserIdentifier identifier = userProvider.findIdentifierByVerifyCode(cmd.getVerifyCode(),
				cmd.getIdentifierToken());
		if (null == identifier) {
			LOGGER.error("invalid operation,can not find verify information");

			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE,
					"invalid params");

		}
		// check the expire time
		if (DateHelper.currentGMTTime().getTime() - identifier.getNotifyTime().getTime() > 10 * 60000) {
			LOGGER.error("the verifycode is invalid with timeout");
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_INVALD_TOKEN_STATUS, "Invalid token status");
		}

		return new RestResponse("OK");
	}

	/**
	 * <b>URL: /user/resetPassword</b>
	 * <p>设置密码</p>
	 * @return  OK
	 */
	@RequestMapping(value = "resetPassword")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse resetPassword(@Valid ResetPasswordCommand cmd) {
		assert StringUtils.isNotEmpty(cmd.getNewPassword());
		assert StringUtils.isNotEmpty(cmd.getIdentifierToken());
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getIdentifierToken());
		if (null == identifier) {
			LOGGER.error("identifier token on-existent. token = {}", cmd.getIdentifierToken());
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALD_TOKEN_STATUS,
					"Invalid token status");

		}

		// find user by uid
		User user = userProvider.findUserById(identifier.getOwnerUid());
		user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getNewPassword(), user.getSalt())));
		userProvider.updateUser(user);
		return new RestResponse("OK");
	}

	/**
	 * <b>URL: /user/searchUsers</b>
	 * <p>搜索用户</p>
	 */
	@RequestMapping(value = "searchUsers")
	@RestReturn(value = SearchUsersResponse.class)
	public RestResponse searchUsers(@Valid SearchUsersCommand cmd) {
		RestResponse resp = new RestResponse(userService.searchUsers(cmd));
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /user/findTargetByNameAndAddress</b>
	 * <p>根据个人用户电话，客户名称，合同号，来确认唯一用户，若无法定位到改域空间下指定园区的唯一用户，则返回null</p>
	 */
	@RequestMapping(value = "findTargetByNameAndAddress")
	@RestReturn(value = TargetDTO.class)
	public RestResponse findTargetByNameAndAddress(FindTargetByNameAndAddressCommand cmd) {
		RestResponse resp = new RestResponse(userService.findTargetByNameAndAddress(cmd.getContractNum(),cmd.getTargetName(),cmd.getOwnerId(),cmd.getTel(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getNamespaceId()));
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}
/**
	 * <b>URL: /user/verificationCodeForBindPhone</b>
	 * <p>发送验证码</p>
	 */
	@RequestMapping("verificationCodeForBindPhone")
	@RestReturn(value = VerificationCodeForBindPhoneResponse.class)
	public RestResponse verificationCodeForBindPhone(@Valid VerificationCodeForBindPhoneCommand cmd) {
		VerificationCodeForBindPhoneResponse response = userService.verificationCodeForBindPhone(cmd);
		RestResponse resp = new RestResponse(response);
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}


	/**
	 * <b>URL: /user/bindPhone</b>
	 * <p>验证并登录</p>
	 * @return
	 */
	@RequestMapping("bindPhone")
	@RestReturn(String.class)
	public RestResponse bindPhone(@Valid BindPhoneCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		UserLogin login = this.userService.bindPhone(cmd);
		if(login != null){

			LoginToken loginToken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
			String tokenString = WebTokenGenerator.getInstance().toWebToken(loginToken);
			
			//微信公众号的accessToken过期时间是7200秒，需要设置cookie小于7200。
			//防止用户在coreserver处于登录状态而accessToken已过期，重新登录之后会刷新accessToken   add by yanjun 20170906
			WebRequestInterceptor.setCookieInResponse("token", tokenString, request, response, 7000);

		}


//		LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
//		cmdResponse.setAccessPoints(listAllBorderAccessPoints());
//		cmdResponse.setContentServer(contentServerService.getContentServer());

		RestResponse resp = new RestResponse();
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

    /**
     * <b>URL: /user/verificationCodeForBindPhoneByApp</b>
     * <p>微信APP绑定手机号发送验证码</p>
     */
    @RequestMapping("verificationCodeForBindPhoneByApp")
    @RestReturn(value = String.class)
    public RestResponse verificationCodeForBindPhoneByApp(@Valid VerificationCodeForBindPhoneCommand cmd) {
        userService.verificationCodeForBindPhoneByApp(cmd);
        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }


	/**
	 * <b>URL: /user/bindPhoneByApp</b>
	 * <p>微信APP登录验证并登录</p>
	 * @return
	 */
	@RequestMapping("bindPhoneByApp")
	@RestReturn(LogonCommandResponse.class)
	public RestResponse bindPhoneByApp(@Valid BindPhoneCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		UserLogin login = this.userService.bindPhoneByApp(cmd);
		LogonCommandResponse logonCommandResponse = new LogonCommandResponse();
		if(login != null){

			LoginToken loginToken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
			String tokenString = WebTokenGenerator.getInstance().toWebToken(loginToken);
			logonCommandResponse.setLoginToken(tokenString);
			logonCommandResponse.setUid(login.getUserId());
			//微信公众号的accessToken过期时间是7200秒，需要设置cookie小于7200。
			//防止用户在coreserver处于登录状态而accessToken已过期，重新登录之后会刷新accessToken   add by yanjun 20170906
			WebRequestInterceptor.setCookieInResponse("token", tokenString, request, response, 7000);

		}


		logonCommandResponse.setAccessPoints(listAllBorderAccessPoints());
		logonCommandResponse.setContentServer(contentServerService.getContentServer());

		RestResponse resp = new RestResponse(logonCommandResponse);
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /user/checkVerifyCodeAndResetPassword</b>
	 * <p>校验验证码并重置密码</p>
	 * @return  OK
	 */
	@RequestMapping(value = "checkVerifyCodeAndResetPassword")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse checkVerifyCodeAndResetPassword(@Valid CheckVerifyCodeAndResetPasswordCommand cmd) {
		userService.checkVerifyCodeAndResetPassword(cmd);
		RestResponse resp = new RestResponse();
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /user/checkVerifyCodeAndResetPasswordWithoutIdentifyToken</b>
	 * <p>校验验证码并重置密码（无需手机号码）</p>
	 * @return  OK
	 */
	@RequestMapping(value = "checkVerifyCodeAndResetPasswordWithoutIdentifyToken")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public RestResponse checkVerifyCodeAndResetPasswordWithoutIdentifyToken(@Valid CheckVerifyCodeAndResetPasswordWithoutIdentifyTokenCommand cmd) {
		userService.checkVerifyCodeAndResetPasswordWithoutIdentifyToken(cmd);
		RestResponse resp = new RestResponse();
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /user/checkUserTemporaryToken</b>
	 * 检验用户临时token，标准是：1、是本系统加密的token，2、token未过期
	 * 成功会返回改token内容
	 * @return  OK
	 */
	@RequestMapping(value = "checkUserTemporaryToken")
	@RequireAuthentication(false)
	@RestReturn(UserTemporaryTokenDTO.class)
	public RestResponse checkUserTemporaryToken(@Valid CheckUserTemporaryTokenCommand cmd) {
		UserTemporaryTokenDTO token =  userService.checkUserTemporaryToken(cmd);
		RestResponse resp = new RestResponse(token);
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;

	}
	
	/**
     * <b>URL: /user/systemInfo</b>
     * <p>SystemInfo</p>
     * @return {@link SystemInfoResponse}
     */
    @RequestMapping("systemInfo")
    @RestReturn(SystemInfoResponse.class)
    public RestResponse systemInfo(@Valid SystemInfoCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        SystemInfoResponse obj = userService.updateUserBySystemInfo(cmd, request, response);
        RestResponse resp = new RestResponse(obj);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp; 
    }

    /**
     * <b>URL: /user/getUserConfigAfterStartup</b>
     * <p>启动之后，去统一的接口拿用户相关的配置信息</p>
     * @return {@link GetUserConfigAfterStartupResponse}
     */
    @RequestMapping("getUserConfigAfterStartup")
    @RestReturn(GetUserConfigAfterStartupResponse.class)
    public RestResponse getUserConfigAfterStartup(GetUserConfigAfterStartupCommand cmd) {
        GetUserConfigAfterStartupResponse obj = userService.getUserConfigAfterStartup(cmd);
        RestResponse resp = new RestResponse(obj);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /user/smartCardVerify</b>
     * <p>校验用户的 TOTP 是否合法 </p>
     * @return {@link SmartCardVerifyResponse}
     */
    @RequestMapping("smartCardVerify")
    @RequireAuthentication(false)
    @RestReturn(SmartCardVerifyResponse.class)
    public RestResponse smartCardVerify(SmartCardVerifyCommand cmd) {
        SmartCardVerifyResponse obj = userService.smartCardVerify(cmd);
        RestResponse resp = new RestResponse(obj);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /user/smartCardVerify</b>
     * <p>校验用户的 TOTP 是否合法 </p>
     * @return {@link SmartCardVerifyResponse}
     */
    @RequestMapping("smartCardBarcodeVerify")
    @RequireAuthentication(false)
    @RestReturn(SmartCardVerifyResponse.class)
    public RestResponse smartCardBarcodeVerify(SmartCardVerifyCommand cmd) {
        SmartCardVerifyResponse obj = userService.smartCardBarcodeVerify(cmd);
        RestResponse resp = new RestResponse(obj);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /user/generateSmartCardCode</b>
     * <p>测试 TOTP 的生成结果</p>
     * @return {@link GenerateSmartCardCodeResponse}
     */
    @RequestMapping("generateSmartCardCode")
    @RestReturn(GenerateSmartCardCodeResponse.class)
    public RestResponse generateOneCardCode(GenerateSmartCardCodeCommand cmd) {
        GenerateSmartCardCodeResponse obj = userService.generateSmartCardCode(cmd);
        RestResponse resp = new RestResponse(obj);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /user/genAppKey</b>
     * <p>test</p>
     * @return
     */
    @RequestMapping("genAppKey")
    @RestReturn(String.class)
    public RestResponse genAppKey() {
        String s = UUID.randomUUID().toString() + " " + SignatureHelper.generateSecretKey();
        RestResponse resp = new RestResponse(s);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp; 
    }

	/**
	 * <b>URL: /user/querySubjectIdForScan</b>
	 * <p>test</p>
	 * @return
	 */
	@RequestMapping("querySubjectIdForScan")
	@RequireAuthentication(false)
	@RestReturn(QRCodeDTO.class)
	public RestResponse querySubjectIdForScan() {
		RestResponse resp = new RestResponse(userService.querySubjectIdForScan());
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /user/waitScanForLogon</b>
	 * <p>test</p>
	 * @return
	 */
	@RequestMapping("waitScanForLogon")
	@RequireAuthentication(false)
	@RestReturn(String.class)
	public DeferredResult<RestResponse> waitScanForLogon(BlockingEventCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		return userService.waitScanForLogon(cmd.getSubjectId(), request, response);
	}


	/**
	 * <b>URL: /user/getSercetKeyForScan</b>
	 * <p>test</p>
	 * @return
	 */
	@RequestMapping("getSercetKeyForScan")
	@RestReturn(String.class)
	public RestResponse getSercetKeyForScan(GetSercetKeyForScanCommand cmd) {
		RestResponse resp = new RestResponse(userService.getSercetKeyForScan(cmd.getArgs()));
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /user/logonByScan</b>
	 * <p>test</p>
	 * @return
	 */
	@RequestMapping("logonByScan")
	@RestReturn(String.class)
	public RestResponse logonByScan(BlockingEventCommand cmd) {
		userService.logonByScan(cmd.getSubjectId(), cmd.getMessage());
		RestResponse resp = new RestResponse();
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}


	/**
	 * <b>URL: /user/listUserRelatedCards</b>
	 * <p>test</p>
	 * @return
	 */
	@RequestMapping("listUserRelatedCards")
	@RestReturn(String.class)
	public RestResponse listUserRelatedCards() {
		RestResponse resp = new RestResponse(userService.listUserRelatedCards());
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /user/getUserByPhone</b>
	 * <p>通过域空间和手机号查询用户信息</p>
	 * @return
	 */
	@RequestMapping("getUserByPhone")
	@RestReturn(UserDTO.class)
	public RestResponse getUserByPhone(FindUserByPhoneCommand cmd){

		RestResponse resp = new RestResponse(userService.getUserFromPhone(cmd));
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;

    }

	/**
	 * <b>URL: /user/sendVerificationCodeByPhone</b>
	 * <p>给目标手机发送验证码接口</p>
	 * @return
	 */
	@RequestMapping("sendVerificationCodeByPhone")
	@RestReturn(String.class)
	public RestResponse sendVerificationCodeByPhone(SendVerificationCodeByPhoneCommand cmd ,HttpServletRequest request){
		LOGGER.info("sendVerificationCodeByPhone  -->  cmd:[{}]",cmd);
		FindUserByPhoneCommand cmd1 = ConvertHelper.convert(cmd, FindUserByPhoneCommand.class);
		UserDTO  user = this.userService.getUserFromPhone(cmd1);
		if(user == null) {
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_USER_NOT_EXIST, "user not exist");
		}


		this.userService.sendVerficationCode4Point(cmd.getNamespaceId(), user, cmd.getRegionCode(), request);
		return new RestResponse("OK");

    }


	/**
	 * <b>URL: /user/pointCheckVerificationCode</b>
	 * <p>积分校验验证码接口</p>
	 * @return
	 */
	@RequestMapping("pointCheckVerificationCode")
	@RestReturn(PointCheckVCDTO.class)
	public RestResponse pointCheckVerificationCode(PointCheckVerificationCodeCommand cmd){

		PointCheckVCDTO dto = this.userService.pointCheckVerificationCode(cmd);
		RestResponse resp = new RestResponse(dto);
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;

    }


	/**
	 * <b>URL: /user/listUserAddress</b>
	 * <p>根据用户域空间场景获取用户相关的地址列表</p>
	 */
	@RequestMapping("listUserAddress")
	@RestReturn(value=ListAddressUsersResponse.class)
	public RestResponse listAddressUsers(ListAddressUsersCommand cmd) {
		//List<UserServiceAddressDTO> result = this.userActivityService.getUserRelateServiceAddress();
		ListAddressUsersResponse res = userService.listAddressUsers(cmd);

		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /user/isUserAuth</b>
	 * <p>判断用户是否为认证用户</p>
	 * @return
	 */
	@RequestMapping("isUserAuth")
	@RestReturn(String.class)
	public RestResponse isUserAuth() {
		RestResponse resp = new RestResponse(userService.isUserAuth());
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}


	/**
	 * <b>URL: /user/getTopAdministrator</b>
	 * <p>查询当前企业的超管</p>
	 * @return
	 */
	@RequestMapping("getTopAdministrator")
	@RestReturn(UserDTO.class)
	public RestResponse getTopAdministrator( GetTopAdministratorCommand cmd) {
		RestResponse resp = new RestResponse(userService.getTopAdministrator(cmd));
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}



	/**
	 * <b>URL: /user/findUsersByPhones</b>
	 * <p>通过域空间和手机号查询用户信息</p>
	 * @return
	 */
	@RequestMapping("findUsersByPhones")
	@RestReturn(FindUsersByPhonesResponse.class)
	public RestResponse findUsersByPhones(FindUsersByPhonesCommand cmd){

		RestResponse resp = new RestResponse(userService.findUsersByPhones(cmd));
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;

	}

	/**
	 * <b>URL: /user/getPrintMerchantUrl</b>
	 * <p>提供商户界面跳转URL</p>
	 * @return
	 */
	@RequestMapping("getPrintMerchantUrl")
	@RestReturn(GetPrintMerchantUrlResponse.class)
	public RestResponse getPrintMerchantUrl(GetPrintMerchantUrlCommand cmd){
		RestResponse resp = new RestResponse(userService.getPrintMerchantUrl(cmd));
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}
	/**
	 * <b>URL: /rpcapi/user/logonInfo</b>
	 * <p>获取登录信息</p>
	 */
	@RequestMapping("logonInfo")
	@RestReturn(UserLogonInfo.class)
	public RestResponse resendVerificationCode(@Valid LogonInfoCommand cmd) {
		// xxx
		return new RestResponse("OK");
	}

    /**
     * <b>URL: /user/logonInfo</b>
     * <p>获取登录信息</p>
     */
    @RequestMapping("sendVerificationCodeSms")
    @RestReturn(String.class)
    public RestResponse sendVerificationCodeSms(@Valid SendVerificationCodeCommand cmd) {
        this.userService.sendVerificationCodeSms(cmd.getNamespaceId(), cmd.getPhoneNumber(), cmd.getCode());
		return new RestResponse("OK");
    }

    /**
     * <b>URL: /user/fixUserSync</b>
     * <p>触发用户同步, 从 ehcore 到 ehuser </p>
     */
    @RequestMapping("fixUserSync")
    @RestReturn(String.class)
    public RestResponse fixUserSync(@Valid FixUserSyncCommand cmd) {
        this.userService.fixUserSync(cmd);
		return new RestResponse("OK");
    }
}
