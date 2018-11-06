package com.everhomes.launchpad;

public interface LaunchPadConfigProvider {

    Long createLaunchPadConfig(LaunchPadConfig obj);

    void updateLaunchPadConfig(LaunchPadConfig obj);

    void deleteById(Long id);

    LaunchPadConfig findById(Long id);

    LaunchPadConfig findLaunchPadConfig(Byte ownerType, Long ownerId);

}
