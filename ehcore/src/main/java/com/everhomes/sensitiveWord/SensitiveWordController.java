// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensitive")
public class SensitiveWordController extends ControllerBase {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @RequestMapping("uploadFileToContentServer")
    @RestReturn(String.class)
    public RestResponse uploadFileToContentServer() {
        this.sensitiveWordService.uploadFileToContentServer();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
