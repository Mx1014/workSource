// @formatter:off
package com.everhomes.ui.forum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.everhomes.controller.XssExclude;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.forum.ForumService;
import com.everhomes.messaging.MessagingKickoffService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.ui.forum.GetTopicQueryFilterCommand;
import com.everhomes.rest.ui.forum.GetTopicSentScopeCommand;
import com.everhomes.rest.ui.forum.ListNoticeBySceneCommand;
import com.everhomes.rest.ui.forum.NewTopicBySceneCommand;
import com.everhomes.rest.ui.forum.SearchTopicBySceneCommand;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.search.PostSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;

/**
 * <ul>
 * <li>在客户端组件化的过程中，有一些与界面有关的逻辑会放到服务器端</li>
 * <li>专门提供客户端逻辑的API都放到该Controller中，这类API属于比较高层的API，专门服务于界面</li>
 * </ul>
 */
@RestDoc(value="ForumUi controller", site="forumui")
@RestController
@RequestMapping("/ui/forum")
public class ForumUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private ForumService forumService;
    
    @Autowired
    private PostSearcher postSearcher;

    @Autowired
    private MessagingKickoffService kickoffService;
        
    /**
     * <b>URL: /ui/forum/getTopicQueryFilters</b>
     * <p>获取指定条件对应的论坛帖子查询过滤条件列表。</p>
     */
    @RequestMapping("getTopicQueryFilters")
    @RestReturn(value=TopicFilterDTO.class, collection=true)
    @RequireAuthentication(false)
    public RestResponse getTopicQueryFilters(HttpServletRequest request, GetTopicQueryFilterCommand cmd) {
        List<TopicFilterDTO> filterDtoList = forumService.getTopicQueryFilters(cmd);
        
        RestResponse response = new RestResponse(filterDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    } 
    
    /**
     * <b>URL: /ui/forum/getTopicSentScopes</b>
     * <p>获取全局帖子发送范围。</p>
     */
    @RequestMapping("getTopicSentScopes")
    @RestReturn(value=TopicScopeDTO.class, collection=true)
    public RestResponse getTopicSentScopes(GetTopicSentScopeCommand cmd) {
        List<TopicScopeDTO> filterDtoList = forumService.getTopicSentScopes(cmd);
        
        RestResponse response = new RestResponse(filterDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }  
    
    /**
     * <b>URL: /ui/forum/newTopicByScene</b>
     * <p>根据场景创建新帖</p>
     */
    @XssExclude
    @RequestMapping("newTopicByScene")
    @RestReturn(value=PostDTO.class)
    public RestResponse newTopicByScene(@Valid NewTopicBySceneCommand cmd) {
        PostDTO postDto = this.forumService.createTopicByScene(cmd);
        
        RestResponse response = new RestResponse(postDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/forum/searchByScene</b>
     * <p>按指定条件查询符合条件的帖子列表</p>
     */
    @RequestMapping("searchByScene")
    @RestReturn(value=ListPostCommandResponse.class)
    public RestResponse searchByScene(SearchTopicBySceneCommand cmd) {
        ListPostCommandResponse cmdResponse = postSearcher.queryByScene(cmd);
        
        RestResponse response = new RestResponse();
        response.setResponseObject(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/forum/listNoticeByScene</b>
     * <p>根据场景查询公告</p>
     */
    @RequestMapping("listNoticeByScene")
    @RestReturn(value=ListPostCommandResponse.class)
    @RequireAuthentication(false)
    public RestResponse listNoticeByScene(ListNoticeBySceneCommand cmd) {
    	ListPostCommandResponse res = forumService.listNoticeByScene(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
