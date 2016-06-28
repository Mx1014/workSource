package com.everhomes.ui.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.activity.ActivityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.ui.activity.ListNearbyActivitiesBySceneCommand;
import com.everhomes.util.ConvertHelper;

@RestDoc(value="ActivityUi controller", site="activityui")
@RestController
@RequestMapping("/ui/activity")
public class ActivityUiController extends ControllerBase {
	
	@Autowired
    private ActivityService activityService;
	
	 /**
     * <b>URL: /ui/activity/listNearbyActivitiesByScene</b>
     * <p>根据场景、类型查询周边/同城活动</p>
     */
    @RequestMapping("listNearbyActivitiesByScene")
    @RestReturn(value=ListActivitiesReponse.class)
    public RestResponse listNearbyActivitiesByScene(ListNearbyActivitiesBySceneCommand cmd){
    	com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand command = ConvertHelper.convert(cmd, com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand.class);
        ListActivitiesReponse rsp = activityService.listNearbyActivitiesByScene(command);
        RestResponse response = new RestResponse(rsp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }

}
