// @formatter:off
package com.everhomes.comment;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.XssExclude;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.comment.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestDoc(value="comment controller", site="comment")
@RestController
@RequestMapping("/comment")
public class CommentController extends ControllerBase {
    
    @Autowired
    private CommentService CommentService;


    /**
     * <b>URL: /comment/addComment</b>
     * <p>创建新评论</p>
     */
    @XssExclude
    @RequestMapping("addComment")
    @RestReturn(value=CommentDTO.class)
    public RestResponse addComment(@Valid AddCommentCommand cmd) {
        CommentDTO commentDto = CommentService.addComment(cmd);

        RestResponse response = new RestResponse(commentDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /comment/listComments</b>
     * <p>获取指定对象下的评论列表</p>
     */
    @RequestMapping("listComments")
    @RestReturn(value=ListCommentsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listComments(@Valid ListCommentsCommand cmd) {
        ListCommentsResponse listCommentsResponse = CommentService.listComments(cmd);
        
        RestResponse response = new RestResponse(listCommentsResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /comment/deleteComment</b>
     * <p>删除指定指定评论（需要有删评论权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deleteComment")
    @RestReturn(value=String.class)
    public RestResponse deleteComment(DeleteCommonCommentCommand cmd) {
        CommentService.deleteComment(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /comment/getComment</b>
     * <p>获取指定评论</p>
     * @return 
     */
    @RequestMapping("getComment")
    @RestReturn(value=GetCommentsResponse.class)
    public RestResponse getComment(GetCommentCommand cmd) {
    	GetCommentsResponse rsp = CommentService.getComment(cmd);
        RestResponse response = new RestResponse(rsp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
