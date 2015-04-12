package com.everhomes.fleamarket.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.fleamarket.FleaMarketPostCommand;
import com.everhomes.forum.PostDTO;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/fleamarket")
public class FleaMarketController extends ControllerBase {
    @RequestMapping("post")
    @RestReturn(value=PostDTO.class)
    public RestResponse post(@Valid FleaMarketPostCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
