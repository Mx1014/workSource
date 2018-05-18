// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.ContextDTO;
import com.everhomes.rest.launchpadbase.OPPushCard;

import java.util.List;

public interface OPPushHandler {
    String OPPUSH_ITEMGROUP_TYPE = "OPPushItemGroupType-";
    String OPPUSHACTIVITY = "OPPushActivity";

    List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, ContextDTO context);

    Long getModuleId();

    Byte getActionType();

    String getInstanceConfig(Object instanceConfig);
}
