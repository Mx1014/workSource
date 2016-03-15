// @formatter:off
package com.everhomes.ui.privilege;


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
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeCommand;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeResponse;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.util.WebTokenGenerator;

/**
 * <ul>
 * <li>客户端的权限入口相关api</li>
 * </ul>
 */
@RestDoc(value="privilege controller", site="privilegeUi")
@RestController
@RequestMapping("/ui/privilege")
public class PrivilegeUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    /**
     * <b>URL: /ui/privilege/getEntranceByPrivilege</b>
     * <p>根据权限获取不同的入口</p>
     */
    @RequestMapping("getEntranceByPrivilege")
    @RestReturn(value=GetEntranceByPrivilegeResponse.class)
    public RestResponse getEntranceByPrivilege(@Valid GetEntranceByPrivilegeCommand cmd) {
    	WebTokenGenerator webToken = WebTokenGenerator.getInstance();
 	    SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
 	    GetEntranceByPrivilegeResponse res = null;
 		if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
 			res = organizationService.getEntranceByPrivilege(cmd, sceneToken.getEntityId());
 		}
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
