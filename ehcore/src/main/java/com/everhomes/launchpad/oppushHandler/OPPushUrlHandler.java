// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.launchpad.OPPushUrlDetailHandler;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
import com.everhomes.rest.widget.OPPushInstanceConfig;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 活动
 */
@Component(OPPushHandler.OPPUSH_ITEMGROUP_TYPE + 90100)
public class OPPushUrlHandler implements OPPushHandler {

    @Autowired(required = false)
    List<OPPushUrlDetailHandler> oPPushUrlDetailHandlers;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {

        OPPushInstanceConfig config = (OPPushInstanceConfig) StringHelper.fromJsonString(instanceConfig.toString(), OPPushInstanceConfig.class);

        ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(config.getAppId());

        if(oPPushUrlDetailHandlers == null){
            return null;
        }

        for (OPPushUrlDetailHandler handler: oPPushUrlDetailHandlers){
            if(handler.checkUrl(app.getInstanceConfig())){

                 return handler.listOPPushCard(layoutId, instanceConfig, context);
            }
        }

        return null;
    }


    @Override
    public String refreshInstanceConfig(String instanceConfig) {
        if(oPPushUrlDetailHandlers == null){
            return null;
        }

        for (OPPushUrlDetailHandler handler: oPPushUrlDetailHandlers){
            if(handler.checkUrl(instanceConfig)){

                return handler.refreshInstanceConfig(instanceConfig)
                        ;
            }
        }
        return null;
    }

}
