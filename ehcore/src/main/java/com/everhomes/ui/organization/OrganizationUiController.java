// @formatter:off
package com.everhomes.ui.organization;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;
import com.everhomes.rest.ui.organization.ListTaskPostsCommand;
import com.everhomes.rest.ui.organization.ListTaskPostsResponse;
import com.everhomes.util.EtagHelper;

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
   
    /**
     * <b>URL: /ui/org/listMyTaskPostsByScene</b>
     * <p>获取我的任务贴</p>
     */
    @RequestMapping("listMyTaskPostsByScene")
    @RestReturn(value=PostDTO.class,collection=true)
    public RestResponse listMyTaskPostsByScene(@Valid ListTaskPostsCommand cmd) {
        
        List<BannerDTO> result = null;
        RestResponse resp =  new RestResponse();
        
        ListTaskPostsResponse response = null;
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /ui/org/listTaskPostsByScene</b>
     * <p>任务贴列表</p>
     */
    @RequestMapping("listTaskPostsByScene")
    @RestReturn(value=PostDTO.class,collection=true)
    public RestResponse listTaskPostsByScene(@Valid ListTaskPostsCommand cmd) {
        
        List<BannerDTO> result = null;
        RestResponse resp =  new RestResponse();
        
        ListTaskPostsResponse response = null;
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
}
