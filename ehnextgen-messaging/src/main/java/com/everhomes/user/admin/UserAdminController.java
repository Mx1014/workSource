package com.everhomes.user.admin;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.link.RichLinkDTO;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.admin.*;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequireAuthentication(true)
@RequestMapping("/admin/user")
public class UserAdminController extends ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAdminController.class);

    @Autowired
    private UserAdminService userAdminService;
    
    @Autowired
	 private UserService userService;
    
    @Autowired
    private MessagingService messageServce;

    @RequestMapping("listVerifyCode")
    @RestReturn(ListVerfyCodeResponse.class)
    public RestResponse listVerifyCode(@Valid PaginationCommand cmd) {
        // check acl
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListVerfyCodeResponse object = new ListVerfyCodeResponse();
        Tuple<Long, List<UserIdentifier>> result = userAdminService.listVerifyCode(cmd);
        object.setValues(result.second().stream().map(r -> {
            UserIdentifierDTO userIdentfier = ConvertHelper.convert(r, UserIdentifierDTO.class);
            userIdentfier.setVerifyCode(r.getVerificationCode());
            return userIdentfier;
        }).collect(Collectors.toList()));
        object.setNextPageAnchor(result.first());
        return new RestResponse(object);
    }

    @RequestMapping("listVest")
    @RestReturn(ListVestResponse.class)
    public RestResponse listVest(@Valid PaginationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListVestResponse object = new ListVestResponse();
        Tuple<Long, List<UserInfo>> result = userAdminService.listVets(cmd);
        object.setValues(result.second());
        object.setNextPageAnchor(result.first());
        return new RestResponse(object);
    }

    @RequestMapping("listRegisterUsers")
    @RestReturn(ListRegisterUsersResponse.class)
    public RestResponse listRegisterUsers(@Valid PaginationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListRegisterUsersResponse object = new ListRegisterUsersResponse();
        Tuple<Long, List<UserInfo>> result = userAdminService.listRegisterUsers(cmd);
        object.setValues(result.second());
        object.setNextPageAnchor(result.first());
        return new RestResponse(object);
    }

    @RequestMapping("getUserByIdentifier")
    @RestReturn(UserInfo.class)
    public RestResponse getUserByIdentifier(@RequestParam String identifier) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        // TODO: think about namespaceId
        UserInfo result = userAdminService.findUserByIdentifier(identifier);
        return new RestResponse(result);
    }
    
    /**
	 * 获得有地址用户列表
	 * @return
	 */
	@RequestMapping("listUsersWithAddr")
	@RestReturn(ListUsersWithAddrResponse.class)
	public RestResponse listUsersWithAddr(@Valid ListUsersWithAddrCommand cmd) {
	    SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
		List<ListUsersWithAddrResponse> result = this.userService.listUsersWithAddr(cmd);
		
		 RestResponse response = new RestResponse(result);
		 response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
		
		 return response;
		
	}
	
	/**
     * <p>导入用户信息excel</p>
     */
    @RequestMapping("importUserData")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importUserData(@RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null。userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
		ImportDataResponse importDataResponse = importUserInfo(files[0], userId);
        RestResponse response = new RestResponse(importDataResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
	private ImportDataResponse importUserInfo(MultipartFile mfile, Long userId) {
		ImportDataResponse importDataResponse = new ImportDataResponse();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());
			
			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
						"File content is empty");
			}
			LOGGER.debug("Start import data...,total:" + resultList.size());
			//导入数据，返回导入错误的日志数据集
			List<String> errorDataLogs = userAdminService.importUserData(convertToStrList(resultList), userId);
			LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
			if(null == errorDataLogs || errorDataLogs.isEmpty()){
				LOGGER.debug("Data import all success...");
			}else{
				//记录导入错误日志
				for (String log : errorDataLogs) {
					LOGGER.error(log);
				}
			}
			
			importDataResponse.setTotalCount((long)resultList.size()-1);
			importDataResponse.setFailCount((long)errorDataLogs.size());
			importDataResponse.setLogs(errorDataLogs);
		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return importDataResponse;
	}
	
	
	 private List<String> convertToStrList(List list) {
			List<String> result = new ArrayList<String>();
			boolean firstRow = true;
			for (Object o : list) {
				if(firstRow){
					firstRow = false;
					continue;
				}
				RowResult r = (RowResult)o;
				StringBuffer sb = new StringBuffer();
				sb.append(r.getA()).append("||");
				sb.append(r.getB()).append("||");
				sb.append(r.getC()).append("||");
				sb.append(r.getD()).append("||");
				sb.append(r.getE());
				result.add(sb.toString());
			}
			return result;
	}
	
	 /*
	 * 搜索有地址用户列表
	 */
	
	@RequestMapping("searchUsersWithAddr")
	@RestReturn(UsersWithAddrResponse.class)
	public RestResponse searchUsersWithAddr(@Valid SearchUsersWithAddrCommand cmd) {
	    SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        UsersWithAddrResponse result = this.userService.searchUsersWithAddr(cmd);
		
		 RestResponse response = new RestResponse(result);
		 response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
		
		 return response;
		
	}
	
	/**
	 * 
	 * 获得邀请用户列表
	 * @return
	 */
	@RequestMapping("listInvitatedUser")
	@RestReturn(ListInvitatedUserResponse.class)
	public RestResponse listInvitatedUser(@Valid ListInvitatedUserCommand cmd) {
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
		ListInvitatedUserResponse result = this.userService.listInvitatedUser(cmd);
		
		 RestResponse response = new RestResponse(result);
		 response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
		
		 return response;
		
	}
	
	/**
	 * 
	 * 搜索邀请用户列表
	 * @return
	 */
	@RequestMapping("searchInvitatedUser")
	@RestReturn(ListInvitatedUserResponse.class)
	public RestResponse searchInvitatedUser(@Valid SearchInvitatedUserCommand cmd) {
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
		ListInvitatedUserResponse result = this.userService.searchInvitatedUser(cmd);
		
		 RestResponse response = new RestResponse(result);
		 response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
		
		 return response;
		
	}
    
    /**
     * 
     * 发用户测试短信
     * @return
     */
    @RequestMapping("sendUserTestSms")
    @RestReturn(String.class)
    public RestResponse sendUserTestSms(@Valid SendUserTestSmsCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.userService.sendUserTestSms(cmd);
        
         RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
        
         return response;
        
    }
    
    /**
     * 
     * 发用户测试邮件
     * @return
     */
    @RequestMapping("sendUserTestMail")
    @RestReturn(String.class)
    public RestResponse sendUserTestMail(@Valid SendUserTestMailCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.userService.sendUserTestMail(cmd);
        
         RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
        
         return response;
        
    }
    
    /**
     * 
     * 发用户含rich链接的测试消息
     * @return
     */
    @RequestMapping("sendUserRichLinkMessage")
    @RestReturn(RichLinkDTO.class)
    public RestResponse sendUserRichLinkMessage(SendUserTestRichLinkMessageCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RichLinkDTO dto = this.userService.sendUserRichLinkMessage(cmd);
        
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
        
    }
    
    /**
     * 
     * 生成测试用户密码
     * @return
     */
    @RequestMapping("encryptPlainText")
    @RestReturn(EncriptInfoDTO.class)
    public RestResponse encryptPlainText(@Valid EncryptPlainTextCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        EncriptInfoDTO info = new EncriptInfoDTO();
        info.setPlainText(cmd.getPlainText());
        
        String salt = EncryptionUtils.createRandomSalt();
        info.setSalt(salt);
        
        try {
            String plainTextHash = EncryptionUtils.hashPassword(cmd.getPlainText());
            info.setPlainTextHash(plainTextHash);
            
            String encryptText = EncryptionUtils.hashPassword(String.format("%s%s", plainTextHash, salt));
            info.setEncryptText(encryptText);
        } catch (Exception e) {
            LOGGER.error("Failed to encrypt paint text", e);
        }
        
        RestResponse response = new RestResponse(info);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
	
    /**
     * 
     * 生成测试用户密码
     * @return
     */
    @RequestMapping("createUserImpersonation")
    @RestReturn(UserImpersonationDTO.class)
    public RestResponse createUserImpersonation(@Valid CreateUserImpersonationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse response = new RestResponse(userService.createUserImpersonation(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        if(response.getResponseObject() == null) {
            response.setErrorDescription("User not found");
        } else {
            response.setErrorDescription("OK");
        }
        
        
        return response;
    }
    
    /**
     * 
     * 生成测试用户密码
     * @return
     */
    @RequestMapping("deleteUserImpersonation")
    @RestReturn(UserImpersonationDTO.class)
    public RestResponse createUserImpersonation(@Valid DeleteUserImpersonationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        userService.deleteUserImpersonation(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        if(response.getResponseObject() == null) {
            response.setErrorDescription("User not found");
        } else {
            response.setErrorDescription("OK");
        }
        
        
        return response;
    }
    
    /**
     * 
     * 生成测试用户密码
     * @return
     */
    @RequestMapping("listUserImpersonation")
    @RestReturn(SearchUserImpersonationResponse.class)
    public RestResponse createUserImpersonation(@Valid SearchUserImpersonationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse response = new RestResponse(userService.listUserImpersons(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * 
     * 列出用户
     * @return
     */
    @RequestMapping("searchUserByNamespace")
    @RestReturn(ListRegisterUsersResponse.class)
    public RestResponse searchUserByNamepsace(@Valid SearchUserByNamespaceCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse response = new RestResponse(userService.searchUserByNamespace(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    @RequestMapping("loginMessages")
    @RestReturn(FetchMessageCommandResponse.class)
    public RestResponse loginMessages(@Valid FetchRecentToPastMessageAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        List<UserLogin> logins = userService.listUserLogins(cmd.getUserId());
        for(UserLogin login: logins) {
            if(login.getLoginId() == cmd.getLoginId().intValue()) {
                FetchMessageCommandResponse cmdResponse = this.messageServce.fetchRecentToPastMessagesAny(cmd);
                response.setResponseObject(cmdResponse);
            }
        }
        
        return response;
    }

    /**
     * <b>URL: /admin/user/listUserAppealLogs</b>
     * <p>用户申诉列表</p>
     */
    @RequestMapping("listUserAppealLogs")
    @RestReturn(ListUserAppealLogsResponse.class)
    public RestResponse listUserAppealLogs(@Valid ListUserAppealLogsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /admin/user/updateUserAppealLog</b>
     * <p>修改用户申诉状态</p>
     */
    @RequestMapping("updateUserAppealLog")
    @RestReturn(UserAppealLogDTO.class)
    public RestResponse listUserAppealLogs(@Valid UpdateUserAppealLogCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }
}
