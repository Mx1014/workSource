package com.everhomes.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.RequireAuthentication;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.LoginToken;
import com.everhomes.user.LogonByTokenCommand;
import com.everhomes.user.LogonCommand;
import com.everhomes.user.LogonCommandResponse;
import com.everhomes.user.ResendVerificationCodeCommand;
import com.everhomes.user.SetPasswordCommand;
import com.everhomes.user.SignupCommand;
import com.everhomes.user.SignupToken;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserServiceErrorCode;
import com.everhomes.user.UserServiceProvider;
import com.everhomes.user.VerifyAndLogonCommand;
import com.everhomes.util.PasswordHash;
import com.everhomes.util.RuntimeErrorException;

@RestController
@RequestMapping("/user")
public class UserController extends ControllerBase {

    @Autowired
    private UserServiceProvider userProvider;
    
    public UserController() {
    }
    
    @RequestMapping("signup")
    @RequireAuthentication(false)
    @RestReturn(String.class)
    public RestResponse signup(@Valid SignupCommand cmd) {
        IdentifierType type = IdentifierType.fromString(cmd.getType());
        SignupToken token = userProvider.signup(type, cmd.getToken());
        
        return new RestResponse(token.getTokenString());
    }
    
    @RequestMapping("resendVerificationCode")
    @RequireAuthentication(false)
    @RestReturn(String.class)
    public RestResponse resendVerificationCode(@Valid ResendVerificationCodeCommand cmd) {
        SignupToken token = SignupToken.fromTokenString(cmd.getSignupToken());
        if(token == null) {
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, 
                UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
        }
        this.userProvider.resendVerficationCode(token);
        return new RestResponse("OK");
    }
    
    @RequestMapping("verifyAndLogon")
    @RequireAuthentication(false)
    @RestReturn(LogonCommandResponse.class)
    public RestResponse verifyAndLogon(@Valid VerifyAndLogonCommand cmd, HttpServletResponse response) {
        SignupToken token = SignupToken.fromTokenString(cmd.getSignupToken());
        if(token == null) {
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, 
                UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
        }
        
        UserLogin login = this.userProvider.verifyAndLogon(token, cmd.getVerificationCode(), cmd.getDeviceIdentifier());
        LoginToken loginToken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber());
        String tokenString = loginToken.getTokenString();
        Cookie cookie = new Cookie("token", tokenString);
        response.addCookie(cookie);
        LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
        return new RestResponse(cmdResponse);
    }
    
    @RequestMapping("logon")
    @RequireAuthentication(false)
    @RestReturn(LogonCommandResponse.class)
    public RestResponse logon(@Valid LogonCommand cmd, HttpServletResponse response) {
        UserLogin login = this.userProvider.logon(cmd.getUserIdentifier(), cmd.getPassword(), cmd.getDeviceIdentifier());
        LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber());
        String tokenString = token.getTokenString();
        Cookie cookie = new Cookie("token", tokenString);
        cookie.setPath("/");
        response.addCookie(cookie);
        LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
        return new RestResponse(cmdResponse);
    }
    
    @RequestMapping("logonByToken")
    @RequireAuthentication(false)
    @RestReturn(LogonCommandResponse.class)
    public RestResponse logonByToken(@Valid LogonByTokenCommand cmd, HttpServletResponse response) {
        LoginToken loginToken = LoginToken.fromTokenString(cmd.getLoginToken());
        if(loginToken == null)
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_LOGIN_TOKEN, "Invalid login token");
        
        UserLogin login = this.userProvider.logonByToken(loginToken);
        LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber());
        String tokenString = token.getTokenString();
        Cookie cookie = new Cookie("token", tokenString);
        cookie.setPath("/");
        response.addCookie(cookie);
        LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
        return new RestResponse(cmdResponse);
    }
    
    @RequestMapping("setPassword")
    @RestReturn(String.class)
    public RestResponse setPassword(@Valid SetPasswordCommand cmd) throws Exception {
        User user = UserContext.current().getUser();
        
        boolean allowChange = false;
        if(cmd.getOldPassword() == null && user.getPasswordHash() == null) {
            allowChange = true;
        } else if(cmd.getOldPassword() != null && user.getPasswordHash() != null) {
            allowChange = PasswordHash.validatePassword(cmd.getOldPassword(), user.getPasswordHash());
        }
        
        if(allowChange) {
            user.setPasswordHash(PasswordHash.createHash(cmd.getNewPassword()));
            this.userProvider.updateUser(user);
            return new RestResponse("OK");
        }

        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_OLD_PASSWORD_NOT_MATCH, "Old password does not match");
    }
    
    @RequestMapping("logoff")
    @RestReturn(String.class)
    public RestResponse logoff(HttpServletResponse response) {
        UserLogin login = UserContext.current().getLogin();
        this.userProvider.logoff(login);
        
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new RestResponse("OK");
    }
}
