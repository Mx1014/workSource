package com.everhomes.realestate;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.realestate.RealEstatePostCommand;

@RestController
@RequestMapping("/realestate")
public class RealestateController extends ControllerBase {
    
    @RequestMapping("post")
    @RestReturn(value=PostDTO.class)
    public RestResponse signup(@Valid RealEstatePostCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
