// @formatter:off
package com.everhomes.appwhitelist;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.appwhitelist.ListAppWhiteListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestDoc(value="AppWhiteList controller", site="appWwhiteList")
@RestController
@RequestMapping("/appwhitelist")
public class AppWhiteListController extends ControllerBase {

    @Autowired
    private AppWhiteListService appWhiteListService;

    @RequestMapping("listAppWhiteList")
    @RestReturn(value = ListAppWhiteListResponse.class)
    public RestResponse listAppWhiteList() {
        RestResponse response = new RestResponse(appWhiteListService.listAppWhiteList());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
