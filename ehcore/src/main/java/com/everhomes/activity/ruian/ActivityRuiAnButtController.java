package com.everhomes.activity.ruian;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneCommand;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneReponse;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activityRuiAn")
public class ActivityRuiAnButtController extends ControllerBase {

    @Autowired
    private ActivityButtService activityButtService;

    /**
     * <p>获取会员活动数据</p>
     * <b>URL: /activityRuiAn/listActivityRuiAnEntitiesByScene</b>
     */
    @RequestMapping("listActivityRuiAnEntitiesByScene")
    @RestReturn(value = ListActivityPromotionEntitiesBySceneReponse.class)
    @RequireAuthentication(false)
    public RestResponse listActivityRuiAnEntitiesByScene(ListActivityPromotionEntitiesBySceneCommand cmd){
        RestResponse response = new RestResponse(activityButtService.listActivityRuiAnEntitiesByScene(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
