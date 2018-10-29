// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;

import java.util.List;

public interface OPPushHandler {
    String OPPUSH_ITEMGROUP_TYPE = "OPPushItemGroupType-";

    default String refreshInstanceConfig( String instanceConfig){
        return instanceConfig;
    };

    List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context);

    //String getInstanceConfig(Object instanceConfig);
}
