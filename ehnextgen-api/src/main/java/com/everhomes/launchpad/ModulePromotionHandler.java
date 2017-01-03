// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.ui.activity.ActivityPromotionEntityDTO;

import java.util.List;

/**
 * Created by xq.tian on 2017/1/4.
 */
public interface ModulePromotionHandler {

    /**
     * Handler的component名称构成：MODULE_PROMOTION_HANDLER_PREFIX + "handlerName"
     */
    String MODULE_PROMOTION_HANDLER_PREFIX = "ModulePromotionHandler-";

    List<ActivityPromotionEntityDTO> listModulePromotionEntities(ActionType actionType, String actionData, Integer pageSize);

}
