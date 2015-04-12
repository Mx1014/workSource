package com.everhomes.forum.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.forum.ForwardTopicCommand;
import com.everhomes.forum.ListTopicCommand;
import com.everhomes.forum.ListTopicCommentCommand;
import com.everhomes.forum.MakeTopCommand;
import com.everhomes.forum.NewCommentCommand;
import com.everhomes.forum.NewTopicCommand;
import com.everhomes.forum.PostDTO;
import com.everhomes.forum.SearchTopicCommand;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/forum")
public class ForumController extends ControllerBase {
    
    @RequestMapping("newTopic")
    @RestReturn(value=Long.class)
    public RestResponse newTopic(@Valid NewTopicCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("newComment")
    @RestReturn(value=Long.class)
    public RestResponse newComment(@Valid NewCommentCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("likeTopic")
    @RestReturn(value=Long.class)
    public RestResponse likeTopic(
            @RequestParam(value = "forumId", required = true) Long forumId,
            @RequestParam(value = "topicId", required = true) Long topicId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("deleteTopic")
    @RestReturn(value=String.class)
    public RestResponse deleteTopic(
            @RequestParam(value = "forumId", required = true) Long forumId,
            @RequestParam(value = "topicId", required = true) Long topicId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("deleteComment")
    @RestReturn(value=String.class)
    public RestResponse deleteComment(
            @RequestParam(value = "forumId", required = true) Long forumId,
            @RequestParam(value = "commentId", required = true) Long commentId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("cancelLikeTopic")
    @RestReturn(value=Long.class)
    public RestResponse cancelLikeTopic(
            @RequestParam(value = "forumId", required = true) Long forumId,
            @RequestParam(value = "topicId", required = true) Long topicId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("forwardTopic")
    @RestReturn(value=Long.class)
    public RestResponse forwardTopic(@Valid ForwardTopicCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("makeTop")
    @RestReturn(value=String.class)
    public RestResponse makeTop(@Valid MakeTopCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("getTopic")
    @RestReturn(value=PostDTO.class)
    public RestResponse getTopic(@RequestParam(value = "forumId", required = true) Long forumId,
            @RequestParam(value = "topicId", required = true) Long topicId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listTopicComments")
    @RestReturn(value=PostDTO.class, collection=true)
    public RestResponse listTopicComments(@Valid ListTopicCommentCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listUserPostedTopics")
    @RestReturn(value=PostDTO.class, collection=true)
    public RestResponse listUserPostedTopics() {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listTopics")
    @RestReturn(value=PostDTO.class, collection=true)
    public RestResponse listNeighborhoodTopics(ListTopicCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listUserRelatedTopics")
    @RestReturn(value=PostDTO.class, collection=true)
    public RestResponse listUserRelatedTopics() {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("search")
    @RestReturn(value=PostDTO.class, collection=true)
    public RestResponse search(SearchTopicCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
