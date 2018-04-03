package com.everhomes.pictureValidate;

import com.everhomes.PictureValidate.PictureValidateService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pictureValidate.ValidateCodeCommand;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/PictureValidate")
public class PictureValidateController extends ControllerBase {

    @Autowired
    private PictureValidateService pictureValidateService;
   
    @RequestMapping("newPicture")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse newPicture(HttpServletRequest request) {

        String pictureStr = pictureValidateService.newPicture(request);
        RestResponse response = new RestResponse(pictureStr);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    @RequestMapping("validateCode")
    @RestReturn(value=Boolean.class)
    @RequireAuthentication(false)
    public RestResponse validateCode(HttpServletRequest request, ValidateCodeCommand cmd) {

        Boolean flag = pictureValidateService.validateCode(request, cmd.getCode());
        RestResponse response = new RestResponse(flag);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    

}
