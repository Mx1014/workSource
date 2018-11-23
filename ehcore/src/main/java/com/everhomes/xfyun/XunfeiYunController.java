package com.everhomes.xfyun;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;

@RestDoc(value = "Xufei Yun Controller", site = "core")
@RestController
@RequestMapping("/xfyun")
public class XunfeiYunController {
	
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
	
}
