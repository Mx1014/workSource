// @formatter:off
package com.everhomes.forum;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.CancelLikeTopicCommand;
import com.everhomes.rest.forum.CheckUserPostCommand;
import com.everhomes.rest.forum.CheckUserPostDTO;
import com.everhomes.rest.forum.DeleteCommentCommand;
import com.everhomes.rest.forum.DeleteTopicCommand;
import com.everhomes.rest.forum.FreeStuffCommand;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.IncreasePostViewCountCommand;
import com.everhomes.rest.forum.LikeTopicCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.ListTopicCommand;
import com.everhomes.rest.forum.ListTopicCommentCommand;
import com.everhomes.rest.forum.ListUserRelatedTopicCommand;
import com.everhomes.rest.forum.LostAndFoundCommand;
import com.everhomes.rest.forum.MakeTopCommand;
import com.everhomes.rest.forum.NewCommentCommand;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PublishTopicCommand;
import com.everhomes.rest.forum.QueryTopicByCategoryCommand;
import com.everhomes.rest.forum.QueryTopicByEntityAndCategoryCommand;
import com.everhomes.rest.forum.SearchTopicCommand;
import com.everhomes.rest.forum.UsedAndRentalCommand;
import com.everhomes.search.PostSearcher;
import com.everhomes.search.SearchSyncManager;
import com.everhomes.search.SearchSyncType;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.user.UserContext;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;

@RestDoc(value="Forum controller", site="forum")
@RestController
@RequestMapping("/forum")
public class ForumController extends ControllerBase {
    
    @Autowired
    private ForumService forumService;
    
    @Autowired
    PostSearcher postSearcher;
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private SearchSyncManager searchSyncManager;
    
    /**
     * <b>URL: /forum/newTopic</b>
     * <p>创建新帖</p>
     */
    @RequestMapping("newTopic")
    @RestReturn(value=PostDTO.class)
    public RestResponse newTopic(@Valid NewTopicCommand cmd) {
        PostDTO postDto = this.forumService.createTopic(cmd);
        
        RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/getTopic</b>
     * <p>获取指定论坛里的指定帖子内容</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("getTopic")
    @RestReturn(value=PostDTO.class)
    public RestResponse getTopic(GetTopicCommand cmd) {
        PostDTO postDto = this.forumService.getTopic(cmd);
		
        RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/listTopics</b>
     * <p>查询指定论坛的帖子列表（不区分类型查询）</p>
     */
    @RequestMapping("listTopics")
    @RestReturn(value=ListPostCommandResponse.class)
    @RequireAuthentication(false)
    public RestResponse listTopics(ListTopicCommand cmd) {
        ListPostCommandResponse cmdResponse = this.forumService.listTopics(cmd);
		
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/listUserRelatedTopics</b>
     * <p>查询与请求者所有相关的帖</p>
     */
    @RequestMapping("listUserRelatedTopics")
    @RestReturn(value=ListPostCommandResponse.class)
    public RestResponse listUserRelatedTopics(ListUserRelatedTopicCommand cmd) {
        ListPostCommandResponse cmdResponse = this.forumService.listUserRelatedTopics(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/checkUserPostStatus</b>
     * <p>检查请求者是否有新帖</p>
     */
    @RequestMapping("checkUserPostStatus")
    @RestReturn(value=CheckUserPostDTO.class)
    public RestResponse checkUserPostStatus(CheckUserPostCommand cmd) {
        CheckUserPostDTO cmdResponse = this.forumService.checkUserPostStatus(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/queryTopicsByCategory</b>
     * <p>按指定类型查询的帖子列表（仅查询社区论坛）</p>
     */
    @RequestMapping("queryTopicsByCategory")
    @RestReturn(value=ListPostCommandResponse.class)
    public RestResponse queryTopicsByCategory(QueryTopicByCategoryCommand cmd) {
        ListPostCommandResponse cmdResponse = this.forumService.queryTopicsByCategory(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/queryTopicsByEntityAndCategory</b>
     * <p>按指定类型查询的帖子列表（仅查询社区论坛）</p>
     */
    @RequestMapping("queryTopicsByEntityAndCategory")
    @RestReturn(value=ListPostCommandResponse.class)
    public RestResponse queryTopicsByEntityAndCategory(QueryTopicByEntityAndCategoryCommand cmd) {
        ListPostCommandResponse cmdResponse = this.forumService.queryTopicsByEntityAndCategory(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/likeTopic</b>
     * <p>对指定论坛里的指定帖子点赞</p>
     * @return 点赞的结果
     */
    @RequestMapping("likeTopic")
    @RestReturn(value=String.class)
    public RestResponse likeTopic(LikeTopicCommand cmd) {
        this.forumService.likeTopic(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/cancelLikeTopic</b>
     * <p>对指定论坛里的指定帖子取消赞</p>
     * @return 取消赞的结果
     */
    @RequestMapping("cancelLikeTopic")
    @RestReturn(value=String.class)
    public RestResponse cancelLikeTopic(CancelLikeTopicCommand cmd) {
        this.forumService.cancelLikeTopic(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/deleteTopic</b>
     * <p>删除指定论坛里的指定帖子（需要有删帖权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deleteTopic")
    @RestReturn(value=String.class)
    public RestResponse deleteTopic(DeleteTopicCommand cmd) {
        this.forumService.deletePost(cmd.getForumId(), cmd.getTopicId(), cmd.getCurrentOrgId(), cmd.getOwnerType(), cmd.getOwnerId());
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/forwardTopic</b>
     * <p>转发帖子</p>
     * @return 转发结果
     */
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
    
    /**
     * <b>URL: /forum/makeTop</b>
     * <p>置顶帖子</p>
     * @return 置顶结果
     */
    @RequestMapping("makeTop")
    @RestReturn(value=String.class)
    public RestResponse makeTop(@Valid MakeTopCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/search</b>
     * <p>按指定条件查询符合条件的帖子列表</p>
     */
    @RequestMapping("search")
    @RestReturn(value=ListPostCommandResponse.class)
    public RestResponse search(SearchTopicCommand cmd) {
        ListPostCommandResponse cmdResponse = postSearcher.query(cmd);
        
        RestResponse response = new RestResponse();
        response.setResponseObject(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/listTopicComments</b>
     * <p>获取指定论坛里指定帖子下的评论列表</p>
     */
    @RequestMapping("listTopicComments")
    @RestReturn(value=ListPostCommandResponse.class)
    @RequireAuthentication(false)
    public RestResponse listTopicComments(@Valid ListTopicCommentCommand cmd) {
        ListPostCommandResponse cmdResponse = this.forumService.listTopicComments(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /forum/newComment</b>
     * <p>创建新评论</p>
     */
    @RequestMapping("newComment")
    @RestReturn(value=PostDTO.class)
    public RestResponse newComment(@Valid NewCommentCommand cmd) {
        PostDTO postDTO = this.forumService.createComment(cmd);
        
        RestResponse response = new RestResponse(postDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/deleteComment</b>
     * <p>删除指定论坛里的指定评论（需要有删评论权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deleteComment")
    @RestReturn(value=String.class)
    public RestResponse deleteComment(DeleteCommentCommand cmd) {
        this.forumService.deletePost(cmd.getForumId(), cmd.getCommentId(), cmd.getCurrentOrgId(), cmd.getOwnerType(), cmd.getOwnerId());
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/updateUsedAndRental</b>
     * <p>更新二手与租售信息</p>
     * @return 更新结果
     */
    @RequestMapping("updateUsedAndRental")
    @RestReturn(value=String.class)
    public RestResponse updateUsedAndRental(UsedAndRentalCommand cmd) {
        this.forumService.updateUsedAndRental(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/updateFreeStuff</b>
     * <p>更新免费物品信息</p>
     * @return 更新结果
     */
    @RequestMapping("updateFreeStuff")
    @RestReturn(value=String.class)
    public RestResponse updateFreeStuff(FreeStuffCommand cmd) {
        this.forumService.updateFreeStuff(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/updateLostAndFound</b>
     * <p>更新失物招领信息</p>
     * @return 更新结果
     */
    @RequestMapping("updateLostAndFound")
    @RestReturn(value=String.class)
    public RestResponse updateLostAndFound(LostAndFoundCommand cmd) {
        this.forumService.updateLostAndFound(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("syncTest")
    @RestReturn(value=String.class)
    public RestResponse syncTest() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
                UserContext.current().getUser().getId(), Privilege.Write, null)) {
            
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
            }
        
        searchSyncManager.SyncDb(SearchSyncType.POST);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/increasePostViewCount</b>
     * <p>增加阅读量计数</p>
     */
    @RequestMapping("increasePostViewCount")
    @RestReturn(value=PostDTO.class)
    @RequireAuthentication(false)
    public RestResponse increasePostViewCount(IncreasePostViewCountCommand cmd) {
//        PostDTO postDto = this.forumService.getTopic(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /forum/publishTopic</b>
     * <p>发布活动，将后台暂存的活动发布</p>
     * @return 删除结果
     */
    @RequestMapping("publishTopic")
    @RestReturn(value=String.class)
    public RestResponse publisTopic(PublishTopicCommand cmd) {
        this.forumService.publisTopic(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
