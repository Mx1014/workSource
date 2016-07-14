// @formatter:off
package com.everhomes.ui.community;

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
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.organization.ListCommunitiesByOrganizationIdCommand;
import com.everhomes.rest.ui.organization.ListCommunitiesBySceneCommand;
import com.everhomes.rest.ui.organization.ListCommunitiesBySceneResponse;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.util.WebTokenGenerator;

/**
 * <ul>
 * <li>客户端的小区相关api</li>
 * </ul>
 */
@RestDoc(value="CommunityUi controller", site="communityUi")
@RestController
@RequestMapping("/ui/community")
public class CommunityUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrganizationService organizationService;

    /**
     * <b>URL: /ui/org/listCommunitiesByScene</b>
     * <p>获取小区列表</p>
     */
    @RequestMapping("listCommunitiesByScene")
    @RestReturn(value=ListCommunitiesBySceneResponse.class)
    public RestResponse listCommunitiesByScene(@Valid ListCommunitiesBySceneCommand cmd) {
    	ListCommunitiesBySceneResponse resp = new ListCommunitiesBySceneResponse();
    	WebTokenGenerator webToken = WebTokenGenerator.getInstance();
 	    SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);

// 	   sceneToken = new SceneTokenDTO();
// 	   sceneToken.setEntityType(UserCurrentEntityType.ORGANIZATION.getCode());
// 	   sceneToken.setEntityId(1000001l);
 	    
 		if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
 			List<CommunityDTO> dtos = organizationService.listAllChildrenOrganizationCoummunities(sceneToken.getEntityId());
 			resp.setDtos(dtos);
 		}
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
