// @formatter:off
package com.everhomes.launchpad;

public interface LaunchPadHandler {
    String LAUNCH_PAD_ITEM_RESOLVER_PREFIX = "LaunchPadActionType-";
    
    LaunchPadItem accesProcessLaunchPadItem(String userToken,long userId, long commnunityId, LaunchPadItem launchPadItem);
}
