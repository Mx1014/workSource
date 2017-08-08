package com.everhomes.portal;

import com.everhomes.rest.common.ServiceModuleConstants;
import org.springframework.stereotype.Component;

/**
 * Created by sfyan on 2017/8/8.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.ACTIVITY_MODULE)
public class ActivityPublishHandler implements PortalPublishHandler{

    @Override
    public String publish(String instanceConfig) {
        return null;
    }
}
