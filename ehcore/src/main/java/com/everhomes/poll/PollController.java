package com.everhomes.poll;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.poll.PollDTO;
import com.everhomes.poll.PollPostCommand;
import com.everhomes.poll.PollShowResultCommand;
import com.everhomes.poll.PollShowResultResponse;
import com.everhomes.poll.PollVoteCommand;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/poll")
public class PollController extends ControllerBase {

    @RequestMapping("post")
    @RestReturn(value=PollDTO.class)
    public RestResponse signup(@Valid PollPostCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("vote")
    @RestReturn(value=PollDTO.class)
    public RestResponse signup(@Valid PollVoteCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("showResult")
    @RestReturn(value=PollShowResultResponse.class)
    public RestResponse showResult(@Valid PollShowResultCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
