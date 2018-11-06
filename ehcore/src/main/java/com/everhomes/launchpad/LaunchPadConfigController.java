package com.everhomes.launchpad;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.launchpadbase.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestDoc(value="launchPadConfig controller", site="core")
@RestController
@RequestMapping("/launchPadConfig")
public class LaunchPadConfigController extends ControllerBase {

    @Autowired
    private LaunchPadConfigService launchPadConfigService;

//    /**
//     * <b>URL: /launchPadConfig/createLaunchPadConfig</b>
//     * <p>创建广场配置</p>
//     */
//    @RequestMapping("createLaunchPadConfig")
//    @RestReturn(value=LaunchPadConfigDTO.class)
//    public RestResponse createLaunchPadConfig(CreateLaunchPadConfigCommand cmd) {
//        LaunchPadConfigDTO dto = launchPadConfigService.createLaunchPadConfig(cmd);
//        RestResponse response =  new RestResponse(dto);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }


    /**
     * <b>URL: /launchPadConfig/updateLaunchPadConfig</b>
     * <p>更新广场配置</p>
     */
    @RequestMapping("updateLaunchPadConfig")
    @RestReturn(value=LaunchPadConfigDTO.class)
    public RestResponse updateLaunchPadConfig(UpdateLaunchPadConfigCommand cmd) {

        LaunchPadConfigDTO dto = launchPadConfigService.updateLaunchPadConfig(cmd);

        RestResponse response =  new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchPadConfig/findLaunchPadConfig</b>
     * <p>查询广场配置</p>
     */
    @RequestMapping("findLaunchPadConfig")
    @RestReturn(value=LaunchPadConfigDTO.class)
    public RestResponse findLaunchPadConfig(FindLaunchPadConfigCommand cmd) {

        LaunchPadConfigDTO dto = launchPadConfigService.findLaunchPadConfig(cmd);

        RestResponse response =  new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /launchPadConfig/deleteLaunchPadConfig</b>
     * <p>删除广场配置</p>
     */
    @RequestMapping("deleteLaunchPadConfig")
    @RestReturn(value=String.class)
    public RestResponse deleteLaunchPadConfig(DeleteLaunchPadConfigCommand cmd) {
        launchPadConfigService.deleteLaunchPadConfig(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
