// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uniongroup")
public class UniongroupController extends ControllerBase {

    @Autowired
    private UniongroupService uniongroupService;

    /**
     * <p>保存组配置</p>
     * <b>URL: /uniongroup/saveUniongroupConfigures</b>
     */
    @RequestMapping("saveUniongroupConfigures")
    @RestReturn(String.class)
    public RestResponse createUniongroupConfigures(SaveUniongroupConfiguresCommand cmd) {
        uniongroupService.saveUniongroupConfigures(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}