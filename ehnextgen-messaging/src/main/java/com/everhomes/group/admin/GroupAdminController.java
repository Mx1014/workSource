// @formatter:off
package com.everhomes.group.admin;


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
import com.everhomes.group.GroupService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommand;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.group.GetGroupCommand;
import com.everhomes.rest.group.ListGroupCommand;
import com.everhomes.rest.group.ListGroupCommandResponse;
import com.everhomes.rest.group.ListNearbyGroupCommandResponse;
import com.everhomes.rest.group.SearchGroupCommand;
import com.everhomes.rest.group.SearchGroupTopicAdminCommand;
import com.everhomes.search.SearchSyncManager;
import com.everhomes.search.SearchSyncType;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestDoc(value="Group admin controller", site="core")
@RestController
@RequestMapping("/admin/group")
public class GroupAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupAdminController.class);
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private SearchSyncManager searchSyncManager;
    
    /**
     * <b>URL: /admin/group/syncGroupIndex</b>
     * <p>同步group的搜索索引</p>
     */
    @RequestMapping("syncGroupIndex")
    @RestReturn(value=String.class)
    public RestResponse syncGroupSearchIndexes() {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        searchSyncManager.SyncDb(SearchSyncType.GROUP);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/group/searchGroup</b>
     * <p>管理员搜索兴趣圈</p>
     * @param cmd 参数命令 
     * @return 搜索结果
     */
    @RequestMapping("searchGroup")
    @RestReturn(value=ListGroupCommandResponse.class)
    public RestResponse searchGroup(ListGroupCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListGroupCommandResponse cmdResponse = groupService.listGroups(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/group/deleteGroup</b>
     * <p>管理员删除兴趣圈</p>
     * @param cmd 参数命令 
     * @return 搜索结果
     */
    @RequestMapping("deleteGroup")
    @RestReturn(value=String.class)
    public RestResponse deleteGroup(GetGroupCommand cmd){
//    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.groupService.deleteGroup(cmd.getGroupId());
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /admin/group/searchGroupTopics</b>
     * <p>管理员搜索帖子</p>
     * @param cmd 参数命令 
     * @return 搜索结果
     */
    @RequestMapping("searchGroupTopics")
    @RestReturn(value=SearchTopicAdminCommandResponse.class)
    public RestResponse searchGroupTopics(SearchGroupTopicAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        SearchTopicAdminCommandResponse cmdResponse = groupService.searchGroupTopics(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
}
