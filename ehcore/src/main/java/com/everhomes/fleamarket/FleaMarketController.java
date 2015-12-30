package com.everhomes.fleamarket;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.fleamarket.FleaMarketService;
import com.everhomes.forum.Post;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.fleamarket.FleaMarketPostCommand;
import com.everhomes.rest.fleamarket.FleaMarketUpdateCommand;
import com.everhomes.rest.forum.PostDTO;

@RestController
@RequestMapping("/fleamarket")
public class FleaMarketController extends ControllerBase {
    
    @Autowired
    private FleaMarketService fleaMarketService;
    
    @RequestMapping("post")
    @RestReturn(value=PostDTO.class)
    public RestResponse post(@Valid FleaMarketPostCommand cmd) {
        Post post = fleaMarketService.postItemToForum(cmd);
        
        // TODO Post to PostDTO convertion
        PostDTO postDto = new PostDTO();
        
        RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("update")
    @RestReturn(value=String.class)
    public RestResponse post(@Valid FleaMarketUpdateCommand cmd) {
        
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
