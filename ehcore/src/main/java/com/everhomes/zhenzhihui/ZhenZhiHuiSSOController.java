// @formatter:off
package com.everhomes.zhenzhihui;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserAndEnterpriseInfoCommand;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserInfoCommand;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserInfoResponse;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiRedirectCommand;
import com.everhomes.user.UserLogin;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/zhenzhihuisso")
public class ZhenZhiHuiSSOController extends ControllerBase{
    @Autowired
    private ZhenZhiHuiService zhenZhiHuiService;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ZhenZhiHuiSSOController.class);

    /**
     * <b>URL: /zhenzhihuisso/sso</b>
     * <p>圳智慧单点登录</p>
     */
    @RequestMapping("sso")
    @RequireAuthentication(false)
    public Object share(HttpServletRequest request, HttpServletResponse response) {
        String location = zhenZhiHuiService.ssoService(request,response);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(location));
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            LOGGER.error("redirect failed, location = {}", location);
        }
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
        restResponse.setErrorDescription("invalid token or redirect");
        return restResponse;
    }

    /**
     * <b>URL: /zhenzhihuisso/createUserInfo</b>
     * <p>填写圳智慧所需用户信息</p>
     */
    @RequestMapping("createUserInfo")
    @RestReturn(value = CreateZhenZhiHuiUserInfoResponse.class)
    public Object createUserInfo(CreateZhenZhiHuiUserInfoCommand cmd) {
        CreateZhenZhiHuiUserInfoResponse res = zhenZhiHuiService.createZhenzhihuiUserInfo(cmd);
        RestResponse restResponse = new RestResponse(res);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /zhenzhihuisso/createUserAndEnterpriseInfo</b>
     * <p>填写圳智慧所需用户信息</p>
     */
    @RequestMapping("createUserAndEnterpriseInfo")
    @RestReturn(value = CreateZhenZhiHuiUserInfoResponse.class)
    public Object createUserAndEnterpriseInfo(CreateZhenZhiHuiUserAndEnterpriseInfoCommand cmd) {
        CreateZhenZhiHuiUserInfoResponse res = zhenZhiHuiService.createZhenzhihuiUserAndEnterpriseInfo(cmd);

        RestResponse restResponse = new RestResponse(res);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /zhenzhihuisso/zhenzhihuiRedirect</b>
     * <p>圳智慧跳转URL</p>
     */
    @RequestMapping("zhenzhihuiRedirect")
    public Object zhenzhihuiRedirect(ZhenZhiHuiRedirectCommand cmd) {
        String location = zhenZhiHuiService.zhenzhihuiRedirect(cmd);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(location));
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            LOGGER.error("redirect failed, location = {}", location);
        }
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
        restResponse.setErrorDescription("invalid token or redirect");
        return restResponse;
    }
}
