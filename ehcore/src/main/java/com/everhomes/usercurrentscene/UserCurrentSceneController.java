package com.everhomes.usercurrentscene;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO;
import com.everhomes.rest.usercurrentscene.GetUserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestDoc(value="UserCurrentScene  controller", site="core")
@RestController
@RequestMapping("/userCurrentScene")
public class UserCurrentSceneController extends ControllerBase {


    /**
     * <b>URL: /userCurrentScene/getUserCurrentSceneByUid</b>
     * <p>通过人员ID查询人员所在场景</p>
     */
    @RequestMapping("getUserCurrentSceneByUid")
    @RestReturn(value=UserCurrentSceneDTO.class)
    public RestResponse getUserCurrentSceneByUid(GetUserCurrentSceneCommand cmd) {

        UserCurrentSceneDTO resultDTO =null; //configurationsService.getConfigurationById(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }


    /**
     * <b>URL: /userCurrentScene/saveUserCurrentScene</b>
     * <p>保存人员所在场景</p>
     */
    @RequestMapping("saveUserCurrentScene")
    @RestReturn(value=UserCurrentSceneDTO.class)
    public RestResponse saveUserCurrentScene(UserCurrentSceneCommand cmd) {

        UserCurrentSceneDTO resultDTO =null; //configurationsService.getConfigurationById(cmd);
        RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
    }

    /**
     * <p>设置response 成功信息</p>
     * @param response
     */
    private void setResponseSuccess(RestResponse response){
        if(response == null ) return ;

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
    }
}
