package com.everhomes.organization;

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
import com.everhomes.organization.pm.ListPropMemberCommandResponse;
import com.everhomes.organization.pm.ListPropPostCommandResponse;
import com.everhomes.organization.pm.QueryPropTopicByCategoryCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;

@RestController
@RequestMapping("/org")
public class OrganizationController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

	@Autowired
	OrganizationService organizationService;
	
 	/**
	 * <b>URL: /org/getUserOwningOrganizations</b>
	 * <p>查询用户加入的政府机构</p>
	*/
    @RequestMapping("getUserOwningOrganizations")
    @RestReturn(value=ListOrganizationMemberCommandResponse.class)
    public RestResponse getUserOwningOrganizations() {
    	ListOrganizationMemberCommandResponse commandResponse = organizationService.getUserOwningOrganizations();
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /org/listOrgMembers</b>
     * <p>查询政府机构成员列表</p>
     */
    @RequestMapping("listOrgMembers")
    @RestReturn(value=ListPropMemberCommandResponse.class)
    public RestResponse listOrgMembers(@Valid ListOrganizationMemberCommand cmd) {
    	ListOrganizationMemberCommandResponse commandResponse = organizationService.listOrganizationMembers(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/addOrgMemberByPhone</b>
     * <p>通过手机添加府机构成员</p>
     * @return 添加的结果
     */
    @RequestMapping("addOrgMemberByPhone")
    @RestReturn(value=String.class)
    public RestResponse addOrgMemberByPhone(@Valid CreateOrganizationMemberCommand cmd) {
    	
    	organizationService.createOrganizationMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/findUserByIndentifier</b>
     * <p>根据用户token查询用户信息</p>
     */
    @RequestMapping("findUserByIndentifier")
    @RestReturn(value=UserTokenCommandResponse.class)
    public RestResponse findUserByIndentifier(@Valid UserTokenCommand cmd) {
        UserTokenCommandResponse commandResponse = organizationService.findUserByIndentifier(cmd);
        RestResponse response = new RestResponse(commandResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /org/newOrgTopic</b>
     * <p>物业人员发帖</p>
     * @return 发帖的内容
     */
    @RequestMapping("newOrgTopic")
    @RestReturn(value=PostDTO.class)
    public RestResponse newOrgTopic(@Valid NewTopicCommand cmd) {
    	PostDTO postDto = organizationService.createTopic(cmd);
    	RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/queryOrgTopicsByCategory</b>
     * <p>按指定类型查询的帖子列表（仅查询社区论坛）</p>
     */
    @RequestMapping("queryOrgTopicsByCategory")
    @RestReturn(value=ListPropPostCommandResponse.class)
    public RestResponse queryOrgTopicsByCategory(QueryPropTopicByCategoryCommand cmd) {
    	ListPropPostCommandResponse  cmdResponse = organizationService.queryTopicsByCategory(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/listOrgTopics</b>
     * <p>查询指定论坛的帖子列表（不区分类型查询）</p>
     */
    @RequestMapping("listOrgTopics")
    @RestReturn(value=ListPostCommandResponse.class)
    public RestResponse listOrgTopics(ListTopicCommand cmd) {
        ListPostCommandResponse cmdResponse = organizationService.listTopics(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/getOrgTopic</b>
     * <p>获取指定论坛里的指定帖子内容</p>
     */
    @RequestMapping("getOrgTopic")
    @RestReturn(value=PostDTO.class)
    public RestResponse getOrgTopic(GetTopicCommand cmd) {
        PostDTO postDto = organizationService.getTopic(cmd);
        
        RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/likeOrgTopic</b>
     * <p>对指定论坛里的指定帖子点赞</p>
     * @return 点赞的结果
     */
    @RequestMapping("likeOrgTopic")
    @RestReturn(value=String.class)
    public RestResponse likeOrgTopic(LikeTopicCommand cmd) {
        organizationService.likeTopic(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/cancelLikeOrgTopic</b>
     * <p>对指定论坛里的指定帖子取消赞</p>
     * @return 取消赞的结果
     */
    @RequestMapping("cancelLikeOrgTopic")
    @RestReturn(value=String.class)
    public RestResponse cancelLikeOrgTopic(CancelLikeTopicCommand cmd) {
        organizationService.cancelLikeTopic(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/deleteOrgTopic</b>
     * <p>删除指定论坛里的指定帖子（需要有删帖权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deleteOrgTopic")
    @RestReturn(value=String.class)
    public RestResponse deleteOrgTopic(DeleteTopicCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
//    /**
//     * <b>URL: /org/forwardTopic</b>
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
//     * <b>URL: /org/makeTop</b>
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
//     * <b>URL: /org/search</b>
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
     * <b>URL: /org/listOrgTopicComments</b>
     * <p>获取指定论坛里指定帖子下的评论列表</p>
     */
    @RequestMapping("listOrgTopicComments")
    @RestReturn(value=PostDTO.class, collection=true)
    public RestResponse listOrgTopicComments(@Valid ListTopicCommentCommand cmd) {
        ListPostCommandResponse cmdResponse = organizationService.listTopicComments(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/newOrgComment</b>
     * <p>创建新评论</p>
     */
    @RequestMapping("newOrgComment")
    @RestReturn(value=PostDTO.class)
    public RestResponse newOrgComment(@Valid NewCommentCommand cmd) {
        PostDTO postDTO = organizationService.createComment(cmd);
        
        RestResponse response = new RestResponse(postDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/deleteOrgComment</b>
     * <p>删除指定论坛里的指定评论（需要有删评论权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deleteOrgComment")
    @RestReturn(value=String.class)
    public RestResponse deleteOrgComment(DeleteCommentCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
}
