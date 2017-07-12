// @formatter:off
package com.everhomes.ui.group;

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
import com.everhomes.family.FamilyService;
import com.everhomes.group.GroupService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.group.ListNearbyGroupCommandResponse;
import com.everhomes.rest.ui.group.ListNearbyGroupBySceneCommand;
import com.everhomes.user.UserService;

/**
 * <ul>
 * <li>在客户端组件化的过程中，有一些与界面有关的逻辑会放到服务器端</li>
 * <li>专门提供客户端逻辑的API都放到该Controller中，这类API属于比较高层的API，专门服务于界面</li>
 * </ul>
 */
@RestDoc(value="GroupUi controller", site="groupui")
@RestController
@RequestMapping("/ui/group")
public class GroupUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private GroupService groupService;

    /**
     * <b>URL: /ui/group/listNearbyGroupsByScene</b>
     * <p>根据指定的场景查询周边兴趣圈。</p>
     */
    @RequestMapping(value = "listNearbyGroupsByScene")
    @RestReturn(value=ListNearbyGroupCommandResponse.class)
    public RestResponse listNearbyGroupsByScene(ListNearbyGroupBySceneCommand cmd) {
    
        ListNearbyGroupCommandResponse cmdResponse = this.groupService.listNearbyGroupsByScene(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
