package com.everhomes.admin.activity;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.activity.ActivityDTO;
import com.everhomes.activity.ActivityService;
import com.everhomes.activity.ListActivitiesCommand;
import com.everhomes.activity.ListActivitiesReponse;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.PaginationCommand;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.Tuple;

@RequestMapping("/admin/activity")
@RequireAuthentication(true)
public class ActivityAdminController extends ControllerBase {
    @Autowired
    private ActivityService activityService;

    @RequestMapping("list")
    @RestReturn(value = ListActivitiesReponse.class)
    public RestResponse listActivities(@Valid PaginationCommand cmd) {
        ListActivitiesCommand c = new ListActivitiesCommand();
        c.setAnchor(cmd.getAnchor());
        Tuple<Long, List<ActivityDTO>> result = activityService.listActivities(c);
        ListActivitiesReponse rsp = new ListActivitiesReponse(result.first(), result.second());
        return new RestResponse(rsp);
    }
}
