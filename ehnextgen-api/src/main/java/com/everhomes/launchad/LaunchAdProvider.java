// @formatter:off
package com.everhomes.launchad;

/**
 * Created by xq.tian on 2016/12/9.
 */
public interface LaunchAdProvider {

    LaunchAd findById(Long id);

    LaunchAd findByNamespaceId(Integer namespaceId);

    /**
     * 更新launchAd
     */
    void updateLaunchAd(LaunchAd launchAd);

    /**
     * 创建launchAd
     */
    void createLaunchAd(LaunchAd launchAd);
}
