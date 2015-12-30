package com.everhomes.ecard;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.ecard.ECardPostCommand;
import com.everhomes.rest.forum.PostDTO;

@RestController
@RequestMapping("/ecard")
public class ECardController extends ControllerBase {

    @RequestMapping("post")
    @RestReturn(value=PostDTO.class)
    public RestResponse post(@Valid ECardPostCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}

