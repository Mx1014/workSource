// @formatter:off
package com.everhomes.activity;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.AclProvider;
import com.everhomes.activity.ActivityCheckinCommand;
import com.everhomes.activity.ActivityDTO;
import com.everhomes.activity.ActivityListCommand;
import com.everhomes.activity.ActivityListResponse;
import com.everhomes.activity.ActivitySignupCommand;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/activity")
public class ActivityController extends ControllerBase {

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private AclProvider aclProvider;
    
//    @RequestMapping("post")
//    @RestReturn(value=ActivityDTO.class)
//    public RestResponse signup(@Valid ActivityPostCommand cmd) {
//        ActivityDTO result = activityService.createPost(cmd);
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        response.setResponseObject(result);
//        return response;
//    }
    
    @RequestMapping("signup")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse signup(@Valid ActivitySignupCommand cmd) {
        ActivityDTO result = activityService.signup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    @RequestMapping("cancelSignup")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse cacnelSignup(@Valid ActivityCancelSignupCommand cmd) {
        ActivityDTO result = activityService.cancelSignup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    @RequestMapping("checkin")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse checkin(@Valid ActivityCheckinCommand cmd) {
        ActivityDTO result = activityService.checkin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    @RequestMapping("list")
    @RestReturn(value=ActivityListResponse.class)
    public RestResponse list(@Valid ActivityListCommand cmd) {
        ActivityListResponse activities = activityService.listActivities(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(activities);
        return response;
    }
    
    @RequestMapping("confirm")
    @RestReturn(ActivityDTO.class)
    public RestResponse confirm(@Valid ActivityComfirmCommand cmd){
        ActivityDTO confirm = activityService.confirm(cmd);
        return new RestResponse(confirm);
    }
    
    @RequestMapping("reject")
    @RestReturn(String.class)
    public RestResponse reject(@Valid ActivityRejectCommand cmd){
        activityService.rejectPost(cmd);
        return new RestResponse("OK");
    }
}
