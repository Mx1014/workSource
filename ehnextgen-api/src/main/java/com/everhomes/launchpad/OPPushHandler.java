// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;

import java.util.List;

public interface OPPushHandler {
    String OPPUSH_ITEMGROUP_TYPE = "OPPushItemGroupType-";

    List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context);

    //String getInstanceConfig(Object instanceConfig);
}
