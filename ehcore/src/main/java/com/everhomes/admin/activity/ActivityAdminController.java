package com.everhomes.admin.activity;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.activity.ActivityService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ListActivitiesCommand;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.user.PaginationCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.Tuple;

@RestController
@RequestMapping("/admin/activity")
@RequireAuthentication(true)
public class ActivityAdminController extends ControllerBase {
    @Autowired
    private ActivityService activityService;

    @RequestMapping("list")
    @RestReturn(value = ListActivitiesReponse.class)
    public RestResponse listActivities(@Valid PaginationCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListActivitiesCommand c = new ListActivitiesCommand();
        c.setAnchor(cmd.getAnchor());
        Tuple<Long, List<ActivityDTO>> result = activityService.listActivities(c);
        ListActivitiesReponse rsp = new ListActivitiesReponse(result.first(), result.second());
        return new RestResponse(rsp);
    }
    
    @RequestMapping("syncActivitySignupAttendeeCount")
    @RestReturn(value = String.class)
    public RestResponse syncActivitySignupAttendeeCount() {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		activityService.syncActivitySignupAttendeeCount();
    	RestResponse response = new RestResponse("OK");
        return response;
    }
}
