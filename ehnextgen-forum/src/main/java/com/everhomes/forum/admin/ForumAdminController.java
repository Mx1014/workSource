// @formatter:off
package com.everhomes.forum.admin;


import java.util.List;

import com.everhomes.rest.forum.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.forum.ForumService;
import com.everhomes.group.GroupService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.AssignTopicScopeCommand;
import com.everhomes.rest.forum.AssignedScopeDTO;
import com.everhomes.rest.forum.DeleteCommentCommand;
import com.everhomes.rest.forum.DeleteTopicCommand;
import com.everhomes.rest.forum.ListTopicAssignedScopeCommand;
import com.everhomes.search.SearchSyncManager;
import com.everhomes.search.SearchSyncType;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestDoc(value="Forum admin controller", site="core")
@RestController
@RequestMapping("/admin/forum")
public class ForumAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumAdminController.class);
    
    @Autowired
    private ForumService forumService;
    
    @Autowired
    private SearchSyncManager searchSyncManager;
    
    /**
     * <b>URL: /admin/fourm/syncPostIndex</b>
     * <p>同步Post的搜索索引</p>
     */
    @RequestMapping("syncPostIndex")
    @RestReturn(value=String.class)
    public RestResponse syncPostSearchIndexes() {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        searchSyncManager.SyncDb(SearchSyncType.POST);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/fourm/deleteTopic</b>
     * <p>删除指定论坛里的指定帖子（需要有删帖权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deleteTopic")
    @RestReturn(value=String.class)
    public RestResponse deleteTopic(DeleteTopicAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.forumService.deletePost(cmd.getForumId(), cmd.getTopicId());
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/fourm/deleteComment</b>
     * <p>删除指定论坛里的指定评论（需要有删评论权限）</p>
     * @return 删除结果
     */
    @RequestMapping("deleteComment")
    @RestReturn(value=String.class)
    public RestResponse deleteComment(DeleteCommentAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.forumService.deletePost(cmd.getForumId(), cmd.getCommentId());
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /admin/forum/searchTopics</b>
     * <p>管理员搜索帖子</p>
     * @param cmd 参数命令 
     * @return 搜索结果
     */
    @RequestMapping("searchTopics")
    @RestReturn(value=SearchTopicAdminCommandResponse.class)
    public RestResponse searchTopics(SearchTopicAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        SearchTopicAdminCommandResponse cmdResponse = forumService.searchTopic(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/forum/assignTopicScope</b>
     * <p>置热帖或取消热帖</p>
     * @param cmd 置热帖或取消热帖命令 
     * @return 置热帖结果
     */
    @RequestMapping("assignTopicScope")
    @RestReturn(value=String.class)
    public RestResponse assignTopicScope(AssignTopicScopeCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        forumService.assignTopicScope(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/forum/listTopicAssignedScope</b>
     * <p>列指定热帖的范围</p>
     * @param cmd 范围命令 
     * @return 范围
     */
    @RequestMapping("listTopicAssignedScope")
    @RestReturn(value=String.class)
    public RestResponse listTopicAssignedScope(ListTopicAssignedScopeCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        List<AssignedScopeDTO> scopes = forumService.listTopicAssignedScope(cmd);
        
        RestResponse response = new RestResponse(scopes);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/forum/searchComments</b>
     * <p>管理员搜索评论</p>
     * @param cmd 参数命令 
     * @return 搜索结果
     */
    @RequestMapping("searchComments")
    @RestReturn(value=SearchTopicAdminCommandResponse.class)
    public RestResponse searchComments(SearchTopicAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        SearchTopicAdminCommandResponse cmdResponse = forumService.searchComment(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
