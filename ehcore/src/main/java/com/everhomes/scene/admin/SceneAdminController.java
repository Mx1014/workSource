// @formatter:off
package com.everhomes.scene.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.scene.ListSceneTypesCommand;
import com.everhomes.rest.scene.SceneTypeInfoDTO;
import com.everhomes.scene.SceneService;

@RestDoc(value="Scene admin controller", site="core")
@RestController
@RequestMapping("/admin/scene")
public class SceneAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneAdminController.class);
    
    @Autowired
    private SceneService sceneService;
    
    /**
     * <b>URL: /admin/scene/listSceneTypes</b>
     * <p>获取场景类型</p>
     */
    @RequestMapping("listSceneTypes")
    @RestReturn(value=SceneTypeInfoDTO.class, collection=true)
    public RestResponse listSceneTypes(ListSceneTypesCommand cmd){
        List<SceneTypeInfoDTO> cmdResponse = sceneService.listSceneTypes(cmd);
        RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
