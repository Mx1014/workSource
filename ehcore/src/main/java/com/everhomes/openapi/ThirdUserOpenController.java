package com.everhomes.openapi;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.GetBizSignatureCommand;
import com.everhomes.user.GetSignatureCommandResponse;
import com.everhomes.user.SynThridUserCommand;
import com.everhomes.user.UserService;

@RestDoc(value="ThirdUser open Controller", site="core")
@RestController
@RequestMapping("/openapi/user")
public class ThirdUserOpenController extends ControllerBase {
	
	@Autowired
	private UserService userService;
	
	/**
     * <b>URL: /openapi/user/synCoupon</b>
     */
    @RequestMapping("synCoupon")
    @RestReturn(String.class)
    public RestResponse synCoupon(@Valid SynThridUserCommand cmd) {
    	Long userId = this.userService.synThridUser(cmd);
    	RestResponse response =  new RestResponse(String.valueOf(userId));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * <b>URL: /openapi/user/getBizSignature</b>
     */
    @RequestMapping("getBizSignature")
    @RestReturn(GetSignatureCommandResponse.class)
    public RestResponse getBizSignature(@Valid GetBizSignatureCommand cmd) {
        
        GetSignatureCommandResponse result = userService.getThirdSignature(cmd);
		RestResponse response =  new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
    

}
