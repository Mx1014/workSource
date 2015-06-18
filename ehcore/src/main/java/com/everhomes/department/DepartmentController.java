package com.everhomes.department;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.forum.CancelLikeTopicCommand;
import com.everhomes.forum.DeleteCommentCommand;
import com.everhomes.forum.DeleteTopicCommand;
import com.everhomes.forum.GetTopicCommand;
import com.everhomes.forum.LikeTopicCommand;
import com.everhomes.forum.ListPostCommandResponse;
import com.everhomes.forum.ListTopicCommand;
import com.everhomes.forum.ListTopicCommentCommand;
import com.everhomes.forum.NewCommentCommand;
import com.everhomes.forum.NewTopicCommand;
import com.everhomes.forum.PostDTO;
import com.everhomes.pm.ListPropMemberCommandResponse;
import com.everhomes.pm.ListPropPostCommandResponse;
import com.everhomes.pm.QueryPropTopicByCategoryCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;

@RestController
@RequestMapping("/dpt")
public class DepartmentController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	DepartmentService departmentService;
	
 	/**
	 * <b>URL: /dpt/getUserOwningDepartments</b>
	 * <p>查询用户加入的政府机构</p>
	*/
    @RequestMapping("getUserOwningDepartments")
    @RestReturn(value=ListDptMemberCommandResponse.class)
    public RestResponse getUserOwningDepartments() {
    	ListDptMemberCommandResponse commandResponse = departmentService.getUserOwningDepartments();
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /dpt/listDptMembers</b>
     * <p>查询政府机构成员列表</p>
     */
    @RequestMapping("listDptMembers")
    @RestReturn(value=ListPropMemberCommandResponse.class)
    public RestResponse listDptMembers(@Valid ListDptMemberCommand cmd) {
    	ListDptMemberCommandResponse commandResponse = departmentService.listDptMembers(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/addDptMemberByPhone</b>
     * <p>通过手机添加府机构成员</p>
     * @return 添加的结果
     */
    @RequestMapping("addDptMemberByPhone")
    @RestReturn(value=String.class)
    public RestResponse addDptMemberByPhone(@Valid CreateDepartmentMemberCommand cmd) {
    	
    	departmentService.createDepartmentMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/findUserByIndentifier</b>
     * <p>根据用户token查询用户信息</p>
     */
    @RequestMapping("findUserByIndentifier")
    @RestReturn(value=UserTokenCommandResponse.class)
    public RestResponse findUserByIndentifier(@Valid UserTokenCommand cmd) {
        UserTokenCommandResponse commandResponse = departmentService.findUserByIndentifier(cmd);
        RestResponse response = new RestResponse(commandResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /dpt/newPmTopic</b>
     * <p>物业人员发帖</p>
     * @return 发帖的内容
     */
    @RequestMapping("newPmTopic")
    @RestReturn(value=PostDTO.class)
    public RestResponse newPmTopic(@Valid NewTopicCommand cmd) {
    	PostDTO postDto = departmentService.createTopic(cmd);
    	RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/queryPmTopicsByCategory</b>
     * <p>按指定类型查询的帖子列表（仅查询社区论坛）</p>
     */
    @RequestMapping("queryPmTopicsByCategory")
    @RestReturn(value=ListPropPostCommandResponse.class)
    public RestResponse queryPmTopicsByCategory(QueryPropTopicByCategoryCommand cmd) {
    	ListPropPostCommandResponse  cmdResponse = departmentService.queryTopicsByCategory(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/listPmTopics</b>
     * <p>查询指定论坛的帖子列表（不区分类型查询）</p>
     */
    @RequestMapping("listPmTopics")
    @RestReturn(value=ListPostCommandResponse.class)
    public RestResponse listPmTopics(ListTopicCommand cmd) {
        ListPostCommandResponse cmdResponse = departmentService.listTopics(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/getPmTopic</b>
     * <p>获取指定论坛里的指定帖子内容</p>
     */
    @RequestMapping("getPmTopic")
    @RestReturn(value=PostDTO.class)
    public RestResponse getPmTopic(GetTopicCommand cmd) {
        PostDTO postDto = departmentService.getTopic(cmd);
        
        RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/likePmTopic</b>
     * <p>对指定论坛里的指定帖子点赞</p>
     * @return 点赞的结果
     */
    @RequestMapping("likePmTopic")
    @RestReturn(value=String.class)
    public RestResponse likePmTopic(LikeTopicCommand cmd) {
        departmentService.likeTopic(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/cancelLikePmTopic</b>
     * <p>对指定论坛里的指定帖子取消赞</p>
     * @return 取消赞的结果
     */
    @RequestMapping("cancelLikePmTopic")
    @RestReturn(value=String.class)
    public RestResponse cancelLikePmTopic(CancelLikeTopicCommand cmd) {
        departmentService.cancelLikeTopic(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/deletePmTopic</b>
     * <p>删除指定论坛里的指定帖子（需要有删帖权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deletePmTopic")
    @RestReturn(value=String.class)
    public RestResponse deleteTopic(DeleteTopicCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
//    /**
//     * <b>URL: /dpt/forwardTopic</b>
//     * <p>转发帖子</p>
//     * @return 转发结果
//     */
//    @RequestMapping("forwardTopic")
//    @RestReturn(value=Long.class)
//    public RestResponse forwardTopic(@Valid ForwardTopicCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
//    /**
//     * <b>URL: /dpt/makeTop</b>
//     * <p>置顶帖子</p>
//     * @return 置顶结果
//     */
//    @RequestMapping("makeTop")
//    @RestReturn(value=String.class)
//    public RestResponse makeTop(@Valid MakeTopCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
//    /**
//     * <b>URL: /dpt/search</b>
//     * <p>按指定条件查询符合条件的帖子列表</p>
//     */
//    @RequestMapping("search")
//    @RestReturn(value=ListPostCommandResponse.class)
//    public RestResponse search(SearchTopicCommand cmd) {
//        ListPostCommandResponse cmdResponse = postSearcher.query(cmd);
//        
//        RestResponse response = new RestResponse();
//        response.setResponseObject(cmdResponse);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
   
    /**
     * <b>URL: /dpt/listPmTopicComments</b>
     * <p>获取指定论坛里指定帖子下的评论列表</p>
     */
    @RequestMapping("listPmTopicComments")
    @RestReturn(value=PostDTO.class, collection=true)
    public RestResponse listPmTopicComments(@Valid ListTopicCommentCommand cmd) {
        ListPostCommandResponse cmdResponse = departmentService.listTopicComments(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /dpt/newPmComment</b>
     * <p>创建新评论</p>
     */
    @RequestMapping("newPmComment")
    @RestReturn(value=PostDTO.class)
    public RestResponse newPmComment(@Valid NewCommentCommand cmd) {
        PostDTO postDTO = departmentService.createComment(cmd);
        
        RestResponse response = new RestResponse(postDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /dpt/deletePmComment</b>
     * <p>删除指定论坛里的指定评论（需要有删评论权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deletePmComment")
    @RestReturn(value=String.class)
    public RestResponse deletePmComment(DeleteCommentCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
}
