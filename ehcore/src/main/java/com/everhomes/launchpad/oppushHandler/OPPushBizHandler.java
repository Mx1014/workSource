// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.activity.ActivityService;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.module.RouterInfoService;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityEntryConfigulation;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.launchpadbase.routerjson.ActivityContentRouterJson;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.everhomes.poll.ProcessStatus.NOTSTART;
import static com.everhomes.poll.ProcessStatus.UNDERWAY;

/**
 * 微商城
 */
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 92100)
public class OPPushBizHandler implements OPPushHandler {


    @Autowired
    private RouterInfoService routerService;

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {


        return null;
    }
}
