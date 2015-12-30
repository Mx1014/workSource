package com.everhomes.poll;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.poll.PollService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.poll.PollDTO;
import com.everhomes.rest.poll.PollShowResultCommand;
import com.everhomes.rest.poll.PollShowResultResponse;
import com.everhomes.rest.poll.PollVoteCommand;

@RestController
@RequestMapping("/poll")
public class PollController extends ControllerBase {

    @Autowired
    private PollService pollService;

//    @RequestMapping("post")
//    @RestReturn(value = PollDTO.class)
//    public RestResponse signup(@Valid PollPostCommand cmd) {
//        PollDTO val = pollService.createPoll(cmd);
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        response.setResponseObject(val);
//        return response;
//    }

    @RequestMapping("vote")
    @RestReturn(value = PollDTO.class)
    public RestResponse signup(@Valid PollVoteCommand cmd) {
        PollDTO dto = pollService.createVote(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(dto);
        return response;
    }

    @RequestMapping("showResult")
    @RestReturn(value = PollShowResultResponse.class)
    public RestResponse showResult(@Valid PollShowResultCommand cmd) {
        PollShowResultResponse rsp = pollService.showResult(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(rsp);
        return response;
    }
}
