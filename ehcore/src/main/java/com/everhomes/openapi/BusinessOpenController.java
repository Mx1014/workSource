package com.everhomes.openapi;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.app.AppConstants;
import com.everhomes.business.BusinessService;
import com.everhomes.business.SyncBusinessCommand;
import com.everhomes.business.SyncDeleteBusinessCommand;
import com.everhomes.business.SyncUserAddShopStatusCommand;
import com.everhomes.business.UserFavoriteCommand;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryAdminStatus;
import com.everhomes.category.CategoryConstants;
import com.everhomes.category.CategoryDTO;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.FindTokenByUserIdCommand;
import com.everhomes.user.GetUserByUuidResponse;
import com.everhomes.user.GetUserDefaultAddressCommand;
import com.everhomes.user.GetUserDetailByUuidResponse;
import com.everhomes.user.GetUserInfoByIdCommand;
import com.everhomes.user.GetUserInfoByUuid;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.ListUserCommand;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.SignupToken;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserDtoForBiz;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserInfoFroBiz;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;

@RestDoc(value="Business open Constroller", site="core")
@RestController
@RequestMapping("/openapi")
public class BusinessOpenController extends ControllerBase {
    private static final String DEFAULT_SORT = "default_order";
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
    
    /**
     * <b>URL: /openapi/listBizCategories</b> 列出所有商家分类
     */
    @RequestMapping("listBizCategories")
    @RestReturn(value = CategoryDTO.class, collection = true)
    public RestResponse listBizCategories() {
        
        Tuple<String, SortOrder> orderBy = new Tuple<String, SortOrder>(DEFAULT_SORT, SortOrder.ASC);;
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
    @RestReturn(value=String.class)
    public RestResponse syncBusiness(@Valid SyncBusinessCommand cmd) {
        
        businessService.syncBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/syncDeleteBusiness</b>
     * <p>同步删除店铺</p>
     */
    @RequestMapping("syncDeleteBusiness")
    @RestReturn(value=String.class)
    public RestResponse syncDeleteBusiness(@Valid SyncDeleteBusinessCommand cmd) {
        
        businessService.syncDeleteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/syncUserAddShopStatus</b>
     * <p>同步开店状态</p>
     */
    @RequestMapping("syncUserAddShopStatus")
    @RestReturn(value=String.class)
    public RestResponse syncUserAppliedShopStatus(SyncUserAddShopStatusCommand cmd) {
        
        userActivityService.addUserShop(cmd.getUserId());
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/syncUserFavorite</b>
     * <p>同步用户收藏</p>
     */
    @RequestMapping("syncUserFavorite")
    @RestReturn(value=String.class)
    public RestResponse syncUserFavorite(UserFavoriteCommand cmd) {
        
        businessService.syncUserFavorite(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/syncUserCancelFavorite</b>
     * <p>同步用户取消收藏</p>
     */
    @RequestMapping("syncUserCancelFavorite")
    @RestReturn(value=String.class)
    public RestResponse syncUserCancelFavorite(UserFavoriteCommand cmd) {
        
        businessService.syncUserCancelFavorite(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/findBusinessFavoriteStatus</b>
     * <p>获取收藏状态</p>
     */
    @RequestMapping("findBusinessFavoriteStatus")
    @RestReturn(value=String.class)
    public RestResponse findBusinessFavoriteStatus(UserFavoriteCommand cmd) {
        
        RestResponse response =  new RestResponse(businessService.findBusinessFavoriteStatus(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    private void sendMessageToUser(Long userId, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
    @RequestMapping("sendMessageToUser")
    @RestReturn(value=String.class)
    public RestResponse sendMessageToUser(BusinessMessageCommand cmd) {
        sendMessageToUser(cmd.getUserId(), cmd.getContent(), cmd.getMeta());
        
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
    @RestReturn(value=UserServiceAddressDTO.class, collection=true)
    public RestResponse getUserServiceAddress(GetUserServiceAddressCommand cmd) {
        List<UserServiceAddressDTO> result = this.userActivityService.getUserServiceAddress(cmd);
        
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/receiveCoupon</b>
     * <p>同步用户领取优惠券</p>
     */
    @RequestMapping("receiveCoupon")
    @RestReturn(String.class)
    public RestResponse receiveCoupon(UserCouponsCommand cmd) {
    	userActivityService.receiveCoupon(cmd.getUserId());
    	RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /openapi/invalidCoupon</b>
     * <p>同步用户不能使用的优惠券</p>
     */
    @RequestMapping("invalidCoupon")
    @RestReturn(String.class)
    public RestResponse invalidCoupon(UserCouponsCommand cmd) {
    	userActivityService.invalidCoupon(cmd.getUserId());
    	RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("getUserInfoByUuid")
    @RestReturn(GetUserByUuidResponse.class)
    public RestResponse getUserInfoByUuid(@Valid GetUserInfoByUuid cmd) {
        UserInfo user = userService.getUserBasicByUuid(cmd.getUuid());
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        if(null == user) {
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
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        if(null == user) {
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
        if(result != null && result.size() > 0) {
            resp.setAddress(result.get(0));
        }
        
        response.setResponseObject(resp);
        return response;
    }
    
    @RequestMapping("getUserDefaultAddress")
    @RestReturn(UserServiceAddressDTO.class)
    public RestResponse getUserDefaultAddress(@Valid GetUserDefaultAddressCommand cmd) {
    	UserServiceAddressDTO address = this.businessService.getUserDefaultAddress(cmd);
    	RestResponse response =  new RestResponse(address);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
		return response;
    }
    
    @RequestMapping("listUser")
    @RestReturn(value=UserDtoForBiz.class,collection=true)
    public RestResponse listUser(@Valid ListUserCommand cmd) {
    	List<UserDtoForBiz> users = this.businessService.listUser(cmd);
    	RestResponse response =  new RestResponse(users);
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
		
    	
    	SignupToken signUpToken = new SignupToken(user.getId(),IdentifierType.fromCode(iden.getIdentifierType()),iden.getIdentifierToken());
    	String token = WebTokenGenerator.getInstance().toWebToken(signUpToken);
    	
    	RestResponse response =  new RestResponse(token);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
		return response;
    }
    
    @RequestMapping("getUserInfoById")
    @RestReturn(UserInfoFroBiz.class)
    public RestResponse getUserInfoById(@Valid GetUserInfoByIdCommand cmd) {
    	UserInfoFroBiz user = this.userService.getUserInfoById(cmd);
    	RestResponse response =  new RestResponse(user);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
		return response;
    }
    
}
