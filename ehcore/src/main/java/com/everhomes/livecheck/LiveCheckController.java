package com.everhomes.livecheck;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.user.FetchMessageCommandResponse;
import com.everhomes.rest.user.FetchRecentToPastMessageAdminCommand;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;

@RequestMapping("/livecheck")
@Controller
public class LiveCheckController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiveCheckController.class);
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private MessagingService messageServce;
    
    @RequestMapping("home")
    @RequireAuthentication(value=true)
    public String home(
            @RequestParam(value = "uid", required = false) String uid, 
            @RequestParam(value = "namespaceId", required = false) Integer namespaceId,
            @RequestParam(value = "mobile", required = false) String mobile, 
            Model model) {
        
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
                UserContext.current().getUser().getId(), Privilege.Write, null)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        try {
            if(uid != null && uid.length() > 0) {
                model.addAttribute("userInfo", userService.getUserInfo(Long.parseLong(uid)));
                model.addAttribute("logins", userService.listUserLogins(Long.parseLong(uid)));
                return "livecheck/user-info";
            } else if(mobile != null && mobile.length() > 0) {
                User user = this.userService.findUserByIndentifier(namespaceId, mobile);
                if(user != null) {
                    Long locatedUid = user.getId();
                    model.addAttribute("userInfo", userService.getUserInfo(locatedUid));
                    model.addAttribute("logins", userService.listUserLogins(locatedUid));
                    return "livecheck/user-info";
                }
            }
        } catch(Exception e) {
            LOGGER.error("Unhandled exception", e);
        }
        
        return "livecheck/home";
    }
    
    @RequestMapping("loginDetail")
    @RequireAuthentication(value=true)
    public String loginDetails(
            @RequestParam(value = "uid", required=true) Long uid,
            @RequestParam(value = "loginId", required=true) Integer loginId, 
            @RequestParam(value = "anchor", required=false) Long anchor, 
            Model model) {
        
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
                UserContext.current().getUser().getId(), Privilege.Write, null)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        List<UserLogin> logins = userService.listUserLogins(uid);
        for(UserLogin login: logins) {
            if(login.getLoginId() == loginId.intValue()) {
                model.addAttribute("login", login);
                
                FetchRecentToPastMessageAdminCommand cmd = new FetchRecentToPastMessageAdminCommand();
                cmd.setUserId(uid);
                cmd.setLoginId(loginId);
                cmd.setNamespaceId(login.getNamespaceId());
                cmd.setAppId(AppConstants.APPID_MESSAGING);
                cmd.setAnchor(anchor);
                
                FetchMessageCommandResponse cmdResponse = this.messageServce.fetchRecentToPastMessages(cmd);
                model.addAttribute("messages", cmdResponse.getMessages());
                if(anchor != null)
                    model.addAttribute("anchor", anchor);
            }
        }
        
        return "livecheck/login-detail";
    }
}
