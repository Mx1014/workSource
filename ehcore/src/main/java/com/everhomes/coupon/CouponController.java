package com.everhomes.coupon;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.coupon.CouponDTO;
import com.everhomes.rest.coupon.CouponPostCommand;

@RestController
@RequestMapping("/coupon")
public class CouponController extends ControllerBase {
    
    @RequestMapping("post")
    @RestReturn(value=CouponDTO.class)
    public RestResponse post(@Valid CouponPostCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
