// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.rest.ui.user.SceneTokenDTO;

public interface LaunchPadItemActionDataHandler {
    String LAUNCH_PAD_ITEM_ACTIONDATA_RESOLVER_PREFIX = "LaunchPadItemActionData-";
    String DEFAULT = "default";
    String URL_ORG_PARAM = "urlOrgParam";

    String  refreshActionData(String actionData);
}
