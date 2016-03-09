// @formatter:off
package com.everhomes.ui.organization;

import java.util.List;

import javax.validation.Valid;

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
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.ui.organization.ListCommunitiesBySceneCommand;
import com.everhomes.rest.ui.organization.ListOrganizationPersonnelsCommand;
import com.everhomes.rest.ui.organization.ListOrganizationPersonnelsResponse;
import com.everhomes.rest.ui.organization.ListScenesByCummunityIdCommand;
import com.everhomes.rest.ui.organization.ListTaskPostsCommand;
import com.everhomes.rest.ui.organization.ListTaskPostsResponse;
import com.everhomes.rest.ui.user.SceneDTO;

/**
 * <ul>
 * <li>客户端的机构相关api</li>
 * </ul>
 */
@RestDoc(value="OrganizationUi controller", site="organizationUi")
@RestController
@RequestMapping("/ui/org")
public class OrganizationUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrganizationService organizationService;
   
    /**
     * <b>URL: /ui/org/listMyTaskPostsByScene</b>
     * <p>获取我的任务贴</p>
     */
    @RequestMapping("listMyTaskPostsByScene")
    @RestReturn(value=ListTaskPostsResponse.class)
    public RestResponse listMyTaskPostsByScene(@Valid ListTaskPostsCommand cmd) {
        
        RestResponse resp =  new RestResponse();
        
//        ListTaskPostsResponse response = null;
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /ui/org/listTaskPostsByScene</b>
     * <p>任务贴列表</p>
     */
    @RequestMapping("listTaskPostsByScene")
    @RestReturn(value=ListTaskPostsResponse.class)
    public RestResponse listTaskPostsByScene(@Valid ListTaskPostsCommand cmd) {
        
        RestResponse resp =  new RestResponse();
        
//        ListTaskPostsResponse response = null;
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /ui/org/listCommunitiesByScene</b>
     * <p>获取小区列表</p>
     */
    @RequestMapping("listCommunitiesByScene")
    @RestReturn(value=ListOrganizationPersonnelsResponse.class)
    public RestResponse listCommunitiesByScene(@Valid ListCommunitiesBySceneCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/org/listOrganizationPersonnels</b>
     * <p>通讯录列表</p>
     */
    @RequestMapping("listOrganizationPersonnels")
    @RestReturn(value=ListOrganizationPersonnelsResponse.class)
    public RestResponse listOrganizationPersonnels(@Valid ListOrganizationPersonnelsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/user/listScenesByCummunityId</b>
     * <p>列出小区当前相关场景。</p>
     */
    @RequestMapping("listScenesByCummunityId")
    @RestReturn(value=SceneDTO.class, collection=true)
    public RestResponse listScenesByCummunityId(ListScenesByCummunityIdCommand cmd) {
        List<SceneDTO> sceneDtoList = null;
        
        RestResponse response = new RestResponse(sceneDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
