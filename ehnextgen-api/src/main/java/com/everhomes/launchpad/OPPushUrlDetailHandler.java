// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.OPPushCard;

import java.util.List;

public interface OPPushUrlDetailHandler {

    /**
     * 链接模块的应用配置，instanceConfig可参考："{"url":"http://www.baidu.com"}"
     * @param instanceConfig
     * @return
     */
    boolean checkUrl(Object instanceConfig);
    default String refreshInstanceConfig( String instanceConfig ){
        return instanceConfig;
    };
    List<OPPushCard> listOPPushCard(Long layoutId, Object instanceConfig, AppContext context);
}
