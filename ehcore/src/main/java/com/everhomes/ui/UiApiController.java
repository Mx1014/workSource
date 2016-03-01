// @formatter:off
package com.everhomes.ui;

import java.util.List;

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
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.ui.GetTopicQueryFilterCommand;
import com.everhomes.rest.ui.GetTopicSentScopeCommand;
import com.everhomes.rest.ui.SceneDTO;
import com.everhomes.rest.ui.TopicFilterDTO;
import com.everhomes.rest.ui.TopicScopeDTO;

/**
 * <ul>
 * <li>在客户端组件化的过程中，有一些与界面有关的逻辑会放到服务器端</li>
 * <li>专门提供客户端逻辑的API都放到该Controller中</li>
 * </ul>
 */
@RestDoc(value="UiApi controller", site="uiapi")
@RestController
@RequestMapping("/ui")
public class UiApiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(UiApiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    /**
     * <b>URL: /ui/listUserRelatedScenes</b>
     * <p>列出用户当前域空间下的相关场景。</p>
     * <p>必须在请求的Header中提供域空间。</p>
     */
    @RequestMapping("listUserRelatedScenes")
    @RestReturn(value=SceneDTO.class, collection=true)
    public RestResponse listUserRelatedScenes() {
        List<SceneDTO> sceneDtoList = null;
        
        RestResponse response = new RestResponse(sceneDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    } 
    
    /**
     * <b>URL: /ui/getTopicQueryFilters</b>
     * <p>获取指定条件对应的论坛帖子查询过滤条件列表。</p>
     */
    @RequestMapping("getTopicQueryFilters")
    @RestReturn(value=TopicFilterDTO.class, collection=true)
    public RestResponse getTopicQueryFilters(GetTopicQueryFilterCommand cmd) {
        List<TopicFilterDTO> filterDtoList = null;
        
        RestResponse response = new RestResponse(filterDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    } 
    
    /**
     * <b>URL: /ui/getTopicSentScopes</b>
     * <p>获取全局帖子发送范围。</p>
     */
    @RequestMapping("getTopicSentScopes")
    @RestReturn(value=TopicScopeDTO.class)
    public RestResponse getTopicQueryScopes(GetTopicSentScopeCommand cmd) {
        List<TopicScopeDTO> filterDtoList = null;
        
        RestResponse response = new RestResponse(filterDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }  
}
