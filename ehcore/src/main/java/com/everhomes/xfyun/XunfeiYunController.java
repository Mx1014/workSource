package com.everhomes.xfyun;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.xfyun.AfterDealCommand;
import com.everhomes.rest.xfyun.AfterDealResponse;
import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;
import com.everhomes.util.RequireAuthentication;

@RestDoc(value = "Xufei Yun Controller", site = "core")
@RestController
@RequestMapping("/xfyun")
public class XunfeiYunController  extends ControllerBase {
	
	@Autowired
	XunfeiYunService xunfeiYunService;

    @RequestMapping("queryRouters")
    @RestReturn(value=QueryRoutersResponse.class)
    public RestResponse queryRouters(QueryRoutersCommand cmd) {
    	QueryRoutersResponse res = xunfeiYunService.queryRouters(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
	
    
    @RequestMapping("afterDeal") // 后处理
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
	public RestResponse afterDeal(HttpServletRequest req,HttpServletResponse resp) {
    	xunfeiYunService.afterDeal(req, resp);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
}
