// @formatter:off
package com.everhomes.oauth2client;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.oauth2client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xq.tian on 2017/3/6.
 */
@RequestMapping("/oauth2cli")
@Controller
public class OAuth2ClientController extends ControllerBase {

    @Autowired
    private OAuth2ClientService oAuth2ClientService;

    /**
     * <p>需要和第三方进行OAuth2认证的功能先访问该接口获取重定向的url</p>
     * <b>URL:/oauth2cli/redirect/{vendor}</b>
     */
    @SuppressDiscover
    // @RequireAuthentication(false)
    @RequestMapping(value = "redirect/{vendor}", method = RequestMethod.GET)
    public String redirect(@PathVariable("vendor") String vendor, @RequestParam String serviceUrl) {
        String redirectUrl = oAuth2ClientService.getRedirectUrl(vendor, serviceUrl);
        return "redirect:" + redirectUrl;
    }

    /**
     * <p>用户授权后第三方重定向到此，此接口获取第三方返回的token</p>
     * <b>URL:/oauth2cli/callback/{vendor}</b>
     */
    @SuppressDiscover
    // @RequireAuthentication(false)
    @RequestMapping(value = "callback/{vendor}", method = RequestMethod.GET)
    public String callback(@PathVariable String vendor, @RequestParam("code") String code) {
        String redirectUrl = oAuth2ClientService.getAccessToken(vendor, code);
        return "redirect:" + redirectUrl;
    }

    /**
     * <p>通用的调用第三方api接口，原样返回第三方返回的数据</p>
     * <b>URL:/oauth2cli/api/{vendor}</b>
     */
    @SuppressDiscover
    @RequestMapping(value = "api/{vendor}")
    @RestReturn(OAuth2ClientApiResponse.class)
    // @RequireAuthentication(false)
    @ResponseBody
    public RestResponse api(@PathVariable String vendor, OAuth2ClientApiCommand cmd) {
        OAuth2ClientApiResponse result = oAuth2ClientService.api(vendor, cmd);

        RestResponse response = new RestResponse(result);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    /**
     * <p>修改oauth2server</p>
     * <b>URL:/oauth2cli/updateOAuth2Server</b>
     */
    @RequestMapping("updateOAuth2Server")
    @ResponseBody
    @RestReturn(String.class)
    public RestResponse updateOauth2Server(UpdateOAuth2ServerCommand cmd) {
        oAuth2ClientService.updateOAuth2Server(cmd);
        return new RestResponse();
    }

    /**
     * <p>创建oauth2server</p>
     * <b>URL:/oauth2cli/createOAuth2Server</b>
     */
    @RequestMapping("createOAuth2Server")
    @ResponseBody
    @RestReturn(OAuth2ServerDTO.class)
    public RestResponse createOauth2Server(CreateOAuth2ServerCommand cmd) {
        OAuth2ServerDTO dto = oAuth2ClientService.createOAuth2Server(cmd);
        return new RestResponse(dto);
    }

    /**
     * <p>获取oauth2server</p>
     * <b>URL:/oauth2cli/getOAuth2Server</b>
     */
    @RequestMapping("getOAuth2Server")
    @ResponseBody
    @RestReturn(OAuth2ServerDTO.class)
    public RestResponse getOauth2Server(GetOAuth2ServerCommand cmd) {
        OAuth2ServerDTO dto = oAuth2ClientService.getOAuth2Server(cmd);
        return new RestResponse(dto);
    }
}
