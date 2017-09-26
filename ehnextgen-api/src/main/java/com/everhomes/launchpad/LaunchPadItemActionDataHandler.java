// @formatter:off
package com.everhomes.launchpad;

public interface LaunchPadItemActionDataHandler {
    String LAUNCH_PAD_ITEM_ACTIONDATA_RESOLVER_PREFIX = "LaunchPadItemActionData-";
    String DEFAULT = "default";

    String  refreshActionData(String actionData, Long userId, String sceneToken);
}
