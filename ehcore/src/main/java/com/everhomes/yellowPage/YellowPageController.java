package com.everhomes.yellowPage;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.yellowPage.AddYellowPageCommand;
import com.everhomes.rest.yellowPage.DeleteYellowPageCommand;
import com.everhomes.rest.yellowPage.GetYellowPageDetailCommand;
import com.everhomes.rest.yellowPage.GetYellowPageListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageTopicCommand;
import com.everhomes.rest.yellowPage.UpdateYellowPageCommand;
import com.everhomes.rest.yellowPage.YellowPageDTO;
import com.everhomes.rest.yellowPage.YellowPageListResponse;
import com.everhomes.util.RequireAuthentication;
 

@RestController
@RequestMapping("/yellowPage")
public class YellowPageController  extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageController.class);

    @Autowired
    private YellowPageService yellowPageService;
    
    @RequireAuthentication(false)
    @RequestMapping("getYellowPageDetail")
    @RestReturn(value=YellowPageDTO.class)
    public RestResponse getYellowPageDetail(@Valid GetYellowPageDetailCommand cmd) {
    	YellowPageDTO res = this.yellowPageService.getYellowPageDetail(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }

    @RequireAuthentication(false)
    @RequestMapping("getYellowPageTopic")
    @RestReturn(value=YellowPageDTO.class)
    public RestResponse getYellowPageTopic(@Valid GetYellowPageTopicCommand cmd) {
    	YellowPageDTO res = this.yellowPageService.getYellowPageTopic(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    @RequireAuthentication(false)
    @RequestMapping("getYellowPageList")
    @RestReturn(value=YellowPageListResponse.class)
    public RestResponse getYellowPageList(@Valid GetYellowPageListCommand cmd) {
    	YellowPageListResponse res = this.yellowPageService.getYellowPageList(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    
    @RequestMapping("addYellowPage")
    @RestReturn(value=String.class)
    public RestResponse addYellowPage(@Valid AddYellowPageCommand cmd) {
    	 this.yellowPageService.addYellowPage(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    @RequestMapping("deleteYellowPage")
    @RestReturn(value=String.class)
    public RestResponse deleteYellowPage(@Valid DeleteYellowPageCommand cmd) {
    	 this.yellowPageService.deleteYellowPage(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }

    
    @RequestMapping("updateYellowPage")
    @RestReturn(value=String.class)
    public RestResponse updateYellowPage(@Valid UpdateYellowPageCommand cmd) {
    	 this.yellowPageService.updateYellowPage(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
}
