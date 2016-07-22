package com.everhomes.wanke;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.wanke.ListCommunityCommand;
import com.everhomes.rest.wanke.ListCommunityResponse;
import com.everhomes.rest.wanke.ListCommunityServiceCommand;
import com.everhomes.rest.wanke.ListCommunityServiceResponse;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.wanke.ServiceConfService;

/**
 * 园区：含住宅小区（即平时所说的小区）和商用园区（如科技园）
 */
@RestDoc(value="ServiceConf controller", site="core")
@RestController
@RequestMapping("/serviceConf")
public class ServiceConfController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfController.class);
    
    @Autowired
    private ServiceConfService serviceConfService;
    
    /**
     * <b>URL: /serviceConf/listCommunityServices</b>
     * <p>根据园区获取园区服务</p>
     */
    @RequestMapping("listCommunityServices")
    @RestReturn(value=ListCommunityServiceResponse.class)
    public RestResponse listCommunityServices(ListCommunityServiceCommand cmd) {
    	ListCommunityServiceResponse listCommunityServiceResponse = serviceConfService.listCommunityServices(cmd);
        RestResponse response =  new RestResponse(listCommunityServiceResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /serviceConf/loginAndGetCommunities</b>
     * <p>根据第三方信息登录并获取园区列表</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("loginAndGetCommunities")
    @RestReturn(value=ListCommunityResponse.class)
    public RestResponse loginAndGetCommunities(ListCommunityCommand cmd, HttpServletRequest req, HttpServletResponse resp) {
     	ListCommunityResponse ListCommunityResponse = serviceConfService.loginAndGetCommunities(cmd, req, resp);
        RestResponse response =  new RestResponse(ListCommunityResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
