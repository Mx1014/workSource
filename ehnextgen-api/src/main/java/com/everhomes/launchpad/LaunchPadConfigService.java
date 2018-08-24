package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.*;


public interface LaunchPadConfigService {

    LaunchPadConfigDTO createLaunchPadConfig(CreateLaunchPadConfigCommand cmd);

    LaunchPadConfigDTO updateLaunchPadConfig(UpdateLaunchPadConfigCommand cmd);

    LaunchPadConfigDTO findLaunchPadConfig(FindLaunchPadConfigCommand cmd);

    void deleteLaunchPadConfig(DeleteLaunchPadConfigCommand cmd);
}
