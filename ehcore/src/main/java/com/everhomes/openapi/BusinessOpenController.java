
package com.everhomes.openapi;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetVendor;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.business.BusinessService;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.*;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.admin.ListBuildingByCommunityIdsCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.CheckPaymentUserCommand;
import com.everhomes.rest.asset.CheckPaymentUserResponse;
import com.everhomes.rest.asset.PushUsersCommand;
import com.everhomes.rest.asset.PushUsersResponse;
import com.everhomes.rest.business.*;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.community.GetCommunitiesByNameAndCityIdCommand;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.openapi.*;
import com.everhomes.rest.organization.ListOrganizationContactCommandResponse;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.region.ListRegionByKeywordCommand;
import com.everhomes.rest.region.ListRegionCommand;
import com.everhomes.rest.region.RegionDTO;
import com.everhomes.rest.reserver.CreateReserverOrderCommand;
import com.everhomes.rest.ui.user.ListAnBangRelatedScenesCommand;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserProfileDTO;
import com.everhomes.rest.user.*;
import com.everhomes.rest.wx.GetAccessTokenCommand;
import com.everhomes.rest.wx.GetJsapiTicketCommand;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.wx.WeChatService;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestDoc(value = "Business open Constroller", site = "core")
@RestController
@RequestMapping("/openapi")
public class BusinessOpenController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessOpenController.class);
    private static final String DEFAULT_SORT = "default_order";
    private  static final String ERROR = "error";
    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private CategoryProvider categoryProvider;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    MessagingService messagingService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private WeChatService wechatService;

	private AssetVendor checkAssetVendor(Integer namespaceId,Integer defaultNamespaceId){
        if(null == namespaceId) {
            LOGGER.error("checkAssetVendor namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "checkAssetVendor namespaceId cannot be null.");
        }
        AssetVendor assetVendor = assetProvider.findAssetVendorByNamespace(namespaceId);
        if(null == assetVendor && defaultNamespaceId!=null)  assetVendor = assetProvider.findAssetVendorByNamespace(defaultNamespaceId);
        if(null == assetVendor) {
            LOGGER.error("assetVendor not found, assetVendor namespaceId={}, targetId={}", namespaceId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "assetVendor not found");
        }
        return assetVendor;
    }

	private BusinessOpenVendorHandler getBusinessOpenVendorHandler(String vendorName) {
		BusinessOpenVendorHandler handler = null;
        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = BusinessOpenVendorHandler.BUSINESSOPEN_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }
        return handler;
    }

    /**
     * <b>URL: /openapi/listBizCategories</b> 列出所有商家分类
     */
    @RequestMapping("listBizCategories")
    @RestReturn(value = CategoryDTO.class, collection = true)
    public RestResponse listBizCategories() {

        Tuple<String, SortOrder> orderBy = new Tuple<String, SortOrder>(DEFAULT_SORT, SortOrder.ASC);
        @SuppressWarnings("unchecked")
        //        List<Category> entityResultList = this.categoryProvider.listChildCategories(CategoryConstants.CATEGORY_ID_SERVICE,
                //                CategoryAdminStatus.ACTIVE, orderBy);
                List<Category> entityResultList = this.categoryProvider.listBusinessSubCategories(CategoryConstants.CATEGORY_ID_SERVICE,
                CategoryAdminStatus.ACTIVE, orderBy);

        List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
            return ConvertHelper.convert(r, CategoryDTO.class);
        }).collect(Collectors.toList());

        return new RestResponse(dtoResultList);
    }

    /**
     * <b>URL: /openapi/syncBusiness</b>
     * <p>同步添加/更新店铺</p>
     */
    @RequestMapping("syncBusiness")
    @RestReturn(value = String.class)
    public RestResponse syncBusiness(@Valid SyncBusinessCommand cmd) {

        businessService.syncBusiness(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/syncBusiness</b>
     * <p>biz2.2.0版：同步添加/更新店铺</p>
     */
    @RequestMapping("reSyncBusiness")
    @RestReturn(value = String.class)
    public RestResponse reSyncBusiness(@Valid ReSyncBusinessCommand cmd) {

        businessService.reSyncBusiness(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/syncDeleteBusiness</b>
     * <p>同步删除店铺</p>
     */
    @RequestMapping("syncDeleteBusiness")
    @RestReturn(value = String.class)
    public RestResponse syncDeleteBusiness(@Valid SyncDeleteBusinessCommand cmd) {

        businessService.syncDeleteBusiness(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/syncUserAddShopStatus</b>
     * <p>同步开店状态</p>
     */
    @RequestMapping("syncUserAddShopStatus")
    @RestReturn(value = String.class)
    public RestResponse syncUserAppliedShopStatus(SyncUserAddShopStatusCommand cmd) {

        userActivityService.addUserShop(cmd.getUserId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/syncUserDelShopStatus</b>
     * <p>同步未开店状态</p>
     */
    @RequestMapping("syncUserDelShopStatus")
    @RestReturn(value = String.class)
    public RestResponse syncUserDelShopStatus(SyncUserAddShopStatusCommand cmd) {
        userActivityService.cancelShop(cmd.getUserId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/syncUserFavorite</b>
     * <p>同步用户收藏</p>
     */
    @RequestMapping("syncUserFavorite")
    @RestReturn(value = String.class)
    public RestResponse syncUserFavorite(UserFavoriteCommand cmd) {

        businessService.syncUserFavorite(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/syncUserCancelFavorite</b>
     * <p>同步用户取消收藏</p>
     */
    @RequestMapping("syncUserCancelFavorite")
    @RestReturn(value = String.class)
    public RestResponse syncUserCancelFavorite(UserFavoriteCommand cmd) {

        businessService.syncUserCancelFavorite(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/findBusinessFavoriteStatus</b>
     * <p>获取收藏状态</p>
     */
    @RequestMapping("findBusinessFavoriteStatus")
    @RestReturn(value = String.class)
    public RestResponse findBusinessFavoriteStatus(UserFavoriteCommand cmd) {

        RestResponse response = new RestResponse(businessService.findBusinessFavoriteStatus(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    private void sendMessageToUser(Long userId, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.BIZ_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()),
                new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.BIZ_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if (null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }

        LOGGER.debug("sendMessageToUser-bizuserId=" + User.BIZ_UID);
        LOGGER.debug("sendMessageToUser-BIZ_USER_LOGIN=" + StringHelper.toJsonString(User.BIZ_USER_LOGIN));

        messagingService.routeMessage(User.BIZ_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    // add by momoubin,18/12/14,发送系统消息
    private void sendSystemMessageToUser(Long userId, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()),
                new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if (null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }

        LOGGER.debug("sendMessageToUser-systemId=" + User.BIZ_UID);
        LOGGER.debug("sendMessageToUser-SYSTEM_USER_LOGIN=" + StringHelper.toJsonString(User.SYSTEM_UID));

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    // add by momoubin,18/12/14,发送系统消息
    @RequestMapping("sendSystemMessageToUser")
    @RestReturn(value = String.class)
    public RestResponse sendSystemMessageToUser(BusinessMessageCommand cmd) {
        cmd.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        sendSystemMessageToUser(cmd.getUserId(), cmd.getContent(), cmd.getMeta());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    @RequestMapping("sendMessageToUser")
    @RestReturn(value = String.class)
    public RestResponse sendMessageToUser(BusinessMessageCommand cmd) {
        if (BizMessageType.fromCode(cmd.getBizMessageType()) == BizMessageType.VOICE) {
            cmd.getMeta().put(MessageMetaConstant.VOICE_REMIND, MetaObjectType.BIZ_NEW_ORDER.getCode());
        }
        sendMessageToUser(cmd.getUserId(), cmd.getContent(), cmd.getMeta());

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("sendMessageToUserV2")
    @RestReturn(value = String.class)
    public RestResponse sendMessageToUserV2(BusinessMessageV2Command cmd) {
        Map<String, String> meta = null;
        if (cmd.getMeta() != null) {
            meta = (Map<String, String>) StringHelper.fromJsonString(cmd.getMeta(), HashMap.class);
        }
        if (BizMessageType.fromCode(cmd.getBizMessageType()) == BizMessageType.VOICE) {
            if (meta == null) {
                meta = new HashMap<>();
            }
            meta.put(MessageMetaConstant.VOICE_REMIND, MetaObjectType.BIZ_NEW_ORDER.getCode());
        }
        sendMessageToUser(cmd.getUserId(), cmd.getContent(), meta);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /openapi/sendMessageToCustomUser</b>
	 * <p>提供给深圳湾的定制接口</p>
	 */
	@RequestMapping("sendMessageToCustomUser")
	@RestReturn(value=String.class)
	public RestResponse sendMessageToCustomUser(BusinessMessageCustomCommand cmd) {
		if(BizMessageType.fromCode(cmd.getBizMessageType()) == BizMessageType.VOICE) {
			cmd.getMeta().put(MessageMetaConstant.VOICE_REMIND, MetaObjectType.BIZ_NEW_ORDER.getCode());
		}

		AssetVendor assetVendor = checkAssetVendor(cmd.getNamespaceId(),0);
		String vendorName = assetVendor.getVendorName();
        BusinessOpenVendorHandler handler = getBusinessOpenVendorHandler(vendorName);
        List<Long> userIdList = handler.getUserId(cmd.getCustomJson(), cmd.getNamespaceId());
        for(int i = 0;i <userIdList.size();i++) {
        	Long userId = userIdList.get(i);
        	sendMessageToUser(userId, cmd.getContent(), cmd.getMeta());
        }

		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /openapi/getUserServiceAddress</b>
     * <p>获取用户相关的服务地址</p>
     */
    @RequestMapping("getUserServiceAddress")
    @RestReturn(value = UserServiceAddressDTO.class, collection = true)
    public RestResponse getUserServiceAddress(GetUserServiceAddressCommand cmd) {
        List<UserServiceAddressDTO> result = this.userActivityService.getUserServiceAddress(cmd);

        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/receiveCoupon</b>
     * <p>用户优惠券数加1</p>
     */
    @RequestMapping("receiveCoupon")
    @RestReturn(String.class)
    public RestResponse receiveCoupon(UserCouponsCommand cmd) {
        userActivityService.receiveCoupon(cmd.getUserId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/invalidCoupon</b>
     * <p>用户优惠券数减1</p>
     */
    @RequestMapping("invalidCoupon")
    @RestReturn(String.class)
    public RestResponse invalidCoupon(UserCouponsCommand cmd) {
        userActivityService.invalidCoupon(cmd.getUserId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/findUserCouponCount</b>
     * <p>查询用户优惠券数</p>
     */
    @RequestMapping("findUserCouponCount")
    @RestReturn(Integer.class)
    public RestResponse findUserCouponCount(UserCouponsCommand cmd) {
        Integer couponCount = businessService.findUserCouponCount(cmd);
        RestResponse response = new RestResponse(couponCount);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/updateUserCouponCount</b>
     * <p>更新用户优惠券数</p>
     */
    @RequestMapping("updateUserCouponCount")
    @RestReturn(String.class)
    public RestResponse updateUserCouponCount(UpdateUserCouponCountCommand cmd) {
        businessService.updateUserCouponCount(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("getUserInfoByUuid")
    @RestReturn(GetUserByUuidResponse.class)
    public RestResponse getUserInfoByUuid(@Valid GetUserInfoByUuid cmd) {
        UserInfo user = userService.getUserBasicByUuid(cmd.getUuid());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        if (null == user) {
            response.setErrorCode(ErrorCodes.ERROR_CLASS_NOT_FOUND);
            response.setErrorDescription("User not found");
            return response;
        }

        GetUserByUuidResponse resp = new GetUserByUuidResponse();
        resp.setMobile(user.getPhones().get(0));
        resp.setNickName(user.getNickName());
        resp.setAvatarUrl(user.getAvatarUrl());
        resp.setUuid(user.getUuid());
        resp.setGender(user.getGender());

        response.setResponseObject(resp);
        return response;
    }

    @RequestMapping("getUserDetailByUuid")
    @RestReturn(GetUserDetailByUuidResponse.class)
    public RestResponse getUserDetailByUuid(@Valid GetUserInfoByUuid cmd) {
        UserInfo user = userService.getUserBasicByUuid(cmd.getUuid());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        if (null == user) {
            response.setErrorCode(ErrorCodes.ERROR_CLASS_NOT_FOUND);
            response.setErrorDescription("User not found");
            return response;
        }

        GetUserDetailByUuidResponse resp = new GetUserDetailByUuidResponse();
        resp.setMobile(user.getPhones().get(0));
        resp.setNickName(user.getNickName());
        resp.setAvatarUrl(user.getAvatarUrl());
        resp.setUuid(user.getUuid());
        resp.setGender(user.getGender());

        GetUserServiceAddressCommand getUserCmd = new GetUserServiceAddressCommand();
        getUserCmd.setUserId(user.getId());
        List<UserServiceAddressDTO> result = this.userActivityService.getUserServiceAddress(getUserCmd);
        if (result != null && result.size() > 0) {
            resp.setAddress(result.get(0));
        }

        response.setResponseObject(resp);
        return response;
    }

    @RequestMapping("getUserDefaultAddress")
    @RestReturn(value = UserServiceAddressDTO.class)
    public RestResponse getUserDefaultAddress(@Valid GetUserDefaultAddressCommand cmd) {
        UserServiceAddressDTO address = this.businessService.getUserDefaultAddress(cmd);
        RestResponse response = new RestResponse(address);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("getUserAddress")
    @RestReturn(value = UserAddressDTO.class)
    public RestResponse getUserAddress(@Valid GetUserDefaultAddressCommand cmd) {
        RestResponse response = new RestResponse(businessService.getUserAddress(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("getUserOrganizations")
    @RestReturn(value = OrganizationDTO.class, collection = true)
    public RestResponse getUserOrganizations(@Valid GetUserDefaultAddressCommand cmd) {
        RestResponse response = new RestResponse(businessService.getUserOrganizations(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("listUser")
    @RestReturn(value = UserDtoForBiz.class, collection = true)
    public RestResponse listUser(@Valid ListUserCommand cmd) {
        List<UserDtoForBiz> users = this.businessService.listUser(cmd);
        RestResponse response = new RestResponse(users);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("findTokenByUserId")
    @RestReturn(String.class)
    public RestResponse findTokenByUserId(@Valid FindTokenByUserIdCommand cmd) {

        User user = userProvider.findUserById(cmd.getUserId());
        List<UserIdentifier> idens = userProvider.listUserIdentifiersOfUser(user.getId());
        UserIdentifier iden = idens.get(0);


        SignupToken signUpToken = new SignupToken(user.getId(), IdentifierType.fromCode(iden.getIdentifierType()), iden.getIdentifierToken());
        String token = WebTokenGenerator.getInstance().toWebToken(signUpToken);

        RestResponse response = new RestResponse(token);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("getUserInfoById")
    @RestReturn(value = UserInfo.class)
    public RestResponse getUserInfoById(@Valid GetUserInfoByIdCommand cmd) {
        UserInfo user = this.userService.getUserInfoById(cmd);
        RestResponse response = new RestResponse(user);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("listUserByKeyword")
    @RestReturn(value = UserInfo.class, collection = true)
    public RestResponse listUserByKeyword(@Valid ListUserByKeywordCommand cmd) {
        List<UserInfo> users = this.businessService.listUserByKeyword(cmd);
        RestResponse response = new RestResponse(users);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("listBusinessByCommonityId")
    @RestReturn(value = String.class, collection = true)
    public RestResponse listBusinessByCommonityId(@Valid ListBusinessByCommonityIdCommand cmd) {
        List<String> list = this.businessService.listBusinessByCommonityId(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("listUserByIdentifier")
    @RestReturn(value = UserInfo.class, collection = true)
    public RestResponse listUserByIdentifier(@Valid ListUserByIdentifierCommand cmd) {
        List<UserInfo> users = this.businessService.listUserByIdentifier(cmd);
        RestResponse response = new RestResponse(users);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("openBizNamespaceVisible")
    @RestReturn(String.class)
    public RestResponse openBizNamespaceVisible(@Valid BusinessAsignedNamespaceCommand cmd) {
        businessService.openBusinessAssignedNamespace(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("closeBizNamespaceVisible")
    @RestReturn(String.class)
    public RestResponse closeBizNamespaceVisible(@Valid BusinessAsignedNamespaceCommand cmd) {
        businessService.closeBusinessAssignedNamespace(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("updateReceivedCouponCount")
    @RestReturn(String.class)
    public RestResponse updateReceivedCouponCount(@Valid UpdateReceivedCouponCountCommand cmd) {
        businessService.updateReceivedCouponCount(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("getReceivedCouponCount")
    @RestReturn(value = UserProfileDTO.class)
    public RestResponse getReceivedCouponCount(@Valid GetReceivedCouponCountCommand cmd) {
        UserProfileDTO userProfile = businessService.getReceivedCouponCount(cmd);
        RestResponse response = new RestResponse(userProfile);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/listBuildingsByKeywordAndNameSpace</b>
     * <p>根据关键字查询域空间楼栋</p>
     */
    @RequestMapping("listBuildingsByKeywordAndNameSpace")
    @RestReturn(value = BuildingDTO.class, collection = true)
    public RestResponse listBuildingsByKeywordAndNameSpace(@Valid ListBuildingsByKeywordAndNameSpaceCommand cmd) {
        List<BuildingDTO> data = this.businessService.listBuildingsByKeywordAndNameSpace(cmd);
        RestResponse response = new RestResponse(data);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/listBuildingsByKeyword</b>
     * <p>根据小区Id和关键字查询小区楼栋</p>
     */
    @RequestMapping("listBuildingsByKeyword")
    @RestReturn(value = BuildingDTO.class, collection = true)
    public RestResponse listBuildingsByKeyword(@Valid ListBuildingByCommunityIdsCommand cmd) {
        List<BuildingDTO> data = this.businessService.listBuildingsByKeyword(cmd);
        RestResponse response = new RestResponse(data);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/listApartmentFloor</b>
     * <p>根据小区Id、楼栋号和关键字查询楼层</p>
     */
    @RequestMapping("listApartmentFloor")
    @RestReturn(value = ApartmentFloorDTO.class, collection = true)
    public RestResponse listApartmentFloor(@Valid ListApartmentFloorCommand cmd) {
        Tuple<Integer, List<ApartmentFloorDTO>> data = this.businessService.listApartmentFloor(cmd);
        RestResponse response = new RestResponse(data.second());
        response.setErrorCode(data.first());
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/listApartmentsByKeyword</b>
     * <p>根据小区Id、楼栋号和关键字查询门牌</p>
     */
    @RequestMapping("listApartmentsByKeyword")
    @RestReturn(value = ApartmentDTO.class, collection = true)
    public RestResponse listApartmentsByKeyword(@Valid ListPropApartmentsByKeywordCommand cmd) {
        Tuple<Integer, List<ApartmentDTO>> data = this.businessService.listApartmentsByKeyword(cmd);
        RestResponse response = new RestResponse(data.second());
        response.setErrorCode(data.first());
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /api/getCommunitiesByNameAndCityId</b>
     * <p>根据小区名称和城市id搜索小区</p>
     */
    @RequestMapping("getCommunitiesByNameAndCityId")
    @RestReturn(value = CommunityDTO.class, collection = true)
    public RestResponse getCommunitiesByNameAndCityId(@Valid GetCommunitiesByNameAndCityIdCommand cmd) {
        List<CommunityDTO> data = this.businessService.getCommunitiesByNameAndCityId(cmd);
        RestResponse response = new RestResponse(data);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/getCommunityById</b>
     * <p>根据园区ID获取园区信息</p>
     */
    @RequestMapping("getCommunityById")
    @RestReturn(value = CommunityDTO.class)
    public RestResponse getCommunityById(@Valid GetCommunityByIdCommand cmd) {
        CommunityDTO data = this.businessService.getCommunityById(cmd);
        RestResponse response = new RestResponse(data);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/listRegionByKeyword</b>
     * <p>根据关键字查询区域信息，查询下级区域，需要传入父级区域标识</p>
     */
    @RequestMapping("listRegionByKeyword")
    @RestReturn(value = RegionDTO.class, collection = true)
    public RestResponse listRegionByKeyword(@Valid ListRegionByKeywordCommand cmd) {
        List<RegionDTO> data = this.businessService.listRegionByKeyword(cmd);
        RestResponse response = new RestResponse(data);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/addUserOrderCount</b>
     * <p>用户订单数加1</p>
     */
    @RequestMapping("addUserOrderCount")
    @RestReturn(String.class)
    public RestResponse addUserOrderCount(UserCouponsCommand cmd) {
        businessService.addUserOrderCount(cmd.getUserId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/reduceUserOrderCount</b>
     * <p>用户订单数减1</p>
     */
    @RequestMapping("reduceUserOrderCount")
    @RestReturn(String.class)
    public RestResponse reduceUserOrderCount(UserCouponsCommand cmd) {
        businessService.reduceUserOrderCount(cmd.getUserId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/findUserOrderCount</b>
     * <p>查询用户订单数</p>
     */
    @RequestMapping("findUserOrderCount")
    @RestReturn(Integer.class)
    public RestResponse findUserOrderCount(UserCouponsCommand cmd) {
        Integer orderCount = businessService.findUserOrderCount(cmd);
        RestResponse response = new RestResponse(orderCount);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/updateUserOrderCount</b>
     * <p>更新用户订单数</p>
     */
    @RequestMapping("updateUserOrderCount")
    @RestReturn(String.class)
    public RestResponse updateUserOrderCount(UpdateUserOrderCountCommand cmd) {
        businessService.updateUserOrderCount(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/listRegion</b>
     * 列出指定范围和状态的区域列表（用填父亲区域ID）
     */
    @RequestMapping("listRegion")
    @RestReturn(value = RegionDTO.class, collection = true)
    public RestResponse listRegion(@Valid ListRegionCommand cmd, HttpServletRequest request, HttpServletResponse response) {

        List<RegionDTO> dtoResultList = businessService.listRegion(cmd);

        if (dtoResultList != null) {
            int hashCode = dtoResultList.hashCode();
            if (EtagHelper.checkHeaderEtagOnly(30, hashCode + "", request, response)) {
                return new RestResponse(dtoResultList);
            }
        }

        return new RestResponse();
    }

    /**
     * <b>URL: /openapi/validateUserPass</b>
     */
    @RequestMapping("validateUserPass")
    @RestReturn(value = UserInfo.class)
    public RestResponse validateUserPass(@Valid ValidateUserPassCommand cmd) {
        UserInfo user = businessService.validateUserPass(cmd);
        RestResponse response = new RestResponse(user);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>创建电商拼单group</p>
     * <b>URL: /openapi/createBusinessGroup</b>
     */
    @RequestMapping("createBusinessGroup")
    @RestReturn(value = CreateBusinessGroupResponse.class)
    public RestResponse createBusinessGroup(CreateBusinessGroupCommand cmd) {
        return new RestResponse(businessService.createBusinessGroup(cmd));
    }

    /**
     * <p>加入电商拼单group</p>
     * <b>URL: /openapi/joinBusinessGroup</b>
     */
    @RequestMapping("joinBusinessGroup")
    @RestReturn(value = String.class)
    public RestResponse joinBusinessGroup(JoinBusinessGroupCommand cmd) {
        businessService.joinBusinessGroup(cmd);
        return new RestResponse();
    }

    /**
     * <b>URL: /openapi/createReserverOrder</b>
     * <p>新建位置预订</p>
     */
    @RequestMapping("createReserverOrder")
    @RestReturn(value = String.class)
    public RestResponse createReserverOrder(CreateReserverOrderCommand cmd) {

        //新建flowcase

        UserContext context = UserContext.current();
        User user = userProvider.findUserById(cmd.getId());
        context.setUser(user);

        Integer namespaceId = user.getNamespaceId();
        Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.RESERVER_PLACE,
                FlowModuleType.NO_MODULE.getCode(), 0L, FlowOwnerType.RESERVER_PLACE.getCode());
        if (null == flow) {
            LOGGER.error("Enable reserver flow not found, moduleId={}", FlowConstants.RESERVER_PLACE);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
                    "Enable reserver flow not found.");
        }
        CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
        createFlowCaseCommand.setApplyUserId(user.getId());
        createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
        createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
        createFlowCaseCommand.setReferId(Long.valueOf(cmd.getOrderId()));
        createFlowCaseCommand.setReferType(FlowOwnerType.RESERVER_PLACE.getCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder("");
        sb.append("就餐时间：").append(sdf.format(new Date(cmd.getReserverTime()))).append("\n");
        sb.append("就餐人数：").append(cmd.getReserverNum()).append("人").append("\n");
        sb.append("备注说明：").append(cmd.getRemark()).append("\n");
        sb.append("申请人：").append(cmd.getRequestorName()).append("\n");
        sb.append("店铺名称：").append(cmd.getShopName());
        createFlowCaseCommand.setContent(sb.toString());
//        createFlowCaseCommand.setProjectId(task.getOwnerId());
//        createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());

        FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/listUsersOfEnterprise</b>
     */
    @RequestMapping("listUsersOfEnterprise")
    @RestReturn(value = OrganizationContactDTO.class)
    @RequireAuthentication(false)
    public RestResponse listUsersOfEnterprise(listUsersOfEnterpriseCommand cmd) {
        ListOrganizationContactCommandResponse memberResponse = this.organizationService.listUsersOfEnterprise(cmd);
        RestResponse response = new RestResponse(memberResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查看付款方是否有会员</p>
     * <b>URL: /business/checkPaymentUser</b>
     */
    @RequestMapping("checkPaymentUser")
    @RestReturn(value = CheckPaymentUserResponse.class)
    public RestResponse checkPaymentUser(CheckPaymentUserCommand cmd) {
        CheckPaymentUserResponse response = businessService.checkPaymentUser(cmd);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    @RequestMapping("pushUsers")
    @RestReturn(value = PushUsersResponse.class)
    public RestResponse pushUsers(PushUsersCommand cmd) {
        cmd.setNamespaceId(0);
        PushUsersResponse response = this.userService.createUsersForAnBang(cmd);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    @RequestMapping("verifyUserByTokenFromAnBang")
    @RequireAuthentication(false)
    @RestReturn(value = UserLogin.class)
    @SuppressDiscover
    public RestResponse verifyUserByTokenFromAnBang(LogonCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        UserLogin login = this.userService.verifyUserByTokenFromAnBang(cmd.getUserIdentifier());
        LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
        String tokenString = WebTokenGenerator.getInstance().toWebToken(token);
        setCookieInResponse("token", tokenString, request, response);
        RestResponse restResponse = new RestResponse(login);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }


    @RequestMapping("abdir")
    @RequireAuthentication(false)
    public Object anbangRedirect(AnBangTokenCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            UserLogin login = this.userService.verifyUserByTokenFromAnBang(cmd.getAbtoken());
            if(login != null && cmd.getRedirect() != null) {
                String location = cmd.getRedirect();
                LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
                String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

                Map<String, String[]> paramMap = request.getParameterMap();
                List<String> params = new ArrayList<String>();
                for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                    if (!entry.getKey().equals("token") && !entry.getKey().equals("redirect") && !entry.getKey().equals("abtoken")) {
                        params.add(entry.getKey() + "=" + StringUtils.join(entry.getValue(), ','));
                        }
                    }

                location = this.userService.makeAnbangRedirectUrl(login.getUserId(), location, paramMap);

                setCookieInResponse("token", tokenString, request, response);
                httpHeaders.setLocation(new URI(location));

                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
            }

        } catch (URISyntaxException e) {
        }

        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
        restResponse.setErrorDescription("invalid abtoken or redirect");
        return restResponse;
    }

    /**
     * <b>URL: /openapi/listAnbangRelatedScenes</b>
     * <p>根据用户域空间场景获取用户相关的地址列表</p>
     */
    @RequestMapping("listAnbangRelatedScenes")
    @RequireAuthentication(false)
    @RestReturn(value=SceneDTO.class, collection=true)
    public RestResponse listAnbangRelatedScenes(ListAnBangRelatedScenesCommand cmd) {
        RestResponse response = new RestResponse(userService.listAnbangRelatedScenes(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    private static void setCookieInResponse(String name, String value, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = findCookieInRequest(name, request);
        if (cookie == null)
            cookie = new Cookie(name, value);
        else
            cookie.setValue(value);
        cookie.setPath("/");
        if (value == null || value.isEmpty())
            cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    private static Cookie findCookieInRequest(String name, HttpServletRequest request) {
        List<Cookie> matchedCookies = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    LOGGER.debug("Found matched cookie with name {} at value: {}, path: {}, version: {}", name,
                            cookie.getValue(), cookie.getPath(), cookie.getVersion());
                    matchedCookies.add(cookie);
                }
            }
        }

        if (matchedCookies.size() > 0)
            return matchedCookies.get(matchedCookies.size() - 1);
        return null;
    }


    /**
     * <b>URL: /openapi/redirect</b>
     * <p>统一跳转到传入的redirectUrl，并在后面加上指定的参数</p>
     */
    @RequestMapping(value = "redirect", method = RequestMethod.GET)
    public Object redirect(RedirectCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        String errorDesc = "";

        OpenApiRedirectHandler handler = PlatformContext.getComponent(OpenApiRedirectHandler.PREFIX + cmd.getHandler());
        if (handler != null) {
            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(new URI(handler.build(cmd.getUrl(), request.getParameterMap())));
                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
            } catch (URISyntaxException e) {
                errorDesc = "Invalid handler build " + e.getMessage();
            }
        } else {
            errorDesc = "Can not find handler by " + cmd.getHandler();
        }
        RestResponse restResponse = new RestResponse(ErrorCodes.SCOPE_GENERAL);
        restResponse.setErrorDescription(errorDesc);
        return restResponse;
    }
    
    /**
     * <b>URL: /openapi/postRedirect</b>
     * <p>以POST   的方式跳转</p>
     */
    @RequestMapping(value = "postRedirect", method = RequestMethod.GET)
    public Object postRedirect(RedirectCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        OpenApiRedirectHandler handler = PlatformContext.getComponent(OpenApiRedirectHandler.PREFIX + cmd.getHandler());
        RestResponse restResponse = new RestResponse(ErrorCodes.SCOPE_GENERAL);
        if (handler != null) { 
                String str = handler.build(cmd.getUrl(), request.getParameterMap());
                if(str == null ){
                	
                    restResponse.setErrorDescription("return null from handler by "+ cmd.getHandler());
                    return restResponse;
                }else if(str.startsWith(ERROR)){
                	restResponse.setErrorDescription(str);
                    return restResponse;
                }
                @SuppressWarnings("unchecked")
				Map<String ,String> formParamsMap = (Map<String, String>) JSONObject.parse(str);
                ModelAndView mv = new ModelAndView();
                String viewName = formParamsMap.get("viewName");
                if(StringUtils.isBlank(viewName)){
                	
                    restResponse.setErrorDescription("viewName is blank of handler by "+ cmd.getHandler());
                    return restResponse;
                }
                //mv.setViewName("mybay-redirect");
                mv.setViewName(viewName);
                mv.addAllObjects(formParamsMap);                
                return mv;
           
        } else {
            restResponse.setErrorDescription("Can not find handler by "+ cmd.getHandler());
            return restResponse;
        }
    }



    /**
     *
     * <b>URL: /openapi/getAccessToken</b>
     * <p>获取微信access token</p>
     */
    @RequestMapping("getAccessToken")
    @RestReturn(value = String.class)
    public RestResponse getAccessToken(GetAccessTokenCommand cmd) {
        UserContext.setCurrentNamespaceId(cmd.getNamespaceId());
        RestResponse response = new RestResponse(wechatService.getAccessToken());

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /openapi/getJsapiTicket</b>
     * <p>获取微信jsapi ticket</p>
     */
    @RequestMapping("getJsapiTicket")
    @RestReturn(value = String.class)
    public RestResponse getJsapiTicket(GetJsapiTicketCommand cmd) {
        UserContext.setCurrentNamespaceId(cmd.getNamespaceId());
        RestResponse response = new RestResponse(wechatService.getJsapiTicket());

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
