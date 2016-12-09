// @formatter:off
package com.everhomes.launchad;

import com.everhomes.rest.launchad.LaunchAdDTO;

import java.util.List;

/**
 * Created by xq.tian on 2016/12/9.
 */
public interface LaunchAdProvider {

    /**
     * 获取启动广告
     */
    List<LaunchAdDTO> getLaunchAd();
}
