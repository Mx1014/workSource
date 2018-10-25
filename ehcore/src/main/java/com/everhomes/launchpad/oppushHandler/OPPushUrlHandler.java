// @formatter:off
package com.everhomes.launchpad.oppushHandler;

import com.everhomes.launchpad.OPPushHandler;
import com.everhomes.launchpad.OPPushUrlDetailHandler;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;
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

    @Override
    public List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context) {
        if(oPPushUrlDetailHandlers == null){
            return null;
        }

        for (OPPushUrlDetailHandler handler: oPPushUrlDetailHandlers){
            if(handler.checkUrl(instanceConfig)){

                 return handler.listOPPushCard(layoutId, instanceConfig, context);
            }
        }

        return null;
    }

}
